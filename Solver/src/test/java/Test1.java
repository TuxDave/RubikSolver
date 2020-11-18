import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Solver;

import java.io.IOException;
import java.net.URISyntaxException;

public class Test1 {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Solver s = new Solver(new Cube());
        //System.out.println(s.scrumble(8));
        s.scrumble("F' D2 D B' D B2 B' L R F'");
        s.getCoreCube().reOrientate("f-");
        s.getCoreCube().move('r', true);
        System.out.println(s.getCoreCube());
    }
}
