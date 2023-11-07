import torch, detectron2
import sys, os, distutils.core, json, cv2, random
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

setup_logger()

# dist = distutils.core.run_setup("./detectron2/setup.py")

sys.path.insert(0, os.path.abspath('./detectron2'))

cfg = get_cfg()
cfg.merge_from_file(model_zoo.get_config_file("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml"))
cfg.MODEL.ROI_HEADS.SCORE_THRESH_TEST = 0.5  # set threshold for this model
cfg.MODEL.WEIGHTS = model_zoo.get_checkpoint_url("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml")
predictor = DefaultPredictor(cfg)

origin = cv2.imread("img1.jpg")
height, width = origin.shape[:2]
target_width = 500
aspect_ratio = target_width / width
new_height = int(height * aspect_ratio)
origin = cv2.resize(origin, (target_width, new_height))

target = cv2.imread("img3.jpg")
height, width = target.shape[:2]
target_width = 500
aspect_ratio = target_width / width
new_height = int(height * aspect_ratio)
target = cv2.resize(target, (target_width, new_height))

src_outputs = predictor(origin)
target_outputs = predictor(target)

idx = 0
x = int(src_outputs["instances"].pred_boxes.tensor[idx][1].item())
xx = int(src_outputs["instances"].pred_boxes.tensor[idx][3].item())
y = int(src_outputs["instances"].pred_boxes.tensor[idx][0].item())
yy= int(src_outputs["instances"].pred_boxes.tensor[idx][2].item())
template = origin[x:xx, y:yy]
template_gray = cv2.cvtColor(template, cv2.COLOR_BGR2GRAY)
_, w, h = template.shape[::-1]
cv2.imshow("template", template)
cv2.imshow("template_gray", template_gray)

idx = 0
x = int(target_outputs["instances"].pred_boxes.tensor[idx][1].item())
xx = int(target_outputs["instances"].pred_boxes.tensor[idx][3].item())
y = int(target_outputs["instances"].pred_boxes.tensor[idx][0].item())
yy= int(target_outputs["instances"].pred_boxes.tensor[idx][2].item())
src = target[x:xx, y:yy]
src_gray = cv2.cvtColor(src, cv2.COLOR_BGR2GRAY)
_, w, h = src.shape[::-1]
cv2.imshow("src", src)
cv2.imshow("src_gray", src_gray)

import tensorflow_hub as hub
import tensorflow.compat.v1 as tf
tf.disable_v2_behavior()

CHANNELS = 3 # number of image channels (RGB)
module = hub.load("https://tfhub.dev/google/imagenet/efficientnet_v2_imagenet1k_b0/feature_vector/2")

cv2.imwrite("./target_img.jpg",template)
cv2.imwrite("./input_img.jpg",src)

print(type(template.tobytes()))

target_img_path = 'target_img.jpg'
input_img_paths = ['input_img.jpg']


import time
from IPython.display import Image, display
# tf.logging.set_verbosity(tf.logging.ERROR)

print(tf.__version__)


# Load bytes of image files
image_bytes = [tf.gfile.GFile(name, 'rb').read()
               for name in [target_img_path] + input_img_paths]

for name in [target_img_path] + input_img_paths:
  print("이름 : ", name)
print("image_bytes type : ", type(image_bytes))

hub_module_url = "https://tfhub.dev/google/imagenet/mobilenet_v2_100_96/feature_vector/1" #@param {type:"string"}

with tf.Graph().as_default():
  input_byte, similarity_op = build_graph(hub_module_url, target_img_path)
  print("build 성공!!!!!")
  print(input_byte)
  print(similarity_op)

  with tf.Session() as sess:
    sess.run(tf.global_variables_initializer())
    t0 = time.time() # for time check
    
    # Inference similarities
    similarities = sess.run(similarity_op, feed_dict={input_byte: image_bytes})
    
    print("%d images inference time: %.2f s" % (len(similarities), time.time() - t0))

# Display results
print("# Target image")
display(Image(target_img_path))
print("- similarity: %.2f" % similarities[0])

print("# Input images")
for similarity, input_img_path in zip(similarities[1:], input_img_paths):
  display(Image(input_img_path))
  print("- similarity: %.2f" % similarity)



cv2.waitKey(0)
cv2.destroyAllWindows()