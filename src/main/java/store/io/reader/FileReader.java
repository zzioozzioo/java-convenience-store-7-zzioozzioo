package store.io.reader;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader implements Reader {

    private final BufferedReader reader;

    public FileReader(String fileName) {
        try {
            this.reader = new BufferedReader(new java.io.FileReader(fileName));
        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("파일 읽기 오류");
        }
    }
}
