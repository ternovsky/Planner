package com.ternovsky.domain;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 03.11.12
 * Time: 20:22
 * To change this template use File | Settings | File Templates.
 */
public class ShoppingPlan {
    private Coordinates coordinates;
    private float cost;
    private float distance;
    private List<Shop> shops;
    private Map<Shop, Set<Product>> shopProducts;

    public ShoppingPlan(int shopCount) {
        shops = new ArrayList<Shop>(shopCount);
        shopProducts = new HashMap<Shop, Set<Product>>();
    }

    public void addShop(Shop shop) {
        shops.add(shop);
    }

    public void addShopProduct(Shop shop, Product product) {
        Set<Product> products = shopProducts.get(shop);
        if (products == null) {
            products = new HashSet<Product>();
            products.add(product);
            shopProducts.put(shop, products);
        } else {
            products.add(product);
        }
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
