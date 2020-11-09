package com.tuxdave.solver.core;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import com.google.common.collect.HashBiMap;

public class Cube {
    private Face[] faces;

    /**
     * default position: front: green up: yellow right: orange
     */
    private HashBiMap<String, String> orientation = HashBiMap.create();
    private void setBaseOrientation(){
        orientation.put("up", "yellow");
        orientation.put("front", "green");
        orientation.put("right", "orange");
    }

    private HashMap<Character, Integer> rotateCounter = new HashMap<Character, Integer>();

    public final static HashBiMap<String, String> oppositeColors = HashBiMap.create();{
        oppositeColors.put("red", "orange");
        oppositeColors.put("white", "yellow");
        oppositeColors.put("blue", "green");
        oppositeColors.put("orange", "red");
        oppositeColors.put("yellow", "white");
        oppositeColors.put("green", "blue");
    }

    /**
     * @param path location of the cube source file
     * @throws IOException if the file not exists
     */
    public Cube(String path) throws IOException {
        this(new FileReader(path));
    }

    /**
     * @param file a FileReader opening the cube source file
     * @throws IOException if the file not exists
     */
    public Cube(FileReader file) throws IOException {
        int[] cube = new int[54];
        {//reads the cube from file
            int temp = 0;
            char c;
            do{
                c = (char) file.read();
                if(c != '\n'){
                    cube[temp++] = Integer.parseInt(String.valueOf(c));
                }
            }while(temp != 54);
            file.close();
            /*for(int i = 0; i < 54; i++){
                System.out.println(cube[i]);
            }*/
        }{//creates the faces
            faces = new Face[6];
            int n = 0, start = 0;
            do{
                faces[n++] = new Face(Arrays.copyOfRange(cube, start, start+9));
                start += 9;
                //System.out.println(faces[n-1]);
            }while(n != 6);
        }{//setting the orientation
            setBaseOrientation();
        }{
            rotateCounter.put('r', 0);
            rotateCounter.put('l', 0);
            rotateCounter.put('u', 0);
            rotateCounter.put('f', 0);
            rotateCounter.put('d', 0);
            rotateCounter.put('b', 0);
        }
        //getFaceByColor("white").rotate();
    }

    /**
     * get one face in base the color of the center
     * @param color the color to get the face
     * @return the face, null if not found (so the color doesn't exist)
     */
    public Face getFaceByColor(String color){
        return getFaceByColor(Face.fromColorToInt(color));
    }

    /**
     * get one face in base the color of the center
     * @param color the color to get the face written in int (0=white ecc)
     * @return the face required
     * @throws IllegalArgumentException when the color doesn't exist
     */
    public Face getFaceByColor(int color){
        for(Face f : faces){
            if(f.getSpot(0) == color){
                return f;
            }
        }
        throw new IllegalArgumentException("Color \"" + color + "\" not found");
    }

    /**
     * get the current face on the selected position
     * @param pos up/down/right/left/front/back
     * @return the face pointer
     */
    public Face getFaceByPosition(String pos){
        switch(pos.toLowerCase()){
            case "up":{
                return getFaceByColor(orientation.get("up"));
            }
            case "down":{
                return getFaceByColor(oppositeColors.get(orientation.get("up")));
            }
            case "front":{
                return getFaceByColor(orientation.get("front"));
            }
            case "back":{
                return getFaceByColor(oppositeColors.get(orientation.get("front")));
            }
            case "right":{
                return getFaceByColor(orientation.get("right"));
            }
            case "left":{
                return getFaceByColor(oppositeColors.get(orientation.get("right")));
            }
            default:{
                throw new IllegalArgumentException("\"" + pos + "\" doesn't exist!");
            }
        }
    }

    //ORIENTATION MOVEMENT

    /**
     * r+ cube rotate on the x axis clockwise
     * r- reverse
     * u+ cube rotate on the y axis clockwise
     * u- reverse
     * f+ cube rotate on the z axis clockwise
     * f- reverse
     * @param dir r+/r-/u+/u-/f+/f-
    */    
    /*public void reOrientate(String dir){
        String front = getFaceByPosition("front").getColor(), up = getFaceByPosition("up").getColor(), right = getFaceByPosition("right").getColor();
        boolean clockwise = (dir.toCharArray()[1] == '+' ? true : false);
        switch(dir.toLowerCase().toCharArray()[0]){
            case 'r':{
                if(clockwise){
                    front = getFaceByPosition("down").getColor();
                    up = getFaceByPosition("front").getColor();
                    getFaceByPosition("right").rotate();
                    getFaceByPosition("left").rotate(3);
                }else{
                    front = getFaceByPosition("up").getColor();
                    up = getFaceByPosition("back").getColor();
                    getFaceByPosition("left").rotate();
                    getFaceByPosition("right").rotate(3);
                }
                break;
            }
            case 'u':{
                if(clockwise){
                    front = getFaceByPosition("right").getColor();
                    right = getFaceByPosition("back").getColor();
                    getFaceByPosition("up").rotate();
                    getFaceByPosition("down").rotate(3);
                }else{
                    front = getFaceByPosition("left").getColor();
                    right = getFaceByPosition("front").getColor();
                    getFaceByPosition("down").rotate();
                    getFaceByPosition("up").rotate(3);
                }
                break;
            }
            default:
            case 'f':{
                if(clockwise){
                    up = getFaceByPosition("left").getColor();
                    right = getFaceByPosition("up").getColor();
                    getFaceByPosition("front").rotate();
                    getFaceByPosition("back").rotate(3);
                }else{
                    up = getFaceByPosition("right").getColor();
                    right = getFaceByPosition("down").getColor();
                    getFaceByPosition("back").rotate();
                    getFaceByPosition("front").rotate(3);
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
    }*/

