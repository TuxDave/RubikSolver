package com.tuxdave.solver.logic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.tree.FixedHeightLayoutCache;

import com.google.common.collect.HashBiMap;
import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;
import com.tuxdave.solver.extra.JsonManager;
import com.tuxdave.solver.extra.MoveListener;
import com.tuxdave.solver.extra.Position;
import com.tuxdave.solver.extra.Utils;
import com.tuxdave.solver.extra.ValueNotInRangeException;

import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.lang3.ObjectUtils.Null;
import org.json.JSONException;

public class Solver implements MoveListener {
    private Cube core;
    private Scrambler scrambler;
    private JsonManager algorithms;
    private Algorithm moveHistory;
    private int baseColor = -1;

    // about myself as moveListener in other obj...
    private boolean active = true;

    /**
     * turn down the logger
     */
    public void turnOnLogger() {
        active = true;
    }

    /**
     * turn on the logger
     */
    public void turnOffLogger() {
        active = false;
    }

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
    }

    public void setBaseColor() throws IOException, URISyntaxException {
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
    public void runAlgorithm(String algName) throws JSONException {
        Algorithm a = new Algorithm(algorithms.getValueByKey(algName));
        runAlgorithm(a);
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
                continue;
            } else if (s.charAt(0) == 'y') {
                core.reOrientate(Character.toString('u') + s.charAt(1));
                continue;
            } else if (s.charAt(0) == 'z') {
                core.reOrientate(Character.toString('f') + s.charAt(1));
                continue;
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

    private boolean is4InTheCorrectPosition() {
        Face rightFace = core.getFaceByPosition(Position.RIGHT);
        Face frontFace = core.getFaceByPosition(Position.FRONT);
        if (frontFace.getSpot(4) == frontFace.getColorInt() && rightFace.getSpot(8) == rightFace.getColorInt()) {
            return true;
        }
        return false;
    }

    /**
     * always related the 5 spot of the front face
     * 
     * @return true if is in the correct position
     */
    private boolean is5InTheCorrectPosition() {
        Face rightFace = core.getFaceByPosition(Position.RIGHT);
        Face frontFace = core.getFaceByPosition(Position.FRONT);
        Face downFace = core.getFaceByPosition(Position.DOWN);
        if (frontFace.getSpot(5) == frontFace.getColorInt() && downFace.getSpot(3) == baseColor
                && rightFace.getSpot(7) == rightFace.getColorInt()) {
            return true;
        }
        return false;
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
        makeDownCross();

        // found the baseColor piece in the right column if exist
        final int checkPointFront[] = { 3, 5 };
        final int checkPointRight[] = { 1, 7 };
        final int checkPointDown = 3;
        final int checkPointUp = 5;

        Face rightFace;
        Face frontFace;
        Face upFace;
        Face downFace;

        for (int i = 0; i < 4; i++) {
            // prendo tutte le faccie interessate tutte le volte
            rightFace = core.getFaceByPosition(Position.RIGHT);
            frontFace = core.getFaceByPosition(Position.FRONT);
            downFace = core.getFaceByPosition(Position.DOWN);
            upFace = core.getFaceByPosition(Position.UP);

            // spostare tutti i baseColor in cima (layer up)
            if (frontFace.getSpot(checkPointFront[1]) == baseColor || rightFace.getSpot(checkPointRight[1]) == baseColor
                    || downFace.getSpot(checkPointDown) == baseColor) {
                if (!is5InTheCorrectPosition()) {
                    while (frontFace.getSpot(checkPointFront[0]) == baseColor
                            || rightFace.getSpot(checkPointRight[0]) == baseColor
                            || upFace.getSpot(checkPointUp) == baseColor) {
                        // se sopra c'è un pezzo bianco faccio u per trovare uno spazio vuoto
                        core.move('u', true);
                    }
                    runAlgorithm("sexyMove");
                }
            }
            core.reOrientate("u+");
        }

        // assegnazione colori richiesti e presenti
        int[] current = new int[3];// the fact color on 3 spot of front and the adiacent
        int[] expected = new int[3];// the expected color in 5 spot of front and the adiacent
        for (int i = 0; i < 4; i++) {
            rightFace = core.getFaceByPosition(Position.RIGHT);
            frontFace = core.getFaceByPosition(Position.FRONT);
            downFace = core.getFaceByPosition(Position.DOWN);
            upFace = core.getFaceByPosition(Position.UP);

            while (!is5InTheCorrectPosition()) {
                expected[0] = frontFace.getColorInt();
                expected[1] = rightFace.getColorInt();
                expected[2] = downFace.getColorInt();
                current[0] = frontFace.getSpot(checkPointFront[0]);
                current[1] = upFace.getSpot(checkPointUp);
                current[2] = rightFace.getSpot(checkPointRight[0]);
                Arrays.sort(expected);
                Arrays.sort(current);

                if (expected[0] == current[0] && expected[1] == current[1] && expected[2] == current[2]) {
                    int counter = 0;
                    turnOffLogger();
                    while (!is5InTheCorrectPosition()) {
                        counter++;
                        runAlgorithm("sexyMove");
                    }
                    for (int k = counter; k < 6; k++) {
                        runAlgorithm("sexyMove");
                    }
                    turnOnLogger();
                    boolean anti = false;
                    if (counter > 6 - counter) {
                        counter = 6 - counter;
                        anti = true;
                    }
                    for (int k = 0; k < counter; k++) {
                        if (anti) {
                            runAlgorithm("antiSexyMove");
                        } else {
                            runAlgorithm("sexyMove");
                        }
                    }
                } else {
                    core.move('u', true);
                }
            }
            core.reOrientate("u+");
        }
    }

    public void makeSecondLayer() {
        makeDownFace();
        Face frontFace;
        Face rightFace;
        Face upFace;

        int oppositeBaseColor = Face.fromColorToInt(Cube.oppositeColors.get(Face.colorMap.get(baseColor)));

        for (int k = 0; k < 16; k++) {// per le 4 faccie
            frontFace = core.getFaceByPosition(Position.FRONT);
            rightFace = core.getFaceByPosition(Position.RIGHT);
            upFace = core.getFaceByPosition(Position.UP);

            if (is4InTheCorrectPosition()) {// se è gia a posto salta la faccia
                core.reOrientate("u+");
            } else {
                // controllo se sopra c'è almeno un pezzetto spostabile
                boolean flag = false; // true se c'è un pezzetto spostabile
                turnOffLogger();
                for (int i = 0; i < 4; i++) {
                    if (frontFace.getSpot(2) != oppositeBaseColor && upFace.getSpot(6) != oppositeBaseColor) {
                        flag = true;
                    }
                    core.move('u', true);
                }
                turnOnLogger();
                if (!flag) {// se non ci sono spostabili
                    while (is4InTheCorrectPosition()) {// giro fino a quando non trovo un pezzetto messo male
                        core.reOrientate("u+");
                    }
                    runAlgorithm("slR");// uso questo per mettere lo spostabile messo male sopra
                }

                int[] required = new int[] { frontFace.getColorInt(), rightFace.getColorInt() };
                Arrays.sort(required);
                int[] current = new int[2];
                for (int i = 0; i < 4; i++) {// prendo i colori dello spot front2 di questo orientamento
                    current[0] = frontFace.getSpot(2);
                    current[1] = upFace.getSpot(6);
                    Arrays.sort(current);
                    if (Arrays.equals(current, required)) {// se è il cubetto giusto ed è frontale
                        if (frontFace.getColorInt() == frontFace.getSpot(2)) {
                            runAlgorithm("slR");
                        } else {
                            core.move('u', false);
                            core.reOrientate("u+");
                            runAlgorithm("slL");
                            core.reOrientate("u-");
                        }
                        break;// questo l'ho sistemato, prossima faccia
                    } else {
                        core.move('u', true);
                    }
                }
                core.reOrientate("u+");// non è detto che ho trovato il pezzetto giusto, ma passo alla prossima
                                       // ugualmente
            }
        }
    }

    public void makeOLL() throws IOException, URISyntaxException {
        makeSecondLayer();

        // tentativo di oll per ogni orientamento del upLayer
        Solver tester;
        int i;
        for (i = 0; i < 4; i++) {
            tester = new Solver(new Cube(core));
            for (int j = 0; j < i; j++) {
                tester.core.move('u', true);
            }
            try {
                tester.runAlgorithm(Utils.ollToString(tester.core));
            } catch (NullPointerException e) {
                // orientamento upLayer non coretto, al prossimo ciclo potrebbe mettersi aposto
                // ignored
            }
            Face upFace = tester.core.getFaceByPosition(Position.UP);
            int upColor = upFace.getColorInt();
            boolean ok = true;
            for (int c : upFace.getBody()) {
                if (c != upColor) {
                    ok = false;
                    break;
                }
            }
            if (ok) {
                break;
            }
        }
        if (i != 4) {
            for (int j = 0; j < i; j++) {
                core.move('u', true);
            }
            runAlgorithm(Utils.ollToString(core));
        } else {
            System.out.println(Utils.ollToString(core));
            core.move('u', true);
            System.out.println(Utils.ollToString(core));
            core.move('u', true);
            System.out.println(Utils.ollToString(core));
            core.move('u', true);
            System.out.println(Utils.ollToString(core));
            core.move('u', true);
            System.out.println("stop");
        }
    }

    private interface LocalUtils {
        /**
         * @param face the face to scan
         * @return true if the upper layer of face front is all equals
         */
        public boolean checkFrontUpLayerEquals(Face face);

        /**
         * @param face the face to scan
         * @return true if both edges of the upper layer of front face are equals
         */
        public boolean checkFrontEdgeEquals(Face face);

        /**
         * @param face the face to scan
         * @return true if the spot 0 == spot 2
         */
        public boolean check2EqualsTo0(Face face);
    }

    private void makePLL() throws IOException, URISyntaxException {
        makeOLL();

        Face frontFace;
        Face rightFace;
        Face backFace;

        LocalUtils utils = new LocalUtils() {
            @Override
            public boolean checkFrontUpLayerEquals(Face face) {
                return face.getSpot(1) == face.getSpot(2) && face.getSpot(2) == face.getSpot(3);
            }

            @Override
            public boolean checkFrontEdgeEquals(Face face) {
                return face.getSpot(1) == face.getSpot(3);
            }

            @Override
            public boolean check2EqualsTo0(Face face) {
                return face.getSpot(0) == face.getSpot(2);
            }

        };

        // adjust edges
        boolean tPermDone = false;
        frontFace = core.getFaceByPosition(Position.FRONT);
        rightFace = core.getFaceByPosition(Position.RIGHT);
        for (int i = 0; i < 4; i++) {
            if (utils.checkFrontEdgeEquals(frontFace) && !utils.checkFrontEdgeEquals(rightFace)) {
                core.move('u', true);
                runAlgorithm("TPerm");
                /*
                 * while (!utils.checkFrontEdgeEquals(frontFace)) { core.move('u', true); }
                 */
                tPermDone = true;
                break;
            } else {
                core.move('u', true);
            }
        }
        if (!tPermDone && !utils.checkFrontEdgeEquals(rightFace) && !utils.checkFrontEdgeEquals(frontFace)) {
            for (int i = 0; i < 4; i++) {
                if (frontFace.getSpot(1) == frontFace.getColorInt()) {
                    runAlgorithm("YPerm");
                    break;
                } else {
                    core.move('u', true);
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            if (frontFace.getSpot(1) == frontFace.getColorInt()) {
                break;
            }
            core.move('u', true);
        }

        // edges adjusted, adjust corners
        if (utils.check2EqualsTo0(frontFace) && utils.check2EqualsTo0(rightFace)) {
            return;
        }
        boolean uPermDone = false;
        for (int i = 0; i < 4; i++) {
            if (utils.checkFrontUpLayerEquals(frontFace)) {
                uPermDone = true;
                core.move('u');
                if (frontFace.getSpot(2) == rightFace.getSpot(1)) {
                    runAlgorithm("UaPerm");
                } else {
                    runAlgorithm("UbPerm");
                }
                break;
            } else {
                core.move('u', true);
            }
        }

        if (!uPermDone) {
            backFace = core.getFaceByPosition(Position.BACK);
            if (frontFace.getSpot(2) == backFace.getColorInt()) {
                runAlgorithm("HPerm");
            } else {
                if (frontFace.getSpot(2) == rightFace.getColorInt()) {
                    runAlgorithm("ZPerm");
                } else {
                    runAlgorithm("Z1Perm");
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            if (frontFace.getSpot(1) == frontFace.getColorInt()) {
                break;
            }
            core.move('u', true);
        }
    }

    public String solve() throws IOException, URISyntaxException {
        setBaseColor();
        makePLL();
        return moveHistory.toString();
    }

    // ==========================================================================================================

    @Override
    public void onMove(char m, boolean cw) {
        if (active)
            moveHistory.add(Character.toString(Character.toUpperCase(m)) + (cw ? "" : "'"));
    }

    /*
     * r = x u = y f = z
     */
    @Override
    public void onRotate(String _dir) {
        if (active)
            if (_dir.charAt(0) == 'r') {
                moveHistory.add(Character.toString('x') + _dir.charAt(1));
            } else if (_dir.charAt(0) == 'u') {
                moveHistory.add(Character.toString('y') + _dir.charAt(1));
            } else {
                moveHistory.add(Character.toString('z') + _dir.charAt(1));
            }
    }
}
