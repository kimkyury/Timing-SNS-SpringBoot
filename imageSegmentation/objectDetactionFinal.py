import os
from fastapi import FastAPI, UploadFile, Request, Form, HTTPException, Response
from fastapi.responses import FileResponse, JSONResponse
import torch, detectron2
import sys, distutils.core, json, cv2, random
import numpy as np
from detectron2.utils.logger import setup_logger
from detectron2 import model_zoo
from detectron2.engine import DefaultPredictor
from detectron2.config import get_cfg
from detectron2.utils.visualizer import Visualizer
from detectron2.data import MetadataCatalog, DatasetCatalog
from detectron2.projects.deeplab import add_deeplab_config
from detectron2.projects.panoptic_deeplab import add_panoptic_deeplab_config
from detectron2.utils.visualizer import ColorMode
import io
from starlette.responses import StreamingResponse
import tensorflow_hub as hub
import tensorflow.compat.v1 as tf
import time
from IPython.display import Image, display
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from rembg import remove
import random
import string
import requests
from pydantic import BaseModel

app = FastAPI()

origins = [
    "*"
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
    expose_headers=["polygon", "Polygon"]
)

CHANNELS = 3 # number of image channels (RGB)

class Item(BaseModel):
    object: str
    snapshots: str 

@app.on_event("startup")
async def startup():
    global predictor, cfg, module

    sys.path.insert(0, os.path.abspath('./detectron2'))

    cfg = get_cfg()
    cfg.merge_from_file(model_zoo.get_config_file("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml"))
    cfg.MODEL.ROI_HEADS.SCORE_THRESH_TEST = 0.5  # set threshold for this model
    cfg.MODEL.WEIGHTS = model_zoo.get_checkpoint_url("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml")
    cfg.MODEL.DEVICE = "cpu"
    predictor = DefaultPredictor(cfg)

    tf.disable_v2_behavior()
    module = hub.load("https://tfhub.dev/google/imagenet/efficientnet_v2_imagenet1k_b0/feature_vector/2")

@app.get("/objectDetection/test")
async def test():
    return "test"

@app.post("/objectDetection")
async def objectDetection(snapshot: UploadFile):
    data = await snapshot.read()
    encoded_img = np.fromstring(data, dtype = np.uint8)
    img = cv2.imdecode(encoded_img, cv2.IMREAD_COLOR)

    outputs = predictor(img)

    v = Visualizer(img[:, :, ::-1], MetadataCatalog.get(cfg.DATASETS.TRAIN[0]), scale=1.2, instance_mode=ColorMode.IMAGE_BW)

    instance_result, _= v.draw_instance_predictions(outputs["instances"].to("cpu"))

    if len(outputs["instances"].pred_boxes) == 0:
        raise HTTPException(status_code=400)

    # im_png = remove(instance_result.get_image()[:, :, ::-1])
    # _, im_png = cv2.imencode(".png", im_png)
    _, im_png = cv2.imencode(".png", instance_result.get_image()[:, :, ::-1])

    return StreamingResponse(io.BytesIO(im_png.tobytes()), media_type="image/png")

