package com.json;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
 
public class ReadJsonExample {
 
    public static void main(String a[]) throws IOException{
         
        File jsonInputFile = new File("./src/com/json/batch_log.json");
        FileInputStream is;
        try {
            is = new FileInputStream(jsonInputFile);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
               System.out.println(line);
            }
            // Create JsonReader from Json.
//            JsonReader reader = Json.createReader(is);
            // Get the JsonObject structure from JsonReader.
//            JsonObject empObj = reader.readObject();
            
            // read string data
//            System.out.println("D: " + empObj.getString("D"));
//            Set set = empObj.keySet();
//            Iterator it = set.iterator();
//            while (it.hasNext()) {
//            	System.out.println(it.next());
//            }
//            reader.read();
            
//            reader.close();
            // read inner json element
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
//reference: http://www.java2novice.com/java-json/javax.json/read-json-jsonreader/