import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.*;

import model.Camera;
import model.PerspectiveCamera;
import model.RenderOutput;
import model.SimpleTriangle;
import model.TransformMatrix;
import model.Vector3;
import view.LineRenderer;
import view.LineShader;
import view.Shader;
import view.ShaderApplicator;
import view.SimpleShader;
import model.SimpleSquare;

public class FrameController {

  Shader standardShader;
  SimpleSquare squareModel;
  SimpleSquare secondSquare;
  SimpleTriangle triangle;
  TransformMatrix simpleRotate;
  TransformMatrix reverseRotate;
  TransformMatrix simpleMove;

  Camera mainCam;

  int imageWidth = 400;
  int imageHeight = 400;
  JTextField infoField;
  JFrame mainFrame;
  JPanel renderPanel;

  RenderOutput MRRFrame;
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
    standardShader = new SimpleShader(Color.blue);
    squareModel = new SimpleSquare();
    secondSquare = new SimpleSquare();
    triangle = new SimpleTriangle();

    simpleRotate = new TransformMatrix().rotate(new Vector3(1.0f, 1.0f, 0.0f));
    reverseRotate = new TransformMatrix().rotate(new Vector3(-1.0f, -1.0f, 0.0f));
    simpleMove = new TransformMatrix().move(new Vector3(0.0f, 0.1f, 0.0f));

    //near clipping plane CANNOT be 0
    mainCam = new PerspectiveCamera(90, 0.1f, 1000f, (float)imageHeight / imageWidth);
    mainCam.transform(new TransformMatrix().move(new Vector3(0, 3.0f, 3.0f)));
    mainCam.transform(new TransformMatrix().rotate(new Vector3(45, 180, 0)));
  }


  public static void main(String[] args) {
    FrameController frame = new FrameController();
    while(true) {
      frame.UpdateFrame();
    }
  }

  public void UpdateFrame() {
    ShaderApplicator applicator = new ShaderApplicator(standardShader, imageWidth, imageHeight);
    RenderOutput firstObject = applicator.renderObject(squareModel, mainCam);
    RenderOutput secondObject = applicator.renderObjectOver(secondSquare, mainCam, firstObject.image, firstObject.depthBuffer);
    RenderOutput withLines = drawObjectGrid(secondObject);

    squareModel.transform(simpleRotate);
    secondSquare.transform(reverseRotate);
    MRRFrame = withLines;
    infoField.setText("FPS: " + (1000 / (System.currentTimeMillis() - lastFrameMilli)));
    lastFrameMilli = System.currentTimeMillis();

    //Flip Image vertically
    BufferedImage output = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = output.createGraphics();
    AffineTransform at = AffineTransform.getScaleInstance(1, -1);
    at.translate(0, -imageHeight);
    g2.transform(at);
    g2.drawImage(MRRFrame.image, 0, 0, null);

    renderPanel.removeAll();
    renderPanel.add(new JLabel(new ImageIcon(output)));
    mainFrame.revalidate();
    mainFrame.repaint();
  }

  private RenderOutput drawObjectGrid(RenderOutput in) {
    LineShader lineShader = new LineShader(Color.blue);
    LineRenderer lineRenderer = new LineRenderer(lineShader, imageWidth, imageHeight);
    RenderOutput withLine = in;
    for(int x = -2; x < 3; x++) {
      lineShader.setColor(x == 0 ? Color.blue : Color.white);
      withLine = lineRenderer.renderLineOver(new Vector3(x, 0, 2f), new Vector3(x, 0, -2f),
              0, mainCam, withLine.image, withLine.depthBuffer);
    }
    for(int z = -2; z < 3; z++) {
      lineShader.setColor(z == 0 ? Color.red : Color.white);
      withLine = lineRenderer.renderLineOver(new Vector3(2f, 0, z), new Vector3(-2f, 0, z),
              0, mainCam, withLine.image, withLine.depthBuffer);
    }
    return withLine;
  }
}