@app.post("/objectDetection/chooseObject")
async def chooseObject(snapshot: UploadFile = Form(), x: float = Form(), y: float = Form()):
    # 원본 이미지랑 x, y 좌표 같이 보내줘야됨

    data = await snapshot.read()
    encoded_img = np.fromstring(data, dtype = np.uint8)
    img = cv2.imdecode(encoded_img, cv2.IMREAD_COLOR)

    outputs = predictor(img)
    v = Visualizer(img[:, :, ::-1], MetadataCatalog.get(cfg.DATASETS.TRAIN[0]), scale=1.2, instance_mode=ColorMode.IMAGE_BW)

    _, instance_polygons= v.draw_instance_predictions(outputs["instances"].to("cpu"))

    idx = -1
    for i in range(len(outputs["instances"].pred_boxes)):
        if x <= outputs["instances"].pred_boxes.tensor[i][3].item() and x >= outputs["instances"].pred_boxes.tensor[i][1].item() and y <= outputs["instances"].pred_boxes.tensor[i][2].item() and y >= outputs["instances"].pred_boxes.tensor[i][0].item():
            idx = i
            break

    if idx == -1:
        raise HTTPException(status_code=400)

    x = int(outputs["instances"].pred_boxes.tensor[idx][1].item())
    xx = int(outputs["instances"].pred_boxes.tensor[idx][3].item())
    y = int(outputs["instances"].pred_boxes.tensor[idx][0].item())
    yy= int(outputs["instances"].pred_boxes.tensor[idx][2].item())

    _, im_png = cv2.imencode(".png", img[x:xx, y:yy]) 

    instance_polygon = instance_polygons[idx]

    vertices = instance_polygon.get_path().vertices
    poly = ''
    for index, vertex in list(enumerate(vertices)):
        poly += str(vertex[0]) + "," + str(vertex[1])
        if index != len(vertices) - 1:
            poly += " "

    results = {
        "polygon": poly
    }

    return Response(content=io.BytesIO(im_png.tobytes()).getvalue(), headers=results)
    # return StreamingResponse(io.BytesIO(im_png.tobytes()), media_type="image/png", headers=poly)

# @app.post("/objectDetection/objectSave")
# async def objectSave(img: UploadFile = Form(), challengeId: str = Form()):
#     data = await img.read()
#     encoded_img = np.fromstring(data, dtype = np.uint8)

#     img = cv2.imdecode(encoded_img, cv2.IMREAD_COLOR)
    
#     # cv2.imwrite("./test.png", img)
#     # img = remove(img)

#     cv2.imwrite("./" + challengeId + ".png", img)

#     return "done"

def build_graph(hub_module_url, target_image_path):
    # Step 1) Prepare pre-trained model for extracting image features.

    module = hub.Module(hub_module_url)
    height, width = hub.get_expected_image_size(module)

    # Copied a method of https://github.com/GoogleCloudPlatform/cloudml-samples/blob/bf0680726/flowers/trainer/model.py#L181
    # and fixed for all type images (not only jpeg)
    def decode_and_resize(image_str_tensor):
        """Decodes jpeg string, resizes it and returns a uint8 tensor."""
        image = tf.image.decode_image(image_str_tensor, channels=CHANNELS)
        # Note resize expects a batch_size, but tf_map supresses that index,
        # thus we have to expand then squeeze.  Resize returns float32 in the
        # range [0, uint8_max]
        image = tf.expand_dims(image, 0)
        image = tf.compat.v1.image.resize_bilinear(
            image, [height, width], align_corners=False)
        image = tf.squeeze(image)
        image = tf.cast(image, dtype=tf.uint8)
        return image

    def to_img_feature(images):
        """Extract the feature of image vectors"""
        outputs = module(dict(images=images), signature="image_feature_vector", as_dict=True)
        return outputs['default']

    # Step 2) Extract image features of the target image.
    target_image_bytes = tf.gfile.GFile(target_image_path, 'rb').read()
    target_image = tf.constant(target_image_bytes, dtype=tf.string)
    target_image = decode_and_resize(target_image)
    target_image = tf.image.convert_image_dtype(target_image, dtype=tf.float32)
    target_image = tf.expand_dims(target_image, 0)
    target_image = to_img_feature(target_image)

    # Step 3) Extract image features of input images.
    input_byte = tf.placeholder(tf.string, shape=[None])
    input_image = tf.map_fn(decode_and_resize, input_byte, back_prop=False, dtype=tf.uint8)
    input_image = tf.image.convert_image_dtype(input_image, dtype=tf.float32)
    input_image = to_img_feature(input_image)

    # Step 4) Compare cosine_similarities of the target image and the input images.
    dot = tf.tensordot(target_image, tf.transpose(input_image), 1)
    similarity = dot / (tf.norm(target_image, axis=1) * tf.norm(input_image, axis=1))
    similarity = tf.reshape(similarity, [-1])

    return input_byte, similarity

