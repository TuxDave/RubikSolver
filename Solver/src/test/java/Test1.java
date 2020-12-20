import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Solver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

public class Test1 {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver solver = new Solver(new Cube());
        FileWriter r = new FileWriter(
                new File("/home/tuxdave/Documenti/MyProjects/RubikSolver/Solver/examples/dopoColorNeutral.csv"));
        String scramble;
        double avarageMoves = 0;
        int tries = 10000;
        boolean ok = false;
        Face down;
        Face front;
        Face back;
        for (int i = 0; i < tries; i++) {
            scramble = solver.scramble(15);
            solver.solve();

            down = solver.getCoreCube().getFaceByPosition("down");
            front = solver.getCoreCube().getFaceByPosition("front");
            back = solver.getCoreCube().getFaceByPosition("back");
            int colorDown = down.getColorInt();
            int colorFront = front.getColorInt();
            int colorBack = back.getColorInt();
            ok = down.getSpot(0) == colorDown && down.getSpot(2) == colorDown && down.getSpot(4) == colorDown
                    && down.getSpot(6) == colorDown && down.getSpot(8) == colorDown && down.getSpot(1) == colorDown
                    && down.getSpot(3) == colorDown && down.getSpot(5) == colorDown && down.getSpot(7) == colorDown
                    && front.getSpot(5) == colorFront && front.getSpot(6) == colorFront
                    && front.getSpot(7) == colorFront && back.getSpot(5) == colorBack && back.getSpot(7) == colorBack
                    && back.getSpot(6) == colorBack;
            r.write(scramble + "," + solver.getMoveHistory() + "," + solver.getMoveHistory().getMoveLength() + "," + ok
                    + "\n");
            avarageMoves += solver.getMoveHistory().getMoveLength();
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
