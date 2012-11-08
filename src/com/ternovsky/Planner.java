package com.ternovsky;

import com.ternovsky.domain.ShoppingList;
import com.ternovsky.logic.MinCostAlgorithm;
import com.ternovsky.logic.MinDistanceAlgorithm;
import com.ternovsky.xml.XmlHelper;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 02.11.12
 * Time: 21:09
 * To change this template use File | Settings | File Templates.
 */
public class Planner {

    private PlannerContext plannerContext;
    private ObjectStorage objectStorage;

    public Planner() {
        plannerContext = new PlannerContext();
        objectStorage = new ObjectStorage();
    }

    public PlannerContext getPlannerContext() {
        return plannerContext;
    }

    public ObjectStorage getObjectStorage() {
        return objectStorage;
    }

    public void readData(String shopsXml, String shoppingListXml) throws XMLStreamException, FileNotFoundException {
        XmlHelper.readShoppingList(new File(shoppingListXml), this);
        XmlHelper.readShops(new File(shopsXml), this);
    }

    public void writeData(String shoppingPlanXml) throws XMLStreamException, IOException {
        XmlHelper.writeShoppingPlan(new File(shoppingPlanXml), this);
    }

    public void buildShoppingPlan() {
        ShoppingList.OptimizationParameter optimizationParameter = plannerContext.getShoppingList().getOptimizationParameter();
        if (optimizationParameter == ShoppingList.OptimizationParameter.COST) {
            MinCostAlgorithm algorithm = new MinCostAlgorithm(this);
            algorithm.buildShoppingPlan();
        } else if (optimizationParameter == ShoppingList.OptimizationParameter.DISTANCE) {
            MinDistanceAlgorithm algorithm = new MinDistanceAlgorithm(this);
            algorithm.buildShoppingPlan();
        }
    }
}
