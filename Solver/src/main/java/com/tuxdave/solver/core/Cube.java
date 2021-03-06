package com.tuxdave.solver.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;

import com.google.common.collect.HashBiMap;
import com.tuxdave.solver.extra.Color;
import com.tuxdave.solver.extra.MoveListener;
import com.tuxdave.solver.extra.Position;
import com.tuxdave.solver.extra.Utils;

public class Cube {
    private Face[] faces;

    /**
     * default position: front: green up: yellow right: orange
     */
    private HashBiMap<String, String> orientation = HashBiMap.create();

    public MoveListener moveListener;

    private void setBaseOrientation() {
        orientation.put("up", "yellow");
        orientation.put("front", "green");
        orientation.put("right", "orange");
    }

    public final static HashBiMap<String, String> oppositeColors = HashBiMap.create();
    {
        oppositeColors.put("red", "orange");
        oppositeColors.put("white", "yellow");
        oppositeColors.put("blue", "green");
        oppositeColors.put("orange", "red");
        oppositeColors.put("yellow", "white");
        oppositeColors.put("green", "blue");
    }

    /**
     * @param path location of the cube source file
     * @throws IOException        if the file not exists
     * @throws URISyntaxException
     */
    public Cube(String path) throws IOException, URISyntaxException {
        this(new File(path).toURI().toURL());
    }

    public Cube(Cube toClone) throws IOException, URISyntaxException {
        this();
        for (int i = 0; i < 6; i++) {
            faces[i] = new Face(toClone.faces[i]);
        }
        orientation.put("up", "1");
        orientation.put("front", "2");
        orientation.put("right", "3");
        orientation.put("up", toClone.orientation.get("up"));
        orientation.put("front", toClone.orientation.get("front"));
        orientation.put("right", toClone.orientation.get("right"));
    }

    /**
     * creates a solved cube
     * 
     * @throws IOException
     * @throws URISyntaxException
     */
    public Cube() throws IOException, URISyntaxException {// i created nre Face to get access to getClass
        this(new Face().getClass().getClassLoader().getResource("resources/solved.cube"));
    }

    /**
     * @param file a FileReader opening the cube source file
     * @throws IOException        if the file not exists
     * @throws URISyntaxException
     */
    public Cube(URL file) throws IOException, URISyntaxException {
        int[] cube = new int[54];
        {// reads the cube from file
            String[] stringFile = Utils.fromFileToString(file).split("\n");
            for (int i = 0; i < 54; i++) {
                cube[i] = Integer.parseInt(stringFile[i]);
            }
        }
        // creates the faces
        faces = new Face[6];
        int n = 0, start = 0;
        do {
            faces[n++] = new Face(Arrays.copyOfRange(cube, start, start + 9));
            start += 9;
            // System.out.println(faces[n-1]);
        } while (n != 6);
        // setting the orientation
        setBaseOrientation();
    }

    public Cube(int[] cube) {
        if (cube.length != 54) {
            throw new IllegalArgumentException("The cube array must be a 54 length array");
        }
        // creates the faces
        faces = new Face[6];
        int n = 0, start = 0;
        do {
            faces[n++] = new Face(Arrays.copyOfRange(cube, start, start + 9));
            start += 9;
            // System.out.println(faces[n-1]);
        } while (n != 6);
        // setting the orientation
        setBaseOrientation();
    }

    /**
     * get one face in base the color of the center
     * 
     * @param color the color to get the face
     * @return the face, null if not found (so the color doesn't exist)
     */
    public Face getFaceByColor(String color) {
        return getFaceByColor(Face.fromColorToInt(color));
    }

    /**
     * get one face in base the color of the center
     * 
     * @param color the color to get the face written in int (0=white ecc)
     * @return the face required
     * @throws IllegalArgumentException when the color doesn't exist
     */
    public Face getFaceByColor(int color) {
        for (Face f : faces) {
            if (f.getSpot(0) == color) {
                return f;
            }
        }
        throw new IllegalArgumentException("Color \"" + color + "\" not found");
    }

    public Face getFaceByPosition(Position pos) {
        return getFaceByPosition(pos.getValue());
    }

    public Face getFaceByColor(Color c) {
        return getFaceByColor(c.getValue());
    }

