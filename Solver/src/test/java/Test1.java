import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;
import com.tuxdave.solver.extra.JsonManager;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Solver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Test1 {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver s = new Solver(new Cube());
        //System.out.println(s.scramble());
        //s.scramble("F' D2 D B' D B2 B' L R F'");
        //s.scramble(99999);
        //s.solve();
        s.runAlgorithm("sexyMove");
        System.out.println(s.getCoreCube());
        //System.out.println(s.getMoveHistory());
    }
}
