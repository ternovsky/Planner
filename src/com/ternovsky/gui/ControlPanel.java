package com.ternovsky.gui;

import com.ternovsky.Planner;
import com.ternovsky.domain.*;
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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
    public static final String MIN_DISTANCE = "Минимизировать проходимое расстояние";
    public static final String MIN_COST = "Минимизировать стоимость покупки";
    public static final String ONLY_INTEGER_MESSAGE = "В качестве координат допустимо использовать только целые числа";
    public static final String PRODUCT_REQUIRED_MESSAGE = "Загрузите список магазинов и выберите хотя бы один продукт";
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
    private final JRadioButton minCostJRadioButton;
    private final JRadioButton minDistanceJRadioButton;
    private final ButtonGroup purposeButtonGroup;
    private final JLabel costJLabel;
    private final JLabel distanceJLabel;
    private final JList productsJList;
    private final JLabel shopInfoJlabel;

    public ControlPanel(final MainFrame mainFrame) {
        shoppingList = new ShoppingList();
        this.mainFrame = mainFrame;
        planner = this.mainFrame.planner;
        setSize(WIDTH, HEIGHT);
        setLayout(new FlowLayout(FlowLayout.LEFT));

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
        productCodesJList.setMinimumSize(new Dimension(200, 200));
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
                Integer x = convertToInteger(xJTextField.getText());
                if (x != null && prevX != x) {
                    Integer y = convertToInteger(yJTextField.getText());
                    if (y != null) {
                        shoppingList.setCoordinates(new Coordinates(x, y));
                        planner.getPlannerContext().setShoppingList(shoppingList);
                        mainFrame.chartPanel.changeInitialCoordinates();
                        prevX = x;
                    }
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
                Integer y = convertToInteger(yJTextField.getText());
                if (y != null && prevY != y) {
                    Integer x = convertToInteger(xJTextField.getText());
                    if (x != null) {
                        shoppingList.setCoordinates(new Coordinates(x, y));
                        planner.getPlannerContext().setShoppingList(shoppingList);
                        mainFrame.chartPanel.changeInitialCoordinates();
                        prevY = y;
                    }
                }
            }
        });
        coordinatesJPanel.add(yJTextField);
        add(coordinatesJPanel);

        buttonGroupJPanel = new JPanel(new GridLayout(2, 1));
        purposeButtonGroup = new ButtonGroup();
        minCostJRadioButton = new JRadioButton(MIN_COST);
        purposeButtonGroup.add(minCostJRadioButton);
        buttonGroupJPanel.add(minCostJRadioButton);
        minCostJRadioButton.setSelected(true);
        minDistanceJRadioButton = new JRadioButton(MIN_DISTANCE);
        purposeButtonGroup.add(minDistanceJRadioButton);
        buttonGroupJPanel.add(minDistanceJRadioButton);
        add(buttonGroupJPanel);

        buildShoppingPlanJButton = new JButton();
        buildShoppingPlanJButton.setText("Построить маршрут");
        buildShoppingPlanJButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buildShoppingList();
                planner.buildShoppingPlan();
                mainFrame.chartPanel.showPath();
                ShoppingPlan shoppingPlan = planner.getPlannerContext().getShoppingPlan();
                costJLabel.setText(String.valueOf(shoppingPlan.getCost()));
                distanceJLabel.setText(String.valueOf(shoppingPlan.getDistance()));
            }
        });
        add(buildShoppingPlanJButton);

        JPanel resultPanel = new JPanel(new GridLayout(2, 2));
        resultPanel.add(new JLabel("Стоимость:"));
        costJLabel = new JLabel();
        resultPanel.add(costJLabel);
        resultPanel.add(new JLabel("Пройденный путь:"));
        distanceJLabel = new JLabel();
        resultPanel.add(distanceJLabel);
        add(resultPanel);

        JPanel shopInfoJPanel = new JPanel(new GridLayout(2, 1));
        shopInfoJlabel = new JLabel("Информация о магазине (Кликнете по магазину) ");
        shopInfoJPanel.add(shopInfoJlabel);
        productsJList = new JList();
        shopInfoJPanel.add(new JScrollPane(productsJList));
        add(shopInfoJPanel);
    }

    protected void showShopInfo(Shop shop) {
        ShoppingPlan shoppingPlan = planner.getPlannerContext().getShoppingPlan();
        shopInfoJlabel.setText("Магазин " + shop);
        if (shoppingPlan == null) {
            productsJList.setListData(shop.getProducts().toArray());
        } else {
            Set<String> bought = new TreeSet<String>();
            Set<String> notBought = new TreeSet<String>();
            for (Product product : shop.getProducts()) {
                Set<Product> productsByShop = shoppingPlan.getProductsByShop(shop);
                if (productsByShop != null && productsByShop.contains(product)) {
                    bought.add(" + " + product);
                } else {
                    notBought.add(product.toString());
                }
            }
            ArrayList<String> productInfo = new ArrayList<String>(bought.size() + notBought.size());
            productInfo.addAll(bought);
            productInfo.addAll(notBought);
            productsJList.setListData(productInfo.toArray());
        }
    }

    private Integer convertToInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, ONLY_INTEGER_MESSAGE);
        }
        return null;
    }

    private void buildShoppingList() {
        Integer x = convertToInteger(xJTextField.getText());
        Integer y = convertToInteger(yJTextField.getText());
        if (x == null && y == null) {
            return;
        }
        if (minCostJRadioButton.isSelected()) {
            shoppingList.setOptimizationParameter(ShoppingList.OptimizationParameter.COST);
        } else if (minDistanceJRadioButton.isSelected()) {
            shoppingList.setOptimizationParameter(ShoppingList.OptimizationParameter.DISTANCE);
        }

        Set<Product> products = new HashSet<Product>();
        Object[] selectedValues = productCodesJList.getSelectedValues();
        if (selectedValues.length < 1) {
            JOptionPane.showMessageDialog(this, PRODUCT_REQUIRED_MESSAGE);
            return;
        }
        for (Object o : selectedValues) {
            String productCode = (String) o;
            products.add(new Product(productCode));
        }
        shoppingList.setProducts(products);
        planner.getPlannerContext().setShoppingList(shoppingList);

    }
}
