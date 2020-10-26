import cv2
import numpy as np
import csv



def nothing(x): pass

'''
0, "white"
1, "blue"
2, "yellow"
3, "green"
4, "orange"
5, "red"
'''

def start():
    file = open("./RubiksRecognition/resources/ranges.csv", 'r')
    colorRanges = csv.reader(file)

    #assigning to every ranges a value (-2 = error)
    white_range = [-2,-2,-2,-2,-2,-2,-2]
    blue_range = [-2,-2,-2,-2,-2,-2,-2]
    yellow_range = [-2,-2,-2,-2,-2,-2,-2]
    green_range = [-2,-2,-2,-2,-2,-2,-2]
    orange_range = [-2,-2,-2,-2,-2,-2,-2]
    red_range = [-2,-2,-2,-2,-2,-2,-2]

    #getting ranges
    for row in colorRanges:
        if row[0] == str(0):
            white_range = row[1:]
        elif row[0] == str(1):
            blue_range = row[1:]
        elif row[0] == str(2):
            yellow_range = row[1:]
        elif row[0] == str(3):
            green_range = row[1:]
        elif row[0] == str(4):
            orange_range = row[1:]
        elif row[0] == str(5):
            red_range = row[1:]
        
    #si ok va ottimizzato, non riesco a pensare al momento AHAHA
    lw_mask = np.array([int(white_range[0]), int(white_range[1]), int(white_range[2])])
    uw_mask = np.array([int(white_range[3]), int(white_range[4]), int(white_range[5])])

    lo_mask = np.array([int(orange_range[0]), int(orange_range[1]), int(orange_range[2])])
    uo_mask = np.array([int(orange_range[3]), int(orange_range[4]), int(orange_range[5])])

    ly_mask = np.array([int(yellow_range[0]), int(yellow_range[1]), int(yellow_range[2])])
    uy_mask = np.array([int(yellow_range[3]), int(yellow_range[4]), int(yellow_range[5])])

    lg_mask = np.array([int(green_range[0]), int(green_range[1]), int(green_range[2])])
    ug_mask = np.array([int(green_range[3]), int(green_range[4]), int(green_range[5])])

    lb_mask = np.array([int(blue_range[0]), int(blue_range[1]), int(blue_range[2])])
    ub_mask = np.array([int(blue_range[3]), int(blue_range[4]), int(blue_range[5])])
    
    lr_mask = np.array([int(red_range[0]), int(red_range[1]), int(red_range[2])])
    ur_mask = np.array([int(red_range[3]), int(red_range[4]), int(red_range[5])])

    frameWidth = 680 #680
    frameHeight = 480 #480

    #0 default camera
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

        #mask = cv2.inRange(imgHSV, lower, upper)
        #!! da rivedere !!
        white_mask = cv2.inRange(imgHSV, lw_mask, uw_mask)
        red_mask = cv2.inRange(imgHSV, lower, upper)
        orange_mask = cv2.inRange(imgHSV, lo_mask, uo_mask)
        yellow_mask = cv2.inRange(imgHSV, ly_mask, uy_mask)
        blue_mask = cv2.inRange(imgHSV, lb_mask, ub_mask)
        green_mask = cv2.inRange(imgHSV, lg_mask, ug_mask)

        full_mask = white_mask + red_mask + orange_mask + yellow_mask + blue_mask + green_mask

        full_colored_mask = cv2.bitwise_and(img, img, mask=full_mask)

        #pixel smoothing
        blurred_full_mask = cv2.medianBlur(full_colored_mask, 15)
    
        #mask contours
        w_contours, _ = cv2.findContours(white_mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
        y_contours, _ = cv2.findContours(yellow_mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
        b_contours, _ = cv2.findContours(blue_mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
        r_contours, _ = cv2.findContours(red_mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
        g_contours, _ = cv2.findContours(green_mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)
        o_contours, _ = cv2.findContours(orange_mask, cv2.RETR_TREE, cv2.CHAIN_APPROX_NONE)

        '''
        #center point
        M_w = cv2.moments(w_contours[0])
        M_y = cv2.moments(y_contours[0])
        M_b = cv2.moments(b_contours[0])
        M_r = cv2.moments(r_contours[0])
        M_g = cv2.moments(g_contours[0])
        M_o = cv2.moments(o_contours[0])

        cv2.circle(blurred_full_mask, (round(M_w['m10'] / M_w['m00']), round(M_w['m01'] / round(M_w['m00']))), 5, (0, 255, 0), -1)
        cv2.circle(blurred_full_mask, (round(M_y['m10'] / M_y['m00']), round(M_y['m01'] / M_y['m00'])), 5, (0, 255, 0), -1)
        cv2.circle(blurred_full_mask, (round(M_b['m10'] / M_b['m00']), round(M_b['m01'] / M_b['m00'])), 5, (0, 255, 0), -1)
        cv2.circle(blurred_full_mask, (round(M_r['m10'] / M_r['m00']), round(M_r['m01'] / M_r['m00'])), 5, (0, 255, 0), -1)
        cv2.circle(blurred_full_mask, (round(M_g['m10'] / M_g['m00']), round(M_g['m01'] / M_g['m00'])), 5, (0, 255, 0), -1)
        cv2.circle(blurred_full_mask, (round(M_o['m10'] / M_o['m00']), round(M_o['m01'] / M_o['m00'])), 5, (0, 255, 0), -1)


        cv2.drawContours(blurred_full_mask, w_contours, -1, (0, 255, 0), 3)
        cv2.drawContours(blurred_full_mask, y_contours, -1, (0, 255, 0), 3)
        cv2.drawContours(blurred_full_mask, o_contours, -1, (0, 255, 0), 3)
        cv2.drawContours(blurred_full_mask, r_contours, -1, (0, 255, 0), 3)
        cv2.drawContours(blurred_full_mask, g_contours, -1, (0, 255, 0), 3)
        cv2.drawContours(blurred_full_mask, b_contours, -1, (0, 255, 0), 3)
        '''
        
        cv2.imshow("Result", img)
        cv2.imshow("white mask", blurred_full_mask)


        #cv2.imshow("mask", mask)
        #cv2.imshow("colored mask", colored_mask)
        #cv2.imshow("median blur", median_blur)

        if cv2.waitKey(1) & 0xFF == ord("q"):   break
    file.close()

if __name__ == "__main__":
    start()