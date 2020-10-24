package es.upm.miw.SolitarioCelta.model;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class FileHelper {

    Context context;
    String name = "game.txt";

    public FileHelper(Context context) {
        this.context = context;
    }

    public void write(String text) throws IOException {
        FileOutputStream os = new FileOutputStream(getPath(), false);
        os.write(text.getBytes());
        os.close();
        read();
    }

    public String read() throws IOException {
        BufferedReader fin = new BufferedReader(new FileReader(getPath()));
        String current;
        String toReturn = "";
        while ((current = fin.readLine()) != null) {
            toReturn += current;
        }
        fin.close();
        return toReturn;
    }


    public File getPath() {
        File dir = new File(context.getFilesDir().getPath(), "files");
        if(!dir.exists())
            dir.mkdirs();
        return new File(dir, name);
    }

}
