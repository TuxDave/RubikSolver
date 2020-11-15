import com.tuxdave.solver.core.Cube;

import java.io.IOException;
import java.net.URISyntaxException;

public class Test1 {
    public static void main(String[] args) throws IOException, URISyntaxException {
        Cube c = new Cube();
        c.move('r',true);
        c.move('u',true);
        c.move('r',false);
        c.move('u',false);
        System.out.println(c);
    }
}
