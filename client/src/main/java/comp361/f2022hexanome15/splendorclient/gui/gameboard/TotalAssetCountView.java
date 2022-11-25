package comp361.f2022hexanome15.splendorclient.gui.gameboard;

import comp361.f2022hexanome15.splendorclient.model.cards.Observer;
import javafx.scene.text.Text;

public class TotalAssetCountView extends Text implements Observer {
	
	private static int totalTokenCount = 15;
	
	public TotalAssetCountView(String startupText) {
    setText(startupText);
	}
	
	@Override
	public void onAction(boolean bIncrement) {
		if (bIncrement) {
			totalTokenCount++;
			String text = String.format("Total Token Count: %d/10", totalTokenCount);
			setText(text);
		}
		else {
			totalTokenCount--;
			String text = String.format("Total Token Count: %d", totalTokenCount);
			setText(text);
		}
	}
}
