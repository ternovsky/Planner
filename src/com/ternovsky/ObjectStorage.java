package com.ternovsky;

import com.ternovsky.domain.Product;
import com.ternovsky.domain.Shop;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 04.11.12
 * Time: 20:57
 * To change this template use File | Settings | File Templates.
 */
public class ObjectStorage {

    private Map<String, Shop> lowCostProductCodeShop;
    private Map<String, Product> lowCostProductCodeProduct;

    public ObjectStorage() {
        lowCostProductCodeShop = new HashMap<String, Shop>();
        lowCostProductCodeProduct = new HashMap<String, Product>();
    }

    public void add(Shop shop, Product product) {
        String productCode = product.getCode();
        Product lowCostProduct = lowCostProductCodeProduct.get(productCode);
        if (lowCostProduct == null || lowCostProduct.getPrice() > product.getPrice()) {
            lowCostProductCodeProduct.put(productCode, product);
            lowCostProductCodeShop.put(productCode, shop);
        }
    }

    public Shop getLowCostShop(String productCode) {
        return lowCostProductCodeShop.get(productCode);
    }

    public Product getLowCostProduct(String productCode) {
        return lowCostProductCodeProduct.get(productCode);
    }

    public void clear() {
        lowCostProductCodeProduct.clear();
        lowCostProductCodeShop.clear();
    }
}
