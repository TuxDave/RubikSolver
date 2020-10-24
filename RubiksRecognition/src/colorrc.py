import cv2
import numpy as np

if __name__ == "__main__":
    start()

def nothing(x): pass

def start():
    frameWidth = 680 #680
    frameHeight = 480 #480

    cap = cv2.VideoCapture(0)

    cap.set(3, frameWidth)
    cap.set(4, frameHeight)

    cv2.namedWindow("Trackbars")
    cv2.createTrackbar("L - H", "Trackbars", 0, 179, nothing)
    cv2.createTrackbar("L - S", "Trackbars", 0, 255, nothing)
    cv2.createTrackbar("L - V", "Trackbars", 0, 255, nothing)
    cv2.createTrackbar("U - H", "Trackbars", 0, 179, nothing)
    cv2.createTrackbar("U - S", "Trackbars", 0, 255, nothing)
    cv2.createTrackbar("U - V", "Trackbars", 0, 255, nothing)

    while True:
        success, img = cap.read()
        img = img[65:240, 230:395]
        imgHSV = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)

        l_h = cv2.getTrackbarPos("L - H", "Trackbars")
        l_s = cv2.getTrackbarPos("L - S", "Trackbars")
        l_v = cv2.getTrackbarPos("L - V", "Trackbars")
        u_h = cv2.getTrackbarPos("U - H", "Trackbars")
        u_s = cv2.getTrackbarPos("U - S", "Trackbars")
        u_v = cv2.getTrackbarPos("U - V", "Trackbars")

        tsl_b = np.array([160, 70, 70])
        tsh_b = np.array([190, 90, 80])

        lower = np.array([l_h,l_s,l_v])
        upper = np.array([u_h, u_s, u_v])

        mask = cv2.inRange(imgHSV, lower, upper)
        #mask = mask[65:240, 230:395]
        
        cv2.imshow("Result", img)
        cv2.imshow("mask", mask)

        if cv2.waitKey(1) & 0xFF == ord("q"):   break