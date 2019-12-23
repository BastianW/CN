/**
 * 
 */
package de.bwgames.codenames;

/**
 * @author Bastian
 *
 */
public interface GameListener {
	interface GameContext{
		Game getGame();
	}
	public void update(GameContext ctx);
}
