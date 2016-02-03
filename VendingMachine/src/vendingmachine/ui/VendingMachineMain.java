package vendingmachine.ui;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import vendingMachine.components.ChangeMachine;
import vendingMachine.components.Drink;
import vendingMachine.components.Stock;
import vendingMachine.components.VendingMachineContext;
import vendingmachine.components.*;

public class VendingMachineMain {

	private Context c;
	private VendingMachineGUI gui;
	private static boolean configDone = false;

	public static void main(String[] args) {
		//THREADING ????
		VendingMachineMain vmMain = new VendingMachineMain();
		
    	vmMain.c = config();
    	vmMain.gui = new VendingMachineGUI(vmMain.c);

	}

	private void launch() {
		// TODO - implement VendingMachineMain.run
		
	}

	private static Context config() {
		//-----------------------------------
		//This first part creates the menu UI
		//-----------------------------------
		JFrame myFrame = new JFrame();
		myFrame.setTitle("Vending Machine Initialization");
		Container myPane = myFrame.getContentPane();
		myPane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//create everything for the drinks values
		final String[] COLUMNS_TITLES = {"Enter here the drinks names: ",
				"Contains sugar ?", "Price (in cents)  ", "Initial stock"};
		c.gridy = 0;
		for (int i = 0; i < 4; i++) {
			c.gridx = i;
			myPane.add(new JLabel(COLUMNS_TITLES[i]), c);
		}
		
		final String[] DEFAULT_DRINKS = {"Black Coffee", "Cappuccino", "Hot Chocolate",
				"Hot Milk", "Green Tea", "Earl Grey", "Tomato Soup", "Mushroom Soup"};
		JTextField[] drinksNames = new JTextField[8];
		JCheckBox[] drinksSugar = new JCheckBox[8];
		JTextField[] drinksPrices = new JTextField[8];
		JTextField[] drinksStocks = new JTextField[8];
		
		for (int i = 0; i < 8; i++) {
			c.gridy = i + 1;
			drinksNames[i] = new JTextField(DEFAULT_DRINKS[i], 18);
			drinksSugar[i] = new JCheckBox();
			drinksSugar[i].setSelected(true);
			drinksPrices[i] = new JTextField("100", 5);
			drinksStocks[i] = new JTextField("10", 5);
			c.gridx = 0;
			myPane.add(drinksNames[i], c);
			c.gridx = 1;
			myPane.add(drinksSugar[i], c);
			c.gridx = 2;
			myPane.add(drinksPrices[i], c);
			c.gridx = 3;
			myPane.add(drinksStocks[i], c);
		}
		
		//create everything for the change machine
		final String[] COINS_TITLES = {"Coins and their values: ",
				"Initial stock", "Accepted by the machine?"};
		c.gridy = 9;
		for (int i = 0; i < 3; i++) {
			c.gridx = i;
			myPane.add(new JLabel(COINS_TITLES[i]), c);
		}
		
		JTextField[] coinsStockValues = new JTextField[8];
		JCheckBox[] acceptedCoinsBoxes = new JCheckBox[8];
		for (int i = 0; i < 8; i++) {
			c.gridy = 10 + i;
			coinsStockValues[i] = new JTextField("10", 4);
			acceptedCoinsBoxes[i] = new JCheckBox();
			if (i <= 4) {
				acceptedCoinsBoxes[i].setSelected(true);
			}
			c.gridx = 0;
			myPane.add(new JLabel(ChangeMachine.COINS_TEXT[i]), c);
			c.gridx = 1;
			myPane.add(coinsStockValues[i], c);
			c.gridx = 2;
			myPane.add(acceptedCoinsBoxes[i], c);
		}
		
		//create everything for the stock values
		JLabel sugarCubesNbrLabel = new JLabel("Number of sugar cubes available: ");
		JLabel cupsNbrLabel = new JLabel("Number of cups available: ");
		JLabel spoonsNbrLabel = new JLabel("Number of spoons availables: ");
		JTextField sugarCubesNbrValue = new JTextField("100", 4);
		JTextField cupsNbrValue = new JTextField("100", 4);
		JTextField spoonsNbrValue = new JTextField("200", 4);
		
		c.gridy = 19;	c.gridx = 0;
		myPane.add(sugarCubesNbrLabel, c);
		c.gridy = 19;	c.gridx = 1;
		myPane.add(sugarCubesNbrValue, c);
		
		c.gridy = 20;	c.gridx = 0;
		myPane.add(cupsNbrLabel, c);
		c.gridy = 20;	c.gridx = 1;
		myPane.add(cupsNbrValue, c);
		
		c.gridy = 21;	c.gridx = 0;
		myPane.add(spoonsNbrLabel, c);
		c.gridy = 21;	c.gridx = 1;
		myPane.add(spoonsNbrValue, c);
		
		//create the button that allows to continue the code after the while loop
		JButton create = new JButton("Click here to begin the simulation !");
		c.gridy = 25; c.gridx = 0; c.gridwidth = GridBagConstraints.REMAINDER;
		create.addActionListener(e -> configDone = true);
		myPane.add(create, c);
		
		myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		myFrame.pack();
		myFrame.setVisible(true);
		
		//the program waits here until the user pushes the JButton "create"
		while (!configDone) {
			try {
	            Thread.sleep(100);
	        }
	        catch (InterruptedException e) {}
		}
		
		//----------------------------------------------------------
		//The second part gets all the data from what the user typed
		//----------------------------------------------------------
		//fetch the values for the Drinks
		Drink[] drinkList = new Drink[8];
		for (int i = 0; i < 8; i++) {
			drinkList[i] = new Drink(drinksNames[i].getText(), drinksSugar[i].isSelected(),
									Integer.parseInt(drinksPrices[i].getText()));
		}
		
		//fetch the values for the change machine
		int[] coinsStock = new int[8];
		boolean[] coinsAccepted = new boolean[8];
		for (int i = 0; i < 8; i++) {
			coinsStock[i] = Integer.parseInt(coinsStockValues[i].getText());
			coinsAccepted[i] = acceptedCoinsBoxes[i].isSelected();
		}
		ChangeMachine cm = new ChangeMachine(coinsStock, coinsAccepted);
		
		//fetch the values for the stock
		int sugarCubeNbr = Integer.parseInt(sugarCubesNbrValue.getText());
		int cupsNbr = Integer.parseInt(cupsNbrValue.getText());
		int spoonsNbr = Integer.parseInt(spoonsNbrValue.getText());
		int[] drinkQty = new int[8];
		for (int i = 0; i < 8; i++) {
			drinkQty[i] = Integer.parseInt(drinksStocks[i].getText());
		}
		
		Stock stock = new Stock(sugarCubeNbr, cupsNbr, spoonsNbr, drinkQty);
		myFrame.dispose(); //closes the frame
		return new Context(drinkList, cm, stock);
	}

}