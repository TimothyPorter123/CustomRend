package model;

import java.util.ArrayList;
import java.util.List;

import model.objects.Camera;

public class Scene3D {
  RenderObjectModel[] visibleObjects;
  Camera[] cameras;

  private Scene3D(RenderObjectModel[] visibleObjects, Camera[] cameras) {
    this.visibleObjects = visibleObjects;
    this.cameras = cameras;
  }

  public RenderObjectModel[] getVisibleObjects() { return this.visibleObjects; }

  public static class SceneBuilder {
    List<RenderObjectModel> visibleObjects = new ArrayList<RenderObjectModel>();
    List<Camera> cameras = new ArrayList<Camera>();

    public SceneBuilder addObject(RenderObjectModel o) { visibleObjects.add(o); return this; }
    public SceneBuilder addCamera(Camera c) { cameras.add(c); return this; }
    public Scene3D build() {
      RenderObjectModel[] visibleO = new RenderObjectModel[visibleObjects.size()];
      Camera[] cams = new Camera[cameras.size()];
      visibleO = visibleObjects.toArray(visibleO);
      cams = cameras.toArray(cams);
      return new Scene3D(visibleO, cams);
    }
  }
}
