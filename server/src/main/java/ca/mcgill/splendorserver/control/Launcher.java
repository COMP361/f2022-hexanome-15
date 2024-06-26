package ca.mcgill.splendorserver.control;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Launches spring application.
 *
 * @author zacharyhayden
 */
@SpringBootApplication
public class Launcher {

  /**
   * Creates a Launcher.
   */
  public Launcher() {

  }

  /**
   * Runs the Spring Application.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    SpringApplication.run(Launcher.class, args);
  }

}
