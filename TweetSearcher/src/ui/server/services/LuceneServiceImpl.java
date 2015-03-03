package ui.server.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import ui.client.services.LuceneService;
import ui.shared.Tweet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class LuceneServiceImpl extends RemoteServiceServlet implements
		LuceneService {

	IndexSearcher isearcher = null;
	DirectoryReader ireader = null;
	private Object luceneLock = new Object();

	@SuppressWarnings("deprecation")
	private void initSearcher() throws IOException {
		synchronized (luceneLock) {
			if (isearcher == null) {
				ireader = IndexReader.open(new SimpleFSDirectory(new File(
						"indexes/tweet_index_name")));
				isearcher = new IndexSearcher(ireader);
			}
		}
	}

	public List<Tweet> getTweets(String searchText) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		try {
			if (isearcher == null)
				initSearcher();
			// String[] fields = { "user", "text", "created_at", "geo_location",
			// "linkTitle", "hasBadLink" };

			StandardAnalyzer analyzer = new StandardAnalyzer();

			String fieldname = "user";
			QueryParser queryParser = new QueryParser(fieldname, analyzer);

			Query query = queryParser.parse(searchText);

			// top 10 results
			ScoreDoc[] hits = isearcher.search(query, null, 10).scoreDocs;

			List<ScoreDoc> hitsList = new ArrayList<ScoreDoc>(
					Arrays.asList(hits));

			for (ScoreDoc hit : hitsList) {
				Document d = isearcher.doc(hit.doc);
				tweets.add(newTweet(d));
			}

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return tweets;
	}

	private Tweet newTweet(Document d) {
		Tweet t = new Tweet();
		// TODO: grab document fields and add them to the tweet
		return t;
	}
}
