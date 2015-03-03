package ui.client.search;

import ui.client.services.LuceneService;
import ui.client.services.LuceneServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Search extends Composite {

	private static SearchUiBinder uiBinder = GWT.create(SearchUiBinder.class);

	interface SearchUiBinder extends UiBinder<Widget, Search> {
	}

	@UiField
	Label results;

	public Search() {
		initWidget(uiBinder.createAndBindUi(this));
		LuceneServiceAsync luceneService = GWT.create(LuceneService.class);
//		luceneService.getTweets(new AsyncCallback<String>() {
//
//			@Override
//			public void onSuccess(String result) {
//				results.setText(result);
//
//			}
//
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//
//			}
//		});
	}

}