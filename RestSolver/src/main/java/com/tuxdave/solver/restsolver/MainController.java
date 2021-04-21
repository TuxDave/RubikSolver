package com.tuxdave.solver.restsolver;

import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Solver;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
public class MainController {

    /**
     * @return the render of the index page
     */
    @GetMapping("/")
    public ModelAndView indexGet() {
        return new ModelAndView("index.html");
    }

    /**
     * @return a json string: state: state of scrambled cube, scramble: scrambling sequence
     */
    @GetMapping("/scramble")
    public String scrambleGet() {
        Solver s = null;
        try {
            s = new Solver(new Cube());
        } catch (IOException | URISyntaxException e) {
            throw new IllegalArgumentException("internal error: \"core\"");
        }
        String scramble;
        String cube;
        try {
            scramble = s.scramble();
            cube = s.getCoreCube().toStringLinear();
        } catch (ValueNotInRangeException e) {
            throw new IllegalArgumentException("internal error: \"logic\"");
        }
        JSONObject json = new JSONObject();
        json.put("scramble", scramble);
        json.put("state", cube);
        return json.toString();
    }

    /**
     * @param state url-query, the scrambled cube
     * @return the solving sequence
     */
    @GetMapping("/solve")
    public String solveGet(@RequestParam("cube") String state) {//TODO: put a limit to break the solve if the cube is impossible to solve
        if (state.length() != 54) {
            throw new IllegalArgumentException("Bad Cube Exception");
        } else {
            int[] counter = new int[6];
            for (int i = 0; i < 6; i++) {
                counter[i] = 0;
            }
            for (char c : state.toCharArray()) {
                counter[Integer.valueOf("" + c)]++;
            }
            for (int c : counter) {
                if (c > 9) {
                    throw new IllegalArgumentException("Bad Cube Exception");
                }
            }
            int[] cube = new int[54];
            for (int i = 0; i < 54; i++) {
                cube[i] = Integer.valueOf("" + state.charAt(i));
            }
            Solver s = null;
            try {
                s = new Solver(new Cube(cube));
                JSONObject json = new JSONObject();
                json.put("solve", s.solve());
                return json.toString();
            } catch (IOException | URISyntaxException e) {
                throw new IllegalArgumentException("internal error \"core/logic\"");
            }
        }
    }

}
