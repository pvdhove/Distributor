package vendingmachine.components;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import vendingmachine.Drink;
import vendingmachine.states.NoCup;

/**
 * The Stock class lists all the stock values needed for a drinks vending machine
 * (sugar cubes, cups, spoons and drinks).
 */
public class Stock {

  private static final Logger log = LogManager.getLogger("Stock");

  /*
   * The number of sugar, cups and spoons in stock.
   */
  private int sugarCubesNbr;
  private int cupsNbr;
  private int spoonsNbr;

  /**
   * A Map that maps to each Drink its stock as an Integer.
   */
  private final Map<Drink, Integer> drinkQty;

  /**
   * Creates a Stock with the specified values.
   * Throws an IllegalArgumentException if a value is negative.
   * 
   * @param sugarCubesNbr the number of sugar cubes
   * @param cupsNbr the number of cups
   * @param spoonsNbr the number of spoons
   * @param drinkQty a Map mapping each Drink to its stock value
   */
  public Stock(int sugarCubesNbr, int cupsNbr, int spoonsNbr, Map<Drink, Integer> drinkQty) {
    if (sugarCubesNbr < 0 || cupsNbr < 0 || spoonsNbr < 0) {
      throw new IllegalArgumentException("The values for the stock can not be negative");
    }
    for (Integer i: drinkQty.values()) {
      if (i < 0) {
        throw new IllegalArgumentException("The stock of a Drink can not be negative");
      }
    }
    this.sugarCubesNbr = sugarCubesNbr;
    this.cupsNbr = cupsNbr;
    this.spoonsNbr = spoonsNbr;
    this.drinkQty = drinkQty;
  }

  /**
   * @param sugar the number of sugar cubes that may be in stock
   * @return true if there is at least {@code sugar} sugar cubes in stock.
   */
  public boolean isSugarInStock(int sugar) {
    return sugar <= sugarCubesNbr;
  }

  /**
   * @return true if there is a cup in stock, false otherwise
   */
  public boolean isCupInStock() {
    return cupsNbr > 0;
  }

  /**
   * @return true if there is a spoon in stock, false otherwise
   */
  public boolean isSpoonInStock() {
    return spoonsNbr > 0;
  }

  /**
   * @param drink the Drink that may be in stock
   * @return true if there is a {@code drink} in stock, false otherwise
   */
  public boolean isDrinkInStock(Drink drink) {
    return drinkQty.get(drink) > 0;
  }

  /**
   * Removes {@code i} sugar cubes from the stock.
   * Throws an IllegalArgumentException if there is not {@code i} sugar cubes in stock.
   * 
   * @param i number of sugar cubes to remove.
   */
  public void removeSugarCubes(int i) {
    if (isSugarInStock(i)) {
      sugarCubesNbr -= i;
    } else {
      throw new IllegalArgumentException(
          "Can't remove " + i + " sugar cubes; only " + sugarCubesNbr + " remaining.");
    }
  }

  /**
   * Removes one cup from the stock.
   * Throws an IllegalArgumentException if there is no cup in stock.
   * If the number of cups becomes zero, notifies the specified IContext of the problem.
   * 
   * @param context the IContext to notify if there is no cups left
   */
  public void removeCup(IContext context) {
    if (isCupInStock()) {
      cupsNbr -= 1;
    } else {
      throw new IllegalArgumentException("Can't remove a cup when none in stock");
    }
    if (!isCupInStock()) {
      context.addProblem(NoCup.getInstance());
    }
  }

  /**
   * Removes one spoon from the stock.
   * Throws an IllegalArgumentException if there is no spoons in stock.
   */
  public void removeSpoon() {
    if (isSpoonInStock()) {
      spoonsNbr -= 1;
    } else {
      throw new IllegalArgumentException("Can't remove a spoon when none in stock");
    }
  }

  /**
   * Removes one {@code drink} from the stock.
   * Throws an IllegalArgumentException if this drink was not in stock.
   * 
   * @param drink the Drink to remove
   */
  public void removeDrink(Drink drink) {
    if (isDrinkInStock(drink)) {
      drinkQty.put(drink, drinkQty.get(drink) - 1); 
    } else {
      throw new IllegalArgumentException("Can't remove a " + drink.getName() + "; none left in stock");
    }
  }

  /**
   * @return the number of sugar cubes in stock
   */
  public int getSugarCubesNbr() {
    return sugarCubesNbr;
  }

  /**
   * @return the number of cups in stock
   */
  public int getCupsNbr() {
    return cupsNbr;
  }

