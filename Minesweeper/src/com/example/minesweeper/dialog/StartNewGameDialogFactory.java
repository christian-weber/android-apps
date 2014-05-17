package com.example.minesweeper.dialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import com.example.minesweeper.MineField;
import com.example.minesweeper.R;

public class StartNewGameDialogFactory {

	public static AlertDialog getDialog(final MineField mineField) {

		AlertDialog.Builder builder = new AlertDialog.Builder(mineField);
		builder.setMessage(R.string.start_new_game);

		// Yes button
		builder.setPositiveButton(android.R.string.yes,

		new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				mineField.newGame();

				int msg = R.string.new_game_started;
				Toast.makeText(mineField, msg, Toast.LENGTH_SHORT).show();
			}
		});

		// No button
		builder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}
				});

		return builder.create();
	}

}
