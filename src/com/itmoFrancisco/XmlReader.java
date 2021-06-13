package com.itmoFrancisco;

import javax.print.DocFlavor;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class XmlReader {

    public Hashtable<Long, Dragon> read(String fileName) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        String line;
        String data = "";
        try {
            while ((line = reader.readLine()) != null) {
                data = data + line + "\n";
            }
            data = data.substring(0, data.length() - 1);
            DragonsReader dragonsReader = new DragonsReader();
            return dragonsReader.read(data);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
