package com.json;

import java.io.*;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ReadJSON {
public static void main(String [] args) {
    ArrayList<JSONObject> json=new ArrayList<JSONObject>();
    JSONObject obj;
    // The name of the file to open.
    String fileName = "C:\\Users\\aawad\\workspace\\kura_juno\\data_logger\\log\\Apr_28_2016\\test.txt ";

    // This will reference one line at a time
    String line = null;

    try {
        // FileReader reads text files in the default encoding.
        FileReader fileReader = new FileReader(fileName);

        // Always wrap FileReader in BufferedReader.
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        while((line = bufferedReader.readLine()) != null) {
            obj = (JSONObject) new JSONParser().parse(line);
            json.add(obj);
            System.out.println((String)obj.get("Sensor_ID")+":"+
                               (String)obj.get("Date"));
        }
        // Always close files.
        bufferedReader.close();         
    }
    catch(FileNotFoundException ex) {
        System.out.println("Unable to open file '" + fileName + "'");                
    }
    catch(IOException ex) {
        System.out.println("Error reading file '" + fileName + "'");                  
        // Or we could just do this: 
        // ex.printStackTrace();
    } catch (ParseException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
}