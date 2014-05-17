package com.example.minesweeper.listener;

import static android.content.Context.MODE_PRIVATE;
import static com.example.minesweeper.preferences.Preferences.NUMBER_OF_MARKED_MINES;
import static com.example.minesweeper.preferences.Preferences.STORE_NAME;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.example.minesweeper.MineArea;
import com.example.minesweeper.MineField;

/**
 * {@link OnLongClickListener} implementation used to mark mine areas.
 */
public class AreaLongClickListener implements OnLongClickListener {

	private final MineField mineField;
	private final int rowIndex;
	private final int colIndex;

	public AreaLongClickListener(MineField mineField, int rowIndex, int colIndex) {
		this.mineField = mineField;
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onLongClick(View v) {
		MineArea[][] mineAreas = mineField.getMineAreas();

		String name = STORE_NAME;

		SharedPreferences settings = null;
		settings = v.getContext().getSharedPreferences(name, MODE_PRIVATE);
		int nrOfMarkedMines = settings.getInt(NUMBER_OF_MARKED_MINES, 0);

		if (mineAreas[rowIndex][colIndex].isFree()) {
			return true;
		}

		if (mineAreas[rowIndex][colIndex].isMarked()) {
			mineAreas[rowIndex][colIndex].removeMark();
			--nrOfMarkedMines;
		}

		else {
			mineAreas[rowIndex][colIndex].mark();
			++nrOfMarkedMines;
		}

		nrOfMarkedMines = Math.max(nrOfMarkedMines, 0);

		Editor editor = settings.edit();
		editor.putInt(NUMBER_OF_MARKED_MINES, nrOfMarkedMines);
		editor.apply();

		return true;
	}
}