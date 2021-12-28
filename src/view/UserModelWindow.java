package view;

import java.awt.Color;

import model.RenderObjectModel;
import model.Scene3D;
import model.objects.Camera;
import model.RenderOutput;
import model.math.Vector3;
import view.shaders.LineShader;
import view.shaders.Shader;

public class UserModelWindow {

  private Shader defaultShader;
  private Scene3D scene;
  private final Camera viewerCamera;
  private int windowWidth;
  private int windowHeight;
  private RenderOutput MRR;

  public UserModelWindow(Shader defaultShader, Scene3D scene, Camera viewerCamera, int windowWidth, int windowHeight) {
    this.defaultShader = defaultShader;
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
    //SurfaceRenderer applicator = new SurfaceRenderer(defaultShader, windowWidth, windowHeight);
    RenderOutput objects = RenderOutput.blank(windowWidth, windowHeight);
    for(RenderObjectModel o : scene.getVisibleObjects()) {
      objects = viewerCamera.renderObjectSurfaceOver(o, defaultShader, objects);
    }

    RenderOutput withLines = drawObjectGrid(objects);

    MRR = withLines;
  }

  private RenderOutput drawObjectGrid(RenderOutput in) {
    LineShader lineShader = new LineShader(Color.blue);
    //LineRenderer lineRenderer = new LineRenderer(lineShader, windowWidth, windowHeight);
    RenderOutput withLine = in;
    for(int x = -2; x < 3; x++) {
      lineShader.setColor(x == 0 ? Color.blue : Color.white);
      withLine = viewerCamera.renderLineOver(new Vector3(x, 0, 2f), new Vector3(x, 0, -2f),
              lineShader, withLine);
    }
    for(int z = -2; z < 3; z++) {
      lineShader.setColor(z == 0 ? Color.red : Color.white);
      withLine = viewerCamera.renderLineOver(new Vector3(2f, 0, z), new Vector3(-2f, 0, z),
              lineShader, withLine);
    }
    return withLine;
  }
}
