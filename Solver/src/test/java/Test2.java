import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.google.common.collect.HashBiMap;
import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.extra.Position;
import com.tuxdave.solver.extra.Utils;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Algorithm;
import com.tuxdave.solver.logic.Solver;

public class Test2 {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {
        Solver s = new Solver(new Cube());
        HashMap<String, Algorithm> caseToAlg = new HashMap<String, Algorithm>();
        for (int i = 0; i < 10000; i++) {
            // System.out.println(s.scramble(15));
            s.scramble(21);
            // s.runAlgorithm(new Algorithm("R U R' U R U' R' U R U2 R'"));
            s.solve();
            // secondo layer ok
            Cube original = s.getCoreCube();
            // System.out.println(original);
            int upperColor = original.getFaceByPosition(Position.UP).getColorInt();
            for (int algIndex = 1; algIndex <= 57; algIndex++) {
                int gh = 0;
                do {
                    Cube other = new Cube(original);
                    for (int k = 0; k < gh; k++) {
                        other.move('u', true);
                    }
                    boolean ok = true;
                    String code = Utils.ollToString(other);
                    Solver solver = new Solver(other);
                    solver.runAlgorithm(String.valueOf(algIndex));
                    for (int c : other.getFaceByPosition(Position.UP).getBody()) {
                        if (c != upperColor) {
                            ok = false;
                            break;
                        }
                    }
                    if (ok) {
                        caseToAlg.put(code, new Algorithm(s.getAlgorithms().getValueByKey(String.valueOf(algIndex))));
                        break;
                    }
                    gh++;
                } while (gh < 4);
            }
        }
        System.out.println(caseToAlg.size());
        for (String key : caseToAlg.keySet()) {
            System.out.printf("\"%s\":\"%s\"\n", key, caseToAlg.get(key));
        }
    }
}