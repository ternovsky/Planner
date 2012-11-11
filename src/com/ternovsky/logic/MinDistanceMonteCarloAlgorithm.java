package com.ternovsky.logic;

import com.ternovsky.ObjectStorage;
import com.ternovsky.Planner;
import com.ternovsky.PlannerContext;
import com.ternovsky.domain.*;

import java.util.*;

import static java.lang.Math.abs;

/**
 * Created with IntelliJ IDEA.
 * User: ternovsky
 * Date: 11.11.12
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class MinDistanceMonteCarloAlgorithm {

    private ObjectStorage objectStorage;
    private PlannerContext plannerContext;
    private ShoppingList shoppingList;
    private ShoppingPlan shoppingPlan;

    private Set<Shop> necessaryShops = new HashSet<Shop>();

    public MinDistanceMonteCarloAlgorithm(Planner planner) {
        plannerContext = planner.getPlannerContext();
        objectStorage = planner.getObjectStorage();
        shoppingList = plannerContext.getShoppingList();
        shoppingPlan = plannerContext.getShoppingPlan();
    }

    private void findNecessaryShops() {
        Map<String, Set<Shop>> productCodeShop = objectStorage.getProductCodeShop();
        for (Product product : shoppingList.getProducts()) {
            necessaryShops.addAll(productCodeShop.get(product.getCode()));
        }
    }

    private Map<Integer, Set<Shop>> findShops(Shop startShop, Set<Product> necessaryProducts, Set<Shop> necessaryShops) {
        Set<Shop> shops = new HashSet<Shop>();

        Set<String> necessaryProductCodes = new HashSet<String>();
        for (Product necessaryProduct : necessaryProducts) {
            necessaryProductCodes.add(necessaryProduct.getCode());
        }

        shops.add(startShop);
        necessaryShops.remove(startShop);
        for (Product product : startShop.getProducts()) {
            String productCode = product.getCode();
            if (necessaryProductCodes.contains(productCode)) {
                necessaryProductCodes.remove(productCode);
            }
        }

        return findShops(startShop.getCoordinates(), necessaryShops, shops, necessaryProductCodes);
    }

    private Map<Integer, Set<Shop>> findShops(Coordinates coordinates, Set<Product> necessaryProducts, Set<Shop> necessaryShops) {
        Set<Shop> shops = new HashSet<Shop>();

        Set<String> necessaryProductCodes = new HashSet<String>();
        for (Product necessaryProduct : necessaryProducts) {
            necessaryProductCodes.add(necessaryProduct.getCode());
        }

        return findShops(shoppingList.getCoordinates(), necessaryShops, shops, necessaryProductCodes);
    }

    private Map<Integer, Set<Shop>> findShops(Coordinates coordinates, Set<Shop> necessaryShops, Set<Shop> shops, Set<String> necessaryProductCodes) {
        while (!necessaryProductCodes.isEmpty()) {
            Shop shop = nearestShop(coordinates, necessaryShops);
            for (Product product : shop.getProducts()) {
                String productCode = product.getCode();
                if (necessaryProductCodes.contains(productCode)) {
                    necessaryProductCodes.remove(productCode);
                    shops.add(shop);
                }
            }
            necessaryShops.remove(shop);
        }

        int kpi = findKPI(shoppingList.getCoordinates(), shops);
        return Collections.singletonMap(kpi, shops);
    }

    private int findKPI(Coordinates initialCoordinates, Set<Shop> shops) {
        Coordinates coordinatesCenter = findCoordinatesCenter(initialCoordinates, shops);
        int sumDistance = distance(coordinatesCenter, initialCoordinates);
        for (Shop shop : shops) {
            sumDistance += distance(coordinatesCenter, shop.getCoordinates());
        }
        return sumDistance;
    }

    private Coordinates findCoordinatesCenter(Coordinates initialCoordinates, Set<Shop> shops) {
        int xSum = initialCoordinates.getX();
        int ySum = initialCoordinates.getY();
        int coordinatesCount = shops.size() + 1;
        for (Shop shop : shops) {
            Coordinates coordinates = shop.getCoordinates();
            xSum += coordinates.getX();
            ySum += coordinates.getY();
        }
        return new Coordinates(xSum / coordinatesCount, ySum / coordinatesCount);
    }

    private Shop nearestShop(Coordinates currentCoordinates, Set<Shop> necessaryShops) {
        Shop nearestShop = null;
        int minDistance = Integer.MAX_VALUE;
        for (Shop shop : necessaryShops) {
            int distance = distance(currentCoordinates, shop.getCoordinates());
            if (distance < minDistance) {
                minDistance = distance;
                nearestShop = shop;
            }
        }
        return nearestShop;
    }

    private int distance(Coordinates coordinates1, Coordinates coordinates2) {
        int x1 = coordinates1.getX();
        int y1 = coordinates1.getY();
        int x2 = coordinates2.getX();
        int y2 = coordinates2.getY();
        return abs(x1 - x2) + abs(y1 - y2);
    }

    public void buildShoppingPlan() {
        findNecessaryShops();

        Set<Product> necessaryProducts = new HashSet<Product>(shoppingList.getProducts());
        Set<Shop> necessaryShops = new HashSet<Shop>(this.necessaryShops);

        Map<Integer, Set<Shop>> initialKpiShops = findShops(shoppingList.getCoordinates(), necessaryProducts, necessaryShops);
        int minKpi = initialKpiShops.keySet().iterator().next();
        Set<Shop> minKpiShops = new HashSet<Shop>(initialKpiShops.get(minKpi));

        RandomShopGenerator randomShopGenerator = new RandomShopGenerator(this.necessaryShops);
        int iterationCount = this.necessaryShops.size();
        for (int i = 0; i < iterationCount; i++) {
            necessaryShops = new HashSet<Shop>(this.necessaryShops);
            necessaryProducts = new HashSet<Product>(shoppingList.getProducts());
            Shop shop = randomShopGenerator.randomShop();
            Map<Integer, Set<Shop>> kpiShops = findShops(shop, necessaryProducts, necessaryShops);
            int kpi = kpiShops.keySet().iterator().next();
            if (kpi < minKpi) {
                minKpi = kpi;
                minKpiShops.clear();
                minKpiShops.addAll(kpiShops.get(kpi));
            }
        }

        findBestPath(minKpiShops);
    }

    private void findBestPath(Set<Shop> shops) {
        shoppingPlan = new ShoppingPlan(shops.size());
        Coordinates startCoordinates = shoppingList.getCoordinates();
        Coordinates currentCoordinates = startCoordinates;
        float totalCost = 0;
        int totalDistance = 0;
        Set<Product> shoppingListProducts = new HashSet<Product>(shoppingList.getProducts());
        while (!shops.isEmpty()) {
            Map<Shop, Integer> shopDistanceMap = nearestShopDistance(currentCoordinates, shops);
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
            shops.remove(shop);
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

    private class RandomShopGenerator {

        private List<Shop> shops = new LinkedList<Shop>();
        private Random random = new Random();

        private RandomShopGenerator(Collection<Shop> shops) {
            this.shops.addAll(shops);
        }

        private Shop randomShop() {
            int size = shops.size();
            if (size > 1) {
                return shops.remove(random.nextInt(size - 1));
            } else {
                return shops.iterator().next();
            }
        }
    }
}
