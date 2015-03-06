package ui.client.search;

import java.util.List;

import ui.client.services.LuceneService;
import ui.client.services.LuceneServiceAsync;
import ui.shared.Tweet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapTypeId;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.base.LatLng;
import com.google.gwt.maps.client.events.bounds.BoundsChangeMapEvent;
import com.google.gwt.maps.client.events.bounds.BoundsChangeMapHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class Search extends Composite {

	private static SearchUiBinder uiBinder = GWT.create(SearchUiBinder.class);

	interface SearchUiBinder extends UiBinder<Widget, Search> {
	}

	@UiField
	VerticalPanel results;
	@UiField
	HTMLPanel mapPanel;

	public Search() {
		initWidget(uiBinder.createAndBindUi(this));
	}
	
	double start = 0;

	public Search(String query, String type) {
		initWidget(uiBinder.createAndBindUi(this));

		// LatLng location = LatLng.newInstance(39.509, -98.434);
		// MapOptions opts = MapOptions.newInstance();
		// opts.setCenter(location);
		// opts.setMapTypeId(MapTypeId.ROADMAP);
		// opts.setZoom(12);
		// final MapWidget theMap = new MapWidget(opts);
		// theMap.setSize("500px", "500px");
		// mapPanel.add(theMap);

		LuceneServiceAsync luceneService = GWT.create(LuceneService.class);
		start = System.currentTimeMillis();
		luceneService.getTweets(query, type, new AsyncCallback<List<Tweet>>() {

			@Override
			public void onFailure(Throwable caught) {
				System.err.println(caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(List<Tweet> result) {
				if (result.size() != 0) {
					double timeres = ((System.currentTimeMillis() - start) * 1.0) / 1000;
					results.add(new Label (result.size()+ " tweets found in " + timeres + " seconds"));
					for (Tweet t : result) {
						results.add(new DisplayTweet(t));
					}
				} else {
					results.add(new Label("Sorry, no results were found!"));
				}
			}
		});
	}
}