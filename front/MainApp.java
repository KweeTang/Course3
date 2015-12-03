import java.util.*;
import java.io.File;
import java.awt.Desktop;

public class MainApp {
  public static void main(String[] args) {
    try {
    File html = new File("index.html");
    Desktop.getDesktop().browse(html.toURI());
  } catch (Exception e) {}
  }
}
