package com.ternovsky.xml;

import com.ternovsky.ObjectStorage;
import com.ternovsky.Planner;
import com.ternovsky.PlannerContext;
import com.ternovsky.domain.Coordinates;
import com.ternovsky.domain.Product;
import com.ternovsky.domain.Shop;
import com.ternovsky.domain.ShoppingList;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created with IntelliJ IDEA.
 * User: Елена
 * Date: 02.11.12
 * Time: 21:21
 * To change this template use File | Settings | File Templates.
 */
public class XmlHelper {

    public static final String SHOP = "shop";
    public static final String NAME = "name";
    public static final String COORDINATES = "coordinates";
    public static final String PRODUCT = "product";
    public static final String PRODUCTS = "products";
    public static final String COST = "cost";
    public static final String DISTANCE = "distance";
    public static final String SHOPPING_LIST = "shoppingList";

    public static void readShops(File file, Planner planner)
            throws FileNotFoundException, XMLStreamException {

        XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(new FileReader(file));
        ObjectStorage objectStorage = planner.getObjectStorage();
        Shop shop = new Shop();
        while (xmlStreamReader.hasNext()) {
            if (xmlStreamReader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                String startLocalName = xmlStreamReader.getLocalName();
                if (startLocalName.equals(SHOP)) {
                    shop.setId(xmlStreamReader.getAttributeValue(0));
                } else if (startLocalName.equals(NAME)) {
                    shop.setName(xmlStreamReader.getElementText());
                } else if (startLocalName.equals(COORDINATES)) {
                    Coordinates coordinates = new Coordinates();
                    coordinates.setX(Integer.parseInt(xmlStreamReader.getAttributeValue(0)));
                    coordinates.setY(Integer.parseInt(xmlStreamReader.getAttributeValue(1)));
                    shop.setCoordinates(coordinates);
                } else if (startLocalName.equals(PRODUCT)) {
                    Product product = new Product();
                    product.setCode(xmlStreamReader.getAttributeValue(0));
                    product.setPrice(Float.parseFloat(xmlStreamReader.getAttributeValue(1)));
                    product.setName(xmlStreamReader.getElementText());
                    shop.getProducts().add(product);
                    objectStorage.add(shop, product);
                }
            } else if (xmlStreamReader.getEventType() == XMLStreamConstants.END_ELEMENT) {
                String endLocalName = xmlStreamReader.getLocalName();
                if (endLocalName.equals(SHOP)) {
                    PlannerContext plannerContext = planner.getPlannerContext();
                    plannerContext.getShops().add(shop);
                    shop = new Shop();
                }
            }
            xmlStreamReader.next();
        }
        xmlStreamReader.close();
    }

    public static void readShoppingList(File file, Planner planner)
            throws FileNotFoundException, XMLStreamException {

        XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(new FileReader(file));
        ShoppingList shoppingList = new ShoppingList();
        while (xmlStreamReader.hasNext()) {
            if (xmlStreamReader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                String localName = xmlStreamReader.getLocalName();
                if (localName.equals(PRODUCT)) {
                    Product product = new Product();
                    product.setCode(xmlStreamReader.getAttributeValue(0));
                    product.setName(xmlStreamReader.getElementText());
                    shoppingList.getProducts().add(product);
                } else if (localName.equals(COORDINATES)) {
                    Coordinates coordinates = new Coordinates();
                    coordinates.setX(Integer.parseInt(xmlStreamReader.getAttributeValue(0)));
                    coordinates.setY(Integer.parseInt(xmlStreamReader.getAttributeValue(1)));
                    shoppingList.setCoordinates(coordinates);
                } else if (localName.equals(COST)) {
                    shoppingList.setOptimizationParameter(ShoppingList.OptimizationParameter.COST);
                } else if (localName.equals(DISTANCE)) {
                    shoppingList.setOptimizationParameter(ShoppingList.OptimizationParameter.DISTANCE);
                }
            } else if (xmlStreamReader.getEventType() == XMLStreamConstants.END_ELEMENT) {
                String localName = xmlStreamReader.getLocalName();
                if (localName.equals(SHOPPING_LIST)) {
                    planner.getPlannerContext().setShoppingList(shoppingList);
                    return;
                }
            }
            xmlStreamReader.next();
        }
        xmlStreamReader.close();
    }
}
