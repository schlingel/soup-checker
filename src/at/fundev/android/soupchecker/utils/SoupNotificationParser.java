package at.fundev.android.soupchecker.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.util.Log;
import at.fundev.android.soupchecker.models.Channel;
import at.fundev.android.soupchecker.models.Item;
import at.fundev.android.soupchecker.models.RssRoot;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.MapperWrapper;

// TODO: Auto-generated Javadoc
/**
 * The Class SoupNotificationParser. Parses the RSS via xstream and returns the object hierarchy.
 */
public class SoupNotificationParser {
	
	/** The xs. */
	private XStream xs;

	/**
	 * Instantiates a new soup notification parser.
	 */
	public SoupNotificationParser() {
		// ignore unknown elements in the xml
		xs = new XStream(new DomDriver()) {
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MapperWrapper(next) {
					public boolean shouldSerializeMember(Class definedIn,
							String fieldName) {
						try {
							return definedIn != Object.class
									|| realClass(fieldName) != null;
						} catch (CannotResolveClassException cnrce) {
							return false;
						}
					}
				};
			}
		};
		xs.alias("rss", RssRoot.class);
		xs.alias("item", Item.class);
		xs.addImplicitCollection(Channel.class, "items");
	}

	/**
	 * From xml.
	 *
	 * @param inputStream the input stream
	 * @return the rss root
	 */
	public RssRoot fromXml(InputStream inputStream) {
		try {
			return (RssRoot)xs.fromXML(inputStream);
		}
		catch(Exception e) {
			BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
			String line = "";
			try {
				while((line = r.readLine()) != null) {
					Log.d(SoupNotificationParser.class.getName(), line);
				}
			} catch (IOException e1) {
				
			}
			
			Log.d(SoupNotificationParser.class.getName(), e.getMessage());
		}
		
		return null;
	}

	/**
	 * To xml.
	 *
	 * @param rss the rss
	 * @param stream the stream
	 */
	public void toXml(RssRoot rss, OutputStream stream) {
		xs.toXML(rss, stream);
	}
}
