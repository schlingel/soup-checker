package at.fundev.android.soupchecker;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import at.fundev.android.soupchecker.models.Channel;
import at.fundev.android.soupchecker.models.Item;
import at.fundev.android.soupchecker.models.RssRoot;
import at.fundev.android.soupchecker.ui.NotificationsAdapter;
import at.fundev.android.soupchecker.utils.CredsHelper;
import at.fundev.android.soupchecker.utils.SoupNotificationParser;

// TODO: Auto-generated Javadoc
/**
 * The Class Main.
 */
public class Main extends Activity implements OnItemClickListener {
	public static final int SETTINGS_REQUEST_ID = 93649;
	
	/**
	 * The Class NotificationFetcherTask.
	 */
	private class NotificationFetcherTask extends
			AsyncTask<String, Void, RssRoot> {

		/**
		 * the progress dialog which gets displayed while downloading the items.
		 */
		private ProgressDialog dlgDownloadProgress;

		public NotificationFetcherTask() {
			super();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			dlgDownloadProgress = ProgressDialog.show(Main.this, "",
					"Downloading notifications");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected RssRoot doInBackground(String... url) {
			if (url.length == 0) {
				throw new IllegalArgumentException("URL must be provided!");
			}

			try {
				Log.d(NotificationFetcherTask.class.getName(), "Fetching "
						+ url[0]);
				HttpClient client = new DefaultHttpClient();
				HttpGet request = new HttpGet();

				request.setURI(new URI(url[0]));

				HttpResponse response;
				response = client.execute(request);
				Log.d(NotificationFetcherTask.class.getName(),
						" ... fetching succeeded");
				SoupNotificationParser parser = new SoupNotificationParser();

				return parser.fromXml(response.getEntity().getContent());

			} catch (IOException e) {
				Log.e(Main.class.getName(), e.getMessage());
			} catch (IllegalStateException e) {
				Log.e(Main.class.getName(), e.getMessage());
			} catch (URISyntaxException e) {
				Log.e(Main.class.getName(), e.getMessage());
			}

			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(RssRoot result) {
			if(result == null) {
				dlgNotification.show();
				return;
			}
			
			Log.d(NotificationFetcherTask.class.getName(), Integer.toString(result.getChannel().getItems().length) + " Items!");

			Item[] items = result.getChannel().getItems();

			adapter = new NotificationsAdapter(getApplication(),
					R.layout.list_item, items);

			lvNotifications.setAdapter(adapter);
			lvNotifications.setOnItemClickListener(Main.this);
			lvNotifications.refreshDrawableState();
			setUserHeader(result.getChannel());
			dlgDownloadProgress.cancel();
			adapter.fetchUserImagesIfPrefsActive();
		}
	}

	/** This task handles the fetching of the user image from the channel image url. */
	private class UserImageFetcherTask extends AsyncTask<URL, Void, Drawable> {

		@Override
		protected Drawable doInBackground(URL... soupUrl) {
			URL url = soupUrl[0];
			try {
				return Drawable.createFromStream(url.openStream(), "src");
			} catch (MalformedURLException e) {
				Log.e(UserImageFetcherTask.class.getName(), e.getMessage());
			} catch (IOException e) {
				Log.e(UserImageFetcherTask.class.getName(), e.getMessage());
			} catch (NullPointerException e) {				
				Log.e(UserImageFetcherTask.class.getName(), e.getMessage());
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Drawable result) {
			if(result == null) {
				Log.w(UserImageFetcherTask.class.getName(), "Couldn't set user image - keeps default");
				return;
			}
			
			ImageView imgUser = (ImageView)findViewById(at.fundev.android.soupchecker.R.id.imgVwUserLogo);
			imgUser.setImageDrawable(result);
			
			Log.d(UserImageFetcherTask.class.getName(), "Set user image");
		}
		
	}
	
	/** The lv notifications. */
	private ListView lvNotifications;

	/** The notifications adapter for the list view */
	private NotificationsAdapter adapter;

	/** Dialog for displaying that the user has to enter his soup rss url */
	private Dialog dlgNotification;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 *            the saved instance state
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		buildDialog();

		lvNotifications = (ListView) this.findViewById(R.id.lvNotifications);

		if (!startedCredentialsIfNeeded()) {
			refreshListView(new Item[0]);
		}
	}

	private void setHeaderVisibilityFromPreferences() {
		CredsHelper creds = new CredsHelper(this);
		RelativeLayout layout = null;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.refresh:
				refreshListView(new Item[0]);
				return true;
			case R.id.settings:
				startCredentialsActivity();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	/**
	 * Checks if the user allready set the URL of his RSS feed. If not the app
	 * displays the credentials activity.
	 */
	private boolean startedCredentialsIfNeeded() {
		CredsHelper creds = new CredsHelper(getBaseContext());

		if (!creds.isSoupUrlSet()) {
			dlgNotification.show();
			return true;
		}

		return false;
	}

	/**
	 * Builds the dialog which gets displayed before the user has to enter his
	 * URL.
	 */
	private void buildDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		String dlgText = getString(R.string.credsUrlSummary);
		String btnOKText = getString(R.string.dlgOkBtnText);
		
		builder.setMessage(dlgText);
		builder.setPositiveButton(btnOKText, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				startCredentialsActivity();
			}
		});

		dlgNotification = builder.create();
	}

	/**
	 * Sets the user header. The header is the part in the view which displays the owen user pic from
	 * soup and a little text.
	 */
	private void setUserHeader(Channel channel) {
		((TextView)findViewById(R.id.lblUserTxt)).setText(channel.getTitle());
		CredsHelper creds = new CredsHelper(this);
		
		if(creds.isFetchImagesActive()) {
			UserImageFetcherTask task = new UserImageFetcherTask();
			task.execute(channel.getImage().getUrl());
		}
	}
	
	/**
	 * Starts the credentials activity.
	 */
	private void startCredentialsActivity() {
		Intent i = new Intent(getBaseContext(), Credentials.class);
		startActivityForResult(i, SETTINGS_REQUEST_ID);
	}
	
	/**
	 * Refreshes the list view with the given items and starts the fetcher task
	 * for the user images.
	 * 
	 * @param items
	 */
	private void refreshListView(Item[] items) {
		CredsHelper creds = new CredsHelper(getBaseContext());
		updateListView(items);

		NotificationFetcherTask fetcher = new NotificationFetcherTask();
		fetcher.execute(creds.getURL());
	}

	/**
	 * Update list view.
	 * 
	 * @param items
	 *            the items
	 */
	private void updateListView(Item[] items) {
		NotificationsAdapter adapter = new NotificationsAdapter(this,
				R.layout.list_item, items);
		lvNotifications.setAdapter(adapter);
		lvNotifications.setOnItemClickListener(this);
		lvNotifications.refreshDrawableState();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == SETTINGS_REQUEST_ID) {
			Log.d(Main.class.getName(), "refreshing list in onActivityResult");
	
			startedCredentialsIfNeeded();
			refreshListView(new Item[0]);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Item item = adapter.getItem(arg2);
		String sUrl = item.getLink();
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(sUrl));
		startActivity(i);
	}

}