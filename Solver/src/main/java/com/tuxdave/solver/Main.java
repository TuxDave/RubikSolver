/*
Error codes:
1: args syntax error
2: cube model error (content or syntax)
*/

package com.tuxdave.solver;

import java.io.IOException;
import java.net.URISyntaxException;

import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.extra.ValueNotInRangeException;
import com.tuxdave.solver.logic.Solver;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException, ValueNotInRangeException {

        if (args.length == 1) {
            args = new String[] { args[0], "" };
        }

        System.out.println("###############################################################\n"
                + "#               _     _ _     _____       _                   #\n"
                + "#              | |   (_) |   / ____|     | |                  #\n"
                + "#    _ __ _   _| |__  _| | _| (___   ___ | |_   _____ _ __    #\n"
                + "#   | '__| | | | '_ \\| | |/ /\\___ \\ / _ \\| \\ \\ / / _ \\ '__|   #\n"
                + "#   | |  | |_| | |_) | |   < ____) | (_) | |\\ V /  __/ |      #\n"
                + "#   |_|   \\__,_|_.__/|_|_|\\_\\_____/ \\___/|_| \\_/ \\___|_|      #\n"
                + "#                                                             #\n"
                + "###############################################################");

        ArgumentParser parser = ArgumentParsers.newFor("RubikSolver").build();// used only to display the helps
        parser.addArgument("type").help(
                "1/cube (RECOMMENDED) to get as next the cube model as string of numbers (colors) in the ruled notation (see https://github.com/TuxDave/RubikSolver)\n2/file to get as next the path to the file containing the cube model using the correct notation, see GitHub\n3/auto prints a 21 moves scramble and its solve");
        parser.addArgument("model").help("the model or the path to reach it of the selected working method");
        try {
            parser.parseArgs(args);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }

        String type = args[0];
        String model = args[1];

        if (!type.equals("1") && !type.equals("cube") && !type.equals("2") && !type.equals("file") && !type.equals("3")
                && !type.equals("auto")) {
            System.out.println("Error: invalid first positional argument: " + type + " - see -h for documentation");
            System.exit(1);
        }
        switch (type) {
            case "1":
            case "cube":
                // errors
                if (model.length() != 54) {
                    System.err.println("Cube model syntax ERROR!");
                    System.exit(2);
                } else {
                    int[] counter = new int[6];
                    for (int i = 0; i < 6; i++) {
                        counter[i] = 0;
                    }
                    for (char c : model.toCharArray()) {
                        counter[Integer.valueOf("" + c)]++;
                    }
                    for (int c : counter) {
                        if (c > 9) {
                            System.out.println("Cube model content ERROR!");
                            System.exit(2);
                        }
                    }
                }
                int[] cube = new int[54];
                for (int i = 0; i < 54; i++) {
                    cube[i] = Integer.valueOf("" + model.charAt(i));
                }
                Solver s = new Solver(new Cube(cube));
                s.solve();
                System.out.println("");
                System.out.println(s.getMoveHistory());
                break;
            case "2":
            case "file":
                try {
                    Solver s1 = new Solver(new Cube(model));
                    s1.solve();
                    System.out.println("");
                    System.out.println(s1.getMoveHistory());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Unable to find the file model or content wrong ERROR!");
                    System.exit(2);
                }
                break;
            case "3":
            case "auto":
                Solver s2 = new Solver(new Cube());
                System.out.println("");
                System.out.println(s2.scramble());
                System.out.println("");
                s2.solve();
                System.out.println(s2.getMoveHistory());
                break;
        }
    }
}