    /**
     * get the current face on the selected position
     * 
     * @param pos up/down/right/left/front/back
     * @return the face pointer
     */
    public Face getFaceByPosition(String pos) {
        switch (pos.toLowerCase()) {
            case "up": {
                return getFaceByColor(orientation.get("up"));
            }
            case "down": {
                return getFaceByColor(oppositeColors.get(orientation.get("up")));
            }
            case "front": {
                return getFaceByColor(orientation.get("front"));
            }
            case "back": {
                return getFaceByColor(oppositeColors.get(orientation.get("front")));
            }
            case "right": {
                return getFaceByColor(orientation.get("right"));
            }
            case "left": {
                return getFaceByColor(oppositeColors.get(orientation.get("right")));
            }
            default: {
                throw new IllegalArgumentException("\"" + pos + "\" doesn't exist!");
            }
        }
    }

    // ORIENTATION MOVEMENT

    /**
     * r+ cube rotate on the x axis clockwise r- reverse u+ cube rotate on the y
     * axis clockwise u- reverse f+ cube rotate on the z axis clockwise f- reverse
     * 
     * @param dir r+/r-/u+/u-/f+/f-
     */
    public void reOrientate(String dir) {
        if (moveListener != null && dir.charAt(1) == '+') {
            moveListener.onRotate(dir);
        }

        String front = getFaceByPosition("front").getColor(), up = getFaceByPosition("up").getColor(),
                right = getFaceByPosition("right").getColor();
        boolean clockwise = (dir.toCharArray()[1] == '+' ? true : false);
        switch (dir.toLowerCase().toCharArray()[0]) {
            case 'r': {
                if (clockwise) {
                    up = getFaceByPosition("front").getColor();
                    front = getFaceByPosition("down").getColor();
                    // face rotations
                    getFaceByPosition("right").rotate();
                    getFaceByPosition("left").rotate(3);
                    getFaceByPosition("up").rotate(2);
                    getFaceByPosition("back").rotate(2);
                } else {
                    reOrientate("r+");
                    reOrientate("r+");
                    reOrientate("r+");
                    return;
                }
                break;
            }
            case 'u': {
                if (clockwise) {
                    front = getFaceByPosition("right").getColor();
                    right = getFaceByPosition("back").getColor();

                    getFaceByPosition("up").rotate();
                    getFaceByPosition("down").rotate(3);
                } else {
                    reOrientate("u+");
                    reOrientate("u+");
                    reOrientate("u+");
                    return;
                }
                break;
            }
            default:
            case 'f': {
                if (clockwise) {
                    up = getFaceByPosition("left").getColor();
                    right = getFaceByPosition("up").getColor();

                    getFaceByPosition("front").rotate();
                    getFaceByPosition("back").rotate(3);
                    getFaceByPosition("up").rotate();
                    getFaceByPosition("right").rotate();
                    getFaceByPosition("down").rotate();
                    getFaceByPosition("left").rotate();
                } else {
                    reOrientate("f+");
                    reOrientate("f+");
                    reOrientate("f+");
                    return;
                }
                break;
            }
        }
        orientation.put("front", "1");
        orientation.put("up", "2");
        orientation.put("right", "3");

        orientation.put("front", front);
        orientation.put("up", up);
        orientation.put("right", right);
    }

