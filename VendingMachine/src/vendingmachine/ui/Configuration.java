package vendingmachine.ui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;

import vendingmachine.Coin;
import vendingmachine.Drink;
import vendingmachine.components.Context;
import vendingmachine.components.Stock;

/**
 * This class display a configuration menu to let the user choose the
 * drinks, the stocks and the coins inside a hot drinks vending machine.
 */
public class Configuration extends JFrame {

  private static final long serialVersionUID = 1L; // generated by Eclipse

  private static final MyDocumentFilter myDocumentFilter = new MyDocumentFilter();

  /*
   * Defines some default values for the fields.
   */
  private static final int MAX_NBR_DRINKS = 10;
  private static final Integer[] NBR_DRINKS_LIST = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
  private static final String[] COLUMNS_TITLES = { "  Enter here the drinks names:  ",
    "  Contains sugar?  ", "  Price (in cents):  ", "  Initial stock:  " };
  private static final String[] COINS_TITLES = { "Coins and their values:  ", 
    "  Initial stock:  ", "  Accepted by the machine?  " };
  private static final String[] DEFAULT_DRINKS = { "Black Coffee", "Cappuccino", "Hot Chocolate",
    "Hot Milk", "Green Tea", "Earl Grey", "Tomato Soup", "Mushroom Soup", "Hot Water", "Oolong Tea" };

  /**
   * The panel for the drink info. This is the only one that should be repainted,
   * the other ones are created locally.
   */
  private final JPanel drinkPanel;

  /**
   * Allows to choose the number of drinks among some values
   */
  private final JComboBox<Integer> drinkNbrComboBox;

  /*
   * Allows the user to enter the drinks info
   */
  private final JTextField[] drinksNames;
  private final JCheckBox[] drinksSugar;
  private final JTextField[] drinksPrices;
  private final JTextField[] drinksStocks;
  
  /*
   * Allows the user to enter the coins info
   */
  private final Map<Coin, JTextField> coinsStockValues;
  private final Map<Coin, JCheckBox> acceptedCoinsBoxes;

  /*
   * Allows the user to enter the stock info
   */
  private final JTextField sugarCubesNbrValue;
  private final JTextField cupsNbrValue;
  private final JTextField spoonsNbrValue;

  private final JButton createButton;
  private final JLabel problemLabel;

