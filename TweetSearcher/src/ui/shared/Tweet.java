package ui.shared;

import java.io.Serializable;
import java.util.Date;

public class Tweet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String createdAt;

	private int favoriteCount;
	private int retweets;

	private float longitude;// geolocation.longitude
	private float latitude;// geolocation.latitude

	private String language;
	// private String streetAddress;//place.getStreetAddress
	private String username;// user.getName

	private String text;
	private String link;
	private String linkTitle;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getLinkTitle() {
		return linkTitle;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public int getRetweets() {
		return retweets;
	}

	public float getLongitude() {
		return longitude;
	}

	public float getLatitude() {
		return latitude;
	}

	public String getLanguage() {
		return language;
	}

	public String getUsername() {
		return username;
	}

	public String getText() {
		return text;
	}

	public String getLink() {
		return link;
	}

	public Tweet() {

	}

	public Tweet(String createdAt, int favoriteCount, int retweets,
			float longitude, float latitude, String language, String username,
			String text, String linkTitle, String link) {
		super();
		this.createdAt = createdAt;
		this.favoriteCount = favoriteCount;
		this.retweets = retweets;
		this.longitude = longitude;
		this.latitude = latitude;
		this.language = language;
		this.username = username;
		this.text = text;
		this.linkTitle = linkTitle;
		this.link = link;
	}

	public Tweet(String createdAt, String favoriteCount, String retweets,
			String longitude, String latitude, String language,
			String username, String text, String linkTitle, String link) {
		this.createdAt = createdAt;
		if (favoriteCount == null) {
			this.favoriteCount = 0;
			System.out.println("FAVORITE COUNT WAS NULL");
		} else
			this.favoriteCount = Integer.parseInt(favoriteCount);
		if (retweets == null) {
			this.retweets = 0;
			System.out.println("RETWEETS COUNT WAS NULL");
		} else
			this.retweets = Integer.parseInt(retweets);
		if (longitude == null) {
			this.longitude = 0;
			System.out.println("LONGITUDE COUNT WAS NULL");
		} else
			this.longitude = Float.parseFloat(longitude);
		if (latitude == null) {
			this.latitude = 0;
			System.out.println("LATITUDE COUNT WAS NULL");
		} else
			this.latitude = Float.parseFloat(latitude);
		this.username = username;
		this.text = text;
		this.linkTitle = linkTitle;
		this.link = link;
	}
}