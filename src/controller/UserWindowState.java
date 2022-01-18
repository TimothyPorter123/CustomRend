package controller;

import model.objects.Vertex;

public class UserWindowState {

  public int selectionMode;
  public enum ActionMode {
    Translate,
    Rotate,
    Scale,
    CameraPan,
    CameraRotate,
    CameraZoom
  }
  public ActionMode currentMode;
  public Vertex[] selection;

}
