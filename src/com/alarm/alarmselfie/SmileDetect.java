package com.alarm.alarmselfie;

import java.io.File;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;
import org.opencv.objdetect.CascadeClassifier;

public class SmileDetect {

public void detectSmile(String filename) {
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    System.out.println("\nRunning SmileDetector");

    CascadeClassifier faceDetector = new CascadeClassifier(new File(
            "src/main/resources/haarcascade_frontalface_alt.xml").getAbsolutePath());
    CascadeClassifier smileDetector = new CascadeClassifier(
            new File("src/main/resources/haarcascade_smile.xml").getAbsolutePath());
    Mat image = Highgui.imread(filename);
    MatOfRect faceDetections = new MatOfRect();
    MatOfRect smileDetections = new MatOfRect();
    faceDetector.detectMultiScale(image, faceDetections);

    System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));

    for (Rect rect : faceDetections.toArray()) {
        Core.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                new Scalar(0, 255, 0));
    }
    Mat face = image.submat(faceDetections.toArray()[0]);
    smileDetector.detectMultiScale(face, smileDetections);

    for (Rect rect : smileDetections.toArray()) {
        Core.rectangle(face, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                new Scalar(0, 255, 0));
    }

    String outputFilename = "ouput.png";
    System.out.println(String.format("Writing %s", outputFilename));
    Highgui.imwrite(outputFilename, image);
    Highgui.imwrite("ee.png", face);
}
}