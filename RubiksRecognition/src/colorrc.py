import cv2
import numpy as np
import csv
import sys
import time

def nothing(x): pass

FROM_INT_TO_COLOR = {0: "white",1: "blue",2: "yellow",3: "green",4: "orange",5: "red"}
CUBE_LENGTH = 58
BOUNDS = [((x)*CUBE_LENGTH, (y)*CUBE_LENGTH) for x in range(3) for y in range(3)]
CHECK_POINT = [(29,29)]

def start(color):
    file = open("./RubiksRecognition/resources/ranges.csv", 'r')
    COLOR_RANGES = csv.reader(file)
    RANGES = [(line) for line in COLOR_RANGES]

    frameWidth = 680 #680
    frameHeight = 480 #480

    cap = cv2.VideoCapture("RubiksRecognition/notTracked/cube1.mp4")

    cap.set(3, frameWidth)
    cap.set(4, frameHeight)

    while True:
        success, img = cap.read()
        try:
            img = img[65:240, 230:405] #175*175
        except:
            cap.set(cv2.CAP_PROP_POS_FRAMES, 0)
            success, img = cap.read()
        imgHSV = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
        
        cv2.imshow("Result", img)

        if cv2.waitKey(1) & 0xFF == ord("q"):
            print(fromImageToColorSequence(imgHSV, RANGES.copy()))
        time.sleep(0.1)
    file.close()

#prende un immagine in HSV e restituisce un array di numeri che sono i colori
def fromImageToColorSequence(img, ranges):
    pos = {}
    cropped = 0
    for c in FROM_INT_TO_COLOR.keys():#per ogni colore

        #dopo questo blocco color conterrà il range per il colore corrente
        ranges1 = ranges.copy()
        for line in ranges1:
            if(line[0] == str(c)):
                color = line
                break
        for i in range(len(color)-1):
            color[i+1] = int(color[i+1])

        #eseguo la mask dell'immagine
        mask = cv2.inRange(img, np.array([color[1], color[2], color[3]]), np.array([color[4], color[5], color[6]]))

        for i in range(9):
            cropped = mask[BOUNDS[i][1]:BOUNDS[i][1]+CUBE_LENGTH, BOUNDS[i][0]:BOUNDS[i][0]+CUBE_LENGTH]
            found = True
            for couple in CHECK_POINT:
                if cropped[couple[1], couple[0]] != 255:
                    found = False
            if found:
                pos[i] = int(c)
    return pos

if __name__ == "__main__":
    start(sys.argv[0])