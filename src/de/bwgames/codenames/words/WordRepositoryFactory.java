/**
 * 
 */
package de.bwgames.codenames.words;

import java.net.URI;

/**
 * @author Bastian
 *
 */
public class WordRepositoryFactory implements WordRepoFactory{

	public WordRepositoryFactory() {
	}
	@Override
	public WordRepo createWordRepo(URI uri) {
		return new LocalWordRepo(uri);
	}
	

}
