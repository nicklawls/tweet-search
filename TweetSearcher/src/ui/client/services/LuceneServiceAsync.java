package ui.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LuceneServiceAsync {
	public void getTweets(AsyncCallback<String> callback);
}
