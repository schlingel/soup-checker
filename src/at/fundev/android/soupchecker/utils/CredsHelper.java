package at.fundev.android.soupchecker.utils;

import android.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

/**
 * Helper class. Abstraction for fetching and storing credentials.
 */
public final class CredsHelper {
	private static final String SOUP_DEF_URL = "http://www.soup.io/";
	
	private static String soupUrl = "";
	
	/**
	 * The activity context. Needed for fetching the preferences.
	 */
	private Context cont;
	
	/**
	 * Preferences object for fetching/storing credentials.
	 */
	private SharedPreferences prefs;
	
	/**
	 * Initializes the creds helper with the given context.
	 * @param cont Activity context
	 */
	public CredsHelper(Context cont) {
		this.cont = cont;
		prefs = PreferenceManager.getDefaultSharedPreferences(cont);
		fetchUrlFromPreferences();
	}
	
	/**
	 * Fetches the credentials from the preferences and writes it to the member variable.
	 */
	private void fetchUrlFromPreferences() {
		synchronized(soupUrl) {
			soupUrl = prefs.getString("credRSSUrl", SOUP_DEF_URL);
		}
	}
	
	/**
	 * Returns the soup url.
	 */
	public String getURL() {
		synchronized(soupUrl) {
			return soupUrl;
		}
	}
	
	/**
	 * Writes the given url to the preferences.
	 */
	public void setUrl(String url) {
		synchronized(soupUrl) {
			soupUrl = url;
			
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString("credRSSUrl", url);
			editor.commit();
		}
	}
	
	/**
	 * If true a non default value is set.
	 * @return True if user set the URL, false otherwise
	 */
	public boolean isSet() {
		String value = getURL();
		return value != null && !SOUP_DEF_URL.equals(value);
	}
}
