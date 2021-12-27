import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.*;

import model.*;
import view.UserModelWindow;
import model.objects.Camera;
import model.objects.PerspectiveCamera;
import model.objects.SimpleTriangle;
import model.math.TransformMatrix;
import model.math.Vector3;
import view.SimpleShader;
import model.objects.SimpleSquare;

public class FrameController {

  SimpleSquare squareModel;
  SimpleSquare secondSquare;
  SimpleTriangle triangle;
  TransformMatrix simpleRotate;
  TransformMatrix reverseRotate;
  TransformMatrix simpleMove;

  int imageWidth = 800;
  int imageHeight = 600;
  JTextField infoField;
  JFrame mainFrame;
  JPanel renderPanel;

  //remove
  Camera mainCam;

  long lastFrameMilli = 0;

  public FrameController () {
    mainFrame = new JFrame("Test JFrame");
    this.setClassDefaults();

    renderPanel = new JPanel();
    renderPanel.setPreferredSize(new Dimension(imageWidth, imageHeight));
    JPanel infoPanel = new JPanel();
    infoPanel.setPreferredSize(new Dimension(imageWidth, 50));
    infoField = new JTextField("default text");
    infoPanel.add(infoField);
    renderPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        mainCam.transform(new TransformMatrix().rotate(new Vector3(5, 0, 0)));
        mainFrame.repaint();
      }
    });
    mainFrame.setLayout(new BoxLayout(mainFrame.getContentPane(), BoxLayout.PAGE_AXIS));
    mainFrame.add(renderPanel);
    mainFrame.add(infoPanel);
    mainFrame.pack();

    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setVisible(true);
  }

  private void setClassDefaults() {
    squareModel = new SimpleSquare();
    secondSquare = new SimpleSquare();
    triangle = new SimpleTriangle();

    simpleRotate = new TransformMatrix().rotate(new Vector3(1.0f, 1.0f, 0.0f));
    reverseRotate = new TransformMatrix().rotate(new Vector3(-1.0f, -1.0f, 0.0f));
    simpleMove = new TransformMatrix().move(new Vector3(0.0f, 0.1f, 0.0f));
  }


  public static void main(String[] args) {
    FrameController frame = new FrameController();
    UserModelWindow.UserWindowBuilder mainWindowBuilder = new UserModelWindow.UserWindowBuilder();

    //CLIPPING PLANE =/= 0
    frame.mainCam = new PerspectiveCamera(90, 0.1f, 1000f, (float)frame.imageHeight / frame.imageWidth);
    frame.mainCam.transform(new TransformMatrix().move(new Vector3(0, 3.0f, 3.0f)));
    frame.mainCam.transform(new TransformMatrix().rotate(new Vector3(45, 180, 0)));

    mainWindowBuilder.addShader(new SimpleShader(Color.blue));
    mainWindowBuilder.setCamera(frame.mainCam);
    mainWindowBuilder.setDimension(frame.imageWidth, frame.imageHeight);
    mainWindowBuilder.setScene(new Scene3D.SceneBuilder().addObject(frame.squareModel).addObject(frame.secondSquare).build());
    UserModelWindow mainWindow = mainWindowBuilder.build();

    while(true) {
      frame.squareModel.transform(frame.simpleRotate);
      frame.secondSquare.transform(frame.reverseRotate);
      mainWindow.render();
      frame.UpdateFrame(mainWindow);
    }
  }

  public void UpdateFrame(UserModelWindow mainWindow) {
    infoField.setText("FPS: " + (1000 / (System.currentTimeMillis() - lastFrameMilli)));
    lastFrameMilli = System.currentTimeMillis();

    //Flip Image vertically
    BufferedImage output = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = output.createGraphics();
    AffineTransform at = AffineTransform.getScaleInstance(1, -1);
    at.translate(0, -imageHeight);
    g2.transform(at);
    g2.drawImage(mainWindow.fetchMostRecent().image, 0, 0, null);

    renderPanel.removeAll();
    renderPanel.add(new JLabel(new ImageIcon(output)));
    mainFrame.revalidate();
    mainFrame.repaint();
  }
}