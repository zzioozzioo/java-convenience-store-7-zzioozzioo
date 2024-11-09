package store.config;

import java.util.ArrayList;
import java.util.List;
import store.domain.PromotionManager;
import store.domain.StoreHouse;
import store.dto.PromotionInfo;
import store.io.reader.MissionUtilsReader;
import store.io.reader.Reader;
import store.io.writer.SystemWriter;
import store.io.writer.Writer;

public class Config {
    private final Reader reader;
    private final Writer writer;

    private final PromotionManager promotionManager;


    public Config() {
        this.reader = new MissionUtilsReader();
        this.writer = new SystemWriter();

        this.promotionManager = new PromotionManager(promotionInfos(), new StoreHouse());
    }

    public Reader getReader() {
        return reader;
    }

    public Writer getWriter() {
        return writer;
    }

    public PromotionManager getPromotionManager() {
        return promotionManager;
    }

    private List<PromotionInfo> promotionInfos() {
        return new ArrayList<>();
    }
}
