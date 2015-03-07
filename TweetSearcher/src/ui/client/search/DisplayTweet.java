package ui.client.search;

import ui.client.services.LuceneService;
import ui.client.services.LuceneServiceAsync;
import ui.shared.Tweet;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class DisplayTweet extends Composite {
	private static DisplayTweetUiBinder uiBinder = GWT
			.create(DisplayTweetUiBinder.class);

	interface DisplayTweetUiBinder extends UiBinder<Widget, DisplayTweet> {

	}

	@UiField
	Label userName;
	@UiField
	Label body;
	@UiField
	Label retweets;
	@UiField
	Label creation;
	@UiField
	Anchor linkTitle;
	@UiField
	Label favorites;
	@UiField
	Image userImg;

	DisplayTweet(Tweet t, double curTime) {
		long timeAgo = (long) ((curTime - t.getCreatedAt()) / 1000);
		initWidget(uiBinder.createAndBindUi(this));
		userName.setText(t.getUsername());
		if (t.getUserImg() == null) {
			System.err.println("userImg was null");
			userImg.setVisible(false);
		} else
			userImg.setUrl(t.getUserImg());
		body.setText(t.getText());
		retweets.setText("" + t.getRetweets());
		creation.setText(getTimeAgo(timeAgo));
		if (t.getLink() == null)
			linkTitle.setVisible(false);
		else {
			System.out.println("Found Link: " + t.getLink());
			linkTitle.setText(t.getLinkTitle());
			linkTitle.setHref(t.getLink());
		}
		favorites.setText("" + t.getFavoriteCount());
	}

	private String getTimeAgo(long seconds) {
		if (seconds / 60 == 0) {
			if (seconds == 1)
				return "1 second ago";
			else
				return seconds + " seconds ago";
		}
		seconds %= 60;
		if (seconds / 60 == 0) {
			if (seconds == 1)
				return "1 minute ago";
			else
				return seconds + " minutes ago";
		}
		seconds %= 60;
		if (seconds / 24 == 0) {
			if (seconds == 1)
				return "1 hour ago";
			else
				return seconds + " hours ago";
		}
		seconds %= 24;
		if (seconds / 7 == 0) {
			if (seconds == 1)
				return "1 day ago";
			else
				return seconds + " hours ago";
		}
		seconds %= 7;
		if (seconds / 4 == 0) {
			if (seconds == 1)
				return "1 week ago";
			else
				return seconds + " weeks ago";
		}
		seconds %= 4;
		if (seconds / 12 == 0) {
			if (seconds == 1)
				return "1 month";
			else
				return seconds + " months ago";
		}
		seconds %= 12;
		if (seconds == 1)
			return "1 year";
		else
			return seconds + " years ago";

	}
}
