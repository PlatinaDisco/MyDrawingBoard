package main.shapeRecognizer;

import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

public class ShapeRecognizer {

    public String recognizeShape(MatOfPoint mp, MatOfPoint2f mp2f) {
        String shape = "unidentified";
        double perimeter = Imgproc.arcLength(mp2f,true);
        MatOfPoint2f polyShape = new MatOfPoint2f();
        Imgproc.approxPolyDP(mp2f, polyShape,0.04 * perimeter,true);
        int shapeLen = polyShape.toArray().length;
        //根据轮廓凸点拟合结果，判断属于那个形状
        switch (shapeLen){
            case 3:
                shape = "三角形";
                break;
            case 4:
                Rect rect = Imgproc.boundingRect(mp);
                float width = rect.width;
                float height = rect.height;
                float ar = width / height;
                //计算宽高比，判断是矩形还是正方形
                if (ar >= 0.95 && ar <= 1.05) {
                    shape = "正方形";
                }
                else {
                    shape = "长方形";
                }
                break;
//            case 5:
//                shape = "五边形" ;
//                break;
            default:
                shape = "圆形";
                break;
        }

        return shape;
    }
}
