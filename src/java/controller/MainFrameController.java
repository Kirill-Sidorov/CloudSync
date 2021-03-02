package controller;

import javax.swing.*;

public class MainFrameController extends JFrame {

    private JMenuBar menuBar;
    private JSplitPane splitPane;
    private FilePanelController leftPanel;
    private FilePanelController rightPanel;

    public MainFrameController() {
        menuBar = new JMenuBar();
        menuBar.add(new JMenu("Menu"));
        leftPanel = new FilePanelController();
        rightPanel = new FilePanelController();
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.5);
        setJMenuBar(menuBar);
        getContentPane().add(splitPane);
        setIconImage(new ImageIcon("src/resources/img/app-icon.png").getImage());
        setTitle("CloudSync");
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(1000, 600);
        setVisible(true);
    }
}
