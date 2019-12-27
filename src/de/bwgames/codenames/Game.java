package de.bwgames.codenames;

import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import de.bwgames.codenames.words.WordRepositoryFactory;

/**
 * 
 * @author Bastian
 *
 */
public class Game {
	private static final int GAME_SIZE = 25;
	private static final WordRepositoryFactory repoFactory = new WordRepositoryFactory();
	private String[] words;
	private WordState[] wordStates = new WordState[GAME_SIZE];
	private WordState currentPlayer;
	
	private List<GameListener> listeners = new LinkedList<>();

	public Game(long randomGeneratorKey, URI wordRepoSource) {
		words = repoFactory.createWordRepo(wordRepoSource).getRandomWords(randomGeneratorKey, 25);
		generateMask(randomGeneratorKey);
	}

	private void generateMask(long randomGeneratorKey) {
		Random random = new Random(randomGeneratorKey);
		WordState first = WordState.BLUE;
		WordState second = WordState.RED;
		if (random.nextInt(10) > random.nextInt(10)) {
			first = WordState.RED;
			second = WordState.BLUE;
		}
		currentPlayer = first;
		fillIn(first, 9, random);
		fillIn(second, 8, random);
		fillIn(WordState.BLACK, 1, random);
		fillWithEmpty();
	}

	private void fillIn(WordState state, int count, Random random) {
		for (int i = 0; i < count; i++) {
			int index = random.nextInt(GAME_SIZE);
			while (wordStates[index] != null) {
				index = random.nextInt(GAME_SIZE);
			}
			wordStates[index] = state;
		}
	}

	private void fillWithEmpty() {
		for (int i = 0; i < wordStates.length; i++) {
			if (wordStates[i] == null) {
				wordStates[i] = WordState.EMPTY;
			}
		}

	}

	public String[] getWords() {
		return words;
	}

	public WordState[] getWordStates() {
		return wordStates;
	}

	public WordState getPlayer() {
		return currentPlayer;
	}

	public void setPlayer(WordState player) {
		if (player != WordState.BLUE && player != WordState.RED) {
			return;
		}
		currentPlayer = player;
	}

	public int getGameSize() {
		return GAME_SIZE;
	}

	public GameState set(int index, WordState state) {
		if (wordStates[index] == WordState.BLACK) {
			wordStates[index] = WordState.BLACK_DETECTED;
			callListener();
			return GameState.LOOSE;
		}
		
		if (wordStates[index] == WordState.EMPTY) {
			wordStates[index] = WordState.EMPTY_DETECTED;
			callListener();
			return GameState.SWITCH_PLAYER;
		}
		
		GameState newGameState = check(index, state, WordState.BLUE, WordState.RED);
		if(newGameState == null) {
			newGameState = check(index, state, WordState.RED, WordState.BLUE);
		}
		
		if(newGameState == null) {
			newGameState = GameState.CONTINUE;
		}
		callListener();
		return newGameState;
	}

	private void callListener() {
		for (GameListener gameListener : listeners) {
			gameListener.update(()->this);
		}
	}

	private GameState check(int index, WordState stateToSet, WordState rightState, WordState wrongState) {
		if (stateToSet == rightState) {
			if (wordStates[index] == rightState) {
				wordStates[index] = WordState.valueOf(rightState+"_DETECTED");
				if(checkIfNoStateIs(rightState)) {
					return GameState.WIN;
				}
				return GameState.CONTINUE;
			} else {
				if(wordStates[index] == wrongState) {
					wordStates[index] = WordState.valueOf(wrongState+"_DETECTED");
					if(checkIfNoStateIs(wrongState)) {
						return GameState.LOOSE;
					}
				}
				return GameState.SWITCH_PLAYER;
			}
		}
		return null;
	}

	private boolean checkIfNoStateIs(WordState stateWhichShouldNtBeThere) {
		for (WordState state : wordStates) {
			if(state == stateWhichShouldNtBeThere) {
				return false;
			}
		}
		return true;
	}
	
	public void register(GameListener listener) {
		listeners.add(listener);
	}

}
