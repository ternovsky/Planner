package com.ternovsky;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 02.11.12
 * Time: 22:11
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static final String SHOPS_XML = "C:\\Users\\ternovsky\\Documents\\GitHub\\Planner\\src\\shops.xml";
    public static final String SHOPPING_LIST_XML = "C:\\Users\\ternovsky\\Documents\\GitHub\\Planner\\src\\shopping-list.xml";

    public static void main(String[] args) throws XMLStreamException, FileNotFoundException {
        if (args.length == 0) {

        } else {
            Planner planner = new Planner();
            planner.readData(SHOPS_XML, SHOPPING_LIST_XML);
            planner.buildShoppingPlan();
        }
    }
}
