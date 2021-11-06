package se.kth.nada.bastianf.mastermind;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class MainActivity extends Activity {
	private Board board;
	private int[] currentPegRow;
	private int pegsSet = 0;
	private ArrayList<LinearLayout> pegRows = new ArrayList<LinearLayout>();
	private ArrayList<ImageView[]> pegHoles = new ArrayList<ImageView[]>();
	private HashMap<ImageView, Integer> colors = new HashMap<ImageView, Integer>();
	private HashMap<Integer, Drawable> drawables = new HashMap<Integer, Drawable>();
	private BiMap<ImageView, Drawable> pickers = HashBiMap.create();
	private BiMap<ImageView, Drawable> selectedPickers = HashBiMap.create();
	private BiMap<ImageView, Drawable> pickersLand = HashBiMap.create();
	private BiMap<ImageView, Drawable> selectedPickersLand = HashBiMap.create();
	private boolean[] pickerSelected = new boolean[11];

	private boolean startNewGame = true;
	private boolean trashCanIsEmpty = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set content view
		setContentView(R.layout.activity_main);

		// Add empty peg to drawables
		drawables.put(-1, getResources().getDrawable(R.drawable.empty_peg));

		// Add drawables to toggle pickers
		/* PICKERS */
		pickers.put((ImageView) findViewById(R.id.picker_black_peg),
				getResources()
						.getDrawable(R.drawable.picker_black_peg_selected));
		pickers.put((ImageView) findViewById(R.id.picker_blue_peg),
				getResources().getDrawable(R.drawable.picker_blue_peg_selected));
		pickers.put((ImageView) findViewById(R.id.picker_lilac_peg),
				getResources()
						.getDrawable(R.drawable.picker_lilac_peg_selected));
		pickers.put((ImageView) findViewById(R.id.picker_brown_peg),
				getResources()
						.getDrawable(R.drawable.picker_brown_peg_selected));
		pickers.put(
				(ImageView) findViewById(R.id.picker_orange_peg),
				getResources().getDrawable(
						R.drawable.picker_orange_peg_selected));
		pickers.put(
				(ImageView) findViewById(R.id.picker_yellow_peg),
				getResources().getDrawable(
						R.drawable.picker_yellow_peg_selected));
		pickers.put((ImageView) findViewById(R.id.picker_pink_peg),
				getResources().getDrawable(R.drawable.picker_pink_peg_selected));
		pickers.put((ImageView) findViewById(R.id.picker_red_peg),
				getResources().getDrawable(R.drawable.picker_red_peg_selected));
		pickers.put((ImageView) findViewById(R.id.picker_white_peg),
				getResources()
						.getDrawable(R.drawable.picker_white_peg_selected));
		pickers.put((ImageView) findViewById(R.id.picker_green_peg),
				getResources()
						.getDrawable(R.drawable.picker_green_peg_selected));
		pickers.put((ImageView) findViewById(R.id.picker_lime_peg),
				getResources().getDrawable(R.drawable.picker_lime_peg_selected));

		/* SELECTED PICKERS */
		selectedPickers.put((ImageView) findViewById(R.id.picker_black_peg),
				getResources().getDrawable(R.drawable.picker_black_peg));
		selectedPickers.put((ImageView) findViewById(R.id.picker_blue_peg),
				getResources().getDrawable(R.drawable.picker_blue_peg));
		selectedPickers.put((ImageView) findViewById(R.id.picker_lilac_peg),
				getResources().getDrawable(R.drawable.picker_lilac_peg));
		selectedPickers.put((ImageView) findViewById(R.id.picker_brown_peg),
				getResources().getDrawable(R.drawable.picker_brown_peg));
		selectedPickers.put((ImageView) findViewById(R.id.picker_orange_peg),
				getResources().getDrawable(R.drawable.picker_orange_peg));
		selectedPickers.put((ImageView) findViewById(R.id.picker_yellow_peg),
				getResources().getDrawable(R.drawable.picker_yellow_peg));
		selectedPickers.put((ImageView) findViewById(R.id.picker_pink_peg),
				getResources().getDrawable(R.drawable.picker_pink_peg));
		selectedPickers.put((ImageView) findViewById(R.id.picker_red_peg),
				getResources().getDrawable(R.drawable.picker_red_peg));
		selectedPickers.put((ImageView) findViewById(R.id.picker_white_peg),
				getResources().getDrawable(R.drawable.picker_white_peg));
		selectedPickers.put((ImageView) findViewById(R.id.picker_green_peg),
				getResources().getDrawable(R.drawable.picker_green_peg));
		selectedPickers.put((ImageView) findViewById(R.id.picker_lime_peg),
				getResources().getDrawable(R.drawable.picker_lime_peg));

		/* PICKERS LANDSCAPE */
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_black_peg),
				getResources().getDrawable(
						R.drawable.picker_black_peg_selected_land));
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_blue_peg),
				getResources().getDrawable(
						R.drawable.picker_blue_peg_selected_land));
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_lilac_peg),
				getResources().getDrawable(
						R.drawable.picker_lilac_peg_selected_land));
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_brown_peg),
				getResources().getDrawable(
						R.drawable.picker_brown_peg_selected_land));
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_orange_peg),
				getResources().getDrawable(
						R.drawable.picker_orange_peg_selected_land));
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_yellow_peg),
				getResources().getDrawable(
						R.drawable.picker_yellow_peg_selected_land));
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_pink_peg),
				getResources().getDrawable(
						R.drawable.picker_pink_peg_selected_land));
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_red_peg),
				getResources().getDrawable(
						R.drawable.picker_red_peg_selected_land));
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_white_peg),
				getResources().getDrawable(
						R.drawable.picker_white_peg_selected_land));
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_green_peg),
				getResources().getDrawable(
						R.drawable.picker_green_peg_selected_land));
		pickersLand.put(
				(ImageView) findViewById(R.id.picker_lime_peg),
				getResources().getDrawable(
						R.drawable.picker_lime_peg_selected_land));

		/* SELECTED PICKERS LANDSCAPE */
		selectedPickersLand.put(
				(ImageView) findViewById(R.id.picker_black_peg), getResources()
						.getDrawable(R.drawable.picker_black_peg_land));
		selectedPickersLand.put((ImageView) findViewById(R.id.picker_blue_peg),
				getResources().getDrawable(R.drawable.picker_blue_peg_land));
		selectedPickersLand.put(
				(ImageView) findViewById(R.id.picker_lilac_peg), getResources()
						.getDrawable(R.drawable.picker_lilac_peg_land));
		selectedPickersLand.put(
				(ImageView) findViewById(R.id.picker_brown_peg), getResources()
						.getDrawable(R.drawable.picker_brown_peg_land));
		selectedPickersLand.put(
				(ImageView) findViewById(R.id.picker_orange_peg),
				getResources().getDrawable(R.drawable.picker_orange_peg_land));
		selectedPickersLand.put(
				(ImageView) findViewById(R.id.picker_yellow_peg),
				getResources().getDrawable(R.drawable.picker_yellow_peg_land));
		selectedPickersLand.put((ImageView) findViewById(R.id.picker_pink_peg),
				getResources().getDrawable(R.drawable.picker_pink_peg_land));
		selectedPickersLand.put((ImageView) findViewById(R.id.picker_red_peg),
				getResources().getDrawable(R.drawable.picker_red_peg_land));
		selectedPickersLand.put(
				(ImageView) findViewById(R.id.picker_white_peg), getResources()
						.getDrawable(R.drawable.picker_white_peg_land));
		selectedPickersLand.put(
				(ImageView) findViewById(R.id.picker_green_peg), getResources()
						.getDrawable(R.drawable.picker_green_peg_land));
		selectedPickersLand.put((ImageView) findViewById(R.id.picker_lime_peg),
				getResources().getDrawable(R.drawable.picker_lime_peg_land));

		// Create long click listener for the trash can
		ImageView can = (ImageView) findViewById(R.id.trash_can);
		final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		can.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				vibrator.vibrate(10);
				undoPegRow(v);
				return true;
			}
		});

		// If the application is started for the first time or resumed from
		// Preferences, create a new game
		if (startNewGame) {
			newGame();
			startNewGame = false;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getResources().getConfiguration();
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// landscape mode
			final LinearLayout footer = (LinearLayout) findViewById(R.id.footer);
			footer.post(new Runnable() {

				@Override
				public void run() {
					LinearLayout guesses = (LinearLayout) findViewById(R.id.guesses);
					int w = footer.getWidth();
					guesses.setPadding(w, guesses.getPaddingTop(),
							guesses.getPaddingRight(),
							guesses.getPaddingBottom());
				}
			});
		}
	}

	/**
	 * Undo the last peg set if the last guess contains any pegs.
	 */
	public void undoPeg(View v) {
		if (pegsSet > 0) {
			ImageView[] latestPegs = pegHoles.get(pegHoles.size() - 1);
			if (pegsSet != board.getNumberOfPegs()) {
				latestPegs[pegsSet].setImageResource(R.drawable.empty_peg);
				currentPegRow[pegsSet] = -1;
			}
			pegsSet--;
			currentPegRow[pegsSet] = -1;
			Drawable[] layers = new Drawable[2];
			layers[0] = drawables.get(currentPegRow[pegsSet]);
			layers[1] = getResources().getDrawable(R.drawable.check);
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			latestPegs[pegsSet].setImageDrawable(layerDrawable);

			ImageView can = (ImageView) findViewById(R.id.trash_can);
			can.setImageResource(R.drawable.trash_can_frog);
			trashCanIsEmpty = false;
		}
	}

	/**
	 * Undo the current pegRow. If the current pegRow is empty, undo the
	 * previous pegRow.
	 */
	public void undoPegRow(View v) {
		// Unlock the board if not already unlocked
		board.unlockBoard();

		if (pegsSet > 0) {
			ImageView[] latestPegs = pegHoles.get(pegHoles.size() - 1);
			if (pegsSet == board.getNumberOfPegs()) {
				pegsSet--;
			}
			while (pegsSet > 0) {
				currentPegRow[pegsSet] = -1;
				latestPegs[pegsSet].setImageResource(R.drawable.empty_peg);
				pegsSet--;
			}
			// pegsSet = 0, clear and select this peg
			currentPegRow[pegsSet] = -1;
			Drawable[] layers = new Drawable[2];
			layers[0] = getResources().getDrawable(R.drawable.empty_peg);
			layers[1] = getResources().getDrawable(R.drawable.check);
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			latestPegs[pegsSet].setImageDrawable(layerDrawable);

			ImageView can = (ImageView) findViewById(R.id.trash_can);
			can.setImageResource(R.drawable.trash_can_frog);
			trashCanIsEmpty = false;
		} else {
			// pegsSet == 0 -> remove the current pegRow and clear the previous
			// one (if any)
			if (pegRows.size() > 1) {
				removeGuess(pegRows.size() - 1);
				pegsSet = board.getNumberOfPegs() - 1;
				ImageView[] latestPegs = pegHoles.get(pegHoles.size() - 1);
				while (pegsSet > 0) {
					currentPegRow[pegsSet] = -1;
					latestPegs[pegsSet].setImageResource(R.drawable.empty_peg);
					pegsSet--;
				}
				currentPegRow[pegsSet] = -1;
				Drawable[] layers = new Drawable[2];
				layers[0] = getResources().getDrawable(R.drawable.empty_peg);
				layers[1] = getResources().getDrawable(R.drawable.check);
				LayerDrawable layerDrawable = new LayerDrawable(layers);
				latestPegs[pegsSet].setImageDrawable(layerDrawable);

				board.undo();
				LinearLayout guessesHolder = (LinearLayout) pegRows.get(pegRows
						.size() - 1);
				LinearLayout keyPegHolder = (LinearLayout) guessesHolder
						.findViewById(R.id.key_peg_holder);
				keyPegHolder.removeAllViews();
				for (int i = 0; i < board.getNumberOfPegs(); i++) {
					ImageView iv = new ImageView(this);
					iv.setImageResource(R.drawable.empty_keypeg);
					keyPegHolder.addView(iv);
				}
				ImageView can = (ImageView) findViewById(R.id.trash_can);
				can.setImageResource(R.drawable.trash_can_frog);
				trashCanIsEmpty = false;
			}
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Save the current state of the activity
		outState.putParcelable("board", board);
		outState.putIntArray("currentPegRow", currentPegRow);
		outState.putInt("pegsSet", pegsSet);
		outState.putBoolean("startNewGame", startNewGame);
		outState.putBoolean("trashCanIsEmpty", trashCanIsEmpty);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// Restore the saved instance state
		board = savedInstanceState.getParcelable("board");
		currentPegRow = savedInstanceState.getIntArray("currentPegRow");
		pegsSet = savedInstanceState.getInt("pegsSet");
		startNewGame = savedInstanceState.getBoolean("startNewGame");

		// Restore pegPicker
		setupPegPicker();

		// Restore the guesses using our board
		LinearLayout guesses = (LinearLayout) findViewById(R.id.guesses);
		ArrayList<int[]> guessesFromBoard = board.getGuesses();
		ArrayList<int[]> hintsFromBoard = board.getHints();

		for (int i = 0; i < guessesFromBoard.size(); i++) {
			// Inflate the frame
			LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
			LinearLayout guessHolder = (LinearLayout) inflater.inflate(
					R.layout.guess_holder, null);
			// Get LinearLayouts from frame
			LinearLayout codePegHolder = (LinearLayout) guessHolder
					.findViewById(R.id.code_peg_holder);
			// Get current row of pegs
			int[] codePegs = guessesFromBoard.get(i);
			int[] keyPegs = hintsFromBoard.get(i);
			ImageView[] pegRow = new ImageView[codePegs.length];
			for (int j = 0; j < codePegs.length; j++) {
				// Add code pegs
				ImageView codePeg = new ImageView(this);
				codePeg.setImageDrawable(drawables.get(codePegs[j]));
				codePeg.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						selectPeg(v);
					}
				});
				codePegHolder.addView(codePeg);
				pegRow[j] = codePeg;
			}
			pegRows.add(guessHolder);
			pegHoles.add(pegRow);
			addBoardHint(keyPegs);
			
			// Display counter
			TextView guessCount = (TextView) guessHolder.findViewById(R.id.guess_count);
			LinearLayout guessCountHolder = (LinearLayout) guessHolder.findViewById(R.id.guess_count_holder);
			guessCount.setText("" + guesses.getChildCount());
			Random r = new Random();
			guessCountHolder.setBackgroundColor(r.nextInt());
			
			guesses.addView((LinearLayout) guessHolder);
		}

		// Add contents of currentPegRow if the board is unlocked
		if (!board.isLocked()) {
			addBoardGuess();
			ImageView[] latestPegs = pegHoles.get(pegHoles.size() - 1);
			// Restore last row of pegs
			for (int i = 0; i < board.getNumberOfPegs(); i++) {
				latestPegs[i].setImageDrawable(drawables.get(currentPegRow[i]));
			}
			if (pegsSet < board.getNumberOfPegs()) {
				Drawable[] layers = new Drawable[2];
				layers[0] = getResources().getDrawable(R.drawable.empty_peg);
				layers[1] = getResources().getDrawable(R.drawable.check);
				LayerDrawable layerDrawable = new LayerDrawable(layers);
				latestPegs[pegsSet].setImageDrawable(layerDrawable);
			}
		}

		// Remove the first "empty" row
		removeGuess(0);

		// Restore trash can
		trashCanIsEmpty = savedInstanceState.getBoolean("trashCanIsEmpty");
		if (!trashCanIsEmpty) {
			ImageView can = (ImageView) findViewById(R.id.trash_can);
			can.setImageResource(R.drawable.trash_can_frog);
		}

		// Scroll to bottom
		scrollToBottom();
	}

	/**
	 * Remove the guess with index i.
	 * 
	 * @param i
	 *            A index.
	 */
	private void removeGuess(int i) {
		LinearLayout guesses = (LinearLayout) findViewById(R.id.guesses);
		guesses.removeViewAt(i);
		pegRows.remove(i);
		pegHoles.remove(i);
	}

	/**
	 * Enable or disable pegs in the pegPicker depending on the user settings
	 * stored in SharedPreferences and map each peg to a corresponding color.
	 * Note! If any changes has been made to the SharedPreferences file, you
	 * should clear the board class to make sure that only appropriate values
	 * have been set.
	 * 
	 * @return The number of pegs in the pegPicker.
	 */
	private int setupPegPicker() {
		// Clear the color map
		colors.clear();
		// Get the settings from the SharedPreferences file
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		// Create a counter, to keep track of the number of pegs in the
		// pegPicker
		Integer value = 0;
		if (!prefs.getBoolean("black_enabled", true)) {
			// Black peg is disabled by user
			findViewById(R.id.picker_black_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_black_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_black_peg), value);
			drawables.put(value,
					getResources().getDrawable(R.drawable.black_peg));
			value++;
		}
		if (!prefs.getBoolean("lilac_enabled", true)) {
			findViewById(R.id.picker_lilac_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_lilac_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_lilac_peg), value);
			drawables.put(value,
					getResources().getDrawable(R.drawable.lilac_peg));
			value++;
		}
		if (!prefs.getBoolean("blue_enabled", true)) {
			findViewById(R.id.picker_blue_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_blue_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_blue_peg), value);
			drawables.put(value, getResources()
					.getDrawable(R.drawable.blue_peg));
			value++;
		}
		if (!prefs.getBoolean("green_enabled", true)) {
			findViewById(R.id.picker_green_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_green_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_green_peg), value);
			drawables.put(value,
					getResources().getDrawable(R.drawable.green_peg));
			value++;
		}
		if (!prefs.getBoolean("brown_enabled", true)) {
			findViewById(R.id.picker_brown_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_brown_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_brown_peg), value);
			drawables.put(value,
					getResources().getDrawable(R.drawable.brown_peg));
			value++;
		}
		if (!prefs.getBoolean("yellow_enabled", true)) {
			findViewById(R.id.picker_yellow_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_yellow_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_yellow_peg), value);
			drawables.put(value,
					getResources().getDrawable(R.drawable.yellow_peg));
			value++;
		}
		if (!prefs.getBoolean("red_enabled", true)) {
			findViewById(R.id.picker_red_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_red_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_red_peg), value);
			drawables
					.put(value, getResources().getDrawable(R.drawable.red_peg));
			value++;
		}
		if (!prefs.getBoolean("pink_enabled", true)) {
			findViewById(R.id.picker_pink_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_pink_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_pink_peg), value);
			drawables.put(value, getResources()
					.getDrawable(R.drawable.pink_peg));
			value++;
		}
		if (!prefs.getBoolean("lime_enabled", true)) {
			findViewById(R.id.picker_lime_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_lime_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_lime_peg), value);
			drawables.put(value, getResources()
					.getDrawable(R.drawable.lime_peg));
			value++;
		}
		if (!prefs.getBoolean("orange_enabled", true)) {
			findViewById(R.id.picker_orange_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_orange_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_orange_peg), value);
			drawables.put(value,
					getResources().getDrawable(R.drawable.orange_peg));
			value++;
		}
		if (!prefs.getBoolean("white_enabled", true)) {
			findViewById(R.id.picker_white_peg).setVisibility(View.GONE);
		} else {
			findViewById(R.id.picker_white_peg).setVisibility(View.VISIBLE);
			colors.put((ImageView) findViewById(R.id.picker_white_peg), value);
			drawables.put(value,
					getResources().getDrawable(R.drawable.white_peg));
			value++;
		}

		// Set on touch listeners
		LinearLayout pegPicker = (LinearLayout) findViewById(R.id.peg_picker);
		for (int i = 0; i < pegPicker.getChildCount(); i++) {
			ImageView peg = (ImageView) pegPicker.getChildAt(i);
			peg.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					ImageView peg = (ImageView) v;
					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
							LinearLayout parent = (LinearLayout) v.getParent();
							if (pickerSelected[parent.indexOfChild(v)]) {
								peg.setImageDrawable(selectedPickers.get(peg));
							} else {
								peg.setImageDrawable(pickers.get(peg));
							}
						} else {
							// landscape
							LinearLayout parent = (LinearLayout) v.getParent();
							if (pickerSelected[parent.indexOfChild(v)]) {
								peg.setImageDrawable(selectedPickersLand
										.get(peg));
							} else {
								peg.setImageDrawable(pickersLand.get(peg));
							}
						}
					} else {
						if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
							LinearLayout parent = (LinearLayout) v.getParent();
							if (pickerSelected[parent.indexOfChild(v)]) {
								peg.setImageDrawable(pickers.get(peg));
							} else {
								peg.setImageDrawable(selectedPickers.get(peg));
							}
						} else {
							// landscape
							LinearLayout parent = (LinearLayout) v.getParent();
							if (pickerSelected[parent.indexOfChild(v)]) {
								peg.setImageDrawable(pickersLand.get(peg));
							} else {
								peg.setImageDrawable(selectedPickersLand
										.get(peg));
							}
						}
					}
					return false;
				}
			});
			
			peg.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					ImageView peg = (ImageView) v;
					LinearLayout parent = (LinearLayout) v.getParent();
					int pos = parent.indexOfChild(v);
					pickerSelected[pos] = !pickerSelected[pos];
					if (pickerSelected[pos]) {
						if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
							pickers.get(peg);
						} else {
							pickersLand.get(peg);
						}
					} else {
						if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
							selectedPickers.get(peg);
						} else {
							selectedPickersLand.get(peg);
						}
					}
					final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
					vibrator.vibrate(10);
					return true;
				}
			});
		}
		return (int) value;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public void onRestart() {
		super.onRestart();
		if (startNewGame) {
			newGame();
			startNewGame = false;
		}
	}

	/**
	 * Create a new game.
	 */
	private void newGame() {
		// Setup the peg picker and initialize the internal color representation
		int numberOfColors = setupPegPicker();
		// Setup the board according to user settings
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		int numberOfPegs = prefs.getInt("number_of_pegs", 4);
		int numberOfGuesses = prefs.getInt("number_of_guesses", 12);
		board = new Board(numberOfPegs, numberOfColors, numberOfGuesses);
		// Initialize variables
		currentPegRow = new int[board.getNumberOfPegs()];
		pegsSet = 0;
		pegRows = new ArrayList<LinearLayout>();
		pegHoles = new ArrayList<ImageView[]>();
		// Clear layout
		LinearLayout guesses = (LinearLayout) findViewById(R.id.guesses);
		guesses.removeAllViews();
		// Create the first pegRow
		addBoardGuess();
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.menu_settings) {
			// Open preferences
			startNewGame = true;
			Intent intent = new Intent(MainActivity.this,
					SettingsActivity.class);
			startActivity(intent);
		}
		return true;
	}

	/**
	 * Get the first occurrence of an empty peg in the current peg row after the
	 * position specified or the first empty peg in the row if all pegs are set
	 * after this position.
	 * 
	 * @return The index of the first empty peg as specified or the length of
	 *         the row if all pegs are set.
	 */
	private int indexOfEmptyPeg(int position) {
		for (int i = position + 1; i < board.getNumberOfPegs(); i++) {
			if (currentPegRow[i] == -1) {
				return i;
			}
		}
		for (int i = 0; i < position + 1; i++) {
			if (currentPegRow[i] == -1) {
				return i;
			}
		}
		return board.getNumberOfPegs();
	}

	/**
	 * Set the selected peg to the peg pressed by the user. This method will
	 * automatically complete the row if the last peg in the guess was set and
	 * this option is enabled in Preferences by the user.
	 * 
	 * @param v
	 *            The peg (ImageView) pressed.
	 */
	public void setPeg(View v) {
		if (!board.isLocked()) {
			scrollToBottom();
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(this);
			if (!prefs.getBoolean("autocomplete", true)) {
				// Autocomplete disabled
				if (pegsSet == board.getNumberOfPegs()) {
					// Guess complete, add the guess to the board and add a hint
					int[] keyPegs = board.makeGuess(currentPegRow.clone());
					addBoardHint(keyPegs);
					if (!board.isLocked()) {
						// add a new row
						addBoardGuess();
						pegsSet = 0;
						// select the first peg in the next row
						ImageView[] latestPegs = pegHoles
								.get(pegHoles.size() - 1);
						Drawable[] layers = new Drawable[2];
						layers[0] = getResources().getDrawable(
								R.drawable.empty_peg);
						layers[1] = getResources()
								.getDrawable(R.drawable.check);
						LayerDrawable layerDrawable = new LayerDrawable(layers);
						latestPegs[pegsSet].setImageDrawable(layerDrawable);
					}
				} else {
					// There is at least one peg left
					ImageView[] latestPegs = pegHoles.get(pegHoles.size() - 1);
					latestPegs[pegsSet]
							.setImageResource(getResourceIdFromView(v));
					currentPegRow[pegsSet] = getColorId(v);

					// Get the next peg to select
					pegsSet = indexOfEmptyPeg(pegsSet);

					if (pegsSet < board.getNumberOfPegs()) {
						// select the next peg
						Drawable[] layers = new Drawable[2];
						layers[0] = drawables.get(currentPegRow[pegsSet]);
						layers[1] = getResources()
								.getDrawable(R.drawable.check);
						LayerDrawable layerDrawable = new LayerDrawable(layers);
						latestPegs[pegsSet].setImageDrawable(layerDrawable);
					}
				}
			} else {
				// Autocomplete enabled, set the current peg to the color chosen
				// by the user
				ImageView[] latestPegs = pegHoles.get(pegHoles.size() - 1);
				latestPegs[pegsSet].setImageResource(getResourceIdFromView(v));
				currentPegRow[pegsSet] = getColorId(v);
				// Get the next peg to select
				pegsSet = indexOfEmptyPeg(pegsSet);
				if (pegsSet == board.getNumberOfPegs()) {
					// Guess complete
					int[] store = currentPegRow.clone();
					int[] keyPegs = board.makeGuess(store);
					pegsSet = 0;
					addBoardHint(keyPegs);
					// If the game is NOT over, give the player another
					// chance
					if (!board.isLocked()) {
						addBoardGuess();
					}
				} else {
					// There is (at least one) peg left, select this one
					Drawable[] layers = new Drawable[2];
					layers[0] = drawables.get(currentPegRow[pegsSet]);
					layers[1] = getResources().getDrawable(R.drawable.check);
					LayerDrawable layerDrawable = new LayerDrawable(layers);
					latestPegs[pegsSet].setImageDrawable(layerDrawable);
				}
			}
		}
	}

	/**
	 * Get the color id (an integer representing a peg in the board class) from
	 * a given ImageView.
	 * 
	 * @param v
	 *            A ImageView selected by the user in the pegPicker.
	 * @return The color id.
	 */
	private int getColorId(View v) {
		return colors.get(v);
	}

	/**
	 * Add a LinearLayout to screen containing a set of key pegs.
	 * 
	 * @param int[] The key pegs to inflate.
	 */
	public void addBoardHint(int[] keys) {
		// Add some key pegs to the last guess made using the keys provided
		LinearLayout latestGuess = pegRows.get(pegRows.size() - 1);
		// Get the linearLayout inside latestGuess where the key pegs should be
		// added
		LinearLayout keyPegHolder = (LinearLayout) latestGuess
				.findViewById(R.id.key_peg_holder);
		keyPegHolder.removeAllViews();
		for (int i = 0; i < keys[0]; i++) {
			ImageView iv = new ImageView(this);
			iv.setImageResource(R.drawable.black_keypeg);
			keyPegHolder.addView(iv);
		}
		for (int i = 0; i < keys[1]; i++) {
			ImageView iv = new ImageView(this);
			iv.setImageResource(R.drawable.red_keypeg);
			keyPegHolder.addView(iv);
		}
		for (int i = 0; i < board.getNumberOfPegs() - keys[0] - keys[1]; i++) {
			ImageView iv = new ImageView(this);
			iv.setImageResource(R.drawable.empty_keypeg);
			keyPegHolder.addView(iv);
		}
	}

	/**
	 * Add a LinearLayout to screen containing the a set of empty pegs specified
	 * by the current board class. The pegs are stored in an array of
	 * ImageViews.
	 */
	public void addBoardGuess() {
		/* LAYOUT
		 * ________________________________________________
		 * |                    |  |                  |    |
		 * | code pegs          |  | key pegs         |  1 |  
		 * |____________________|__|__________________|____|    
		 */
		// Reset the current peg row
		currentPegRow = new int[board.getNumberOfPegs()];
		for (int i = 0; i < currentPegRow.length; i++) {
			currentPegRow[i] = -1;
		}
		// Inflate the frame
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		LinearLayout guessHolder = (LinearLayout) inflater.inflate(
				R.layout.guess_holder, null);
		// Add some peg holes
		LinearLayout codePegHolder = (LinearLayout) guessHolder
				.findViewById(R.id.code_peg_holder);
		LinearLayout keyPegHolder = (LinearLayout) guessHolder
				.findViewById(R.id.key_peg_holder);
		ImageView[] pegs = new ImageView[board.getNumberOfPegs()];
		pegs[0] = (ImageView) inflater.inflate(R.layout.peg_hole, null);
		Drawable[] layers = new Drawable[2];
		layers[0] = getResources().getDrawable(R.drawable.empty_peg);
		layers[1] = getResources().getDrawable(R.drawable.check);
		LayerDrawable layerDrawable = new LayerDrawable(layers);
		pegs[0].setImageDrawable(layerDrawable);
		pegs[0].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectPeg(v);
			}

		});
		codePegHolder.addView(pegs[0]);
		for (int i = 1; i < board.getNumberOfPegs(); i++) {
			pegs[i] = (ImageView) inflater.inflate(R.layout.peg_hole, null);
			pegs[i].setImageResource(R.drawable.empty_peg);
			pegs[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					selectPeg(v);
				}
			});
			codePegHolder.addView(pegs[i]);
		}
		for (int i = 0; i < board.getNumberOfPegs(); i++) {
			ImageView iv = (ImageView) inflater.inflate(R.layout.peg_hole, null);
			iv.setImageResource(R.drawable.empty_keypeg);
			keyPegHolder.addView(iv);
		}
		// Merge linear layouts
		LinearLayout guesses = (LinearLayout) findViewById(R.id.guesses);
		guesses.addView(guessHolder);
		// Save linear layouts and peg holes
		pegRows.add(guessHolder);
		pegHoles.add(pegs);
		
		// Display counter
		TextView guessCount = (TextView) guessHolder.findViewById(R.id.guess_count);
		LinearLayout guessCountHolder = (LinearLayout) guessHolder.findViewById(R.id.guess_count_holder);
		guessCount.setText("" + guesses.getChildCount());
		Random r = new Random();
		guessCountHolder.setBackgroundColor(r.nextInt());
		
		// Scroll to bottom
		scrollToBottom();
	}

	/**
	 * Scroll container scroll_view_holder to the bottom.
	 */
	private void scrollToBottom() {
		final ScrollView scroll = (ScrollView) findViewById(R.id.guesses_scroll_view);
		scroll.post(new Runnable() {
			@Override
			public void run() {
				scroll.fullScroll(ScrollView.FOCUS_DOWN);
			}
		});
	}

	/**
	 * Get the resource id of the peg (drawable) that matches the peg selected
	 * in the peg picker.
	 * 
	 * @param v
	 *            The peg (ImageView) from the peg picker selected by the user.
	 * @return The id of the corresponding peg or -1 if no match could be made.
	 */
	private int getResourceIdFromView(View v) {
		int id = v.getId();
		if (id == R.id.picker_black_peg) {
			return R.drawable.black_peg;
		}
		if (id == R.id.picker_blue_peg) {
			return R.drawable.blue_peg;
		}
		if (id == R.id.picker_brown_peg) {
			return R.drawable.brown_peg;
		}
		if (id == R.id.picker_green_peg) {
			return R.drawable.green_peg;
		}
		if (id == R.id.picker_lilac_peg) {
			return R.drawable.lilac_peg;
		}
		if (id == R.id.picker_red_peg) {
			return R.drawable.red_peg;
		}
		if (id == R.id.picker_yellow_peg) {
			return R.drawable.yellow_peg;
		}
		if (id == R.id.picker_pink_peg) {
			return R.drawable.pink_peg;
		}
		if (id == R.id.picker_white_peg) {
			return R.drawable.white_peg;
		}
		if (id == R.id.picker_orange_peg) {
			return R.drawable.orange_peg;
		}
		if (id == R.id.picker_lime_peg) {
			return R.drawable.lime_peg;
		}
		return -1;
	}

	/**
	 * Select a peg clicked by the user. If the peg is not in the last peg row,
	 * nothing will happen.
	 * 
	 * @param v
	 *            The view clicked.
	 */
	private void selectPeg(View v) {
		// Select the correct peg in currentPegRow
		LinearLayout codePegHolder = (LinearLayout) v.getParent();
		LinearLayout guessHolder = (LinearLayout) codePegHolder.getParent();
		LinearLayout guesses = (LinearLayout) guessHolder.getParent();
		if (guesses.indexOfChild(guessHolder) == pegRows.size() - 1) {
			// The last row has been clicked, deselect the old peg (if any)
			if (pegsSet < board.getNumberOfPegs()) {
				int c = currentPegRow[pegsSet];
				ImageView oldPeg = (ImageView) codePegHolder
						.getChildAt(pegsSet);
				oldPeg.setImageDrawable(drawables.get(c));
			}
			// Select new peg
			Drawable[] layers = new Drawable[2];
			int c = currentPegRow[codePegHolder.indexOfChild(v)];
			layers[0] = drawables.get(c);
			layers[1] = getResources().getDrawable(R.drawable.check);
			LayerDrawable layerDrawable = new LayerDrawable(layers);
			ImageView peg = (ImageView) v;
			peg.setImageDrawable(layerDrawable);
			// Select the right element in currentPegRow
			pegsSet = codePegHolder.indexOfChild(v);
		}
	}
}