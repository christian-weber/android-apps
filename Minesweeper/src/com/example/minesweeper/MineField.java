package com.example.minesweeper;

import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import com.example.minesweeper.activity.RankingActivity;
import com.example.minesweeper.listener.AreaClickListener;
import com.example.minesweeper.listener.AreaLongClickListener;
import com.example.minesweeper.preferences.MenuAware;
import com.example.minesweeper.preferences.MineMarkerSharedPreferencesChangedListener;

public class MineField extends Activity implements MenuAware {

	public static final String STORE_NAME = "Minesweeper";
	public static final String NUMBER_OF_TOTAL_MINES = "numberOfTotalMines";
	public static final String NUMBER_OF_MARKED_MINES = "numberOfMarkedMines";
	public static final String TIME_STARTED = "timeStarted";

	private int columnCount = 5;
	private int rowCount = 8;
	private MineArea mineAreas[][];
	private TableLayout mineField;
	private int numberOfTotalMines = 8;
	
	// hold the menu to get access to the menu item
	private Menu menu;

	// SharedPreferences keeps listeners in a WeakHashMap
	// hence listener may be recycled if the code does not hold a reference to
	// it.
	private OnSharedPreferenceChangeListener listener;

	public int getColumnCount() {
		return columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public MineArea[][] getMineAreas() {
		return mineAreas;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine_field);

		mineField = (TableLayout) findViewById(R.id.Mine_Field);
		createMineGrid();
		placeMines();
		displayMineGrid();
		storePreferences();
	}

	/**
	 * Persists the total number of mines as well as the number of marked mines
	 * preferences.
	 */
	private void storePreferences() {
		SharedPreferences sp = getSharedPreferences(STORE_NAME, MODE_PRIVATE);

		Editor editor = sp.edit();
		editor.putInt(NUMBER_OF_TOTAL_MINES, numberOfTotalMines);
		editor.putInt(NUMBER_OF_MARKED_MINES, 0);
		editor.putLong(TIME_STARTED, System.currentTimeMillis());
		editor.apply();

		listener = new MineMarkerSharedPreferencesChangedListener(this);
		sp.registerOnSharedPreferenceChangeListener(listener);
	}

	public void newGame() {
		storePreferences();
		rebuildMineGrid();
		placeMines();
		mineField.setBackgroundResource(R.layout.cellshape);
	}

	/**
	 * Creates the mine grid. Registers click listeners for a simple as well as
	 * a long click.
	 */
	private void createMineGrid() {
		mineAreas = new MineArea[rowCount][columnCount];

		// for each row
		for (int row = 0; row < rowCount; ++row) {
			// for each column
			for (int col = 0; col < columnCount; ++col) {
				mineAreas[row][col] = new MineArea(this);

				// register an area click listener
				AreaClickListener listener1 = null;
				listener1 = new AreaClickListener(this, row, col);
				mineAreas[row][col].setOnClickListener(listener1);

				// register an area long click listener
				AreaLongClickListener listener2 = null;
				listener2 = new AreaLongClickListener(this, row, col);
				mineAreas[row][col].setOnLongClickListener(listener2);
			}
		}
	}

	/**
	 * Rebuilds the mine grid by clearing all mine areas.
	 */
	private void rebuildMineGrid() {
		for (int rowIndex = 0; rowIndex < rowCount; ++rowIndex) {
			for (int columnIndex = 0; columnIndex < columnCount; ++columnIndex) {
				mineAreas[rowIndex][columnIndex].clear();
			}
		}
	}

	private void placeMines() {
		Random rand = new Random();

		int row = 0;
		int col = 0;

		// for each mine
		for (int i = 0; i < numberOfTotalMines; ++i) {

			// find a mine area where to place the mine
			while ((row == 0 && col == 0) || mineAreas[row][col].isMined()) {
				row = rand.nextInt(rowCount);
				col = rand.nextInt(columnCount);
			}

			// place the mine
			mineAreas[row][col].placeMine();
		}

		// for each row
		for (row = 0; row < rowCount; ++row) {
			// for each column
			for (col = 0; col < columnCount; ++col) {
				calculateMineCount(row, col);
			}
		}
	}

	private void calculateMineCount(int currentRow, int currentCol) {
		int mineCount = 0;
		for (int rowIndex = currentRow - 1; rowIndex <= currentRow + 1; ++rowIndex) {
			if (rowIndex < 0 || rowIndex >= rowCount) {
				continue;
			}

			for (int columnIndex = currentCol - 1; columnIndex <= currentCol + 1; ++columnIndex) {
				if (columnIndex < 0 || columnIndex >= columnCount) {
					continue;
				}

				if (columnIndex == currentCol && rowIndex == currentRow) {
					continue;
				}

				if (mineAreas[rowIndex][columnIndex].isMined() == true) {
					++mineCount;
				}
			}
		}
		mineAreas[currentRow][currentCol].setCountOfSurroundingMines(mineCount);
	}

	private void displayMineGrid() {
		for (int row = 0; row < rowCount; row++) {
			TableRow tableRow = new TableRow(this);

			for (int column = 0; column < columnCount; column++) {
				tableRow.addView(mineAreas[row][column]);
			}
			mineField.addView(tableRow);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;

		getMenuInflater().inflate(R.menu.mine_field, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_ranking:
			Intent intent = new Intent(this, RankingActivity.class);
			startActivity(intent);
			return true;
		case R.id.action_newgame:
			showConfirmDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Shows the confirm dialog. The player can choose between the option
	 * to start a new game and to cancel.
	 */
	private void showConfirmDialog() {
		final Context context = getApplicationContext();

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(R.string.start_new_game);

		// Yes button
		builder.setPositiveButton(android.R.string.yes,

		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				newGame();

				int msg = R.string.new_game_started;
				Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
			}
		});

		// No button
		builder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});

		builder.create().show();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Menu getMenu() {
		return menu;
	}

}
