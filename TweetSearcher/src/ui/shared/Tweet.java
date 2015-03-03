package ui.shared;

import java.util.Date;

public class Tweet {

	private Date createdAt;

	private int favoriteCount;
	private int retweets;

	private long longitude;//geolocation.longitude
	private long latitude;//geolocation.latitude

	private String language;
	private String streetAddress;//place.getStreetAddress
	private String username;//user.getName
	private String text;
	private String link;

	public Tweet() {

	}

}