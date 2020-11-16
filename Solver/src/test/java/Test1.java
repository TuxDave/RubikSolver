import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Solver;

import java.io.IOException;
import java.net.URISyntaxException;

public class Test1 {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Solver s = new Solver(new Cube());
        System.out.println(s.scrumble(20));
        System.out.println(s.getCoreCube());
        /*Cube c = new Cube();
        c.move('d', false);
        c.move('l', false);
        c.move('d', true);
        c.move('l', true);
        c.move('l', true);
        System.out.println(c);*/
    }
}
