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

target = cv2.imread("image.jpg")
height, width = target.shape[:2]
target_width = 500
aspect_ratio = target_width / width
new_height = int(height * aspect_ratio)
target = cv2.resize(target, (target_width, new_height))

src_outputs = predictor(origin)

src_v = Visualizer(origin[:, :, ::-1], MetadataCatalog.get(cfg.DATASETS.TRAIN[0]), scale=1.2, instance_mode=ColorMode.IMAGE_BW)

idx = 0
x = int(src_outputs["instances"].pred_boxes.tensor[idx][1].item())
xx = int(src_outputs["instances"].pred_boxes.tensor[idx][3].item())
y = int(src_outputs["instances"].pred_boxes.tensor[idx][0].item())
yy= int(src_outputs["instances"].pred_boxes.tensor[idx][2].item())
template = origin[x:xx, y:yy]
_, w, h = template.shape[::-1]
cv2.imshow("template", template)

image_gray = cv2.cvtColor(target, cv2.COLOR_BGR2GRAY)
template_gray = cv2.cvtColor(template, cv2.COLOR_BGR2GRAY)
# cv2.imshow("image_gray", image_gray)
# cv2.imshow("template_gray", template_gray)
result = cv2.matchTemplate(image_gray, template_gray, cv2.TM_CCOEFF_NORMED)
# threshold = 0.95 # 임계치 설정
# box_loc = np.where(result >= threshold) # 임계치 이상의 값들만 사용
max_value = np.max(result)
print(max_value)
box_loc = np.where(result == max_value) # 임계치 이상의 값들만 사용

for box in zip(*box_loc[::-1]):
    startX, startY = box
    print(box)
    endX, endY = startX + w, startY + h

    cv2.rectangle(target, (startX, startY), (endX, endY), (0,0,255), 1)
    print("make rectangle")

cv2.imshow("result", target)

cv2.waitKey(0)
cv2.destroyAllWindows()
