import java.io.IOException;
import java.net.URISyntaxException;

import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.extra.Utils;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Algorithm;
import com.tuxdave.solver.logic.Solver;

public class RealTimeTest {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver s = new Solver(new Cube());
        // System.out.println(s.scramble(15));
        // s.setBaseColor();
        // s.makeSecondLayer();
        // s.solve();
        s.runAlgorithm(new Algorithm("R U R2 U' R' F R U R U' F'"));
        s.runAlgorithm(new Algorithm("R U R2 U' R' F R U R U' F'"));
        s.solve();
        // s.runAlgorithm(new Algorithm("U"));
        // System.out.println(Utils.ollToString(s.getCoreCube()));
        System.out.println(s.getCoreCube());
        System.out.println(s.getMoveHistory());
    }
}
