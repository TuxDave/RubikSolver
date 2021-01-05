import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;
import com.tuxdave.solver.extra.Position;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Solver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;

public class Test1 {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver solver = new Solver(new Cube());
        FileWriter r = new FileWriter(new File("notTracked/test10000.csv"));
        String scramble;
        double avarageMoves = 0;
        int tries = 10000;
        boolean ok = false;
        Face down;
        Face front;
        Face back;
        Face right;
        Face up;
        for (int i = 0; i < tries; i++) {
            scramble = solver.scramble(15);
            solver.solve();

            up = solver.getCoreCube().getFaceByPosition(Position.UP);
            down = solver.getCoreCube().getFaceByPosition("down");
            front = solver.getCoreCube().getFaceByPosition("front");
            back = solver.getCoreCube().getFaceByPosition("back");
            right = solver.getCoreCube().getFaceByPosition("right");
            int colorDown = down.getColorInt();
            int colorFront = front.getColorInt();
            int colorBack = back.getColorInt();
            int colorUp = up.getColorInt();
            ok = true;
            for (int k = 0; k < 6; k++) {
                Face f = solver.getCoreCube().getFaceByColor(k);
                for (int faceColor : f.getBody()) {
                    if (faceColor != k) {
                        ok = false;
                    }
                }
            }
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
