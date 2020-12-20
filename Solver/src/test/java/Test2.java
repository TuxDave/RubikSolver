import java.io.IOException;
import java.net.URISyntaxException;

import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Solver;

public class Test2 {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver s = new Solver(new Cube());
        // System.out.println(s.scramble(10));
        s.scramble("L' U2 L' U' L D2 R U R F2");
        s.solve();
        System.out.println(s.getCoreCube());
        System.out.println(s.getMoveHistory());
        System.out.println(s.getMoveHistory().getMoveLength());

        // Solver s = new Solver(new Cube());
        // s.runAlgorithm("sexyMove");
        // System.out.println(s.getCoreCube());
    }
}
