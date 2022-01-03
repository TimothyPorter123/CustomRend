package application;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

import javafx.animation.Animation;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.*;
import model.objects.SimpleUVSphere;
import view.UserModelWindow;
import model.objects.Camera;
import model.objects.PerspectiveCamera;
import model.objects.SimpleTriangle;
import model.math.TransformMatrix;
import model.math.Vector3;
import view.shaders.SimpleShader;
import model.objects.SimpleSquare;
import javafx.embed.swing.*;

public class FrameController extends Application {

  SimpleSquare squareModel;
  SimpleSquare secondSquare;
  SimpleTriangle triangle;
  SimpleUVSphere sphere;
  TransformMatrix simpleRotate;
  TransformMatrix reverseRotate;
  TransformMatrix simpleMove;

  int imageWidth = 800;
  int imageHeight = 600;
  Label infoField;
  static FrameController mainFrame;
  ImageView renderPanel;

  //remove
  Camera mainCam;
  UserModelWindow mainWindow;

  long lastFrameMilli = System.currentTimeMillis();
  int framesPassed;

  @Override
  public void start(Stage stage) throws IOException {
    this.setClassDefaults();
    mainFrame = this;

    renderPanel = new ImageView();
    renderPanel.setFitWidth(imageWidth);
    renderPanel.setFitHeight(imageHeight);

    infoField = new Label("default text");
    infoField.setMaxWidth(imageWidth);

    HBox hbox = new HBox(renderPanel, infoField);
    Scene scene = new Scene(hbox, imageWidth, imageHeight);
    stage.setTitle("Test JavaFX");

    stage.setScene(scene);
    stage.show();
    Animation frameAnim = new Animation() {
    }
    while(true) {
      if(mainFrame != null) {
        mainFrame.mainWindow.render();
        mainFrame.UpdateFrame(mainFrame.mainWindow);
      }
    }
    /*renderPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        mainCam.transform(new TransformMatrix().rotate(new Vector3(5, 0, 0)));
      }
    });*/
  }

  private void setClassDefaults() {
    squareModel = new SimpleSquare();
    secondSquare = new SimpleSquare();
    triangle = new SimpleTriangle();
    sphere = new SimpleUVSphere();

    simpleRotate = new TransformMatrix().rotate(new Vector3(1.0f, 1.0f, 0.0f));
    reverseRotate = new TransformMatrix().rotate(new Vector3(-1.0f, -1.0f, 0.0f));
    simpleMove = new TransformMatrix().move(new Vector3(0.0f, 0.1f, 0.0f));

    //temp ?

    //CLIPPING PLANE =/= 0
    mainCam = new PerspectiveCamera(90, 0.1f, 1000f, (float)imageHeight / imageWidth);
    mainCam.transform(new TransformMatrix().move(new Vector3(3.0f, 3.0f, 3.0f)));
    mainCam.transform(new TransformMatrix().rotate(new Vector3(45, 225, 0)));

    UserModelWindow.UserWindowBuilder mainWindowBuilder = new UserModelWindow.UserWindowBuilder();
    //mainWindowBuilder.addShader(new TextureShader(checkerTexture));
    mainWindowBuilder.addShader(new SimpleShader(Color.blue));
    mainWindowBuilder.setCamera(mainCam);
    mainWindowBuilder.setDimension(imageWidth, imageHeight);
    sphere.setSmoothShading(true);
    mainWindowBuilder.setScene(new Scene3D.SceneBuilder().addObject(sphere)/*.addObject(frame.secondSquare)*/.build());
    mainWindow = mainWindowBuilder.build();
  }


  public static void main(String[] args) {

    /*BufferedImage checkerTexture = new BufferedImage(9, 9, BufferedImage.TYPE_INT_ARGB);
    for(int x = 0; x < 9; x++) {
      for(int y = 0; y < 9; y++) {
        if((x + y) % 2 == 0) {
          checkerTexture.setRGB(x, y, Color.black.getRGB());
        } else {
          checkerTexture.setRGB(x, y, Color.white.getRGB());
        }
      }
    }*/

    launch();

    /*while(true) {
      if(mainFrame != null) {
        mainFrame.mainWindow.render();
        mainFrame.UpdateFrame(mainFrame.mainWindow);
      }
    }*/
  }

  public void UpdateFrame(UserModelWindow mainWindow) {
    //frame.squareModel.transform(frame.simpleRotate);
    //frame.secondSquare.transform(frame.reverseRotate);
    sphere.transform(reverseRotate);

    if(framesPassed == 10) {
      infoField.setText("FPS: " + (10000 / (System.currentTimeMillis() - lastFrameMilli)));
      lastFrameMilli = System.currentTimeMillis();
      framesPassed = 0;
    } else {
      framesPassed++;
    }

    //Flip Image vertically
    BufferedImage output = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = output.createGraphics();
    AffineTransform at = AffineTransform.getScaleInstance(1, -1);
    at.translate(0, -imageHeight);
    g2.transform(at);
    g2.drawImage(mainWindow.fetchMostRecent().image, 0, 0, null);

    renderPanel.setImage(SwingFXUtils.toFXImage(output, null));
  }
}