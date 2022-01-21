package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

import model.math.Vector3;
import view.UserModelWindow;

public class KeyboardController implements KeyListener {

  public UserModelWindow window;
  private HashMap<Integer, KeyEvent> keyDown = new HashMap<Integer, KeyEvent>();
  private HashMap<Integer, KeyEvent> keyPressed = new HashMap<Integer, KeyEvent>();

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    keyPressed.put(e.getKeyCode(), e);
    keyDown.put(e.getKeyCode(), e);
  }

  @Override
  public void keyReleased(KeyEvent e) {
    keyDown.remove(e.getKeyCode());
  }

  public void handleInput() {
    if(keyDown.containsKey(KeyEvent.VK_CONTROL)) {
      this.window.state.currentMode = UserWindowState.ActionMode.CameraPan;
    } else if (keyDown.containsKey(KeyEvent.VK_SHIFT)) {
      this.window.state.currentMode = UserWindowState.ActionMode.CameraRotate;
    }

    keyPressed.clear();
  }
}