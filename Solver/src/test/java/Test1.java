import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Algorithm;
import com.tuxdave.solver.logic.Solver;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

public class Test1 {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver solver = new Solver(new Cube());
        FileWriter r = new FileWriter(
                new File("/home/tuxdave/Documenti/MyProjects/RubikSolver/Solver/examples/outputWhiteFace.csv"));
        String[] s;
        String scramble;
        double avarageMoves = 0;
        String solve;
        int moves = 0;
        int tries = 10000;
        boolean ok = false;
        Face f;
        for (int i = 0; i < tries; i++) {
            scramble = solver.scramble(15);
            solver.solve();
            moves = 0;
            s = solver.getMoveHistory().split(" ");
            solve = "";
            for (String temp : s) {
                if (temp.charAt(0) != 'y') {
                    solve += temp + " ";
                }
            }
            solve = solve.substring(0, solve.length() - 1);
            for (String move : solve.split(" ")) {
                if (move.length() == 1) {
                    moves++;
                } else {
                    if (move.charAt(1) == '2') {
                        moves += 2;
                    } else {
                        moves++;
                    }
                }
            }

            f = solver.getCoreCube().getFaceByColor(0);
            ok = f.getSpot(0) == 0 && f.getSpot(2) == 0 && f.getSpot(4) == 0 && f.getSpot(6) == 0 && f.getSpot(8) == 0;
            r.write(scramble + "," + solver.getMoveHistory() + "," + moves + "," + ok + "\n");
            avarageMoves += moves;
        }
        avarageMoves /= tries;
        r.write("\n\n" + avarageMoves);
        r.close();

        // Solver solver = new Solver(new Cube());
        // solver.scramble("F2 D R2 U2 F U R F2 L U2 R2 B' D B L2");
        // solver.solve();
        // System.out.println(solver.getCoreCube());
        // System.out.println(solver.getMoveHistory());

        // Algorithm alg = new Algorithm("y+ F' D R' D' F R' F' y+ D L y+ y+");
        // alg.shorten();
        // alg.shorten();
        // alg.shorten();
        // alg.shorten();
        // System.out.println(alg);
    }
}
