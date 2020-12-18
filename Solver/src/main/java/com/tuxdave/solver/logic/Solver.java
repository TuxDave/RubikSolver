package com.tuxdave.solver.logic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.google.common.collect.HashBiMap;
import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;
import com.tuxdave.solver.extra.JsonManager;
import com.tuxdave.solver.extra.MoveListener;
import com.tuxdave.solver.extra.Position;
import com.tuxdave.solver.extra.ValueNotInRangeException;

public class Solver implements MoveListener {
    private Cube core;
    private Scrambler scrambler;
    private JsonManager algorithms;
    private Algorithm moveHistory;
    private int baseColor = -1;

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
        moveHistory = new Algorithm();
        algorithms = new JsonManager(new Face().getClass().getClassLoader().getResource("resources/algorithm.json"));
    }

    public Cube getCoreCube() {
        return core;
    }

    public void setCoreCube(Cube _new) {
        this.core = _new;
        this.core.moveListener = this;
        System.out.println("");
    }

    private void setBaseColor() throws IOException, URISyntaxException {
        HashMap<Integer, Integer> moves = new HashMap<>();
        Cube original = new Cube(core);
        Cube pivot = new Cube(core);
        int orientationPointer = 0;
        String[] orienter = "r+ r+ r+ f+ f+ f+".split(" "); // white blue yellow green orange
                                                            // -blue- red
        int baseColorTemp;
        do {
            moveHistory.reset();// azzero il log delle mosse
            core = new Cube(new Cube(pivot));// clonato il cubo per testarlo
            core.moveListener = this;
            baseColorTemp = core.getFaceByPosition("down").getColorInt();
            makeDownCross(baseColorTemp);
            // System.out
            // .println("Color: " + baseColorTemp + " === " + moveHistory + " === " +
            // moveHistory.getMoveLength());
            moves.put(baseColorTemp, moveHistory.getMoveLength());
            try {
                pivot.reOrientate(orienter[orientationPointer]);
            } catch (Exception e) {
                // e.printStackTrace();
                pivot.reOrientate(orienter[orientationPointer - 1]);
            } // ignored out of bound
            orientationPointer++;
        } while (orientationPointer < 7);

        moveHistory.reset();
        core = original;
        core.moveListener = this;

        baseColorTemp = 0;
        int min = moves.get(baseColorTemp);
        int i = 0;
        for (int current : moves.values()) {
            if (current < min) {
                min = current;
                baseColorTemp = i;
            }
            i++;
        }
        this.baseColor = baseColorTemp;// assegnato
        // orientate the original cube
        // baseColorTemp--;
        orienter = "r+ r+ r+ f+ f+".split(" "); // white blue yellow green orange -blue- red
        for (i = 0; i < baseColorTemp; i++) {
            core.reOrientate(orienter[i]);
            if (i == 4) {
                core.reOrientate(orienter[i]);
            }

        }
    }

    public Algorithm getMoveHistory() {
        return moveHistory;
    }

    public JsonManager getAlgorithms() {
        return algorithms;
    }

    /**
     * scramble a solved cube randomly, THIS RESETS THE CUBE
     * 
     * @param moves number of moves to do
     * @return the string containing the scramble sequence
     */
    public String scramble(int moves) throws ValueNotInRangeException {
        String history = "";
        String[] arr = scrambler.getScramble(moves);
        for (String s : arr) {
            history += s + " ";
        }
        scramble(history);
        return history;
    }

    public String scramble() throws ValueNotInRangeException {
        return scramble(21);
    }

    /**
     * scramble the cube in base the sequence passed, THIS RESETS THE CUBE
     * 
     * @param moves the scramble sequence (es: R U2 L')
     */
    public void scramble(String moves) {
        try {
            core = new Cube();
            core.moveListener = this;
        } catch (IOException | URISyntaxException ignored) {
        }

        String[] sequence = moves.toLowerCase().split(" ");
        for (String move : sequence) {
            if (move.length() > 1) {
                if (move.toCharArray()[1] == '\'')
                    getCoreCube().move(move.toCharArray()[0], false);
                else
                    getCoreCube().move(move.toCharArray()[0]);
            } else
                getCoreCube().move(move.toCharArray()[0], true);
        }
        moveHistory = new Algorithm();
    }

    /**
     * run on the core cube the specified algorithm in base the cube orientation
     * 
     * @param algName the name of an existing algorithm
     */
    public void runAlgorithm(String algName) {
        runAlgorithm(new Algorithm(algorithms.getValueByKey(algName)));
    }

    /**
     * Gets the selected algorithm form a file
     * 
     * @param alg the algorithm
     */
    public void runAlgorithm(Algorithm alg) {
        for (String s : alg) {

            if (s.charAt(0) == 'x') {
                core.reOrientate(Character.toString('r') + s.charAt(1));
                return;
            } else if (s.charAt(0) == 'y') {
                core.reOrientate(Character.toString('u') + s.charAt(1));
                return;
            } else if (s.charAt(0) == 'z') {
                core.reOrientate(Character.toString('f') + s.charAt(1));
                return;
            }

            s = s.toLowerCase();
            if (s.length() == 1) {
                core.move(s.toCharArray()[0], true);// clockwise
            } else if (s.charAt(1) == '\'') {
                core.move(s.charAt(0), false);// counter-clockwise
            } else {
                core.move(s.charAt(0));// two spins
            }
        }
    }

    private void makeDownCross() {
        makeDownCross(this.baseColor);
    }

    /**
     * @param localBaseColor the base color
     * @return
     */
    private void makeDownCross(int localBaseColor) {
        Face downFace = core.getFaceByPosition("down");
        for (int z = 0; z < 3; z++)
            for (int i = 0; i < 4; i++) {// for the vertical faces

                for (int j = 0; j < 4; j++) {
                    // when white is in the south spot (6)
                    if (core.getFaceByPosition("front").getSpot(6) == localBaseColor) {
                        core.move('f', false);
                        core.move('d', true);
                        core.move('r', false);
                    }
                    // when white is in the north spot (2)
                    if (core.getFaceByPosition("front").getSpot(2) == localBaseColor) {
                        while (downFace.getSpot(4) == localBaseColor) {
                            core.move('d', false);
                        }
                        runAlgorithm(new Algorithm("F R' F'"));
                    }
                    // when white is in the easth spot (4)
                    if (core.getFaceByPosition("front").getSpot(4) == localBaseColor) {
                        while (downFace.getSpot(4) == localBaseColor) {
                            core.move('d', false);
                        }
                        core.move('r', false);
                    }
                    // when white is in the west spot (8)
                    if (core.getFaceByPosition("front").getSpot(8) == localBaseColor) {
                        while (downFace.getSpot(8) == localBaseColor) {
                            core.move('d', false);
                        }
                        core.move('l', true);
                    }
                }

                core.reOrientate("u+");
            }
        // for the yellow face
        int add = 4;
        for (int i = 2; i < 9; i += 2) {
            if (core.getFaceByPosition("up").getSpot(i) == localBaseColor) {
                while (downFace.getSpot((i % 4 == 0 ? i : i + add)) == localBaseColor) {
                    core.move('d', false);
                }
                switch (i) {
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
            if (i == 4) {
                add = -4;
            }
        }
        HashBiMap<Integer, String> fromUpToClosest = HashBiMap.create();
        fromUpToClosest.put(2, "back");
        fromUpToClosest.put(4, "right");
        fromUpToClosest.put(6, "front");
        fromUpToClosest.put(8, "left");

        for (int i = 0; i < 4; i++) {
            Face front = core.getFaceByPosition("front");
            Face up = core.getFaceByPosition("up");
            if (front.getSpot(0) != front.getSpot(6)) {
                core.move('f');

                int pos = 2;
                int spot = up.getSpot(fromUpToClosest.inverse().get(fromUpToClosest.values().toArray()[pos]));
                while (spot == localBaseColor) {
                    {
                        Face face = null;
                        int color = -1;
                        do {
                            core.move('u', true);
                            pos = ++pos % 4;
                            face = core.getFaceByPosition(fromUpToClosest
                                    .get(fromUpToClosest.inverse().get(fromUpToClosest.values().toArray()[pos])));
                            color = face.getSpot(2);
                        } while (color != face.getSpot(0));
                    } // il pezzettino è nel posto giusto
                    core.move(((String) fromUpToClosest.values().toArray()[pos]).charAt(0));
                    spot = up.getSpot(fromUpToClosest.inverse().get(fromUpToClosest.values().toArray()[pos]));
                }
            }

            core.reOrientate("u+");
        }
    }

    private void makeDownFace() {
        makeDownCross(core.getFaceByPosition(Position.DOWN).getColorInt());

    }

    public String solve() throws IOException, URISyntaxException {
        setBaseColor();
        makeDownFace();
        return "";
    }

    @Override
    public void onMove(char m, boolean cw) {
        moveHistory.add(Character.toString(Character.toUpperCase(m)) + (cw ? "" : "'"));
    }

    @Override
    public void onRotate(String _dir) {
        /*
         * r = x u = y f = z
         */
        if (_dir.charAt(0) == 'r') {
            moveHistory.add(Character.toString('x') + _dir.charAt(1));
        } else if (_dir.charAt(0) == 'u') {
            moveHistory.add(Character.toString('y') + _dir.charAt(1));
        } else {
            moveHistory.add(Character.toString('z') + _dir.charAt(1));
        }
    }
}
