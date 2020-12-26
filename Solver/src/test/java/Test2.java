import java.io.IOException;
import java.net.URISyntaxException;

import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Solver;

public class Test2 {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver s = new Solver(new Cube());
        // System.out.println(s.scramble(15));
        s.scramble("D2 B D' R2 L2 R' L' R' B R B' R' B' D' U2");
        s.solve();
        System.out.println(s.getCoreCube());
        System.out.println(s.getMoveHistory());
        System.out.println(s.getMoveHistory().getMoveLength());

        // Solver s = new Solver(new Cube());
        // s.runAlgorithm("sexyMove");
        // System.out.println(s.getCoreCube());
    }
}
