import java.io.IOException;
import java.net.URISyntaxException;

import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Solver;

public class Test2 {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver s = new Solver(new Cube());
        System.out.println(s.scramble(10));
        // s.scramble("B R2 L' D F' D F R' D2 B");
        s.solve();
        System.out.println(s.getCoreCube());
        System.out.println(s.getMoveHistory());
        // TODO: fare il metodo con gli array

        // Solver s = new Solver(new Cube());
        // s.runAlgorithm("sexyMove");
        // System.out.println(s.getCoreCube());
    }
}
