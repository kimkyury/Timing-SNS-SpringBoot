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

img = cv2.imread("./1spor1998@naver.com/1spor1998@naver.com0.png")

# cv2.imshow("123asdf", img)

resized_img = cv2.resize(img, dsize=(500,500), interpolation=cv2.INTER_LINEAR)

cfg = get_cfg()
cfg.merge_from_file(model_zoo.get_config_file("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml"))
cfg.MODEL.ROI_HEADS.SCORE_THRESH_TEST = 0.5  # set threshold for this model
cfg.MODEL.WEIGHTS = model_zoo.get_checkpoint_url("COCO-InstanceSegmentation/mask_rcnn_R_50_FPN_3x.yaml")
predictor = DefaultPredictor(cfg)
outputs = predictor(resized_img)

# print(outputs["instances"].pred_boxes.tensor)



v = Visualizer(resized_img[:, :, ::-1], MetadataCatalog.get(cfg.DATASETS.TRAIN[0]), scale=1.2, instance_mode=ColorMode.IMAGE_BW)

instance_result, instance_polygons= v.draw_instance_predictions(outputs["instances"].to("cpu"))
cv2.imshow("bb",instance_result.get_image()[:, :, ::-1])
idx = 0
instancce_shape, instance_polygon = v.get_shape(outputs["instances"].to("cpu"), idx)
instancce_shape = instancce_shape.get_image()
cv2.imshow("a",instancce_shape[:, :, ::-1])

x = int(outputs["instances"].pred_boxes.tensor[idx][1].item())
xx = int(outputs["instances"].pred_boxes.tensor[idx][3].item())
y = int(outputs["instances"].pred_boxes.tensor[idx][0].item())
yy= int(outputs["instances"].pred_boxes.tensor[idx][2].item())
# cv2.imwrite("./test.png",resized_img[x:xx, y:yy])


# cv2.imwrite('./img.png',instance_result.get_image()[:, :, ::-1])


# print(outputs["instances"].pred_masks.tolist())

# file = open("poly.txt", "w")
# file.write("[")
# for instance_polygon in instance_polygons:
#     vertices = instance_polygon.get_path().vertices
#     file.write("[")
#     # 정점 좌표 출력
#     # print("다각형의 점들:")
#     for vertex in vertices:
#         file.write("[")
#         file.write(str(vertex[0]))
#         file.write(",")
#         file.write(str(vertex[1]))
#         file.write("], ")
#         # print(vertex)
#     file.write("],")
# file.write("]")
# file.close()

# file = open("mask.txt", "w")
# file.write(str(outputs["instances"].pred_masks.tolist()))
# file.close()
# file.write("[")
# for instance_polygon in instance_polygons:
#     vertices = instance_polygon.get_path().vertices
#     file.write("[")
#     # 정점 좌표 출력
#     # print("다각형의 점들:")
#     for vertex in vertices:
#         file.write("[")
#         file.write(str(vertex[0]))
#         file.write(",")
#         file.write(str(vertex[1]))
#         file.write("], ")
#         # print(vertex)
#     file.write("],")
# file.write("]")
# file.close()


# path = instance_polygon[0].get_path()
# vertices = path.vertices

# file = open("test.txt", "w")


# file.write("[")
# # 정점 좌표 출력
# # print("다각형의 점들:")
# for vertex in vertices:
#     file.write("[")
#     file.write(str(vertex[0]))
#     file.write(",")
#     file.write(str(vertex[1]))
#     file.write("], ")
#     # print(vertex)
# file.write("]")
# file.close()


cv2.waitKey(0)
cv2.destroyAllWindows()
