package application;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.*;

import controller.KeyboardController;
import controller.MouseController;
import model.*;
import model.math.Vector4;
import model.objects.SimpleUVSphere;
import view.PreImage;
import view.UserModelWindow;
import model.objects.Camera;
import model.objects.PerspectiveCamera;
import model.objects.SimpleTriangle;
import model.math.Vector3;
import view.shaders.SimpleShader;
import model.objects.SimpleSquare;

public class FrameController {

  SimpleSquare squareModel;
  SimpleSquare secondSquare;
  SimpleTriangle triangle;
  SimpleUVSphere sphere;

  int imageWidth = 800;
  int imageHeight = 600;
  JTextField infoField;
  JFrame mainFrame;
  JPanel renderPanel;

  //remove ?
  Camera mainCam;
  KeyboardController keyController;
  MouseController mouseController;

  long lastFrameMilli = System.currentTimeMillis();
  int framesPassed;

  public FrameController () {
    mainFrame = new JFrame("Test JFrame");
    this.setClassDefaults();

    renderPanel = new JPanel();
    renderPanel.setPreferredSize(new Dimension(imageWidth, imageHeight));
    renderPanel.setFocusable(true);
    JPanel infoPanel = new JPanel();
    infoPanel.setPreferredSize(new Dimension(imageWidth, 50));
    infoField = new JTextField("default text");
    infoPanel.add(infoField);
    keyController = new KeyboardController();
    mouseController = new MouseController();
    renderPanel.addKeyListener(keyController);
    renderPanel.addMouseListener(mouseController);
    renderPanel.addMouseMotionListener(mouseController);
    mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.PAGE_AXIS));
    mainFrame.add(renderPanel);
    mainFrame.add(infoPanel);
    renderPanel.requestFocus();
    mainFrame.pack();

    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setVisible(true);
  }

  private void setClassDefaults() {
    squareModel = new SimpleSquare();
    secondSquare = new SimpleSquare();
    triangle = new SimpleTriangle();
    sphere = new SimpleUVSphere();
  }


  public static void main(String[] args) {
    FrameController frame = new FrameController();
    UserModelWindow.UserWindowBuilder mainWindowBuilder = new UserModelWindow.UserWindowBuilder();

    //CLIPPING PLANE =/= 0
    frame.mainCam = new PerspectiveCamera(90, 0.1f, 1000f, (float)frame.imageHeight / frame.imageWidth);
    frame.mainCam.getTransform().translate(new Vector3(-3.0f, 3.0f, -3.0f));

    //frame.mainCam.getTransform().rotate(new Vector4( 0, 0.923879f, 0, -0.38268f).normalized());

    frame.mainCam.getTransform().rotate(new Vector3(0, 1, 0), 45);
    frame.mainCam.getTransform().rotate(new Vector3(1, 0, 0), 45);

    frame.mainCam.updateViewMatrix();

    BufferedImage checkerTexture = new BufferedImage(9, 9, BufferedImage.TYPE_INT_ARGB);
    for(int x = 0; x < 9; x++) {
      for(int y = 0; y < 9; y++) {
        if((x + y) % 2 == 0) {
          checkerTexture.setRGB(x, y, Color.black.getRGB());
        } else {
          checkerTexture.setRGB(x, y, Color.white.getRGB());
        }
      }
    }

    //mainWindowBuilder.addShader(new TextureShader(checkerTexture));
    mainWindowBuilder.addShader(new SimpleShader(Color.blue));
    mainWindowBuilder.setCamera(frame.mainCam);
    mainWindowBuilder.setDimension(frame.imageWidth, frame.imageHeight);
    frame.sphere.setSmoothShading(true);
    mainWindowBuilder.setScene(new Scene3D.SceneBuilder().addObject(frame.sphere)/*.addObject(new SimpleUVSphere())*/.build());
    //frame.sphere.getTransform().translate(new Vector3(2, 0, 0));
    //frame.sphere.getTransform().scale(new Vector3(0.5f, 0.5f, 0.5f));
    UserModelWindow mainWindow = mainWindowBuilder.build();
    mainWindow.setKeyController(frame.keyController);
    mainWindow.setMouseController(frame.mouseController);

    while(true) {
      //frame.squareModel.transform(frame.simpleRotate);
      //frame.secondSquare.transform(frame.reverseRotate);
      //frame.sphere.getTransform().globalRotate(new Vector3(1, 0, 0), 10);

      mainWindow.render();
      frame.UpdateFrame(mainWindow);
      frame.keyController.handleInput();
      frame.mouseController.handleInput();
    }
  }

  public void UpdateFrame(UserModelWindow mainWindow) {
    if(framesPassed == 10) {
      infoField.setText("FPS: " + (10000 / (System.currentTimeMillis() - lastFrameMilli)));
      lastFrameMilli = System.currentTimeMillis();
      framesPassed = 0;
    } else {
      framesPassed++;
    }

    //Flip Image vertically
    BufferedImage base = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    PreImage returned = mainWindow.fetchMostRecent().image;
    for(int x = 0; x < imageWidth; x++) {
      for(int y = 0; y < imageHeight; y++) {
        base.setRGB(x, imageHeight - 1 - y, returned.getPixel(x, y).getRGB());
      }
    }

    renderPanel.removeAll();
    renderPanel.add(new JLabel(new ImageIcon(base)));
    mainFrame.revalidate();
    mainFrame.repaint();
  }
}