package view;

import java.awt.Color;

import model.RenderObjectModel;
import model.Scene3D;
import model.math.Vector4;
import model.objects.Camera;
import model.RenderOutput;
import model.math.Vector3;
import view.shaders.LineShader;
import view.shaders.Shader;

public class UserModelWindow {

  private Shader defaultShader;
  private Shader defaultLineShader;
  private Scene3D scene;
  private final Camera viewerCamera;
  private int windowWidth;
  private int windowHeight;
  private RenderOutput MRR;

  public UserModelWindow(Shader defaultShader, Scene3D scene, Camera viewerCamera, int windowWidth, int windowHeight) {
    this.defaultShader = defaultShader;
    this.defaultLineShader = new LineShader(new Vector4(0, 0, 0, 1));
    this.scene = scene;
    this.viewerCamera = viewerCamera;
    this.windowWidth = windowWidth;
    this.windowHeight = windowHeight;
  }

  public static class UserWindowBuilder {
    private Shader defaultShader;
    private Scene3D scene;
    private Camera viewerCamera;
    private int windowWidth;
    private int windowHeight;

    public void addShader(Shader shader) { this.defaultShader = shader; }
    public void setDimension(int width, int height) { this.windowWidth = width; this.windowHeight = height; }
    public void setCamera(Camera camera) { this.viewerCamera = camera; }
    public void setScene(Scene3D scene) { this.scene = scene; }
    public UserModelWindow build() {
      if(defaultShader != null && viewerCamera != null && scene != null) {
        return new UserModelWindow(defaultShader, scene, viewerCamera, windowWidth, windowHeight);
      } else {
        return null;
      }
    }
  }

  public RenderOutput fetchMostRecent() {
    return MRR;
  }

  public void render() {

    RenderOutput objects = RenderOutput.blank(windowWidth, windowHeight);
    for(RenderObjectModel o : scene.getVisibleObjects()) {
      objects = viewerCamera.renderObjectSurfaceOver(o, defaultShader, objects);
    }


    RenderOutput lines = drawObjectGrid(objects);

    for(RenderObjectModel o : scene.getVisibleObjects()) {
      lines = viewerCamera.renderObjectWireFrameOver(o, defaultLineShader, lines);
    }

    MRR = lines;
  }

  private RenderOutput drawObjectGrid(RenderOutput in) {
    Vector4 blue = new Vector4(0 ,0, 1, 1);
    Vector4 gray = new Vector4(0.5f, 0.5f, 0.5f, 1);
    Vector4 red = new Vector4(1, 0, 0, 1);

    LineShader lineShader = new LineShader(blue);
    //LineRenderer lineRenderer = new LineRenderer(lineShader, windowWidth, windowHeight);
    int lineCount = 30;
    RenderOutput withLine = in;
    for(int x = -lineCount; x < lineCount + 1; x++) {
      lineShader.setColor(x == 0 ? blue : gray);
      withLine = viewerCamera.renderLineOver(new Vector3(x, 0, lineCount), new Vector3(x, 0, -lineCount),
              lineShader, withLine);
    }
    for(int z = -lineCount; z < lineCount + 1; z++) {
      lineShader.setColor(z == 0 ? red : gray);
      withLine = viewerCamera.renderLineOver(new Vector3(lineCount, 0, z), new Vector3(-lineCount, 0, z),
              lineShader, withLine);
    }
    return withLine;
  }
}
