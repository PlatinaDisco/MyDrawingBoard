package main.drawingBoard;

import main.model.Figure;
import main.model.Picture;

import javax.swing.*;
import java.awt.*;

public class DrawingBoard extends JFrame {

    //画笔颜色（默认为黑色）
    private static final Color color = Color.BLACK;
    //画笔粗细
    private static final int width = 1;

    private Graphics2D g;
    //容器
    private Picture picture = new Picture();

    private JPanel panelCenter;

    private JTextArea textArea;

    public void initFrame()throws Exception{

        //设置窗体相关属性
        this.setSize(600,500);
        this.setTitle("My drawing board");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //把添加菜单作为一个方法封装起来
        addMenu();

        //窗体添加主面板
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        this.add(panel);

        //主面板添加中心面板以供绘画
        panelCenter = new JPanel() {
            public void paint(Graphics g1) {
                g = (Graphics2D) g1;
                //画船体
                super.paint(g);
                for(Figure figure : picture.getFiguresList()) {
                    figure.draw(g);
                }
            }
        };
        panelCenter.setBackground(Color.white);
        panel.add(panelCenter);


        //主面板添加下方文本域
        textArea = new JTextArea("",2, 20);
        textArea.setFont(new Font("宋体", Font.PLAIN,20));
        textArea.setEditable(false);
        textArea.setBackground(Color.LIGHT_GRAY);
        panel.add(textArea, BorderLayout.SOUTH);

        this.setVisible(true);

        //画笔必须在setVisible后才能拿
        g = (Graphics2D) panelCenter.getGraphics();
        g.setColor(color);
        g.setStroke(new BasicStroke(width));

        //传递画笔，以及this对象
        DrawListener dl = new DrawListener(g, this);

        //添加普通鼠标监听器
        panelCenter.addMouseListener(dl);

        //添加鼠标拖动监听器
        panelCenter.addMouseMotionListener(dl);

    }

    private void addMenu() throws Exception{
        //菜单条对象创建
        JMenuBar bar = new JMenuBar();
        //菜单创建
        JMenu file = new JMenu("文件");
        JMenu function = new JMenu("功能");
        //菜单项监听器创建
        ItemListener il = new ItemListener(this);
        //创建三个菜单项
        JMenuItem create = new JMenuItem("新建");
        JMenuItem open = new JMenuItem("打开");
        JMenuItem save = new JMenuItem("保存");
        JMenuItem detect = new JMenuItem("识别");
        JMenuItem label = new JMenuItem("标注");
        //给每个菜单项添加监听器
        create.addActionListener(il);
        open.addActionListener(il);
        save.addActionListener(il);
        detect.addActionListener(il);
        label.addActionListener(il);
        //将菜单条添加到窗体上
        this.setJMenuBar(bar);
        //将菜单添加到菜单条上
        bar.add(file);
        bar.add(function);
        //将菜单项添加到菜单上
        file.add(create);
        file.add(open);
        file.add(save);
        function.add(detect);
        function.add(label);
    }

    Picture getPicture() { return this.picture; }

    void setPicture(Picture picture) { this.picture = picture; }

    void getPanelCenterRepaint() {
        this.panelCenter.repaint();
    }

    JPanel getPanelCenter() { return this.panelCenter; }

    void setTestAreaContent(String content) {
        textArea.setEditable(true);
        textArea.setText(content);
        textArea.setEditable(false);
    }

    JTextArea getTextArea() { return this.textArea; }

}
