package com.example.minesweeper.preferences;

import static com.example.minesweeper.preferences.Preferences.NUMBER_OF_MARKED_MINES;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.Menu;
import android.view.MenuItem;

import com.example.minesweeper.R;

public class MineMarkerSharedPreferencesChangedListener implements OnSharedPreferenceChangeListener {

	private final MenuAware menuAware;
	
	public MineMarkerSharedPreferencesChangedListener(MenuAware menuAware) {
		this.menuAware = menuAware;
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs,
			String key) {

		int nrOfMarkedMines = prefs.getInt(NUMBER_OF_MARKED_MINES, 0);

		Menu menu = menuAware.getMenu();
		MenuItem item = menu.findItem(R.id.action_markedMines);
		item.setTitle(String.valueOf(nrOfMarkedMines));
	}

}
