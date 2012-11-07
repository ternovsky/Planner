package com.ternovsky.logic;

import com.ternovsky.ObjectStorage;
import com.ternovsky.Planner;
import com.ternovsky.PlannerContext;
import com.ternovsky.domain.*;

import java.util.*;

import static java.lang.Math.abs;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 04.11.12
 * Time: 20:49
 * To change this template use File | Settings | File Templates.
 */
public class MinCostAlgorithm {

    private ObjectStorage objectStorage;
    private Planner planner;
    private PlannerContext plannerContext;
    private ShoppingList shoppingList;
    private ShoppingPlan shoppingPlan;

    public MinCostAlgorithm(Planner planner) {
        this.planner = planner;
        plannerContext = planner.getPlannerContext();
        objectStorage = planner.getObjectStorage();
        shoppingList = plannerContext.getShoppingList();
        shoppingPlan = plannerContext.getShoppingPlan();
    }

    public void buildShoppingPlan() {
        Map<Shop, Set<Product>> necessaryShopProducts = findShops();
        Set<Shop> necessaryShops = necessaryShopProducts.keySet();
        shoppingPlan = new ShoppingPlan(necessaryShops.size());
        Coordinates startCoordinates = shoppingList.getCoordinates();
        Coordinates currentCoordinates = startCoordinates;
        float totalCost = 0;
        int totalDistance = 0;
        while (!necessaryShops.isEmpty()) {
            Map<Shop, Integer> shopDistanceMap = nearestShopDistance(currentCoordinates, necessaryShopProducts.keySet());
            Shop shop = shopDistanceMap.keySet().iterator().next();
            shoppingPlan.addShop(shop);
            totalDistance += shopDistanceMap.get(shop);
            for (Product product : necessaryShopProducts.get(shop)) {
                totalCost += product.getPrice();
                shoppingPlan.addShopProduct(shop, product);
            }
            currentCoordinates = shop.getCoordinates();
            necessaryShops.remove(shop);
        }
        totalDistance += distance(currentCoordinates, startCoordinates);
        shoppingPlan.setCoordinates(startCoordinates);
        shoppingPlan.setCost(totalCost);
        shoppingPlan.setDistance(totalDistance);

        plannerContext.setShoppingPlan(shoppingPlan);
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

    private int distance(Coordinates coordinates1, Coordinates coordinates2) {
        int x1 = coordinates1.getX();
        int y1 = coordinates1.getY();
        int x2 = coordinates2.getX();
        int y2 = coordinates2.getY();
        return abs(x1 - x2) + abs(y1 - y2);
    }

    private Map<Shop, Set<Product>> findShops() {
        Map<Shop, Set<Product>> necessaryShopProducts = new HashMap<Shop, Set<Product>>();
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
        return necessaryShopProducts;
    }
}