@app.post("/objectDetection/similarity")
async def similarity(snapshot: UploadFile = Form(), objectUrl: str = Form()):
    # 챌린지ID, 선택된 객체 이미지, 새로 찍은 이미지

    print(objectUrl)

    challengeId = "".join([random.choice(string.ascii_letters) for _ in range(15)])
    
    data = await snapshot.read()
    encoded_img = np.fromstring(data, dtype = np.uint8)
    img = cv2.imdecode(encoded_img, cv2.IMREAD_COLOR)

    data = np.asarray(bytearray(requests.get(objectUrl).content), dtype=np.uint8)
    target = cv2.imdecode(data, cv2.IMREAD_COLOR)
    cv2.imwrite("./" + challengeId + ".png", target)

    outputs = predictor(img)

    target_img_path = challengeId + '.png'
    input_img_paths = []

    for i in range(len(outputs["instances"].pred_boxes)):
        x = int(outputs["instances"].pred_boxes.tensor[i][1].item())
        xx = int(outputs["instances"].pred_boxes.tensor[i][3].item())
        y = int(outputs["instances"].pred_boxes.tensor[i][0].item())
        yy= int(outputs["instances"].pred_boxes.tensor[i][2].item())
        
        # img_remove = remove(img[x:xx, y:yy])
        # cv2.imwrite("./" + challengeId + str(i) + ".png", img_remove)
        cv2.imwrite("./" + challengeId + str(i) + ".png", img[x:xx, y:yy])
        input_img_paths.append(challengeId + str(i) + ".png")
    
    image_bytes = [tf.gfile.GFile(name, 'rb').read()
               for name in input_img_paths]
    
    hub_module_url = "https://tfhub.dev/google/imagenet/mobilenet_v2_100_96/feature_vector/1" #@param {type:"string"}

    with tf.Graph().as_default():
        input_byte, similarity_op = build_graph(hub_module_url, target_img_path)
        print("build 성공!!!!!")

        with tf.Session() as sess:
            sess.run(tf.global_variables_initializer())
            t0 = time.time() # for time check
            
            # Inference similarities
            similarities = sess.run(similarity_op, feed_dict={input_byte: image_bytes})

    idx = 0
    max = 0
    maxIdx = -1
    for similarity, input_img_path in zip(similarities, input_img_paths):
        print("- similarity: %.2f" % similarity)
        

        if similarity > max and similarity > 0.6:
            maxIdx = idx
            max = similarity
        os.remove(input_img_path)
        idx+=1
    os.remove(target_img_path)

    if maxIdx == -1:
        raise HTTPException(status_code=400)
    
    return "객체 찾기 완료!!"

# @app.get("/objectDetection/getPoly/{challengeId}")
# async def getPoly(challengeId: str):
    
#     if os.path.isfile("./" + challengeId + "poly.txt"):
#         f = open("./" + challengeId + "poly.txt", 'r')
#         data = f.read()
#         return data
        
#     raise HTTPException(status_code=204)

