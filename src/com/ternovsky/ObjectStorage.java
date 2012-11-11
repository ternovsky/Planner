package com.ternovsky;

import com.ternovsky.domain.Coordinates;
import com.ternovsky.domain.Product;
import com.ternovsky.domain.Shop;

import java.util.*;

import static java.lang.Math.abs;

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
    private Coordinates initialCoordinates = new Coordinates();

    public ObjectStorage() {
        lowCostProductCodeShop = new HashMap<String, Shop>();
        lowCostProductCodeProduct = new HashMap<String, Product>();
        productCodeShop = new HashMap<String, Set<Shop>>();
    }

    public void setInitialCoordinates(Coordinates initialCoordinates) {
        this.initialCoordinates = initialCoordinates;
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
        if (lowCostProduct != null) {
            float lowCostProductPrice = lowCostProduct.getPrice();
            float productPrice = product.getPrice();
            if (lowCostProductPrice > productPrice) {
                lowCostProductCodeProduct.put(productCode, product);
                lowCostProductCodeShop.put(productCode, shop);
            } else if (lowCostProductPrice == productPrice) {
                int productDistance = distance(initialCoordinates, shop.getCoordinates());
                Shop lowCostProductShop = lowCostProductCodeShop.get(lowCostProduct.getCode());
                int lowCostProductDistance = distance(initialCoordinates, lowCostProductShop.getCoordinates());
                if (lowCostProductDistance > productDistance) {
                    lowCostProductCodeProduct.put(productCode, product);
                    lowCostProductCodeShop.put(productCode, shop);
                }
            }
        } else {
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

    private int distance(Coordinates coordinates1, Coordinates coordinates2) {
        int x1 = coordinates1.getX();
        int y1 = coordinates1.getY();
        int x2 = coordinates2.getX();
        int y2 = coordinates2.getY();
        return abs(x1 - x2) + abs(y1 - y2);
    }

    public void clear() {
        lowCostProductCodeShop.clear();
        lowCostProductCodeProduct.clear();
        productCodeShop.clear();
        initialCoordinates = new Coordinates();
    }
}
