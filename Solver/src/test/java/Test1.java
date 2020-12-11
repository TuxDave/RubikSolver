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
        int tries = 5000;
        boolean ok = false;
        Face f;
        for (int i = 0; i < tries; i++) {
            scramble = solver.scramble(15);
            solver.solve();

            f = solver.getCoreCube().getFaceByPosition("down");
            int color = f.getColorInt();
            ok = f.getSpot(0) == color && f.getSpot(2) == color && f.getSpot(4) == color && f.getSpot(6) == color
                    && f.getSpot(8) == color;
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
