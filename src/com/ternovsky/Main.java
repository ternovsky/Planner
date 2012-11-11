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

    public static final String BASE_DIR = "default" + "\\";
    public static final String BASE_PATH = "C:\\Users\\ternovsky\\Documents\\GitHub\\Planner\\test\\" + BASE_DIR;

    public static final String SHOPS_XML = BASE_PATH + "shops.xml";
    public static final String SHOPPING_LIST_XML = BASE_PATH + "shopping-list.xml";
    public static final String SHOPPING_PLAN_XML = BASE_PATH + "shopping-plan.xml";

    public static void main(String[] args) throws XMLStreamException, IOException {
        int argsCount = args.length;
        if (argsCount == 0) {
            EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                    mainFrame.setVisible(true);
                }
            });
        } else if (argsCount == 1) {
            Planner planner = new Planner();
            planner.readData(SHOPS_XML, SHOPPING_LIST_XML);
            planner.buildShoppingPlan();
            planner.writeData(SHOPPING_PLAN_XML);
        } else if (argsCount == 3) {
            Planner planner = new Planner();
            planner.readData(args[0], args[1]);
            planner.buildShoppingPlan();
            planner.writeData(args[2]);
        }
    }
}
