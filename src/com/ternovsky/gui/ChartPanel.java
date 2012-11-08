package com.ternovsky.gui;

import com.ternovsky.Planner;
import com.ternovsky.domain.Coordinates;
import com.ternovsky.domain.Shop;

import javax.swing.*;
import java.awt.*;

import static java.lang.Math.abs;

/**
 * Created with IntelliJ IDEA.
 * User: ternovsky
 * Date: 07.11.12
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
public class ChartPanel extends JPanel {

    public static final int WIDTH = 700;
    public static final int HEIGHT = 700;

    protected MainFrame mainFrame;
    private Planner planner;
    private int absMaxCoordinate = 0;
    private int shift;
    private double coefficient;
    private ChartPoint initialChartPoint;

    public ChartPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        planner = this.mainFrame.planner;
        setBackground(Color.WHITE);
        setLayout(null);
        setSize(WIDTH, HEIGHT);
    }

    private void findAbsMaxCoordinate() {
        absMaxCoordinate = 0;
        for (Shop shop : planner.getPlannerContext().getShops()) {
            Coordinates coordinates = shop.getCoordinates();
            int absX = abs(coordinates.getX());
            if (absX > absMaxCoordinate) {
                absMaxCoordinate = absX;
            }
            int absY = abs(coordinates.getY());
            if (absY > absMaxCoordinate) {
                absMaxCoordinate = absY;
            }
        }
        Coordinates coordinates = planner.getPlannerContext().getShoppingList().getCoordinates();
        int absX = abs(coordinates.getX());
        if (absX > absMaxCoordinate) {
            absMaxCoordinate = absX;
        }
        int absY = abs(coordinates.getY());
        if (absY > absMaxCoordinate) {
            absMaxCoordinate = absY;
        }
    }

    private void findShift() {
        findAbsMaxCoordinate();
        coefficient = 700.0 / (2 * absMaxCoordinate + 40);
        shift = (int) (absMaxCoordinate * coefficient);
    }

    private Coordinates convertToNewCoordinates(Coordinates coordinates) {
        int x = (int) (coefficient * coordinates.getX() + shift);
        int y = (int) (-coefficient * coordinates.getY() + shift);
        return new Coordinates(x, y);
    }

    private void addShop(Shop shop) {
        Coordinates coordinates = convertToNewCoordinates(shop.getCoordinates());
        ChartPoint chartPoint = new ChartPoint(coordinates.getX(), coordinates.getY());
        add(chartPoint);
    }

    protected final void showChart() {
        findShift();
        for (Shop shop : planner.getPlannerContext().getShops()) {
            addShop(shop);
        }
        Coordinates coordinates = convertToNewCoordinates(planner.getPlannerContext().getShoppingList().getCoordinates());
        initialChartPoint = new ChartPoint(coordinates.getX(), coordinates.getY(), Color.CYAN);
        add(initialChartPoint);
        repaint();
    }

    protected void changeInitialCoordinates() {
        if (initialChartPoint != null) {
            removeAll();
            showChart();
        }
    }
}
