package at.fundev.android.soupchecker.models;

import com.thoughtworks.xstream.annotations.XStreamAlias;

// TODO: Auto-generated Javadoc
/**
 * The Class Channel. Used for RSS deserializaion.
 */
@XStreamAlias("channel")
public class Channel {
	
	/** The title. */
	private String title;
	
	/** The link. */
	private String link;
	
	/** The description. */
	private String description;
	
	/** The image. */
	private RssImage image;
	
	/** The items. */
	private Item[] items;

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the link.
	 *
	 * @param link the new link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Gets the link.
	 *
	 * @return the link
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the image.
	 *
	 * @param image the new image
	 */
	public void setImage(RssImage image) {
		this.image = image;
	}

	/**
	 * Gets the image.
	 *
	 * @return the image
	 */
	public RssImage getImage() {
		return image;
	}

	/**
	 * Sets the items.
	 *
	 * @param items the new items
	 */
	public void setItems(Item[] items) {
		this.items = items;
	}

	/**
	 * Gets the items.
	 *
	 * @return the items
	 */
	public Item[] getItems() {
		return items;
	}
}
