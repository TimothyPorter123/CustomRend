module CustomRend {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.base;
  requires javafx.swing;

  requires java.datatransfer;

  requires java.desktop;

  opens application to javafx.fxml;
  exports application;
}