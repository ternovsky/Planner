package com.ternovsky.gui;

import com.ternovsky.domain.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Created with IntelliJ IDEA.
 * User: ternovsky
 * Date: 08.11.12
 * Time: 16:03
 * To change this template use File | Settings | File Templates.
 */
public class ArrowComponent extends JComponent {

    private Coordinates start;
    private Coordinates finish;

    public ArrowComponent(Coordinates start, Coordinates finish) {
        this.start = start;
        this.finish = finish;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics2D = (Graphics2D) g;
        graphics2D.setColor(Color.BLACK);
        Point2D.Float startPoint = new Point2D.Float(start.getX(), start.getY());
        Point2D.Float endPoint = new Point2D.Float(finish.getX(), finish.getY());
        graphics2D.draw(new Line2D.Float(startPoint, endPoint));
    }
}
