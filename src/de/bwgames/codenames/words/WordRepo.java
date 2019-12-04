/**
 * 
 */
package de.bwgames.codenames.words;

/**
 * @author Bastian
 *
 */
public interface WordRepo {
	String[] getRandomWords(long key, int count);
}
