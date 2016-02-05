package vendingmachine.states;

import vendingmachine.components.*;

public class Idle extends State {

	private static Idle instance;
	
	//Singleton design pattern
	private Idle() {}
	public static Idle Instance() {
		if (instance == null) instance = new Idle();
		return instance;
	}
	
	@Override
	public String getDefaultText() {
		String msg = "Please make your choice";
		if (amountInside > 0) {
			msg += (" - " + amountInside/100.0 + " � entered");
		}
		return msg;
	}
	
	@Override
	public void drinkButton(Drink drink, Context c) {
		if (!c.getStock().isCupInStock()) {
			c.getObserver().setTemporaryNorthText("Cups are out of stock. No drink can be ordered");
		}
		if (!c.getStock().isDrinkInStock(drink)) {
			c.getObserver().setTemporaryNorthText("Drink out of stock (otherwise " + drink.getPrice()/100.0 + " �)");
		}
		else if (drink.getPrice() > amountInside) {
			c.getObserver().setTemporaryNorthText("Price: " + drink.getPrice()/100.0 + " �");
		}
		else if (c.getChangeMachine().giveChange(amountInside - drink.getPrice()) == 1) {
			if (drink.isSugar())
				c.changeState(Asking.Instance());
			else {
				c.changeState(Preparing.Instance());
			}
		}
		else {
			c.getObserver().setTemporaryNorthText("Unable to give the exact change");
		}
	}
	
	@Override
	public void coinInserted(Coin coin, Context c) {
		super.coinInserted(coin, c);
		if (c.getChangeMachine().isCoinAccepted(coin)) {
			amountInside += coin.VALUE;
			c.getChangeMachine().insertCoin(coin);
			c.getObserver().setTemporaryNorthText(Double.toString(coin.VALUE/100.0) + " � inserted");
		}
		else {
			c.getObserver().setChangeBool(true);
			c.getObserver().setTemporaryNorthText("Coin not recognized by the machine");
		}
		c.getObserver().setInfo(); //Le mettre ici ?????
	}
}