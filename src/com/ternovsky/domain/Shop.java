package com.ternovsky.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 02.11.12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public class Shop {
    private String id;
    private String name;
    private Coordinates coordinates;
    private Set<Product> products;

    public Shop() {
        products = new HashSet<Product>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Shop: " + id + "-" + name + " (" + coordinates.getX() + ", " + coordinates.getY() + ")";
    }
}
