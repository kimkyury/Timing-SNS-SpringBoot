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

briskF = cv2.AKAZE_create()

kp1, des1 = briskF.detectAndCompute(template_gray, None)
kp2, des2 = briskF.detectAndCompute(src_gray, None)

bfm = cv2.BFMatcher_create()
# matches = bfm.match(des1, des2)
matches = bfm.knnMatch(des1, des2, 2)

# matches = sorted(matches, key=lambda x:x.distance)
# 최소 거리 값과 최대 거리 값 확보 
# min_dist, max_dist = matches[0].distance, matches[-1].distance
# # 최소 거리의 20% 지점을 임계점으로 설정 
# ratio = 0.1
# good_thresh = (max_dist - min_dist) * ratio + min_dist
# # 임계점 보다 작은 매칭점만 좋은 매칭점으로 분류 
# good_matches = [m for m in matches if m.distance < good_thresh]
# print('matches:%d/%d, min:%.2f, max:%.2f, thresh:%.2f' \
#         %(len(good_matches),len(matches), min_dist, max_dist, good_thresh))
# print(matches)
good_matches = []
for m in matches: # matches는 두개의 리스트로 구성
    if m[0].distance / m[1].distance < 0.7: # 임계점 0.7
        good_matches.append(m[0]) # 저장

print(len(matches), "/", len(good_matches))

# 좋은 매칭점만 그리기 
res = cv2.drawMatches(template, kp1, src, kp2, good_matches, None, \
                flags=cv2.DRAW_MATCHES_FLAGS_NOT_DRAW_SINGLE_POINTS)

cv2.imshow("result", res)


cv2.waitKey(0)
cv2.destroyAllWindows()