  /**
   * Creates a Configuration object that initializes all the fields.
   * The frame is then created and made visible by calling {@code init()} on it.
   */
  public Configuration() {
    super();
    this.setTitle("Vending Machine Configuration");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    drinkPanel = new JPanel(new GridBagLayout());
    drinkNbrComboBox = new JComboBox<Integer>(NBR_DRINKS_LIST);
    drinkNbrComboBox.setSelectedIndex(7); //to have 8 drinks as a default value
    drinkNbrComboBox.addActionListener(e -> updateDrinkPanel());
    
    // Initializes the fields for the drinks info
    drinksNames = new JTextField[MAX_NBR_DRINKS];
    drinksSugar = new JCheckBox[MAX_NBR_DRINKS];
    drinksPrices = new JTextField[MAX_NBR_DRINKS];
    drinksStocks = new JTextField[MAX_NBR_DRINKS];
    for (int i = 0; i < MAX_NBR_DRINKS; i++) {
      drinksNames[i] = new JTextField(18);
      if (i < DEFAULT_DRINKS.length) {
        drinksNames[i].setText(DEFAULT_DRINKS[i]);
      }
      drinksSugar[i] = new JCheckBox();
      if (i < 6) { // to make only some drinks sugared by default
        drinksSugar[i].setSelected(true);
      }
      drinksPrices[i] = new JTextField("100", 5);
      ((AbstractDocument)drinksPrices[i].getDocument()).setDocumentFilter(myDocumentFilter);
      drinksStocks[i] = new JTextField("5", 5);
      ((AbstractDocument)drinksStocks[i].getDocument()).setDocumentFilter(myDocumentFilter);
    }
    
    // Initializes the fields for the coins info
    coinsStockValues = new Hashtable<Coin, JTextField>();
    acceptedCoinsBoxes = new Hashtable<Coin, JCheckBox>();
    for (Coin coin: Coin.COINS) {
      coinsStockValues.put(coin, new JTextField("5", 4));
      ((AbstractDocument)coinsStockValues.get(coin).getDocument()).setDocumentFilter(myDocumentFilter);
      acceptedCoinsBoxes.put(coin, new JCheckBox());
    }
    
    // Initializes the fields for the stock info
    sugarCubesNbrValue = new JTextField("20", 4);
    ((AbstractDocument)sugarCubesNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    cupsNbrValue = new JTextField("10", 4);
    ((AbstractDocument)cupsNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    spoonsNbrValue = new JTextField("8", 4);
    ((AbstractDocument)spoonsNbrValue.getDocument()).setDocumentFilter(myDocumentFilter);
    
    createButton = new JButton("Click here to begin the simulation!");
    createButton.addActionListener(e -> check());
    problemLabel = new JLabel();
    problemLabel.setForeground(Color.RED);
  }

  /**
   * Places the JComponent's on the frame and makes the frame visible.
   */
  public void init() {
    final JPanel myPanel = new JPanel();
    myPanel.setLayout(new GridBagLayout());
    final GridBagConstraints c1 = new GridBagConstraints();
    final Border grayLine = BorderFactory.createLineBorder(Color.GRAY);
    c1.insets = new Insets(5, 10, 5, 10); // puts some space around the components

    final JLabel drinkNbrLabel = new JLabel("Number of drinks:");   
    c1.gridy = 0;
    myPanel.add(drinkNbrLabel, c1);
    c1.gridy = 1;
    myPanel.add(drinkNbrComboBox, c1);
    
    // Creates the drinkPanel
    drinkPanel.setBorder(BorderFactory.createTitledBorder(grayLine, "Drink Information"));
    this.updateDrinkPanel();
    c1.gridy = 2;
    myPanel.add(drinkPanel, c1);
    
    // Creates the coinPanel
    final JPanel coinPanel = new JPanel(new GridBagLayout());
    coinPanel.setBorder(BorderFactory.createTitledBorder(grayLine, "Coin Information"));
    final GridBagConstraints c2 = new GridBagConstraints();
    c2.gridy += 1; c2.gridx = 0;
    for (String s: COINS_TITLES) {
      coinPanel.add(new JLabel(s), c2);
      c2.gridx += 1;
    }

    for (Coin coin: Coin.COINS) {
      c2.gridy += 1; c2.gridx = 0;
      coinPanel.add(new JLabel(coin.TEXT), c2);
      c2.gridx = 1;
      coinPanel.add(coinsStockValues.get(coin), c2);
      c2.gridx = 2;
      coinPanel.add(acceptedCoinsBoxes.get(coin), c2);
      acceptedCoinsBoxes.get(coin).setSelected(true);
    }

    c1.gridy = 3;
    myPanel.add(coinPanel, c1);
    
    // Creates the stock Panel
    final JPanel stockPanel = new JPanel(new GridBagLayout());
    stockPanel.setBorder(BorderFactory.createTitledBorder(grayLine, "Stock Information"));
    final GridBagConstraints c3 = new GridBagConstraints();
    final JLabel sugarCubesNbrLabel = new JLabel("Number of sugar cubes available: ");
    final JLabel cupsNbrLabel = new JLabel("Number of cups available: ");
    final JLabel spoonsNbrLabel = new JLabel("Number of spoons availables: ");
    
    c3.gridy = 0; c3.gridx = 0;
    stockPanel.add(sugarCubesNbrLabel, c3);
    c3.gridx = 1;
    stockPanel.add(sugarCubesNbrValue, c3);

    c3.gridy += 1;  c3.gridx = 0;
    stockPanel.add(cupsNbrLabel, c3);
    c3.gridx = 1;
    stockPanel.add(cupsNbrValue, c3);

    c3.gridy += 1;  c3.gridx = 0;
    stockPanel.add(spoonsNbrLabel, c3);
    c3.gridx = 1;
    stockPanel.add(spoonsNbrValue, c3);

    c1.gridy = 4;
    myPanel.add(stockPanel, c1);
    
    // Places the button, the problem label and makes the final operations
    c1.gridy = 5; c1.gridx = 0;
    c1.gridwidth = GridBagConstraints.REMAINDER;
    myPanel.add(createButton, c1);
    c1.gridy = 6;
    myPanel.add(problemLabel, c1);

    myPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    this.add(myPanel);
    final JScrollPane scrPane = new JScrollPane(myPanel); // makes the frame scrollable
    this.add(scrPane);

    this.getRootPane().setDefaultButton(createButton); // pressing Enter pushes the button
    this.pack();
    this.setLocationRelativeTo(null);
    this.setVisible(true);
  }

  /**
   * Called when {@code createButton} is pressed. Checks the information the user entered.
   * If everything is OK, creates the vending machine. Otherwise, displays an error message
   * that allows the user to change what was wrong.
   */
  private void check() {
    // Fetches the values for the drinks
    final List<Drink> drinkList = new ArrayList<Drink>();
    final Map<Drink, Integer> drinkQty = new Hashtable<Drink, Integer>();
    try {
      for (int i = 0; i < (Integer)drinkNbrComboBox.getSelectedItem(); i++) {
        if (drinksNames[i].getText().equals("") || drinksNames[i].getText().length() > 18) {
          throw new IllegalArgumentException();
        }
        final Drink d = new Drink(drinksNames[i].getText(), drinksSugar[i].isSelected(),
            Integer.parseInt(drinksPrices[i].getText()));
        drinkList.add(d);
        drinkQty.put(d, Integer.parseInt(drinksStocks[i].getText()));
      }
    } catch (IllegalArgumentException e) {
      problemLabel.setText(getProblemText("drink"));
      this.pack();
      return;
    }

    // Fetches the values for the change machine
    final Map<Coin, Integer> coinsStock = new Hashtable<Coin, Integer>();
    final Map<Coin, Boolean> coinsAccepted = new Hashtable<Coin, Boolean>();
    try {
      for (Coin coin: Coin.COINS) {
        coinsStock.put(coin, Integer.parseInt(coinsStockValues.get(coin).getText()));
        coinsAccepted.put(coin, acceptedCoinsBoxes.get(coin).isSelected());
      }
    } catch (NumberFormatException e) {
      problemLabel.setText(getProblemText("change machine"));
      this.pack();
      return;
    }

    // Fetches the values for the stock
    int sugarCubeNbr;
    int cupsNbr;
    int spoonsNbr;
    try {
      sugarCubeNbr = Integer.parseInt(sugarCubesNbrValue.getText());
      cupsNbr = Integer.parseInt(cupsNbrValue.getText());
      spoonsNbr = Integer.parseInt(spoonsNbrValue.getText());
    } catch (NumberFormatException e) {
      problemLabel.setText(getProblemText("stock"));
      this.pack();
      return;
    }
    final Stock stock = new Stock(sugarCubeNbr, cupsNbr, spoonsNbr, drinkQty);

    final Context context = new Context(
        (Integer)drinkNbrComboBox.getSelectedItem(), drinkList, coinsStock, coinsAccepted, stock);
    final VendingMachineGUI gui = new VendingMachineGUI(context);
    this.dispose(); // closes the configuration frame
    
    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {
        gui.init(); // if everything went well, creates the GUI on the EDT
      }
    });
  }
  
