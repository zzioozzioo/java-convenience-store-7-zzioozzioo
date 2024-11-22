package store.config;

import java.util.ArrayList;
import java.util.List;
import store.dto.PromotionInfo;
import store.io.reader.MissionUtilsReader;
import store.io.reader.Reader;
import store.io.writer.SystemWriter;
import store.io.writer.Writer;

public class Config {
    private final Reader reader;
    private final Writer writer;


    public Config() {
        this.reader = new MissionUtilsReader();
        this.writer = new SystemWriter();

    }

    public Reader getReader() {
        return reader;
    }

    public Writer getWriter() {
        return writer;
    }

    private List<PromotionInfo> getPromotionInfos() {
        return new ArrayList<>();
    }
}
