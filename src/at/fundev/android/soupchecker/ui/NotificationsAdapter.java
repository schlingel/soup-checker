package at.fundev.android.soupchecker.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.R;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.fundev.android.soupchecker.models.Item;
import at.fundev.android.soupchecker.utils.CredsHelper;

// TODO: Auto-generated Javadoc
/**
 * The Class NotificationsAdapter.
 */
public class NotificationsAdapter extends ArrayAdapter<Item> {
	
	/**
	 * The Class ImageFetcherTask.
	 */
	private class ImageFetcherTask extends
			AsyncTask<Item[], Drawable, Drawable> {
		
		/** The cur index. */
		private int curIndex;

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onProgressUpdate(Progress[])
		 */
		@Override
		protected void onProgressUpdate(Drawable... values) {
			Drawable d = values[0];
			
			if(d != null) {
				drawables[curIndex] = d;
			} else {
				Log.w(NotificationsAdapter.class.getName(), String.format("Couldn't load user pic for: %s", items[curIndex].getTitle()));
				Log.w(NotificationsAdapter.class.getName(), " ... using default icon instead");
			}
			
			notifyDataSetChanged();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Drawable doInBackground(Item[]... itemParam) {
			Item[] items = itemParam[0];

			for (int i = 0; i < items.length; i++) {
				Item item = items[i];

				try {
					URL url = getImageUrl(item);

					if (url == null) {
						continue;
					}

					InputStream is = url.openStream();
					/* dirty hack to know which element should be updated */
					curIndex = i;
					Drawable d = Drawable.createFromStream(is, "src");
					publishProgress(d);
					is.close();
				} catch (IOException e) {
					Log.e(ImageFetcherTask.class.getName(), e.getMessage());
					break;
				}
			}

			return null;
		}

		/**
		 * Gets the image url.
		 *
		 * @param item the item
		 * @return the image url
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		private URL getImageUrl(Item item) throws IOException {
			String sUrl = null;

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(item.getLink());

			HttpResponse response = client.execute(request);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sUrl = getUrlAsString(line);
				if (sUrl != null) {
					return new URL(sUrl);
				}
			}

			reader.close();

			return null;
		}

		/**
		 * Checks if the given string contains a soup fav icon. If so it returns
		 * the URL as string, if it doesn't contain it, it returns null.
		 *
		 * @param line the line
		 * @return the url as string
		 */
		private String getUrlAsString(String line) {
			int urlEnd = line.indexOf("\" rel=\"shortcut icon\" type=\"image/png\" />");

			if (urlEnd != -1) {
				int urlStart = urlEnd - 1;

				for (; urlStart > 0 && line.charAt(urlStart) != '"'; urlStart--);

				String url = line.substring(urlStart + 1, urlEnd);
				Log.d(ImageFetcherTask.class.getName(), "  ... contains url " + url);

				return url;
			} else {
				return null;
			}
		}
	}

	/** The items. */
	private Item[] items;

	/** The drawables. */
	private Drawable[] drawables;
	
	/**
	 * Instantiates a new notifications adapter.
	 *
	 * @param context the context
	 * @param textViewResourceId the text view resource id
	 * @param objects the objects
	 */
	public NotificationsAdapter(Context context, int textViewResourceId, Item[] objects) {
		super(context, textViewResourceId, objects);
		items = objects;
		initializeDrawables(objects.length);
	}

	/**
	 * Initialize drawables.
	 *
	 * @param length the length
	 */
	private void initializeDrawables(int length) {
		drawables = new Drawable[length];

		for (int i = 0; i < length; i++) {
			drawables[i] = getContext().getResources().getDrawable(
					at.fundev.android.soupchecker.R.drawable.soup_icon);
		}
	}

	/**
	 * Fetch user images from soup if the preference value is set to true.
	 */
	public void fetchUserImagesIfPrefsActive() {
		CredsHelper creds = new CredsHelper(getContext());
		
		if(creds.isFetchImagesActive()) {
			ImageFetcherTask task = new ImageFetcherTask();
			task.execute(items);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			// convertView = new TextView(getContext());
			LayoutInflater li = (LayoutInflater) getContext().getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			convertView = li.inflate(
					at.fundev.android.soupchecker.R.layout.list_item, null);
		}
		TextView txtVwDesc = (TextView) convertView
				.findViewById(at.fundev.android.soupchecker.R.id.lblDescription);
		txtVwDesc.setText(items[position].getTitle().trim());

		TextView txtVwDateTime = (TextView) convertView
				.findViewById(at.fundev.android.soupchecker.R.id.lblDate);
		txtVwDateTime.setText(items[position].getPubDate().trim());

		ImageView imgVw = (ImageView) convertView
				.findViewById(at.fundev.android.soupchecker.R.id.imgVwUserLogo);
		imgVw.setImageDrawable(drawables[position]);

		return convertView;
	}
}
