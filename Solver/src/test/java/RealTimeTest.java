import java.io.IOException;
import java.net.URISyntaxException;

import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Solver;

public class RealTimeTest {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver s = new Solver(new Cube());
        // System.out.println(s.scramble(15));
        s.scramble("U2 B D2 R U' R U2 D2 R F2 D2 B' R' F U");
        s.solve();
        System.out.println(s.getCoreCube());
        System.out.println(s.getMoveHistory());
    }
}
