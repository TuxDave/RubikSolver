import java.io.IOException;
import java.net.URISyntaxException;

import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Solver;

public class Test2 {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver s = new Solver(new Cube());
        s.scramble("B2 D L U B' U' B' D U2 L2");
        s.solve();
        System.out.println(s.getCoreCube());
        System.out.println(s.getMoveHistory());
        // TODO: fare il metodo con gli array

        // Solver s = new Solver(new Cube());
        // s.runAlgorithm("sexyMove");
        // System.out.println(s.getCoreCube());
    }
}
