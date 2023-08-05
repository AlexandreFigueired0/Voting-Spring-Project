module pt.ul.fc.css.f2.nativeapp.fx_app {
  requires javafx.controls;
  requires javafx.fxml;
  requires javafx.web;
  requires com.fasterxml.jackson.databind;
  requires com.fasterxml.jackson.core;
  requires java.net.http;

  opens pt.ul.fc.css.f2.nativeapp.fx_app to
      javafx.fxml,
      javafx.web;
  opens pt.ul.fc.css.f2.nativeapp.fx_app.controllers to
      javafx.fxml;

  exports pt.ul.fc.css.f2.nativeapp.fx_app.dtos to
      com.fasterxml.jackson.databind;

  opens pt.ul.fc.css.f2.nativeapp.fx_app.models to
      javafx.base;

  exports pt.ul.fc.css.f2.nativeapp.fx_app;
}
