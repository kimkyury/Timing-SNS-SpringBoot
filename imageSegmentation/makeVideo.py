import cv2
import os

challengeId = "1spor1998@naver.com"

pathOut = "./" + challengeId + ".mp4"
fps = 4
frame_array = []

paths = os.listdir(challengeId)

img = cv2.imread("./" + challengeId + "/" + paths[0])
height, width, layers = img.shape
size = (width,height)

for idx , path in enumerate(paths) : 
    img = cv2.imread("./" + challengeId + "/" + path)
    img = cv2.resize(img, size)
    frame_array.append(img)

out = cv2.VideoWriter(pathOut,cv2.VideoWriter_fourcc(*'DIVX'), fps, size)
for i in range(len(frame_array)):
    out.write(frame_array[i])
out.release()