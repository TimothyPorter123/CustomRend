import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Camera;
import model.PerspectiveCamera;
import model.RenderOutput;
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
    //super("Test JFrame");
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
    /*Insets insets = mainFrame.getInsets();
    mainFrame.add(renderPanel);
    renderPanel.setBounds(insets.left, insets.top, imageWidth, imageHeight);
    mainFrame.add(infoPanel);
    infoPanel.setBounds(insets.left, insets.top + imageHeight, imageWidth, 50);
    //this.setSize(imageWidth, imageHeight + 50);
    mainFrame.setSize(imageWidth + insets.left + insets.right, imageHeight + 50 + insets.top + insets.bottom);*/
    mainFrame.pack();

    mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    mainFrame.setVisible(true);
    UpdateFrame();
  }

  private void setClassDefaults() {
    standardShader = new SimpleShader(Color.blue);
    squareModel = new SimpleSquare();
    secondSquare = new SimpleSquare();
    simpleRotate = new TransformMatrix().rotate(new Vector3(1.0f, 1.0f, 0.0f));
    reverseRotate = new TransformMatrix().rotate(new Vector3(-1.0f, -1.0f, 0.0f));
    simpleMove = new TransformMatrix().move(new Vector3(0.0f, 0.1f, 0.0f));

    //near clipping plane CANNOT be 0
    mainCam = new PerspectiveCamera(90, 0.1f, 1000f, (float)imageHeight / imageWidth);
    mainCam.transform(new TransformMatrix().move(new Vector3(0, 0, 2)));
    mainCam.transform(new TransformMatrix().rotate(new Vector3(0, 180, 0)));
    //mainCam.transform(new TransformMatrix().rotate(new Vector3(45, 0, 0)));
    mainCam.transform(new TransformMatrix().move(new Vector3(0, 1.0f, 0)));
  }


  public static void main(String[] args) {
    FrameController frame = new FrameController();
  }

  public void UpdateFrame() {
    ShaderApplicator applicator = new ShaderApplicator(standardShader, imageWidth, imageHeight);
    Shader lineShader = new LineShader(Color.blue);
    LineRenderer lineRenderer = new LineRenderer(lineShader, imageWidth, imageHeight);
    RenderOutput firstObject = applicator.renderObject(squareModel, mainCam);
    RenderOutput secondObject = applicator.renderObjectOver(secondSquare, mainCam, firstObject.image, firstObject.depthBuffer);
    Vector3 start = new Vector3(0, 0, -2f);
    Vector3 end = new Vector3(0, 0, 2f);
    //RenderOutput withLine = lineRenderer.renderLineOver(start, end, 0.005f, mainCam, firstObject.image, firstObject.depthBuffer);
    //withLine = lineRenderer.renderLineOver(new Vector3(1.5f, 0, 0), new Vector3(-1.5f, 0, 0), 0.005f, mainCam, firstObject.image, firstObject.depthBuffer);

    squareModel.transform(simpleRotate);
    secondSquare.transform(reverseRotate);
    MRRFrame = secondObject;
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
    UpdateFrame();
  }

  /*@Override
  public void paint(Graphics g) {
    //super.paint(g);
    Graphics2D g2 = (Graphics2D) g;


    if(MRRFrame != null) {
      //Flips image (y up)
      AffineTransform at = AffineTransform.getScaleInstance(1, -1);
      at.translate(0, -imageHeight);
      g2.transform(at);
      g2.drawImage(MRRFrame.depthBuffer, 0, 0, null);
    }
    repaint();
  }*/
}