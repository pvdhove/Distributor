package vendingmachine.components;

import javax.swing.ImageIcon;

import vendingmachine.PictureLoader;

/**
 * The enumeration Coin gives all the coins that could be accepted by a vending machine.
 * This gives all the Euro coins, but it can be easily changed.
 */
public enum Coin {
  COIN200 (200, "2 �", PictureLoader.EURO2_ICON),
  COIN100 (100, "1 �", PictureLoader.EURO1_ICON),
  COIN50  (50, "0.50 �", PictureLoader.CENT50_ICON),
  COIN20  (20, "0.20 �", PictureLoader.CENT20_ICON), 
  COIN10  (10, "0.10 �", PictureLoader.CENT10_ICON),
  COIN5   (5, "0.05 �", PictureLoader.CENT5_ICON),
  COIN2   (2, "0.02 �", PictureLoader.CENT2_ICON),
  COIN1   (1, "0.01 �", PictureLoader.CENT1_ICON);

  /**
   * The value of the coin, expressed in cents.
   */
  public final int VALUE;
  
  /**
   * The String representation of the coin ("0.50 �" for instance).
   */
  public final String TEXT;
  
  /**
   * An icon that looks like the coin. It can be null, but the
   * vending machine will be uglier.
   */
  public final ImageIcon ICON;

  private Coin(int value, String text, ImageIcon icon) {
    this.VALUE = value;
    this.TEXT = text;
    this.ICON = icon;
  }
  
}