package com.tuxdave.solver.logic;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import com.google.common.collect.HashBiMap;
import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;
import com.tuxdave.solver.extra.JsonManager;
import com.tuxdave.solver.extra.MoveListener;
import com.tuxdave.solver.extra.ValueNotInRangeException;

import org.json.JSONArray;
import org.json.JSONObject;

public class Solver implements MoveListener{
    private Cube core;
    private Scrambler scrambler;
    private JsonManager algorithms;
    private ArrayList<String> moveHistory;

    public HashBiMap<Integer, Character> FROM_NUMBER_TO_MOVE = HashBiMap.create();

    /**
     * construct the Solver
     * 
     * @param _c the cube to solve
     * @throws URISyntaxException
     * @throws IOException
     */
    public Solver(Cube _c) throws IOException, URISyntaxException {
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
        moveHistory = new ArrayList<String>();
        algorithms = new JsonManager(new File(new Face().getClass().getResource("/algorithm.json").toURI()));
    }

    public Cube getCoreCube() {
        return core;
    }

    public void setCoreCube(Cube _new) {
        this.core = _new;
        this.core.moveListener = this;
        System.out.println("");
    }

    public String getMoveHistory(){
        String s = "";
        for(String s1 : moveHistory){
            s += s1 + " ";
        }
        return s;
    }

    public JsonManager getAlgorithms(){
        return algorithms;
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
        scramble(history);
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
            core.moveListener = this;
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
    
    public void runAlgorithm(String algName){
        runAlgorithm(algorithms.getValueByKey(algName).split(" "));
    }

    /**
     * Gets the selected algorithm form a file
     */
    public void runAlgorithm(String[] alg){
        for(String s : alg){
            s = s.toLowerCase();
            if(s.length() == 1){
                core.move(s.toCharArray()[0], true);//clockwise
            }else if(s.charAt(1) == '\''){
                core.move(s.charAt(0), false);//counter-clockwise
            }else{
                core.move(s.charAt(0));//two spins
            }
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
        for(int i = 2; i < 9; i += 2){
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
            }
            if(i == 4){
                add = -4;
            }
        }

        //make the white cross right
        //TODO: fare la croce bianca giusta
        return ret;
    }

    public String solve(){
        makeWhiteCross();
        return "";
    }

    @Override
    public void onMove(char m, boolean cw) {
        moveHistory.add(Character.toString(Character.toUpperCase(m)) + (cw ? "" : "'"));
    }
}
