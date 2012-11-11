package com.ternovsky;

import com.ternovsky.gui.MainFrame;

import javax.swing.*;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.io.IOException;

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
    public static final String SHOPPING_PLAN_XML = "C:\\Users\\ternovsky\\Documents\\GitHub\\Planner\\shopping-plan.xml";

    public static void main(String[] args) throws XMLStreamException, IOException {
        if (args.length != 0) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    mainFrame.setVisible(true);
                }
            });
        } else {
            Planner planner = new Planner();
            planner.readData(SHOPS_XML, SHOPPING_LIST_XML);
            planner.buildShoppingPlan();
            planner.writeData(SHOPPING_PLAN_XML);
        }
    }
}
