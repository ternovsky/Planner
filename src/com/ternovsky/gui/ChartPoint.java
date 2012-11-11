package com.ternovsky.gui;

import com.ternovsky.domain.Shop;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created with IntelliJ IDEA.
 * User: ternovsky
 * Date: 08.11.12
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class ChartPoint extends JPanel {

    public static final int SIZE = 6;
    private Shop shop;
    private MainFrame mainFrame;

    public ChartPoint(Shop shop, MainFrame mainFrame, int x, int y, Color color) {
        this(x, y, color);
        this.mainFrame = mainFrame;
        this.shop = shop;
    }

    public ChartPoint(int x, int y) {
        setBackground(Color.RED);
        setBounds(x, y, SIZE, SIZE);
        setSize(SIZE, SIZE);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (mainFrame != null) {
                    mainFrame.controlPanel.showShopInfo(shop);
                }
            }
        });
    }

    public ChartPoint(int x, int y, Color color) {
        this(x, y);
        setBackground(color);
    }
}
