package com.tuxdave.solver.extra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Utils {
    
    public static String fromFileToString(File f){
        String ret = "";
        BufferedReader r = new BufferedReader(new FileReader(f);
        String line = "";
        do{
            line = r.readLine();
            if(line != null)
                ret += line + '\n';
        }while(line != null);
        return ret;
    }
}
