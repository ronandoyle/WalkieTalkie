package doyle.ronan.walkietalkie.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import doyle.ronan.walkietalkie.MainActivity;
import doyle.ronan.walkietalkie.R;

/**
 * The Preferences class is used to set the IP address an port of the Fit-PC3 to be connected to.
 * @author Ronan Doyle
 *
 */
public class Preferences extends PreferenceActivity {

	private static final String TAG = "walkietalkie.prefs";
	public static final String IP_ADDRESS = "ipAddress";
	public static final String PORT = "8080";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
		PreferenceManager.setDefaultValues(Preferences.this, R.xml.preferences, false);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.setClass(Preferences.this, MainActivity.class);
		startActivity(intent);
		finish();
		return;
	}


}