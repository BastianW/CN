/**
 * 
 */
package de.bwgames.codenames.words;

import java.net.URI;

/**
 * @author Bastian
 *
 */
public interface WordRepoFactory {
	WordRepo createWordRepo(URI uri);
}
