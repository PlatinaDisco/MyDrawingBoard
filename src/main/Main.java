package main;

import main.drawingBoard.DrawingBoard;
import org.opencv.core.Core;

import java.io.*;

public class Main {
    static{
        //System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        loadLibrary();
    }
    public static void main(String[] args) throws  Exception{
        DrawingBoard db = new DrawingBoard();
        db.initFrame();
    }

    private static void loadLibrary() {
        try (InputStream in = new FileInputStream("./opencv4j/x64/opencv_java343.dll")) {
            File dll = new File(System.getProperty("java.io.tmpdir") + "/opencv_java343.dll");
            if (!dll.exists()) {
                dll.createNewFile();
            }
            OutputStream out = new FileOutputStream(dll);

            byte[] buf = new byte[1024];
            int i;
            while ((i = in.read(buf)) != -1) {
                out.write(buf, 0, i);
            }

            out.close();

            System.load(dll.toString());
            dll.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
