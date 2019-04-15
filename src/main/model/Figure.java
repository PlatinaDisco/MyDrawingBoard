package main.model;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Figure implements Serializable {
    private List<Point> coordinate = new ArrayList<>(); //坐标点序列

    public void draw(Graphics2D g) {
        for(int i = 0; i < coordinate.size()-1; i++) {
            int x1 = (int)coordinate.get(i).getX();
            int y1 = (int)coordinate.get(i).getY();
            int x2 = (int)coordinate.get(i+1).getX();
            int y2 = (int)coordinate.get(i+1).getY();
            g.drawLine(x1, y1, x2, y2);
        }
    }

    public void addPoint(int x, int y) {
        Point p = new Point(x, y);
        coordinate.add(p);
    }

    public List<Point> getCoordinate() {
        return this.coordinate;
    }

}

