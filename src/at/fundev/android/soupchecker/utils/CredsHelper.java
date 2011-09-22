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
	
	private static final String CREDS_URL_NAME = "credRSSUrl";
	
	private static final String CREDS_FETCH_IMAGES = "credFetchImages";
	
	private static final boolean DEF_FETCH_IMAGES_VALUE = true;
	
	private static String soupUrl = "";
	
	private static Boolean fetchImages = null;
	
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
		fetchPreferences();
	}
	
	/**
	 * Fetches the credentials from the preferences and writes it to the member variable.
	 */
	private void fetchPreferences() {
			soupUrl = prefs.getString(CREDS_URL_NAME, SOUP_DEF_URL);
			fetchImages = prefs.getBoolean(CREDS_FETCH_IMAGES, DEF_FETCH_IMAGES_VALUE);
	}
	
	/**
	 * Returns the soup url.
	 */
	public String getURL() {
			return soupUrl;
	}
	
	/**
	 * Writes the given url to the preferences.
	 */
	public void setUrl(String url) {
			soupUrl = url;
			
			SharedPreferences.Editor editor = prefs.edit();
			editor.putString(CREDS_URL_NAME, url);
			editor.commit();
	}
	
	/**
	 * If true a non default value is set.
	 * @return True if user set the URL, false otherwise
	 */
	public boolean isSoupUrlSet() {
		String value = getURL();
		return value != null && !SOUP_DEF_URL.equals(value);
	}
	
	/**
	 * Sets the fetch images value. Needed to determine wether the soup user images should 
	 * be loaded or not.
	 * @param fetchImages
	 */
	public void setFetchImagesActive(boolean fetchImages) {
		this.fetchImages = fetchImages;
		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(CREDS_FETCH_IMAGES, fetchImages);
		editor.commit();
	}
	
	/**
	 * Returns a boolean value wether the user images should be downloaded from soup or not.
	 * @return
	 */
	public boolean isFetchImagesActive() {
		return fetchImages;
	}
}
