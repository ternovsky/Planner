package com.ternovsky;

import com.ternovsky.domain.Shop;
import com.ternovsky.domain.ShoppingList;
import com.ternovsky.domain.ShoppingPlan;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 03.11.12
 * Time: 21:56
 * To change this template use File | Settings | File Templates.
 */
public class PlannerContext {

    private Set<Shop> shops;
    private ShoppingList shoppingList;
    private ShoppingPlan shoppingPlan;

    public PlannerContext() {
        shops = new HashSet<Shop>();
        shoppingList = new ShoppingList();
    }

    public Set<Shop> getShops() {
        return shops;
    }

    public void setShops(Set<Shop> shops) {
        this.shops = shops;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public ShoppingPlan getShoppingPlan() {
        return shoppingPlan;
    }

    public void setShoppingPlan(ShoppingPlan shoppingPlan) {
        this.shoppingPlan = shoppingPlan;
    }
}
