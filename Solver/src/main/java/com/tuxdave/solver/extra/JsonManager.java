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
        src = new JSONObject(Utils.fromFileToString(_src));
    }

    public String getValueByKey(String _key){
        try{
            return src.getString(_key);
        }catch(NullPointerException e){
            throw new NullPointerException("Algorithm :" + _key + " not found");
        }
    }
}