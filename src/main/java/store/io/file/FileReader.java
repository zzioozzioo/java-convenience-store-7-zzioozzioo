package store.io.file;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

public class FileReader implements Reader {

    @Override
    public BufferedReader getReader() {
        try {
            java.io.FileReader fileReader = new java.io.FileReader("src/main/resources/products.md");
            return new BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
    }
}
