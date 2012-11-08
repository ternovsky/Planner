package com.ternovsky.gui;

import com.ternovsky.Planner;
import com.ternovsky.domain.Coordinates;
import com.ternovsky.domain.ShoppingList;
import com.ternovsky.xml.XmlHelper;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created with IntelliJ IDEA.
 * User: ternovsky
 * Date: 07.11.12
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
public class ControlPanel extends JPanel {

    public static final int WIDTH = 300;
    public static final int HEIGHT = 700;
    protected Planner planner;
    protected JButton shopsJButton;
    protected JFileChooser shopsJFileChooser;
    protected JList productCodesJList;
    protected JPanel buttonGroupJPanel;
    protected JButton buildShoppingPlanJButton;
    private MainFrame mainFrame;
    private ShoppingList shoppingList;
    private final JTextField xJTextField;
    private final JTextField yJTextField;

    public ControlPanel(final MainFrame mainFrame) {
        shoppingList = new ShoppingList();
        this.mainFrame = mainFrame;
        planner = this.mainFrame.planner;
        setSize(WIDTH, HEIGHT);
        setLayout(new FlowLayout(FlowLayout.CENTER));

        shopsJButton = new JButton();
        shopsJButton.setText("Загрузить список магазинов из файла");
        shopsJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shopsJFileChooser = new JFileChooser();
                shopsJFileChooser.setCurrentDirectory(new File("."));
                shopsJFileChooser.setFileFilter(new FileNameExtensionFilter("*.xml", "xml"));
                int result = shopsJFileChooser.showDialog(shopsJFileChooser.getParent(), "Выбрать");
                if (result == JFileChooser.APPROVE_OPTION) {
                    try {
                        XmlHelper.readShops(shopsJFileChooser.getSelectedFile(), planner);
                        productCodesJList.setListData(planner.getObjectStorage().orderedProductCodesByAlphabet().toArray());
                        mainFrame.chartPanel.showChart();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (XMLStreamException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
            }
        });
        add(shopsJButton);

        productCodesJList = new JList();
        add(new JScrollPane(productCodesJList));

        JLabel coordinatesJLabel = new JLabel();
        coordinatesJLabel.setText("Начальная точка");
        add(coordinatesJLabel);
        JPanel coordinatesJPanel = new JPanel(new GridLayout(2, 2));
        coordinatesJPanel.add(new JLabel("x: "));
        xJTextField = new JTextField(0);
        xJTextField.setColumns(4);
        xJTextField.setText("0");
        xJTextField.addFocusListener(new FocusAdapter() {
            private int prevX;

            @Override
            public void focusLost(FocusEvent e) {
                int x = convertToInteger(xJTextField.getText());
                if (prevX != x) {
                    int y = convertToInteger(yJTextField.getText());
                    shoppingList.setCoordinates(new Coordinates(x, y));
                    planner.getPlannerContext().setShoppingList(shoppingList);
                    mainFrame.chartPanel.changeInitialCoordinates();
                    prevX = x;
                }
            }
        });
        coordinatesJPanel.add(xJTextField);
        coordinatesJPanel.add(new JLabel("y: "));
        yJTextField = new JTextField();
        yJTextField.setColumns(4);
        yJTextField.setText("0");
        yJTextField.addFocusListener(new FocusAdapter() {
            private int prevY;

            @Override
            public void focusLost(FocusEvent e) {
                int y = convertToInteger(yJTextField.getText());
                if (prevY != y) {
                    int x = convertToInteger(xJTextField.getText());
                    shoppingList.setCoordinates(new Coordinates(x, y));
                    planner.getPlannerContext().setShoppingList(shoppingList);
                    mainFrame.chartPanel.changeInitialCoordinates();
                    prevY = y;
                }
            }
        });
        coordinatesJPanel.add(yJTextField);
        add(coordinatesJPanel);

        buttonGroupJPanel = new JPanel(new GridLayout(2, 1));
        ButtonGroup purposeButtonGroup = new ButtonGroup();
        JRadioButton minCostJRadioButton = new JRadioButton("Минимизировать стоимость покупки");
        purposeButtonGroup.add(minCostJRadioButton);
        buttonGroupJPanel.add(minCostJRadioButton);
        minCostJRadioButton.setSelected(true);
        JRadioButton minDistanceJRadioButton = new JRadioButton("Минимизировать проходимое расстояние");
        purposeButtonGroup.add(minDistanceJRadioButton);
        buttonGroupJPanel.add(minDistanceJRadioButton);
        add(buttonGroupJPanel);

        buildShoppingPlanJButton = new JButton();
        buildShoppingPlanJButton.setText("Построить маршрут");
        add(buildShoppingPlanJButton);
    }

    private int convertToInteger(String s) {
        return Integer.parseInt(s);
    }

    private void showAvailableProducts() {

    }
}
