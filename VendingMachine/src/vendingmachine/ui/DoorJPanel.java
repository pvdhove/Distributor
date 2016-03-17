package vendingmachine.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;

import javax.swing.JPanel;
import javax.swing.Timer;

import vendingmachine.PictureLoader;

/**
 * This class defines a JPanel that paints a black rectangle (the door).
 * The size of the rectangle is based on the CUP_ICON of PictureLoader.
 * It supplies an animation of the opening of the door.
 */
public class DoorJPanel extends JPanel {

  private static final long serialVersionUID = 1L;

  /**
   * The width of the black rectangle.
   */
  public final int WIDTH;
  
  /**
   * The maximal height of the black rectangle
   * (from which the step will be subtracted).
   */
  public final int HEIGHT;

  /**
   * A value that is subtracted from the height to draw the rectangle.
   */
  private int step;

  /**
   * Timer that creates the animation of the door.
   */
  private final Timer doorTimer;

  public DoorJPanel() {
    super();
    this.step = 0;
    this.setDoubleBuffered(true);
    
    PictureLoader pictures = PictureLoader.getInstance();
    WIDTH = pictures.CUP_ICON.getIconWidth();
    HEIGHT = pictures.CUP_ICON.getIconHeight();
    
    doorTimer = new Timer(5, e -> {
      step += 1;
      repaint();
      if (step >= this.HEIGHT) {
        ((Timer)e.getSource()).stop(); // stops the doorTimer
      }
    });
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    Toolkit.getDefaultToolkit().sync();
    g.setColor(Color.BLACK);
    g.fillRect((getWidth() - WIDTH) / 2, getHeight() - HEIGHT,
        WIDTH, HEIGHT - step);
  }
  
  /**
   * Starts the timer triggering the animation of the door.
   */
  public void doorAnimation() {
    doorTimer.restart();
  }
  
  /**
   * @return true if the door is currently opening, false otherwise
   */
  public boolean animationIsRunning() {
    return doorTimer.isRunning();
  }

  /**
   * Closes the door (ie sets the step to 0 and repaints the panel).
   */
  public void closeDoor() {
    step = 0;
    repaint();
  }

}
