package com.example.minesweeper.activity;

import android.app.Activity;
import android.content.Intent;
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

		for (int i = 1; i < 10; i++) {
			TableRow row = new TableRow(getApplicationContext());
			row.setGravity(Gravity.CENTER);

			row.addView(greenBg(bold(center(text(i + "")))));
			row.addView(greenFg(left(text("Name .........................................................."))));
			row.addView(right(bold(text("Zeit"))));

			layout.addView(row);
		}
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
		TextView textView = new TextView(getApplicationContext());
		textView.setText(text);
		textView.setMinimumHeight(40);
		textView.setTextAppearance(getApplicationContext(),
				android.R.style.TextAppearance_Large);

		return textView;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ranking, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()) {
		case R.id.action_ranking:
			return true;
		case R.id.action_newgame:
			Intent intent = new Intent(getApplicationContext(), MineField.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
