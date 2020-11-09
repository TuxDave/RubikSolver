import com.tuxdave.solver.core.Cube;

import java.io.IOException;

public class Test1 {
    public static void main(String[] args) throws IOException {
        Cube c = new Cube("/home/tuxdave/Documenti/MyProjects/RubikSolver/Solver/examples/cube2.cube");
        c.move('r',true);
        c.move('u',true);
        c.move('r',false);
        c.move('u',false);
        System.out.println(c);
    }
}
