package com.tuxdave.solver.extra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;

import com.tuxdave.solver.core.Cube;
import com.tuxdave.solver.core.Face;

public class Utils {

    public static String fromFileToString(File f) throws IOException {
        String ret = "";
        BufferedReader r = new BufferedReader(new FileReader(f));
        String line = "";
        do {
            line = r.readLine();
            if (line != null)
                ret += line + '\n';
        } while (line != null);
        return ret;
    }

    public static String fromFileToString(URL file) throws IOException {
        String ret = "";
        BufferedReader r = new BufferedReader(new InputStreamReader(file.openStream()));
        String line = "";
        do {
            line = r.readLine();
            if (line != null)
                ret += line + '\n';
        } while (line != null);
        return ret;
    }

    public static int[] concatenaArrays(int[]... arrays) {
        int length = 0;
        for (int[] arr : arrays) {
            length += arr.length;
        }
        int i = 0;
        int[] ret = new int[length];
        for (int[] arr : arrays) {
            for (int ar : arr) {
                ret[i++] = ar;
            }
        }
        return ret;
    }

    public static String ollToString(Cube cube) {
        int upperColor = cube.getFaceByPosition(Position.UP).getColorInt();
        int[] ollFaceArray = Utils.concatenaArrays(cube.getFaceByPosition(Position.UP).getBody(),
                cube.getFaceByPosition(Position.FRONT).getBorder("u"),
                cube.getFaceByPosition(Position.RIGHT).getBorder("u"),
                cube.getFaceByPosition(Position.BACK).getBorder("u"),
                cube.getFaceByPosition(Position.LEFT).getBorder("u"));

        String ollFace = "";

        for (int color : ollFaceArray) {
            ollFace += color == upperColor ? "1" : "0";
        }
        return ollFace;
    }
}
