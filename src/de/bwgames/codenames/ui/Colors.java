/**
 * 
 */
package de.bwgames.codenames.ui;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

/**
 * @author Bastian
 *
 */
public final class Colors {

	public static final Background BLACK = new Background(new BackgroundFill(Color.rgb(0x91, 0x91, 0x91), null, null));
	public static final Background BLUE = new Background(new BackgroundFill(Color.rgb(0x00, 0xbf, 0xff), null, null));
	public static final Background RED = new Background(new BackgroundFill(Color.rgb(0xff, 0x40, 0x40), null, null));
	public static final Background WHITE = new Background(new BackgroundFill(Color.rgb(0xff, 0xff, 0xff), null, null));

	private Colors() {
	}

}
