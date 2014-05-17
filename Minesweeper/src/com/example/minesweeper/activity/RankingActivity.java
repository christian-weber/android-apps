package com.example.minesweeper.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.minesweeper.MineField;
import com.example.minesweeper.R;
import com.example.minesweeper.dialog.DeleteRankingDialogFactory;
import com.example.minesweeper.storage.DictionaryOpenHelper;

public class RankingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);

		TableLayout layout = (TableLayout) findViewById(R.id.container);
		layout.setColumnShrinkable(0, true);
		layout.setColumnStretchable(1, true);
		layout.setColumnShrinkable(2, true);

		TableRow header = new TableRow(getApplicationContext());
		header.setGravity(Gravity.CENTER);

		header.addView(left(bold(text("Rang/"))));
		header.addView(greenFg(bold(left(text("Name")))));
		header.addView(greenBg(bold(center(text("Zeit")))));

		layout.addView(header);

		DictionaryOpenHelper dictionary = null;
		dictionary = new DictionaryOpenHelper(getApplicationContext());
		SQLiteDatabase db = dictionary.getReadableDatabase();

		// TODO: create a repository class
		// and a Ranking bean class
		Cursor cursor = null;
		cursor = db.query("RANKING", null, null, null, null, null, "TIME");

		if (!cursor.moveToFirst()) {
			return;
		}

		int i = 1;
		boolean hasNext = true;
		while (hasNext) {

			String name = cursor.getString(cursor.getColumnIndex("NAME"));
			long time = cursor.getLong(cursor.getColumnIndex("TIME"));

			TableRow row = new TableRow(getApplicationContext());
			row.setGravity(Gravity.CENTER);

			row.addView(greenBg(bold(center(text(i++ + "")))));
			row.addView(greenFg(left(text(name))));
			row.addView(right(bold(text(String.valueOf(time)))));

			layout.addView(row);

			hasNext = cursor.moveToNext();
		}
		cursor.close();
	}

	private TextView right(TextView textView) {
		textView.setGravity(Gravity.RIGHT);
		return textView;
	}

	private TextView center(TextView textView) {
		textView.setGravity(Gravity.CENTER);
		return textView;
	}

	private TextView left(TextView textView) {
		textView.setGravity(Gravity.LEFT);
		return textView;
	}

	private TextView bold(TextView textView) {
		textView.setTypeface(Typeface.DEFAULT_BOLD);
		return textView;
	}

	private TextView greenBg(TextView textView) {
		textView.setBackgroundColor(Color.rgb(0, 244, 0));
		return textView;
	}

	private TextView greenFg(TextView textView) {
		textView.setTextColor(Color.rgb(0, 244, 0));
		return textView;
	}

	private TextView text(String text) {
		int style = android.R.style.TextAppearance_Large;

		TextView textView = new TextView(getApplicationContext());
		textView.setText(text);
		textView.setMinimumHeight(40);
		textView.setTextAppearance(getApplicationContext(), style);

		return textView;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ranking, menu);
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_ranking:
			return true;
		case R.id.action_newgame:
			Intent intent = new Intent(getApplicationContext(), MineField.class);
			startActivity(intent);
			return true;
		case R.id.action_clearRanking:
			DeleteRankingDialogFactory.getDialog(this).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
