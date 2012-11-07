package com.ternovsky.gui;

import com.ternovsky.Planner;
import com.ternovsky.xml.XmlHelper;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLStreamException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    protected Planner planner;
    protected JButton shopsJButton;
    protected JFileChooser shopsJFileChooser;
    protected JList productCodesJList;
    protected JPanel buttonGroupJPanel;
    protected JButton buildShoppingPlanJButton;

    public ControlPanel(MainFrame mainFrame) {
        planner = mainFrame.planner;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);

        shopsJButton = new JButton();
        shopsJButton.setText("Загрузить список городов из файла");
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
                        productCodesJList.setListData(planner.getObjectStorage().getOrderedProductCodes().toArray());
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

    private void showAvailableProducts() {

    }
}
