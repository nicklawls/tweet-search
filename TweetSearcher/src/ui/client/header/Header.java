package ui.client.header;

import ui.client.resources.CSSAndImageResources;
import ui.client.search.Search;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class Header extends Composite {

	private static HeaderUiBinder uiBinder = GWT.create(HeaderUiBinder.class);

	interface HeaderUiBinder extends UiBinder<Widget, Header> {
	}

	@UiField
	TextBox searchText;
	@UiField
	Button searchButton;
	@UiField
	HorizontalPanel main;

	public Header() {
		initWidget(uiBinder.createAndBindUi(this));
		
		CSSAndImageResources.INSTANCE.main().ensureInjected();
		CSSAndImageResources.INSTANCE.header().ensureInjected();
		switchPage(searchButton, new Search());
		// switchPage(home, new Home());
		// switchPage(stories, new Story());
	}

	public static void switchPage(Button b, final Widget x) {
		b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				RootPanel.get("body").clear();
				RootPanel.get("body").add(x);
			}
		});
	}

}