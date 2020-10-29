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
TRIES = 7

def start():
    file = open("./RubiksRecognition/resources/ranges.csv", 'r')
    COLOR_RANGES = csv.reader(file)
    RANGES = [(line) for line in COLOR_RANGES]

    frameWidth = 680 #680
    frameHeight = 480 #480

    cap = cv2.VideoCapture("RubiksRecognition/notTracked/cube1.mp4")

    cap.set(3, frameWidth)
    cap.set(4, frameHeight)

    while True:
        img = fromCapToHSVImage(cap)
        
        cv2.imshow("Result", cv2.cvtColor(img, cv2.COLOR_HSV2BGR))

        if cv2.waitKey(1) & 0xFF == ord("q"):
            faces = []
            for i in range(TRIES):
                faces.append(fromImageToColorSequence(img, RANGES))
                img = fromCapToHSVImage(cap)
            print(getMostCommon(faces, english=True))
        time.sleep(0.1)
    file.close()

#prende un immagine in HSV e restituisce un array di numeri che sono i colori
def fromImageToColorSequence(img, ranges):
    pos = {}
    for i in range(9):
        pos[i] = None

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
            cropped = mask[BOUNDS[i][0]:BOUNDS[i][0]+CUBE_LENGTH, BOUNDS[i][1]:BOUNDS[i][1]+CUBE_LENGTH]
            found = True
            for couple in CHECK_POINT:
                if cropped[couple[1], couple[0]] != 255:
                    found = False
            if found:
                pos[i] = int(c)
    
    pos = ordinaDict(pos)

    pos2 = {}
    list = [4,0,1,2,5,8,7,6,3]
    for n in range(9):
        try:
            # pos2[n] = FROM_INT_TO_COLOR[pos[list[n]]] #per ottenere i colori in inglese
            pos2[n] = pos[list[n]] # per ottenere i colori in numeri
        except:
            pos2[n] = None
    '''
    pos2[0] = FROM_INT_TO_COLOR[pos[4]]
    pos2[1] = FROM_INT_TO_COLOR[pos[0]]
    pos2[2] = FROM_INT_TO_COLOR[pos[1]]
    pos2[3] = FROM_INT_TO_COLOR[pos[2]]
    pos2[4] = FROM_INT_TO_COLOR[pos[5]]
    pos2[5] = FROM_INT_TO_COLOR[pos[8]]
    pos2[6] = FROM_INT_TO_COLOR[pos[7]]
    pos2[7] = FROM_INT_TO_COLOR[pos[6]]
    pos2[8] = FROM_INT_TO_COLOR[pos[3]]
    '''

    #TODO: Fare il sistema che trova il colore più comune per ogni posizione

    return ordinaDict(pos2)
    
def fromCapToHSVImage(cap):
    success, img = cap.read()
    try:
        img = img[65:240, 230:405] #175*175
    except:
        cap.set(cv2.CAP_PROP_POS_FRAMES, 0)
        success, img = cap.read()            
    imgHSV = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    return imgHSV

def ordinaDict(dict1):
    ret = {}
    keys = [(key) for key in dict1.keys()]
    keys.sort()
    for key in keys:
        ret[key] = dict1[key]
    return ret

def getMostCommon(faces, english=False):
    common = {}
    keys = [(i) for i in range(9)]

    for key in keys:
        counter = {n:0 for n in range(6)} #conterrà quante volte sono ustiti tutti i colori
        for face in faces:
            if face[key] != None:
                counter[face[key]] += 1
        max = -1
        colorMax = -1
        for i in range(len(counter.keys())):
            if counter[i] > max:
                colorMax = i # il colore che è apparso piu volte
                max = i
        common[key] = colorMax
    
    if english:
        for key in common.keys():
            common[key] = FROM_INT_TO_COLOR[common[key]]

    return common



if __name__ == "__main__":
    start()