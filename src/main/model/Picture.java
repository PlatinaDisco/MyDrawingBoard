package main.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Picture implements Serializable {
    private List<Figure> list = new ArrayList<>();
    private String label = ""; //标注内容

    public void addFigure(Figure figure) { list.add(figure); }

    public List<Figure> getFiguresList() {return this.list; }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

}
