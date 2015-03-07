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
	Anchor link;
	@UiField
	Label favorites;

	DisplayTweet(Tweet t) {
		initWidget(uiBinder.createAndBindUi(this));
		userName.setText(t.getUsername());
		body.setText(t.getText());
		retweets.setText("" + t.getRetweets());
		creation.setText("" + t.getCreatedAt());
		if (t.getLink() == null)
			link.setVisible(false);
		else {
			System.out.println("Found Link: " + t.getLink());
			link.setText("View Page");
			link.setHref(t.getLink());
		}
		favorites.setText("" + t.getFavoriteCount());
	}
}
