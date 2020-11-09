package com.tuxdave.solver.core;

import com.google.common.collect.HashBiMap;
import java.util.Arrays;

public class Face {
    private int color;
    private int[] body;

    public static final HashBiMap<Integer, String> colorMap = HashBiMap.create();{
        colorMap.put(0, "white");
        colorMap.put(1, "blue");
        colorMap.put(2, "yellow");
        colorMap.put(3, "green");
        colorMap.put(4, "orange");
        colorMap.put(5, "red");
    }

    /**
     * @param _color the color to fill the face
     */
    public Face(String _color){
        setColor(_color);
        body = new int[9];
        Arrays.fill(body, color);
    }

    /**
     * @param other the face to copy
     */
    public Face(Face other){
        body = other.body.clone();
        setColor(body[0]);
    }

    public Face(int[] _colors){
        this();
        if(_colors.length != 9){
            throw new IllegalArgumentException("_colors must be an 9 length array!");
        }
        for(int i = 0; i < _colors.length; i++){
            if(_colors[i] >= 0 && _colors[i] < 6){
                body[i] = _colors[i];
            }else{
                throw new IllegalArgumentException("the numbers contained in the array must be 0-5!");
            }
        }
        setColor(_colors[0]);
    }

    /**
     * create a default face all white
     */
    public Face(){
        this("white");
    }

    /**
     * set the "color" attribute to a 0-5 number in base the value of the parameter
     * @param _color a color written in english ("white/yellow")
     */
    public void setColor(String _color) {
        _color = _color.toLowerCase();
        if(!colorMap.containsValue(_color)){
            throw new IllegalArgumentException("Color \"" + _color + "\" not valid!");
        }
        for(String s : colorMap.values()){
            if(s.equals(_color)){
                color = colorMap.inverse().get(_color);
            }
        }
    }
    /**
     * sets the "color" attribute to a 0-5 number in base the value of the parameter
     * @param _color a color written in number
     */
    private void setColor(int _color){
        if(_color < 6 && _color >= 1){
            color = _color;
        }
    }

    /**
     * get the color in human mode (using its name)
     * @return es: "white/yellow/ecc"
     */
    public String getColor(){
        return colorMap.get(body[0]);
    }

    /**
     * get the color position
     * @param _border u/d/r/l
     * @return provides an array with the column from below to top and from left to right
     */
    public int[] getBorder(String _border){
        switch(_border.toLowerCase()){
            case "u":
                return new int[]{body[1],body[2],body[3]}.clone();
            case "d":
                return new int[]{body[7],body[6],body[5]}.clone();
            default:
            case "r":
                return new int[]{body[3],body[4],body[5]}.clone();
            case "l":
                return new int[]{body[1],body[8],body[7]}.clone();
        }
    }

    /**
     * 
     * @param _border u/d/r/l
     * @param colors int array containing the colors from high to low or from left to right
     */
    public void setBorder(String _border, int[] colors){
        //checks
        if(colors.length != 3){
            throw new ArrayIndexOutOfBoundsException("The array containing the colors must be size = 3!");
        }
        for(int i = 0; i < 3; i++){
            if(colors[i] > 5 || colors[i] < 0){
                throw new IllegalArgumentException("Numbers must be between 0 and 5!");
            }
        }

        int[] _colors = colors.clone();
        switch(_border.toLowerCase()){
            case "u":
                body[1] = _colors[0];
                body[2] = _colors[1];
                body[3] = _colors[2];
                break;
            case "d":{    
                body[7] = _colors[0];
                body[6] = _colors[1];
                body[5] = _colors[2];
                break;
            }
            default:
            case "r":{    
                body[3] = _colors[0];
                body[4] = _colors[1];
                body[5] = _colors[2];
                break;
            }
            case "l":
                body[1] = _colors[0];
                body[8] = _colors[1];
                body[7] = _colors[2];
        }
    }

    /**
     * the positions are like this:
     * 1 2 3
     * 8 0 4
     * 7 6 5
     * to make easier the movements
     * so if you want to get the north-easth spot put 3
     * @param _pos the number of the required spot
     * @return the required spot color
     */
    public int getSpot(int _pos){
        if(_pos < 0 || _pos > 8){
            throw new ArrayIndexOutOfBoundsException("Index must be between 0 and 8 included!");
        }
        return body[_pos];
    }

    /**
     * set the color to one spot of the face
     * the positions are like this:
     * 1 2 3
     * 8 0 4
     * 7 6 5
     * to make easier the movements
     * so if you want to set the north-easth spot put 3
     * @param _pos the number of the interested spot
     * @param _newColor the color from 0 to 5 (you can use the converter static method fromColorToInt)
     */
    public void setSpot(int _pos, int _newColor){
        if(_pos < 0 || _pos > 8 || _newColor < 0 || _newColor > 5){
            throw new ArrayIndexOutOfBoundsException("Index must be between 0-8(_pos) and 0-5(_newColor) included!");
        }//else
        body[_pos] = _newColor;
        if(_pos == 0){
            color = _newColor;
        }
    }

    /**
     * totates the spot's colors in clock direction
     */
    public void rotate(){
        int[] temp = new int[]{body[7], body[8]}.clone();
        for(int i = 8; i > 0; i--){
            int l = (i-2);
            if(i > 2){
                body[i] = body[l];
            }else if(i == 2){
                body[2] = temp[1];
            }else if(i == 1){
                body[1] = temp[0];
            }
        }
    }
    /**
     * rotate "times" times
     * @param times how many totation must be
     */
    public void rotate(int times){
        for(int i = 0; i < times; i++){
            rotate();
        }
    }

    /**
     * convert a color from string to integer
     * @param _color the string format in english
     * @return the relative integer
     * 
     * @throws IllegalArgumentException when the color doesn't exist
     */
    public static int fromColorToInt(String _color){
        if(colorMap.containsValue(_color.toLowerCase())){
            return colorMap.inverse().get(_color.toLowerCase());
        }else{
            throw new IllegalArgumentException("insert a right color name!");
        }
    }

    @Override
    public String toString() {
        return body[1] + " " + body[2] + " " + body[3] + "\n" + body[8] + " " + body[0] + " " + body[4] + "\n" + body[7] + " " + body[6] + " " + body[5];
    }
}
