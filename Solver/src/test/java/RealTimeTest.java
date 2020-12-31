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
        // s.scramble("F2 R2 F D F B' F L2 F2 D2 L' F2 L' B2 F");
        // s.setBaseColor();
        // s.makeSecondLayer();
        // s.runAlgorithm(new Algorithm("U"));
        s.runAlgorithm("110100000111010010010");
        System.out.println(Utils.ollToString(s.getCoreCube()));
        System.out.println(s.getCoreCube());
        System.out.println(s.getMoveHistory());
    }
}
