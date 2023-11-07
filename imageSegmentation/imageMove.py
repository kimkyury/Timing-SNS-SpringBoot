import os
from fastapi import FastAPI, UploadFile, Request, Form, HTTPException
from fastapi.responses import FileResponse
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

setup_logger()

challengeId = "1spor1998@naver.com"
CHANNELS = 3 # number of image channels (RGB)

sys.path.insert(0, os.path.abspath('./detectron2'))

cfg = get_cfg()
cfg.merge_from_file(model_zoo.get_config_file("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml"))
cfg.MODEL.ROI_HEADS.SCORE_THRESH_TEST = 0.5  # set threshold for this model
cfg.MODEL.WEIGHTS = model_zoo.get_checkpoint_url("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml")
predictor = DefaultPredictor(cfg)

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

target_img_path = '1spor1998@naver.com.png'

target = cv2.imread(target_img_path)

paths = os.listdir(challengeId)

pathOut = "./" + challengeId + ".mp4"
fps = 4
frame_array = []

height, width = 645, 390

for idx , path in enumerate(paths) :
    # if idx == 2:
    #     break
    img = cv2.imread("./" + challengeId + "/" + path)
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
out = cv2.VideoWriter(pathOut,cv2.VideoWriter_fourcc(*'DIVX'), fps, size)
for i in range(len(frame_array)):
    out.write(frame_array[i])
out.release()

    # break














# print(img)
# rows,cols = img.shape[0:2]  # 영상의 크기
# # print(rows, cols)

# dx, dy = -100, 50            # 이동할 픽셀 거리

# # # ---① 변환 행렬 생성 
# mtrx = np.float32([[1, 0, dx],
#                    [0, 1, dy]])  
# # # ---② 단순 이동
# dst = cv2.warpAffine(img, mtrx, (cols+dx, rows+dy))  
# # print(dst)

# cv2.imshow("original", img)
# cv2.imshow("test", dst)

# cv2.waitKey(0)
# cv2.destroyAllWindows()