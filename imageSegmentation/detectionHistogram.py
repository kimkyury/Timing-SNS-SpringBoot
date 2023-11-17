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


template_hsv = cv2.cvtColor(template, cv2.COLOR_BGR2HSV)
# H,S 채널에 대한 히스토그램 계산
template_hist = cv2.calcHist([template_hsv], [0,1], None, [180,256], [0,180,0, 256])
# 0~1로 정규화
cv2.normalize(template_hist, template_hist, 0, 1, cv2.NORM_MINMAX)

src_hsv = cv2.cvtColor(src, cv2.COLOR_BGR2HSV)
# H,S 채널에 대한 히스토그램 계산
src_hist = cv2.calcHist([src_hsv], [0,1], None, [180,256], [0,180,0, 256])
# 0~1로 정규화
cv2.normalize(src_hist, src_hist, 0, 1, cv2.NORM_MINMAX)


methods = {'CORREL' :cv2.HISTCMP_CORREL, 'CHISQR':cv2.HISTCMP_CHISQR, 
           'INTERSECT':cv2.HISTCMP_INTERSECT,
           'BHATTACHARYYA':cv2.HISTCMP_BHATTACHARYYA}

for j, (name, flag) in enumerate(methods.items()): 
    print('%-10s'%name, end='\t')
    # 각 메서드에 따라 img1과 각 이미지의 히스토그램 비교
    ret = cv2.compareHist(src_hist, template_hist, flag)
    
    if flag == cv2.HISTCMP_INTERSECT: #교차 분석인 경우 
        ret = ret/np.sum(src_hist)        #비교대상으로 나누어 1로 정규화
    print(" : %7.2f"% (ret))


cv2.waitKey(0)
cv2.destroyAllWindows()