package ui.server.services;

import ui.client.services.LuceneService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LuceneServiceImpl extends RemoteServiceServlet implements
		LuceneService {
	public String getTweets(){
		return "testing";
	}
}
