package com.ternovsky.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 02.11.12
 * Time: 21:08
 * To change this template use File | Settings | File Templates.
 */
public class ShoppingList {
    private Coordinates coordinates;
    private OptimizationParameter optimizationParameter;
    private Set<Product> products;

    public ShoppingList() {
        products = new HashSet<Product>();
        coordinates = new Coordinates();
        optimizationParameter = OptimizationParameter.COST;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public OptimizationParameter getOptimizationParameter() {
        return optimizationParameter;
    }

    public void setOptimizationParameter(OptimizationParameter optimizationParameter) {
        this.optimizationParameter = optimizationParameter;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public enum OptimizationParameter {
        COST, DISTANCE
    }

    public void clear() {
        products.clear();
        coordinates = new Coordinates();
    }
}
