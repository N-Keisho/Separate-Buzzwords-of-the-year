package src;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {
    public static void ShowTaisyo(int year) throws IOException{
        try {
            ObjectMapper  objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.readTree(new File("./data/" + String.valueOf(year)+ ".json"));
            System.out.println("award: " + json.get("awards").get(0).get("award").textValue()); 
            System.out.println("ward: " + json.get("awards").get(0).get("ward").textValue()); 
            System.out.println("winner: " + json.get("awards").get(0).get("winner").textValue());
            System.out.println("text: " + json.get("awards").get(0).get("text").textValue());  
        } catch (FileNotFoundException e) {
            System.out.println(e);
        }
    }
}
