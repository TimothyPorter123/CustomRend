package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import model.math.TransformMatrix;
import model.math.Vector3;
import model.math.Vector4;
import view.UserModelWindow;

public class KeyboardController implements KeyListener {

  public UserModelWindow window;

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_A) {
      this.window.viewerCamera.getTransform().rotate(new Vector4(0.99619f, 0, 0.087156f, 0).normalized());
    } else if (e.getKeyCode() == KeyEvent.VK_D) {
      this.window.viewerCamera.getTransform().rotate(new Vector4(0.99619f, 0, -0.087156f, 0).normalized());
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {

  }
}
