package com.ternovsky;

import com.ternovsky.domain.Product;
import com.ternovsky.domain.Shop;

import java.util.*;

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
    private Map<String, Set<Shop>> productCodeShop;

    public ObjectStorage() {
        lowCostProductCodeShop = new HashMap<String, Shop>();
        lowCostProductCodeProduct = new HashMap<String, Product>();
        productCodeShop = new HashMap<String, Set<Shop>>();
    }

    public Set<String> orderedProductCodesByAlphabet() {
        return new TreeSet<String>(productCodeShop.keySet());
    }

    public Map<String, Set<Shop>> getProductCodeShop() {
        return productCodeShop;
    }

    public void add(Shop shop, Product product) {
        String productCode = product.getCode();
        Product lowCostProduct = lowCostProductCodeProduct.get(productCode);
        if (lowCostProduct == null || lowCostProduct.getPrice() > product.getPrice()) {
            lowCostProductCodeProduct.put(productCode, product);
            lowCostProductCodeShop.put(productCode, shop);
        }
        Set<Shop> shops = productCodeShop.get(productCode);
        if (shops == null) {
            shops = new HashSet<Shop>();
            shops.add(shop);
            productCodeShop.put(productCode, shops);
        } else {
            shops.add(shop);
        }
    }

    public Shop getLowCostShop(String productCode) {
        return lowCostProductCodeShop.get(productCode);
    }

    public Product getLowCostProduct(String productCode) {
        return lowCostProductCodeProduct.get(productCode);
    }

    public TreeSet<String> getOrderedProductCodes() {
        TreeSet<String> orderedProductCodes = new TreeSet<String>(new Comparator<String>() {
            @Override
            public int compare(String productCode1, String productCode2) {
                return productCodeShop.get(productCode1).size() > productCodeShop.get(productCode2).size() ? 1 : -1;
            }
        });
        orderedProductCodes.addAll(productCodeShop.keySet());
        return orderedProductCodes;
    }

    public Set<Shop> getShops(String productCode) {
        return productCodeShop.get(productCode);
    }
}
