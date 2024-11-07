package store.io.file;

import java.io.FileNotFoundException;

public class FileBufferedReader implements BufferedReader {

    @Override
    public java.io.BufferedReader getReader() {
        try {
            java.io.FileReader fileReader = new java.io.FileReader("src/main/resources/products.md");
            return new java.io.BufferedReader(fileReader);
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
    }
}
