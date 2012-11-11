package com.ternovsky.logic;

import com.ternovsky.ObjectStorage;
import com.ternovsky.Planner;
import com.ternovsky.PlannerContext;
import com.ternovsky.domain.*;

import java.util.Collections;
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
public class MinDistanceAlgorithm {

    private ObjectStorage objectStorage;
    private Planner planner;
    private PlannerContext plannerContext;
    private ShoppingList shoppingList;
    private ShoppingPlan shoppingPlan;

    public MinDistanceAlgorithm(Planner planner) {
        this.planner = planner;
        plannerContext = planner.getPlannerContext();
        objectStorage = planner.getObjectStorage();
        shoppingList = plannerContext.getShoppingList();
        shoppingPlan = plannerContext.getShoppingPlan();
    }

    private Set<Shop> findShops() {
        Set<Shop> necessaryShops = new HashSet<Shop>();
        Set<String> productCodes = objectStorage.getOrderedProductCodes();
        Coordinates coordinatesCenter = shoppingList.getCoordinates();
        Coordinates coordinatesSum = coordinatesCenter;
        int coordinatesCount = 1;
        for (String productCode : productCodes) {
            Shop nearestShop = objectStorage.getShops(productCode).iterator().next();
            int minDistance = Integer.MAX_VALUE;
            for (Shop shop : objectStorage.getShops(productCode)) {
                int distance = distance(coordinatesCenter, shop.getCoordinates());
                if (distance <= minDistance) {
                    minDistance = distance;
                    nearestShop = shop;
                }
            }
            coordinatesCenter = recalculateCoordinatesCenter(coordinatesSum, nearestShop.getCoordinates(), coordinatesCount);
            necessaryShops.add(nearestShop);
        }
        return necessaryShops;
    }

    private Map<Shop, Integer> nearestShopDistance(Coordinates currentCoordinates, Set<Shop> necessaryShops) {
        Shop nearestShop = null;
        int minDistance = Integer.MAX_VALUE;
        for (Shop shop : necessaryShops) {
            int distance = distance(currentCoordinates, shop.getCoordinates());
            if (distance < minDistance) {
                minDistance = distance;
                nearestShop = shop;
            }
        }
        return Collections.singletonMap(nearestShop, minDistance);
    }

    public void buildShoppingPlan() {
        Set<Shop> necessaryShops = findShops();
        shoppingPlan = new ShoppingPlan(necessaryShops.size());
        Coordinates startCoordinates = shoppingList.getCoordinates();
        Coordinates currentCoordinates = startCoordinates;
        float totalCost = 0;
        int totalDistance = 0;
        while (!necessaryShops.isEmpty()) {
            Map<Shop, Integer> shopDistanceMap = nearestShopDistance(currentCoordinates, necessaryShops);
            Set<Product> shoppingListProducts = new HashSet<Product>(shoppingList.getProducts());
            Shop shop = shopDistanceMap.keySet().iterator().next();
            for (Product product : shop.getProducts()) {
                if (!shoppingListProducts.isEmpty() && shoppingListProducts.contains(product)) {
                    totalCost += product.getPrice();
                    shoppingPlan.addShopProduct(shop, product);
                    shoppingListProducts.remove(product);
                }
            }
            shoppingPlan.addShop(shop);
            totalDistance += shopDistanceMap.get(shop);
            currentCoordinates = shop.getCoordinates();
            necessaryShops.remove(shop);
        }
        totalDistance += distance(currentCoordinates, startCoordinates);
        shoppingPlan.setCoordinates(startCoordinates);
        shoppingPlan.setCost(totalCost);
        shoppingPlan.setDistance(totalDistance);

        plannerContext.setShoppingPlan(shoppingPlan);
    }

    private Coordinates recalculateCoordinatesCenter(Coordinates coordinatesSum,
                                                     Coordinates newCoordinates,
                                                     int coordinatesCount) {
        coordinatesCount++;
        int x1 = coordinatesSum.getX();
        int y1 = coordinatesSum.getY();
        int x2 = newCoordinates.getX();
        int y2 = newCoordinates.getY();
        return new Coordinates((x1 + x2) / coordinatesCount, (y1 + y2) / coordinatesCount);
    }

    private int distance(Coordinates coordinates1, Coordinates coordinates2) {
        int x1 = coordinates1.getX();
        int y1 = coordinates1.getY();
        int x2 = coordinates2.getX();
        int y2 = coordinates2.getY();
        return abs(x1 - x2) + abs(y1 - y2);
    }
}
