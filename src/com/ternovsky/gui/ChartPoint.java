package com.ternovsky.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: ternovsky
 * Date: 08.11.12
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class ChartPoint extends JPanel {

    public static final int SIZE = 10;

    public ChartPoint(int x, int y) {
        setBackground(Color.RED);
        setBounds(x, y, SIZE, SIZE);
        setSize(SIZE, SIZE);
    }

    public ChartPoint(int x, int y, Color color) {
        this(x, y);
        setBackground(color);
    }
}
