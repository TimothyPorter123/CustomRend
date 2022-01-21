package controller;

import model.objects.Vertex;

public class UserWindowState {

  public int selectionMode;
  public enum ActionMode {
    None,
    Translate,
    Rotate,
    Scale,
    CameraPan,
    CameraRotate,
    CameraZoom
  }
  public ActionMode currentMode = ActionMode.None;
  public Vertex[] selection;

}
