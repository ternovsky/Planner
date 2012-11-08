package com.ternovsky.gui;

import com.ternovsky.Planner;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: ternovsky
 * Date: 07.11.12
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public class MainFrame extends JFrame {

    private static final int HEIGHT = 700;
    private static final int WIDTH = 1000;

    protected Planner planner = new Planner();
    protected final ChartPanel chartPanel;
    protected final ControlPanel controlPanel;

    public MainFrame() {
        setTitle("Планировщик покупок");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLayout(null);
        chartPanel = new ChartPanel(this);
        add(chartPanel);
        controlPanel = new ControlPanel(this);
        controlPanel.setBounds(700, 0, 300, 700);
        add(controlPanel);
    }


}
