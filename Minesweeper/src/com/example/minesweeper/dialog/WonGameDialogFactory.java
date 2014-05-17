package com.example.minesweeper.dialog;

import static android.R.style.TextAppearance_Large;
import static com.example.minesweeper.preferences.Preferences.STORE_NAME;
import static com.example.minesweeper.preferences.Preferences.TIME_STARTED;

import java.util.Date;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.minesweeper.R;
import com.example.minesweeper.activity.RankingActivity;
import com.example.minesweeper.storage.DictionaryOpenHelper;

public class WonGameDialogFactory {

	public static AlertDialog getDialog(final Context context) {

		SharedPreferences sp = null;
		sp = context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE);
		
		long started = sp.getLong(TIME_STARTED, -1);
		final long diff = (new Date().getTime() - started);

		AlertDialog.Builder builder = new AlertDialog.Builder(context);

		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);

		TextView msg1 = new TextView(context);
		msg1.setText(R.string.won_game);
		msg1.setTextAppearance(context, TextAppearance_Large);
		msg1.setTypeface(Typeface.DEFAULT_BOLD);

		TextView msg2 = new TextView(context);
		msg2.setText(R.string.insert_name);
		msg1.setTextAppearance(context, TextAppearance_Large);

		final EditText editText = new EditText(context);
		editText.setEms(10);
		editText.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		editText.requestFocus();

		layout.addView(msg1);
		layout.addView(msg2);
		layout.addView(editText);

		builder.setView(layout);

		builder.setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int id) {

						DictionaryOpenHelper dictionary = null;
						dictionary = new DictionaryOpenHelper(context);

						SQLiteDatabase database = null;
						database = dictionary.getWritableDatabase();

						try {
							database.beginTransaction();

							ContentValues cv = new ContentValues();
							cv.put("NAME", editText.getText().toString());
							cv.put("TIME", diff);

							database.insert("RANKING", null, cv);
							database.setTransactionSuccessful();

						} finally {
							database.endTransaction();
							database.close();
						}

						Intent intent = null;
						intent = new Intent(context, RankingActivity.class);
						context.startActivity(intent);
					}

				});

		return builder.create();
	}

}
