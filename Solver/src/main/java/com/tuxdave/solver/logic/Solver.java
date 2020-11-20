package com.tuxdave.solver.logic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Random;

import com.google.common.collect.HashBiMap;
import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;
import com.tuxdave.solver.extra.ValueNotInRangeException;

public class Solver {
    Cube core;
    Scrambler scrambler;

    public HashBiMap<Integer, Character> FROM_NUMBER_TO_MOVE = HashBiMap.create();

    /**
     * construct the Solver
     * 
     * @param _c the cube to solve
     */
    public Solver(Cube _c) {
        {
            FROM_NUMBER_TO_MOVE.put(0, 'r');
            FROM_NUMBER_TO_MOVE.put(1, 'l');
            FROM_NUMBER_TO_MOVE.put(2, 'b');
            FROM_NUMBER_TO_MOVE.put(3, 'f');
            FROM_NUMBER_TO_MOVE.put(4, 'u');
            FROM_NUMBER_TO_MOVE.put(5, 'd');
        }
        setCoreCube(_c);
        scrambler = new Scrambler();
    }

    public Cube getCoreCube() {
        return core;
    }

    public void setCoreCube(Cube _new) {
        this.core = _new;
    }

    /**
     * scramble a solved cube randomly, THIS RESETS THE CUBE
     * 
     * @param moves number of moves to do
     * @return the string containing the scramble sequence
     */
    public String scramble(int moves) throws ValueNotInRangeException{
        String history = "";
        String[] arr = scrambler.getScramble(moves);
        for(String s : arr){
            history += s + " ";
        }
        return history;
    }

    public String scramble() throws ValueNotInRangeException{
        return scramble(21);
    }

    /**
     * scramble the cube in base the sequence passed, THIS RESETS THE CUBE
     * @param moves the scramble sequence (es: R U2 L')
     */
    public void scramble(String moves){
        try {
            core = new Cube();
        } catch (IOException | URISyntaxException ignored) {}
        
        String[] sequence = moves.toLowerCase().split(" ");
        for(String move : sequence){
            if(move.length() > 1){
                if(move.toCharArray()[1] == '\'')
                    getCoreCube().move(move.toCharArray()[0], false);
                else
                    getCoreCube().move(move.toCharArray()[0]);
            }else
                getCoreCube().move(move.toCharArray()[0], true);
        }
    }

    private String makeWhiteCross(){
        String ret = "";
        Face whiteFace = core.getFaceByPosition("down");

        for(int i = 0; i < 4; i++){//for the vertical faces

            //when white is in the north spot (2)
            if(core.getFaceByPosition("front").getSpot(2) == Face.fromColorToInt("white")){
                while(whiteFace.getSpot(4) == Face.fromColorToInt("white")){
                    core.move('d', false);
                }
                core.move('f', true);
                core.move('r', false);
                core.move('f', false);
            }
            //when white is in the easth spot (4)
            if(core.getFaceByPosition("front").getSpot(4) == Face.fromColorToInt("white")){
                while(whiteFace.getSpot(4) == Face.fromColorToInt("white")){
                    core.move('d', false);
                }
                core.move('r', false);
            }
            //when white is in the south spot (6)
            if(core.getFaceByPosition("front").getSpot(6) == Face.fromColorToInt("white")){
                core.move('f', false);
                core.move('d', true);
                core.move('r', false);
            }
            //when white is in the west spot (8)
            if(core.getFaceByPosition("front").getSpot(2) == Face.fromColorToInt("white")){
                while(whiteFace.getSpot(8) == Face.fromColorToInt("white")){
                    core.move('d', false);
                }
                core.move('l', true);
            }

            core.reOrientate("u+");
        }
        //for the yellow face
        int add = 4;
        for(int i = 2; i < 9; i = i + 2){
            if(core.getFaceByPosition("up").getSpot(i) == Face.fromColorToInt("white")){
                while(whiteFace.getSpot((i % 4 == 0 ? i : i + add)) == Face.fromColorToInt("white")){
                    core.move('d', false);
                }
                switch(i){
                    case 2:
                        core.move('b');
                        break;
                    case 4:
                        core.move('r');
                        break;
                    case 6:
                        core.move('f');
                        break;
                    case 8:
                        core.move('l');
                }
                add = -add;//TODO: Togliere index out of bound perchè ho usato add invece che il dict ecc
            }
        }

        return ret;
    }

    public String solve(){
        makeWhiteCross();
        return "";
    }
}
