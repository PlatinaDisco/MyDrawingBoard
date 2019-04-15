package main.drawingBoard;

import main.model.Figure;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class DrawListener implements MouseListener,MouseMotionListener {
    private Graphics2D g;
    private Figure figure = new Figure();
    private DrawingBoard board;

    //构造函数
    DrawListener(Graphics g2, DrawingBoard db) {
        this.g = (Graphics2D)g2;
        this.board = db;
    }

    public void mousePressed(MouseEvent e) {}

    //获取鼠标释放的坐标，并且认定为绘画完一个图形，并存入容器中
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        figure.addPoint(x, y);
        figure.draw(g);
        board.getPicture().addFigure(figure);
        figure = new Figure();
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        figure.addPoint(x, y);
        figure.draw(g);
    }

    public void mouseMoved(MouseEvent e) {}

}
