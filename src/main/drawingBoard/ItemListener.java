package main.drawingBoard;

import main.model.Figure;
import main.model.Picture;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import main.shapeRecognizer.ShapeRecognizer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ItemListener implements ActionListener {
    private DrawingBoard paint;

    //构造方法
    ItemListener(DrawingBoard paint){
        this.paint = paint;
    }

    //监听器的具体实现逻辑
    public void actionPerformed(ActionEvent e) {

        //判断是哪个菜单项被按下
        String command = e.getActionCommand();

        switch (command) {
            case "新建":
                createFile();
                break;
            case "打开":
                openFile();
                break;
            case "保存":
                saveFile();
                break;
            case "识别":
                detectFigure();
                break;
            case "标注":
                labelPicture();
                break;
            default:
                break;
        }
    }

    /*
     * 新建逻辑实现：
     * 当新建的时候，只需要把容器里面所有的对象和标注信息清空，然后将中间面板重绘就可以了
     */
    private void createFile() {
        int value = JOptionPane.showConfirmDialog(null, "是否需要保存当前文件？", "提示信息", 0);
        if(value == 0){
            saveFile();
        }
        if(value == 1){
            paint.setPicture(new Picture());
            paint.getPanelCenterRepaint();
            paint.setTestAreaContent("");
        }
    }

    /*
     * 打开逻辑实现：
     * 当点击打开菜单项时，首先应该清空容器里面的东西，然后面板重绘
     * 然后再把打开的文件利用对象输入流读入
     * 将读入的信息取出来，转换成相应的容器
     * 将容器中的对象和标注信息取出来，调用中间画板，进行绘制
     */
    private void openFile() {
        int value = JOptionPane.showConfirmDialog(null, "是否需要保存当前文件？", "提示信息", 0);
        if(value == 0){
            saveFile();
        }
        else if(value == 1){
            //清空容器里面的东西
            paint.setPicture(new Picture());
            paint.getPanelCenterRepaint();

            try {
                //弹出选择对话框，选择需要读入的文件
                JFileChooser chooser = new JFileChooser();
                chooser.showOpenDialog(null);
                File file = chooser.getSelectedFile();
                //如果为选中文件
                if(file == null){
                    JOptionPane.showMessageDialog(null, "没有选择文件");
                }
                else {
                    //选中了相应的文件，则选中的文件创建对象输入流
                    FileInputStream fis = new FileInputStream(file);
                    ObjectInputStream ois = new ObjectInputStream(fis);
                    //将读出来的对象转换成对象的容器进行接收
                    Picture picture = (Picture) ois.readObject();
                    //遍历容器里面的具体对象，将取出来的对象保存到容器里面
                    for (Figure figure : picture.getFiguresList()) {
                        paint.getPicture().addFigure(figure);
                        //调用中心画板的repaint()方法，将容器里面的图形绘制出来
                        paint.getPanelCenterRepaint();
                    }
                    //输出原先保存的标注信息
                    paint.setTestAreaContent(picture.getLabel());
                    ois.close();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void saveFile(){
        //现将可能存在的标注信息添加进Picture对象中保存
        String label = paint.getTextArea().getText();
        paint.getPicture().setLabel(label);

        //选择要保存的位置以及文件名字和信息
        JFileChooser chooser = new JFileChooser();
        chooser.showSaveDialog(null);
        File file = chooser.getSelectedFile();

        if(file == null){
            JOptionPane.showMessageDialog(null, "没有选择文件");
        }else {
            try {
                //根据要保存的文件创建对象输出流
                FileOutputStream fis = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fis);
                //将容器里面所绘制的图形及标注信息利用对象流全部写入选中的文件中
                oos.writeObject(paint.getPicture());
                JOptionPane.showMessageDialog(null, "保存成功！");
                oos.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    /*
     *通过调用OpenCV的图形识别接口，对加工过的绘画图形进行识别
     */
    private void detectFigure() {
        try {
            standardizePicture();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //读入图片
        Mat image = Imgcodecs.imread("src/main/temp/sc.png");
        //缩放之后的图片
        Mat imageResized = image.clone();
        double width = image.width();
        double height = image.height();
        //缩放图片
        Imgproc.resize(image, imageResized, new Size(300,height * (300 / width)));
        //模糊图像
        Mat blurredImg = imageResized.clone();
        Imgproc.GaussianBlur(imageResized, blurredImg, new Size(5,5),0);
        //彩色空间转换
        Mat grayImg = blurredImg.clone();
        Imgproc.cvtColor(blurredImg, grayImg, Imgproc.COLOR_BGR2GRAY);
        //生成阈值图像
        Mat threshImg = grayImg.clone();
        Imgproc.threshold(grayImg, threshImg, 60, 255, Imgproc.THRESH_BINARY);
        //定义2个
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(threshImg, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        String result = "识别结果：";
        for (int i = 0; i<contours.size(); i++) {
            String shape;
            MatOfPoint2f newMatOfPoint2f = new MatOfPoint2f(contours.get(i).toArray());
            ShapeRecognizer shapeRecognizer = new ShapeRecognizer();
            shape = shapeRecognizer.recognizeShape(contours.get(i), newMatOfPoint2f);
            result += shape;
            if(i != contours.size()-1) {
                result += "，";
            }
        }
        paint.setTestAreaContent(result);
    }

    /*
     *进行标注功能，打开文本域
     */
    private void labelPicture() {
        paint.setTestAreaContent("");
        paint.getTextArea().setEditable(true);
    }

    /*
     *将画板上的图形转换存为OpenCV图形识别的规定图像
     */
    private void standardizePicture() throws IOException {
        Dimension imageSize = paint.getPanelCenter().getSize();
        BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        for(Figure figure : paint.getPicture().getFiguresList()) {
            int length = figure.getCoordinate().size();
            int[] x = new int[length];
            int[] y = new int[length];
            for(int i = 0; i<length; i++) {
                x[i] = (int) figure.getCoordinate().get(i).getX();
                y[i] = (int) figure.getCoordinate().get(i).getY();
            }
            Polygon polygon = new Polygon(x, y, x.length);
            graphics.draw(polygon);
            graphics.setColor(Color.yellow);
            graphics.fill(polygon);
        }
        graphics.dispose();
        File f = new File("src/main/temp/sc.png");
        if(!f.exists()) {
            if(f.createNewFile()) {
                System.out.println("创建图片成功");
            }
            else {
                System.out.println("创建图片失败");
            }
        }
        ImageIO.write(image, "png", f);
    }
}