    /**
     * the base rotate to do all the others rotation, it's a r or r'
     * @param clockwise true if needs the clockwise rotation
     */
    public void baseRotate(boolean clockwise){
        if(clockwise){
            Face front = new Face(getFaceByPosition("front"));
            getFaceByPosition("right").rotate();
            getFaceByPosition("front").setBorder("r", getFaceByPosition("down").getBorder("r"));
            getFaceByPosition("down").setBorder("r", getFaceByPosition("back").getBorder("l"));
            getFaceByPosition("back").setBorder("l", getFaceByPosition("up").getBorder("r"));
            getFaceByPosition("up").setBorder("r", front.getBorder("r"));
        }else{
            baseRotate(true);
            baseRotate(true);
            baseRotate(true);
        }
    }

    /**
     * Method to move the cube's sides (the rotation is 90°, like r or r')
     * @param _move defines the cube notation (r, l, u, d, f, b)
     * @param _cw defines its rotation (true = clockwise, false = counter-clockwise)
     * 
     * @author TuxDave-Kawagit
     */
    public void move(char _move, boolean _cw){
        switch(_move){
            case 'l':{
                if(_cw){
                    //boolean temp = rotateCounter.get('l') % 4 != 0;//TODO: capire perchè duplica il bianco
                    Face front = new Face(getFaceByPosition("front"));

                    getFaceByPosition("left").rotate();
                    getFaceByPosition("front").setBorder("l", getFaceByPosition("up").getBorder("l"));
                    getFaceByPosition("up").setBorder("l", reverse(getFaceByPosition("back").getBorder("r")));
                    getFaceByPosition("back").setBorder("r", reverse(getFaceByPosition("down").getBorder("l")));
                    getFaceByPosition("down").setBorder("l", front.getBorder("l"));

                    //rotateCounter.put('l', rotateCounter.get('l') + 1);
                }else{
                    move('l', true);
                    move('l', true);
                    move('l', true);
                }
                break;
            }
            case 'r':{
                if(_cw){
                    Face front = new Face(getFaceByPosition("front"));
                    getFaceByPosition("right").rotate();
                    getFaceByPosition("front").setBorder("r", getFaceByPosition("down").getBorder("r"));
                    getFaceByPosition("down").setBorder("r", getFaceByPosition("back").getBorder("l"));
                    getFaceByPosition("back").setBorder("l", getFaceByPosition("up").getBorder("r"));
                    getFaceByPosition("up").setBorder("r", front.getBorder("r"));
                }else{rotateCounter.put('l', rotateCounter.get('l') + 1);
                    move('r', true);
                }
                break;
            }
            case 'u':{
                if(_cw){
                    Face front = new Face(getFaceByPosition("front"));
                    getFaceByPosition("up").rotate();
                    getFaceByPosition("front").setBorder("u", rotateCounter.get('u') % 2 == 0 ? getFaceByPosition("right").getBorder("u") : reverse(getFaceByPosition("right").getBorder("u")));
                    getFaceByPosition("right").setBorder("u", rotateCounter.get('u') % 2 == 0 ? getFaceByPosition("back").getBorder("u") : reverse(getFaceByPosition("back").getBorder("u")));
                    getFaceByPosition("back").setBorder("u", rotateCounter.get('u') % 2 == 0 ? getFaceByPosition("left").getBorder("u") : reverse(getFaceByPosition("left").getBorder("u")));
                    getFaceByPosition("left").setBorder("u", rotateCounter.get('u') % 2 == 0 ? front.getBorder("u") : reverse(front.getBorder("u")));
                    
                    rotateCounter.put('u', rotateCounter.get('u') + 1);
                }else{
                    move('u', true);
                    move('u', true);
                    move('u', true);
                }
                break;

            }
            case 'd':{
                break;
            }
            case 'f':{
                break;
            }
            case 'b':{
                break;
            }
        }
    }

    private static int[] reverse(int[] ar){
        ar = ar.clone();
        int[] ret = new int[ar.length];
        for(int i = 0; i < ar.length; i++){
            ret[ar.length-1-i] = ar[i];
        }
        return ret;
    }

    /**
     * Method to move the cube's sides (the trotation is 180°, like r2)
     * @param _move defines the cube notation (r, l, u, d, f, b)
     * 
     * @author TuxDave
     */
    public void move(char _move){
        move(_move, true);
        move(_move, true);
    }

    //overrides

    @Override
    public String toString(){
        String ret = "";
        String[] poses = {"up","front","right","back","left","down"};
        for(int i = 0; i < poses.length; i++){
            ret += getFaceByPosition(poses[i]) + "\n\n";
        }
        return ret;
    }
}
