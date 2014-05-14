package com.example.minesweeper.listener;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.minesweeper.MineArea;
import com.example.minesweeper.MineField;

public class AreaClickListener extends Activity implements OnClickListener {

	private final MineField mineField;
	private final int rowIndex;
	private final int colIndex;

	public AreaClickListener(MineField mineField, int rowIndex, int colIndex) {
		this.mineField = mineField;
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}

	public MineArea[][] getMineAreas() {
		return mineField.getMineAreas();
	}
	
	@Override
	public void onClick(View v) {
		MineArea mineArea = getMineAreas()[rowIndex][colIndex];

		if (mineArea.isFree() || mineArea.isMarked()) {
			return;
		}

		if (mineArea.uncover()) {
			if (mineArea.getCountSurroundingMines() == 0) {
				uncoverSurrounding(rowIndex, colIndex);
			}
		} else {
			// TODO: End game
		}
	}
	
	public void uncoverSurrounding(int currentRowIndex, int currentColIndex) {
		MineArea[][]mineAreas = getMineAreas();
		int rowCount = mineField.getRowCount();
		int colCount = mineField.getColumnCount();
		
		for (int rowIndex = currentRowIndex - 1; rowIndex <= currentRowIndex + 1; ++rowIndex) {
			if (rowIndex < 0 || rowIndex >= rowCount) {
				continue;
			}

			for (int colIndex = currentColIndex - 1; colIndex <= currentColIndex + 1; ++colIndex) {
				if (colIndex < 0 || colIndex >= colCount) {
					continue;
				}

				if (mineAreas[rowIndex][colIndex].isFree() == true
						|| mineAreas[rowIndex][colIndex].isMined() == true
						|| mineAreas[rowIndex][colIndex].isMarked() == true) {
					continue;
				}

				mineAreas[rowIndex][colIndex].uncover();

				if (mineAreas[rowIndex][colIndex].getCountSurroundingMines() == 0) {
					uncoverSurrounding(rowIndex, colIndex);
				}
			}
		}
	}
	
}