    /**
     * Method to move the cube's sides (the rotation is 90°, like r or r')
     * 
     * @param _move defines the cube notation (r, l, u, d, f, b)
     * @param _cw   defines its rotation (true = clockwise, false =
     *              counter-clockwise)
     * 
     * @author TuxDave-Kawagit
     */
    public void move(char _move, boolean _cw) {
        if (moveListener != null && _cw)
            moveListener.onMove(_move, _cw);

        switch (_move) {
            case 'l': {
                if (_cw) {
                    Face front = new Face(getFaceByPosition("front"));
                    getFaceByPosition("left").rotate();
                    getFaceByPosition("front").setBorder("l", getFaceByPosition("up").getBorder("l"));
                    getFaceByPosition("up").setBorder("l", reverse(getFaceByPosition("back").getBorder("r")));
                    getFaceByPosition("back").setBorder("r", reverse(getFaceByPosition("down").getBorder("l")));
                    getFaceByPosition("down").setBorder("l", front.getBorder("l"));
                } else {
                    move('l', true);
                    move('l', true);
                    move('l', true);
                }
                break;
            }
            case 'r': {
                if (_cw) {
                    Face front = new Face(getFaceByPosition("front"));
                    getFaceByPosition("right").rotate();
                    getFaceByPosition("front").setBorder("r", getFaceByPosition("down").getBorder("r"));
                    getFaceByPosition("down").setBorder("r", reverse(getFaceByPosition("back").getBorder("l")));
                    getFaceByPosition("back").setBorder("l", reverse(getFaceByPosition("up").getBorder("r")));
                    getFaceByPosition("up").setBorder("r", front.getBorder("r"));
                } else {
                    move('r', true);
                    move('r', true);
                    move('r', true);
                }
                break;
            }
            case 'u': {
                if (_cw) {
                    Face front = new Face(getFaceByPosition("front"));
                    getFaceByPosition("up").rotate();
                    getFaceByPosition("front").setBorder("u", getFaceByPosition("right").getBorder("u"));
                    getFaceByPosition("right").setBorder("u", getFaceByPosition("back").getBorder("u"));
                    getFaceByPosition("back").setBorder("u", getFaceByPosition("left").getBorder("u"));
                    getFaceByPosition("left").setBorder("u", front.getBorder("u"));
                } else {
                    move('u', true);
                    move('u', true);
                    move('u', true);
                }
                break;
            }
            case 'd': {
                if (_cw) {
                    Face front = new Face(getFaceByPosition("front"));
                    getFaceByPosition("down").rotate();
                    getFaceByPosition("front").setBorder("d", getFaceByPosition("left").getBorder("d"));
                    getFaceByPosition("left").setBorder("d", getFaceByPosition("back").getBorder("d"));
                    getFaceByPosition("back").setBorder("d", getFaceByPosition("right").getBorder("d"));
                    getFaceByPosition("right").setBorder("d", front.getBorder("d"));
                } else {
                    move('d', true);
                    move('d', true);
                    move('d', true);
                }
                break;
            }
            case 'f': {
                if (_cw) {
                    Face up = new Face(getFaceByPosition("up"));
                    getFaceByPosition("front").rotate();
                    getFaceByPosition("up").setBorder("d", reverse(getFaceByPosition("left").getBorder("r")));
                    getFaceByPosition("left").setBorder("r", getFaceByPosition("down").getBorder("u"));
                    getFaceByPosition("down").setBorder("u", reverse(getFaceByPosition("right").getBorder("l")));
                    getFaceByPosition("right").setBorder("l", up.getBorder("d"));
                } else {
                    move('f', true);
                    move('f', true);
                    move('f', true);
                }
                break;
            }
            case 'b': {
                if (_cw) {
                    Face up = new Face(getFaceByPosition("up"));
                    getFaceByPosition("back").rotate();
                    getFaceByPosition("up").setBorder("u", getFaceByPosition("right").getBorder("r"));
                    getFaceByPosition("right").setBorder("r", reverse(getFaceByPosition("down").getBorder("d")));
                    getFaceByPosition("down").setBorder("d", getFaceByPosition("left").getBorder("l"));
                    getFaceByPosition("left").setBorder("l", reverse(up.getBorder("u")));
                } else {
                    move('b', true);
                    move('b', true);
                    move('b', true);
                }
                break;
            }
        }
    }

    /**
     * Method to move the cube's sides (the rotation is 180°, like r2)
     * 
     * @param string defines the cube notation (r, l, u, d, f, b)
     * 
     * @author TuxDave Kawa-git
     */
    public void move(char string) {
        move(string, true);
        move(string, true);
    }

    private static int[] reverse(int[] ar) {
        ar = ar.clone();
        int[] ret = new int[ar.length];
        for (int i = 0; i < ar.length; i++) {
            ret[ar.length - 1 - i] = ar[i];
        }
        return ret;
    }

    // overrides

    @Override
    public String toString() {
        String ret = "";
        String[] poses = { "up", "front", "right", "back", "left", "down" };
        for (int i = 0; i < poses.length; i++) {
            ret += getFaceByPosition(poses[i]) + "\n\n";
        }
        return ret;
    }

    public String toStringLinear() {
        String ret = "";
        String[] poses = { "up", "front", "right", "back", "left", "down" };
        for (int i = 0; i < poses.length; i++) {
            Face face = getFaceByPosition(poses[i]);
            for (int j = 0; j < 9; j++) {
                ret += Integer.toString(face.getSpot(j));
            }
        }
        return ret;
    }
}