  /**
   * Called when the number of drinks is changed to repaint {@code drinkPanel} accordingly.
   * It removes all the components and then places them again with the new number of drinks.
   */
  private void updateDrinkPanel() {
    final Integer NBR_DRINKS = (Integer)drinkNbrComboBox.getSelectedItem();
    drinkPanel.removeAll();
    
    final GridBagConstraints c = new GridBagConstraints();
    c.gridy = 0; c.gridx = 0;
    for (int i = 0; i < COLUMNS_TITLES.length; i++) { // places the titles of the columns
      drinkPanel.add(new JLabel(COLUMNS_TITLES[i]), c);
      c.gridx += 1;
    }

    for (int i = 0; i < NBR_DRINKS; i++) { // places the drinks fields to enter info
      c.gridy += 1; c.gridx = 0;
      drinkPanel.add(drinksNames[i], c);
      c.gridx = 1;
      drinkPanel.add(drinksSugar[i], c);
      c.gridx = 2;
      drinkPanel.add(drinksPrices[i], c);
      c.gridx = 3;
      drinkPanel.add(drinksStocks[i], c);
    }
    
    drinkPanel.revalidate();
    this.repaint();
  }

  private static String getProblemText(String part) {
    return "<html>Error while parsing " + part + " info. Fields can't be empty.<br>"
        + "Names can't be longer than 18 characters. Integers can't be larger than 2^31.</html>";
  }

}
