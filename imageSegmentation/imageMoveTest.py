import cv2
import numpy as np

# 빈 이미지 생성

# criteria_rows, criteria_cols = 645, 390
width, height = 390, 645  # 이미지의 너비와 높이를 원하는 크기로 지정합니다.
blank_image = np.zeros((height, width, 3), np.uint8)  # 3 채널 BGR 이미지를 생성합니다.

# 이미지를 넣을 위치(x, y)를 지정합니다.
# x = 50  # x 좌표
# y = 50  # y 좌표

# 넣을 이미지를 불러오거나 생성합니다.
image_to_insert = cv2.imread('./test.png')  # 이미지 파일을 읽어올 때
# image_to_insert = cv2.resize(image_to_insert, dsize=(image_to_insert.shape[1] * 2, image_to_insert.shape[0] * 2), interpolation=cv2.INTER_LINEAR)

ratio = max(image_to_insert.shape[0] / height ,image_to_insert.shape[1] / width)
# print(image_to_insert.shape[0] / height)
# print(image_to_insert.shape[1] / width)
if ratio > 1:
    image_to_insert = cv2.resize(image_to_insert, dsize=(int(image_to_insert.shape[1] / ratio) , int(image_to_insert.shape[0] / ratio)), interpolation=cv2.INTER_LINEAR)
    # cv2.resize(img, dsize=(500,500), interpolation=cv2.INTER_LINEAR)

print(image_to_insert.shape[0])
print(image_to_insert.shape[1])

print(blank_image.shape[0])
print(blank_image.shape[1])

y = int((blank_image.shape[0] - image_to_insert.shape[0]) / 2) 
x = int((blank_image.shape[1] - image_to_insert.shape[1]) / 2)

blank_image[y:y + image_to_insert.shape[0], x:x + image_to_insert.shape[1]] = image_to_insert

cv2.imshow('Result Image', blank_image)  # 이미지 표시
cv2.waitKey(0)
cv2.destroyAllWindows()