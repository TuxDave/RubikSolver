package com.tuxdave.solver.extra;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Is used to work with JSon files
 * 
 * @throws IOException
 */
public class JsonManager {
    private JSONObject src;

    public JsonManager(File _src) throws IOException{
        BufferedReader temp = new BufferedReader(new FileReader(_src));
        String jsonString = "";
        String line;
        do{
            line = temp.readLine();
            if(line != null)
                jsonString += line + '\n';
        }while(line != null);
        src = new JSONObject(jsonString);
    }

    public String getValueByKey(String _key){
        try{
            return src.getString(_key);
        }catch(NullPointerException e){
            System.err.println("Impossibile trovare chiave valore: " + _key);
            e.printStackTrace();
        }
        return "";
    }
}