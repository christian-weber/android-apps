package com.example.minesweeper.listener;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.example.minesweeper.MineArea;
import com.example.minesweeper.MineField;

public class AreaLongClickListener implements
		OnLongClickListener {

	private final MineField mineField;
	private final int rowIndex;
	private final int colIndex;

	public AreaLongClickListener(MineField mineField, int rowIndex, int colIndex) {
		this.mineField = mineField;
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}

	@Override
	public boolean onLongClick(View v) {
		MineArea[][]mineAreas = mineField.getMineAreas();
		
		String name = MineField.STORE_NAME;
		
		SharedPreferences settings = v.getContext().getSharedPreferences(name, Context.MODE_PRIVATE);
		int nrOfMarkedMines = settings.getInt(MineField.NUMBER_OF_MARKED_MINES, 0);
		
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
		editor.putInt(MineField.NUMBER_OF_MARKED_MINES, nrOfMarkedMines);
		editor.apply();
		
		return true;
	}
}