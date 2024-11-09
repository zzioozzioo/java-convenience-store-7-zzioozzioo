package store;

import store.config.Config;
import store.config.IoConfig;
import store.controller.StoreController;
import store.service.StoreService;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현

        StoreController controller = new StoreController(
                new IoConfig(new Config()),
                new StoreService()
        );

        controller.run();
    }
}
