package store.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import store.domain.Product;

public class Receipt {

    private List<Purchase> purchaseList;
    private final Map<Product, Integer> freebieProduct = new LinkedHashMap<>();

    public void addFreebieProduct(Product product, int quantity) {
        freebieProduct.put(product, freebieProduct.getOrDefault(product, 0) + quantity);
    }

    public List<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public void setPurchaseList(List<Purchase> purchaseList) {
        this.purchaseList = purchaseList;
    }

    public Map<Product, Integer> getFreebieProduct() {
        return freebieProduct;
    }
}
