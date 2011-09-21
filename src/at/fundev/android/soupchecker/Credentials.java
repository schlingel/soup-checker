package at.fundev.android.soupchecker;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * <p>Very basic preference activity. Holds only the URL for the soup RSS feed.</p>
 */
public class Credentials extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.credentials);
		
		if(!isUrlSet()) {
			applyDefaultValue();
		}
	}
	
	/**
	 * Adds the default URL value to the credentials.
	 */
	private void applyDefaultValue() {
		
	}
	
	/**
	 * <p>Checks wether the URL is set or not.</p>
	 * <p>A URL counts as checked if it's not null, empty or is a longer URL than "http://www.soup.io/"</p
	 * @return true if the url is set, false otherwise
	 */
	private boolean isUrlSet() {
		return false;
	}
}
