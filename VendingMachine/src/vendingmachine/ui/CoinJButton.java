package vendingmachine.ui;

import javax.swing.*;
import vendingmachine.components.Coin;

public class CoinJButton extends JButton {

	private static final long serialVersionUID = 1L; //generated by Eclipse
	private Coin coin;

	public CoinJButton(Coin coin, Icon icon) {
		super(icon);
		this.coin = coin;
		setBorder(BorderFactory.createEmptyBorder());
		setContentAreaFilled(false);
	}

	public Coin getCoin() {
		return this.coin;
	}

}