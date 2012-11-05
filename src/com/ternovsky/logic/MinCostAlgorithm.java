package com.ternovsky.logic;

import com.ternovsky.ObjectStorage;
import com.ternovsky.Pair;
import com.ternovsky.Planner;
import com.ternovsky.PlannerContext;
import com.ternovsky.domain.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.abs;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 04.11.12
 * Time: 20:49
 * To change this template use File | Settings | File Templates.
 */
public class MinCostAlgorithm {

    ObjectStorage objectStorage;
    Planner planner;
    PlannerContext plannerContext;
    ShoppingList shoppingList;
    ShoppingPlan shoppingPlan;

    Map<Shop, Set<Product>> necessaryShopProducts;

    public MinCostAlgorithm(Planner planner) {
        this.planner = planner;
        plannerContext = planner.getPlannerContext();
        objectStorage = planner.getObjectStorage();
        shoppingList = plannerContext.getShoppingList();
        shoppingPlan = plannerContext.getShoppingPlan();
        necessaryShopProducts = new HashMap<Shop, Set<Product>>();
    }

    private void findShops() {
        for (Product product : shoppingList.getProducts()) {
            String productCode = product.getCode();
            Shop lowCostShop = objectStorage.getLowCostShop(productCode);
            Product lowCostProduct = objectStorage.getLowCostProduct(productCode);
            Set<Product> products = necessaryShopProducts.get(lowCostShop);
            if (products == null) {
                products = new HashSet<Product>();
                products.add(lowCostProduct);
                necessaryShopProducts.put(lowCostShop, products);
            } else {
                products.add(lowCostProduct);
            }
        }
    }

    private Pair<Pair<Product, Shop>, Integer> findNearestProductShopDistance(Coordinates currentCoordinates) {
        int minDistance = 0;
        Pair<Product, Shop> nearestProductShopPair = necessaryProductShopPairs.iterator().next();
        for (Pair<Product, Shop> productShopPair : necessaryProductShopPairs) {
            Shop shop = nearestProductShopPair.getSecond();
            int distance = distance(currentCoordinates, shop.getCoordinates());
            if (distance < minDistance) {
                minDistance = distance;
                nearestProductShopPair = productShopPair;
            }
        }
        return new Pair<Pair<Product, Shop>, Integer>(nearestProductShopPair, minDistance);
    }

    public void buildShoppingPlan() {
        findShops();
        Set<Shop> necessaryShops = necessaryShopProducts.keySet();
        Coordinates startCoordinates = shoppingList.getCoordinates();
        Coordinates coordinates = startCoordinates;
        int totalDistance = 0;
        float totalCost = 0;
        while (!necessaryProductShopPairs.isEmpty()) {
            Pair<Pair<Product, Shop>, Integer> productShopDistance = findNearestProductShopDistance(coordinates);
            Pair<Product, Shop> productShopPair = productShopDistance.getFirst();
            totalDistance += productShopDistance.getSecond();
            totalCost += productShopPair.getFirst().getPrice();
            coordinates = productShopPair.getSecond().getCoordinates();
            necessaryProductShopPairs.remove(productShopPair);
        }
        totalDistance += distance(startCoordinates, coordinates);

        shoppingPlan.setCoordinates(startCoordinates);
        shoppingPlan.setCost(totalCost);
        shoppingPlan.setDistance(totalDistance);
    }

    private int distance(Coordinates coordinates1, Coordinates coordinates2) {
        int x1 = coordinates1.getX();
        int y1 = coordinates1.getY();
        int x2 = coordinates2.getX();
        int y2 = coordinates2.getY();
        return abs(x1 - x2) + abs(y1 - y2);
    }
}
