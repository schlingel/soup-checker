package at.fundev.android.soupchecker.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;

// TODO: Auto-generated Javadoc
/**
 * The Class RssRoot. Used for deserializing RSS feeds.
 */
@XStreamAlias("root")
public class RssRoot {
	
	/** The channel. */
	private Channel channel;

	/**
	 * Sets the channel.
	 *
	 * @param channel the new channel
	 */
	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	/**
	 * Gets the channel.
	 *
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}
}
