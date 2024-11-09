package store.testutil;

import java.util.ArrayList;
import java.util.List;
import store.io.writer.Writer;

public class WriterFake implements Writer {

    private final List<String> outputs = new ArrayList<>();

    public List<String> getOutputs() {
        return outputs;
    }

    public void write(String value) {
        outputs.add(value);
    }
}
