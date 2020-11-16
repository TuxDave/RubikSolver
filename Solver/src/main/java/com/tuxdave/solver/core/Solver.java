package com.tuxdave.solver.core;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Random;

import com.google.common.collect.HashBiMap;

public class Solver {
    Cube core;

    public HashBiMap<Integer, Character> FROM_NUMBER_TO_MOVE = HashBiMap.create();

    public Cube getCoreCube() {
        return core;
    }

    public void setCoreCube(Cube _new) {
        this.core = _new;
    }

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
    }

    /**
     * scrumble a solved cube randomly, THIS RESETS THE CUBE
     * 
     * @param moves number of moves to do
     * @return the string containing the scrumble sequence
     */
    public String scrumble(int moves) {
        try {
            core = new Cube();
        } catch (IOException | URISyntaxException ignored) {}

        if(moves <= 0){
            throw new IllegalArgumentException("the moves (" + moves + ") number must be higher then 0");
        }
        String history = "";
        Random r = new Random();
        char move;
        int round = 0;
        for(int i = 0; i < moves; i++){
            move = FROM_NUMBER_TO_MOVE.get(r.nextInt(6));
            round = r.nextInt(3);
            if(round == 0){
                history += Character.toUpperCase(move) + " ";
                getCoreCube().move(move, true);
            }else if(round == 1){
                history += Character.toUpperCase(move) + "2 ";
                getCoreCube().move(move);
            }else{
                history += Character.toUpperCase(move) + "' ";
                getCoreCube().move(move, false);
            }
        }
        return history;
    }

    /**
     * scrumble the cube in base the sequence passed, THIS RESETS THE CUBE
     * @param moves the scrumble sequence (es: R U2 L')
     */
    public void scrumble(String moves){
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
}
