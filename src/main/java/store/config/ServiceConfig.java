package store.config;

import store.service.StoreService;

public class ServiceConfig {

    private final StoreService service;

    public ServiceConfig(Config config) {

        this.service = new StoreService(
                config.getPromotionManager()
        );
    }

    public StoreService getService() {
        return service;
    }
}