  /**
   * @return the number of spoons in stock
   */
  public int getSpoonsNbr() {
    return spoonsNbr;
  }

  /**
   * @param drink the drink whose stock value must be known
   * @return the number of the specified Drink in stock
   */
  public int getDrinkQty(Drink drink) {
    return drinkQty.get(drink);
  }

  /**
   * @return a List of the drinks the machine can dispense
   */
  public List<Drink> getDrinks() {
    return new ArrayList<Drink>(drinkQty.keySet());
  }

  /**
   * @return a String containing all the information about the current stock.
   */
  public String getInfo() {
    final StringBuilder sb = new StringBuilder(120);
    sb.append("Drink(s): \n");
    for (Drink drink: this.getDrinks()) {
      sb.append(drink.getName()).append(": ")
      .append(drinkQty.get(drink)).append(" available.\n");
    }

    sb.append('\n')
    .append(cupsNbr).append(" cup(s) available.\n")
    .append(sugarCubesNbr).append(" sugar cube(s) available.\n")
    .append(spoonsNbr).append(" spoon(s) available.\n");
    return sb.toString();
  }

  /**
   * Sets a new number of sugar cubes in stock. Logs the change that is done.
   * If the number is negative, throws an IllegalArgumentException.
   * 
   * @param newSugarCubesNbr the number of sugar cubes to set
   */
  public void setSugarStock(int newSugarCubesNbr) {
    if (newSugarCubesNbr < 0) {
      throw new IllegalArgumentException();
    }
    final int difference = newSugarCubesNbr - this.sugarCubesNbr;
    if (difference > 0) {
      log.info(difference + " cubes resupplied (now " + newSugarCubesNbr + " available).");
    } else if (difference < 0) {
      log.info(-difference + " cubes removed from the stock (now " + newSugarCubesNbr + " available).");
    }
    
    this.sugarCubesNbr = newSugarCubesNbr;
  }

  /**
   * Sets a new number of cups in stock. Logs the change that is done.
   * If the number is negative, throws an IllegalArgumentException.
   * If the number of cups reaches 0 or is no more 0, updates the Context.
   * 
   * @param newCupsNbr the number of cups to set
   * @param context the IContext to update if the number of cups reaches 0
   */
  public void setCupStock(int newCupsNbr, IContext context) {
    if (newCupsNbr < 0) {
      throw new IllegalArgumentException();
    }
    final int difference = newCupsNbr - this.cupsNbr;
    if (difference > 0) {
      log.info(difference + " cups resupplied (now " + newCupsNbr + " available).");
    } else if (difference < 0) {
      log.info(-difference + " cups removed from the stock (now " + newCupsNbr + " available).");
    }
    if (newCupsNbr == 0) {
      context.addProblem(NoCup.getInstance());
    } else if (this.cupsNbr == 0 && newCupsNbr > 0) {
      context.problemSolved(NoCup.getInstance());
    }
    
    this.cupsNbr = newCupsNbr;
  }

  /**
   * Sets a new number of spoons in stock. Logs the change that is done.
   * If the number is negative, throws an IllegalArgumentException.
   * 
   * @param newSpoonsNbr the number of spoons to set
   */
  public void setSpoonsStock(int newSpoonsNbr) {
    if (newSpoonsNbr < 0) {
      throw new IllegalArgumentException();
    }
    final int difference = newSpoonsNbr - this.spoonsNbr;
    if (difference > 0) {
      log.info(difference + " spoons resupplied (now " + newSpoonsNbr + " available).");
    } else if (difference < 0) {
      log.info(-difference + " spoons removed from the stock (now " + newSpoonsNbr + " available).");
    }
    
    this.spoonsNbr = newSpoonsNbr;
  }

  /**
   * Updates the stock of {@code drink} to the {@code value} specified.
   * Throws an IllegalArgumentException if {@code value} is negative.
   * Logs the change that is done.
   * 
   * @param drink the Drink whose stock must be changed
   * @param value the new value for the {@code drink} stock (must be positive)
   */
  public void setDrinkQty(Drink drink, int value) {
    if (value < 0) {
      throw new IllegalArgumentException("The value can not be negative");
    }
    final int difference = value - drinkQty.get(drink);
    if (difference > 0) {
      log.info(difference + " " + drink.getName() + " resupplied (" + value + " in stock).");
    } else if (difference < 0) {
      log.info(-difference + " " + drink.getName() + " removed from the stock (" + value + " remaining).");
    }

    drinkQty.put(drink, value);
  }

}