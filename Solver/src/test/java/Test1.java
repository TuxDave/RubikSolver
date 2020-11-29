import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;
import com.tuxdave.solver.extra.JsonManager;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Algorithm;
import com.tuxdave.solver.logic.Solver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

public class Test1 {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Algorithm a = new Algorithm(
                "y+ R R R y+ y+ y+ D D D F F D D D D D D D D D L L y+ F F U U U R R U F F y+ y+ y+");
        System.out.println(a);
        // System.out.println(a.equalsThenNext(1, 3));
    }
}
