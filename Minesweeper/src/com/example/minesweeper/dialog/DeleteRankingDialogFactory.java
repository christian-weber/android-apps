package com.example.minesweeper.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.app.Activity;

import com.example.minesweeper.R;
import com.example.minesweeper.storage.DictionaryOpenHelper;

public class DeleteRankingDialogFactory {

	public static AlertDialog getDialog(final Context context) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.clearRanking);

		builder.setPositiveButton(android.R.string.yes,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						DictionaryOpenHelper dictionary = null;
						dictionary = new DictionaryOpenHelper(context);

						SQLiteDatabase database = null;
						database = dictionary.getWritableDatabase();

						database.delete("RANKING", null, null);
						((Activity) context).recreate();
					}

				});
		
		builder.setNegativeButton(android.R.string.no,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// do nothing
					}

				});

		return builder.create();
	}

}
