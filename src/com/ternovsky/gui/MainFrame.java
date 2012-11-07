package com.ternovsky.gui;

import com.ternovsky.Planner;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ternovsky
 * Date: 07.11.12
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public class MainFrame extends JFrame {

    public static final int HEIGHT = 700;
    public static final int WIDTH = 900;
    protected Planner planner = new Planner();

    public MainFrame() {
        setTitle("Планировщик покупок");
        setSize(WIDTH, HEIGHT);
        ControlPanel controlPanel = new ControlPanel(this);
        add(controlPanel, BorderLayout.EAST);
        ChartPanel chartPanel = new ChartPanel();
        add(chartPanel, BorderLayout.CENTER);
    }
}