@app.post("/objectDetection/makeVideo")
async def makeVideo(item: Item):

    object = item.object
    snapshots = item.snapshots

    print(object)
    print(snapshots)

    challengeId = "".join([random.choice(string.ascii_letters) for _ in range(15)])

    snapshots = snapshots.split(",")

    data = np.asarray(bytearray(requests.get(object).content), dtype=np.uint8)
    target = cv2.imdecode(data, cv2.IMREAD_COLOR)
    cv2.imwrite("./" + challengeId + ".png", target)
    
    target_img_path = "./" + challengeId + ".png"

    pathOut = "./" + challengeId + ".mp4"
    fps = 4
    frame_array = []

    height, width = 645, 390

    for idx , path in enumerate(snapshots) :
        # if idx == 2:
        #     break
        data = np.asarray(bytearray(requests.get(path).content), dtype=np.uint8)
        img = cv2.imdecode(data, cv2.IMREAD_COLOR)
        # img = cv2.imread("./" + challengeId + "/" + path)
        outputs = predictor(img)

        input_img_paths = []

        for i in range(len(outputs["instances"].pred_boxes)):
            x = int(outputs["instances"].pred_boxes.tensor[i][1].item())
            xx = int(outputs["instances"].pred_boxes.tensor[i][3].item())
            y = int(outputs["instances"].pred_boxes.tensor[i][0].item())
            yy= int(outputs["instances"].pred_boxes.tensor[i][2].item())
            
            # img_remove = remove(img[x:xx, y:yy])
            # cv2.imwrite("./" + challengeId + str(i) + ".png", img_remove)
            cv2.imwrite("./" + challengeId + str(i) + ".png", img[x:xx, y:yy])
            input_img_paths.append(challengeId + str(i) + ".png")

        image_bytes = [tf.gfile.GFile(name, 'rb').read()
                for name in input_img_paths]
        
        hub_module_url = "https://tfhub.dev/google/imagenet/mobilenet_v2_100_96/feature_vector/1" #@param {type:"string"}

        with tf.Graph().as_default():
            input_byte, similarity_op = build_graph(hub_module_url, target_img_path)
            # print("build 성공!!!!!")

            with tf.Session() as sess:
                sess.run(tf.global_variables_initializer())
                t0 = time.time() # for time check
                
                # Inference similarities
                similarities = sess.run(similarity_op, feed_dict={input_byte: image_bytes})

        idx = 0
        maxx = 0
        maxIdx = -1
        for similarity, input_img_path in zip(similarities, input_img_paths):
            # print(input_img_path)
            # print("- similarity: %.2f" % similarity)
            if similarity > maxx and similarity > 0.6:
                maxIdx = idx
                maxx = similarity
            os.remove(input_img_path)
            idx+=1

        if maxIdx == -1:
            continue

        x = int(outputs["instances"].pred_boxes.tensor[i][1].item())
        xx = int(outputs["instances"].pred_boxes.tensor[i][3].item())
        y = int(outputs["instances"].pred_boxes.tensor[i][0].item())
        yy= int(outputs["instances"].pred_boxes.tensor[i][2].item())

        centerX = (xx + x) / 2
        centerY = (yy - y) / 2

        rows,cols = img.shape[0:2]  # 영상의 크기

        half_width = min(rows - centerX, centerX)
        half_height = min(cols - centerY, centerY)

        # print(centerX, centerY)
        # print(rows, cols)
        # print(half_width, half_height)
        # print(int(centerX-half_width), int(centerX+half_width), int(centerY-half_height), int(centerY+half_height))

        image_to_insert = img[int(centerX-half_width):int(centerX+half_width), int(centerY-half_height):int(centerY+half_height)]
        # print(image_to_insert)

        # cv2.imwrite("./test.png", reshape_img)

        if idx == 0:
            height, width = img.shape[0:2]
            frame_array.append(img)
            continue
        else:
            blank_image = np.zeros((height, width, 3), np.uint8)
            # print(image_to_insert.shape[0] / height)
            # print(image_to_insert.shape[1] / width)

            ratio = max(image_to_insert.shape[0] / height , image_to_insert.shape[1] / width)
            if ratio > 1:
                image_to_insert = cv2.resize(image_to_insert, dsize=(int(image_to_insert.shape[1] / ratio) , int(image_to_insert.shape[0] / ratio)), interpolation=cv2.INTER_LINEAR)

            y = int((blank_image.shape[0] - image_to_insert.shape[0]) / 2) 
            x = int((blank_image.shape[1] - image_to_insert.shape[1]) / 2)

            blank_image[y:y + image_to_insert.shape[0], x:x + image_to_insert.shape[1]] = image_to_insert
            frame_array.append(blank_image)

    size = (width,height)
    out = cv2.VideoWriter(pathOut,cv2.VideoWriter_fourcc(*'H264'), fps, size)
    for i in range(len(frame_array)):
        out.write(frame_array[i])
    out.release()

    os.remove(target_img_path)

    with open(pathOut, "rb") as video_file:
        video_data = video_file.read()

    os.remove(pathOut)

    return StreamingResponse(io.BytesIO(video_data), media_type="video/mp4")

