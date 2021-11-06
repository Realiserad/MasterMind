package se.kth.nada.bastianf.mastermind;

import java.util.ArrayList;
import java.util.Random;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * A MasterMind board.
 * 
 * @author Realiserad
 * @version 1.0
 */
public class Board implements Parcelable {
	private int[] secret;
	private int colors;
	private int pegs;
	private int maxGuesses;
	private ArrayList<int[]> guesses;
	private ArrayList<int[]> hints;
	private boolean locked;

	/* CONSTRUCTORS */
	/***************************************************************/

	/**
	 * Setup a board from a parcel.
	 * 
	 * @param in
	 *            A parcel.
	 */
	public Board(Parcel in) {
		secret = in.createIntArray();
		colors = in.readInt();
		pegs = in.readInt();
		maxGuesses = in.readInt();
		in.readList(guesses, Integer.class.getClassLoader());
		in.readList(hints, Integer.class.getClassLoader());
		boolean[] booleans = new boolean[1];
		in.readBooleanArray(booleans);
		locked = booleans[0];
		if (guesses.size() - 1 >= maxGuesses
				|| compareIntegers(secret, guesses.get(guesses.size() - 1))) {
			locked = true;
		}
	}

	/**
	 * Setup an empty board with the number of pegs, colors and guesses and the
	 * secret pattern specified.
	 * 
	 * @param pegs
	 *            The number of pegs for each guess.
	 * @param colors
	 *            The number of colors for each peg.
	 * @param maxGuesses
	 *            The maximum number of guesses.
	 * @param secret
	 *            The secret pattern of this game.
	 */
	public Board(int pegs, int colors, int maxGuesses, int[] secret) {
		this.colors = colors;
		this.pegs = pegs;
		this.maxGuesses = maxGuesses;
		guesses = new ArrayList<int[]>();
		hints = new ArrayList<int[]>();
		this.secret = secret;
	}

	/**
	 * Setup an empty board with a random secret pattern and the number of pegs,
	 * colors and guesses specified.
	 * 
	 * @param pegs
	 *            The number of pegs for each guess.
	 * @param colors
	 *            The number of colors for each peg.
	 * @param maxGuesses
	 *            The maximum number of guesses.
	 * @param secret
	 *            The secret pattern for this game.
	 */
	public Board(int pegs, int colors, int maxGuesses) {
		this.colors = colors;
		this.pegs = pegs;
		this.maxGuesses = maxGuesses;
		guesses = new ArrayList<int[]>();
		hints = new ArrayList<int[]>();
		secret = new int[pegs];
		Random r = new Random();
		for (int i = 0; i < pegs; i++) {
			secret[i] = r.nextInt(colors);
		}
	}

	/**
	 * Load an old game from a board provided.
	 * 
	 * @param board
	 *            An old board.
	 */
	public Board(Board board) {
		this.colors = board.colors;
		this.pegs = board.pegs;
		this.maxGuesses = board.maxGuesses;
		this.guesses = board.guesses;
		this.hints = board.hints;
		this.secret = board.secret;
		if (guesses.size() >= maxGuesses
				|| compareIntegers(guesses.get(guesses.size() - 1), secret)) {
			locked = true;
		}
	}

	/* PUBLIC FUNCTIONS */
	/***************************************************************/

	/**
	 * Make a guess. This function returns a hint consisting of two integers.
	 * The first integer specifies the number of pegs with correct position and
	 * color, while the second integer specifies the number of pegs that have
	 * the right color but are in the wrong position. Calling this function may
	 * also lock the board if the number of guesses is equal to the maximum
	 * number of guesses allowed or if the guess matches the secret pattern.
	 * 
	 * @param guess
	 *            A guess to make.
	 * @return A hint, or null if the board is locked.
	 */
	public int[] makeGuess(int[] guess) {
		int[] hint = evaluateGuess(guess);
		if (guess != null) {
			guesses.add(guess);
			hints.add(hint);
			if (guesses.size() >= maxGuesses && maxGuesses != 0
					|| compareIntegers(guess, secret)) {
				lockBoard();
			}
		}
		return hint;
	}

	/**
	 * Evaluate a guess given, with the secret pattern and return a hint. This
	 * function is similar to the method makeGuess, but it does add anything to
	 * the board. The hint consists of two integers. The first integer specifies
	 * the number of pegs with correct position and color, while the second
	 * integer specifies the number of pegs that have the right color but are in
	 * the wrong position.
	 * 
	 * @param A
	 *            guess to evaluate.
	 * @return A hint, or null if the board is locked.
	 */
	public int[] evaluateGuess(int[] guess) {
		if (!locked) {
			int[] hint = new int[2];
			boolean[] guessSkip = new boolean[pegs];
			boolean[] secretSkip = new boolean[pegs];
			for (int i = 0; i < pegs; i++) {
				if (secret[i] == guess[i]) {
					guessSkip[i] = true;
					secretSkip[i] = true;
					hint[0]++;
				}
			}
			for (int i = 0; i < pegs; i++) {
				if (!guessSkip[i]) {
					for (int j = 0; j < pegs; j++) {
						if (!secretSkip[j] && guess[i] == secret[j]) {
							secretSkip[j] = true;
							hint[1]++;
							break;
						}
					}
				}
			}
			return hint;
		}
		return null;
	}

	/**
	 * Reset the board.
	 */
	public void reset() {
		guesses.clear();
		hints.clear();
	}

	/**
	 * Undo the last guess made.
	 */
	public void undo() {
		if (!guesses.isEmpty()) {
			guesses.remove(guesses.size() - 1);
			hints.remove(hints.size() - 1);
		}
	}

	/* ACCESSORS AND MODIFIERS */
	/***************************************************************/

	/**
	 * Reveal the secret pattern for this game.
	 * 
	 * @return The secret pattern for this game.
	 */
	public int[] getSecret() {
		return secret;
	}

	/**
	 * Get the number of pegs in a guess.
	 * 
	 * @return The number of pegs allowed in each guess.
	 */
	public int getNumberOfPegs() {
		return pegs;
	}

	/**
	 * Determine if the board is locked.
	 * 
	 * @return True if the board is locked.
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * Lock the board.
	 */
	public void lockBoard() {
		locked = true;
	}

	/**
	 * Unlock the board.
	 */
	public void unlockBoard() {
		locked = false;
	}

	/**
	 * Get the guesses for this game.
	 * 
	 * @return Guesses made by the player.
	 */
	public ArrayList<int[]> getGuesses() {
		return guesses;
	}

	/**
	 * Get the hints for this game.
	 * 
	 * @return Hints generated by the computer.
	 */
	public ArrayList<int[]> getHints() {
		return hints;
	}

	/* HELPER FUNCTIONS */
	/***************************************************************/

	/**
	 * Compare two vectors with integers.
	 * 
	 * @return True if the content of the vectors are equal in both positions
	 *         and values.
	 */
	private boolean compareIntegers(int[] a, int[] b) {
		// Check that the vectors are equal in size
		if (a.length != b.length) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

	/* PARCELABLE INTERFACE */
	/***************************************************************/

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeIntArray(secret);
		out.writeInt(colors);
		out.writeInt(pegs);
		out.writeInt(maxGuesses);
		out.writeList(guesses);
		out.writeList(hints);
		out.writeBooleanArray(new boolean[] { locked });
	}

	public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>() {
		public Board createFromParcel(Parcel in) {
			return new Board(in);
		}

		public Board[] newArray(int size) {
			return new Board[size];
		}
	};
}