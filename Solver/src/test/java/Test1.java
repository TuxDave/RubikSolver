import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;

import java.io.IOException;

public class Test1 {
    public static void main(String[] args) throws IOException {
        /*Face f = new Face("white");
        f.setSpot(1, Face.fromColorToInt("green"));
        f.setSpot(2, Face.fromColorToInt("red"));
        f.setSpot(3, Face.fromColorToInt("blue"));
        f.setSpot(4, Face.fromColorToInt("green"));
        f.setSpot(5, Face.fromColorToInt("white"));
        f.setSpot(6, Face.fromColorToInt("yellow"));
        f.setSpot(7, Face.fromColorToInt("orange"));
        f.setSpot(8, Face.fromColorToInt("green"));
        System.out.println(f);
        f.rotate();
        System.out.println();
        System.out.println(f);*/
        Cube c = new Cube("/home/tuxdave/Documenti/MyProjects/RubikSolver/Solver/examples/cube2.cube");
        c.move('l',true);
        System.out.println(c);
    }
}
