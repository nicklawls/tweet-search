package ui.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("luceneServlet")
public interface LuceneService extends RemoteService {
public String getTweets();
}
