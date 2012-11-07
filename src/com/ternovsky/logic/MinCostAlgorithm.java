package com.ternovsky.logic;

import com.ternovsky.Planner;
import com.ternovsky.domain.Product;
import com.ternovsky.domain.Shop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 04.11.12
 * Time: 20:49
 * To change this template use File | Settings | File Templates.
 */
public class MinCostAlgorithm extends Algorithm {

    public MinCostAlgorithm(Planner planner) {
        this.planner = planner;
        plannerContext = planner.getPlannerContext();
        objectStorage = planner.getObjectStorage();
        shoppingList = plannerContext.getShoppingList();
        shoppingPlan = plannerContext.getShoppingPlan();
    }

    protected Map<Shop, Set<Product>> findShops() {
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
