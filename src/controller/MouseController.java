package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.event.MouseInputAdapter;

import model.math.Vector2;
import model.math.Vector3;
import view.UserModelWindow;

public class MouseController extends MouseInputAdapter {

  public UserModelWindow window;
  boolean mouseDown;
  boolean mousePressed;
  boolean mouseReleased;
  Vector2 lastMousePos = new Vector2(0, 0);
  Vector2 mouseMovement = new Vector2(0, 0);

  @Override
  public void mousePressed(MouseEvent e) {
    this.mouseDown = true;
    this.mousePressed = true;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    this.mouseDown = false;
    this.mouseReleased = true;
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    Vector2 position = new Vector2((float)e.getX() / window.windowWidth, (float)e.getY() / window.windowHeight);
    this.mouseMovement = position.minus(lastMousePos);
  }

  @Override
  public void mouseMoved(MouseEvent e) {
    Vector2 position = new Vector2((float)e.getX() / window.windowWidth, (float)e.getY() / window.windowHeight);
    this.mouseMovement = position.minus(lastMousePos);
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    System.out.println("test");
    float zoomFactor = e.getWheelRotation() / 20.0f;
    Vector3 movementV = this.window.viewerCameraFocus.minus(this.window.viewerCamera.getTransform().position);
    this.window.viewerCamera.getTransform().translate(Vector3.lerp(new Vector3(0, 0, 0), movementV, zoomFactor));
  }

  @Override
  public void mouseEntered(MouseEvent e) {
    this.lastMousePos = new Vector2((float)e.getX() / window.windowWidth, (float)e.getY() / window.windowHeight);
  }

  public void handleInput() {
    if(mouseDown) {
      this.handleDrag();
    }

    this.mousePressed = false;
    this.mouseReleased = false;
    this.lastMousePos = lastMousePos.plus(mouseMovement);
    this.mouseMovement = new Vector2(0, 0);
  }

  private void handleDrag() {
    switch(window.state.currentMode) {
      case CameraRotate:
        this.window.viewerCamera.getTransform().orbit(this.window.viewerCameraFocus, new Vector3(0, 1, 0), mouseMovement.x * 180);
        this.window.viewerCamera.getTransform().orbit(this.window.viewerCameraFocus, window.viewerCamera.getTransform().right(), mouseMovement.y * 180);
        break;
      case CameraPan:
        Vector3 rightMovement = window.viewerCamera.getTransform().right().scale(-mouseMovement.x * 10);
        this.window.viewerCamera.getTransform().translate(rightMovement);
        this.window.viewerCameraFocus = this.window.viewerCameraFocus.plus(rightMovement);
        Vector3 upMovement = window.viewerCamera.getTransform().up().scale(mouseMovement.y * 10);
        this.window.viewerCamera.getTransform().translate(upMovement);
        this.window.viewerCameraFocus = this.window.viewerCameraFocus.plus(upMovement);
        break;
    }
    this.window.viewerCamera.updateViewMatrix();
  }
}
