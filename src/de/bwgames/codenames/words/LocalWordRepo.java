/**
 * 
 */
package de.bwgames.codenames.words;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;


/**
 * @author Bastian
 *
 */
public class LocalWordRepo implements WordRepo, WordRepoFactory{
	
	
	private URI path;

	public LocalWordRepo() {
	}
	
	public LocalWordRepo(URI path) {
		this.path = path;
	}
	
	
	public String[] getRandomWords(long key, int count) {
		String[] words = new String[count];
		List<String> wordList= createWordList();
		Random random = new Random(key);
		fill(words, random, wordList);
		return words;
	}

	private void fill(String[] words, Random random, List<String> wordList) {
		for (int i = 0; i < words.length; i++) {
			int nextIndex = random.nextInt(wordList.size());
			String word = wordList.get(nextIndex);
			wordList.remove(nextIndex);
			words[i]=word;
		}
	}

	private List<String> createWordList() {
		LinkedList<String> wordList = new LinkedList<>();
		try(Scanner scan = new Scanner(path.toURL().openStream())) {
			while (scan.hasNextLine()) {
				String nextLine = scan.nextLine().trim();
				if(!nextLine.isEmpty()) {
					wordList.add(nextLine);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wordList;
	}

	@Override
	public WordRepo createWordRepo(URI uri) {
		return new LocalWordRepo(uri);
	}

}
