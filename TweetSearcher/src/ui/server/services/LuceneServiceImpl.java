package ui.server.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import ui.client.services.LuceneService;
import ui.shared.Tweet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

public class LuceneServiceImpl extends RemoteServiceServlet implements
		LuceneService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	IndexSearcher isearcher = null;
	DirectoryReader ireader = null;
	private Object luceneLock = new Object();

	@SuppressWarnings("deprecation")
	private void initSearcher() throws IOException {
		synchronized (luceneLock) {
			if (isearcher == null) {
				ireader = IndexReader.open(new SimpleFSDirectory(new File(
						"indexes/index")));
				isearcher = new IndexSearcher(ireader);
			}
		}
	}

	public List<Tweet> getTweets(String searchText, String type) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		try {
			if (isearcher == null)
				initSearcher();
			// if (searchText.length() == 0)
			// searchText = "test";
			// String[] fields = { "user", "text", "created_at", "geo_location",
			// "linkTitle", "hasBadLink" };

			BooleanQuery bq = new BooleanQuery();
			String[] fields = new String[] { "user", "text" };

			StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_40);
			MultiFieldQueryParser mfqp = new MultiFieldQueryParser(
					Version.LUCENE_40, fields, analyzer);
			Query q = null;

			q = new TermQuery(new Term("text", searchText));
			bq.add(q, Occur.SHOULD);
			q = new TermQuery(new Term("user", searchText));
			bq.add(q, Occur.SHOULD);

			TopScoreDocCollector collector = TopScoreDocCollector.create(10,
					true);
			isearcher.search(bq, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			List<ScoreDoc> hitsList = new ArrayList<ScoreDoc>(
					Arrays.asList(hits));

			for (ScoreDoc hit : hitsList) {
				Document d = isearcher.doc(hit.doc);
				tweets.add(newTweet(d));
			}

		} catch (IOException | java.text.ParseException e) {
			e.printStackTrace();
		}

		return tweets;
	}

	private Tweet newTweet(Document d) throws java.text.ParseException {
		// DateFormat format = new SimpleDateFormat("", Locale.ENGLISH);
		// Date date = format.parse(d.get("created_at"));
		// System.out.println(date);
		// System.out.println(d.get("created_at"));

		// Tweet t = new Tweet(d.get("created_at"),
		// Integer.parseInt(d.get("favoriteCount")),
		// Integer.parseInt(d.get("retweets")), Float.parseFloat(d
		// .get("longitude")),
		// Float.parseFloat(d.get("latitude")), d.get("language"),
		// d.get("username"), d.get("text"), d.get("link"));
		Tweet t = new Tweet(d.get("created_at"), d.get("favoriteCount"),
				d.get("retweets"), d.get("longitude"), d.get("latitude"),
				d.get("language"), d.get("user"), d.get("text"), d.get("link"),
				getUrlStringFrom(d.get("text")), d.get("hashtags"),
				d.get("profileImageUrl"));

		return t;
	}

	private String getUrlStringFrom(String text) {
		String[] words = text.split("\\s+|”|\"");
		String urlString = null;

		for (String word : words)
			try {
				URL url = new URL(word); // parse with URL constructor
				urlString = url.toString(); // but just returns a string

				if (urlString.endsWith(".")) {
					urlString = urlString.substring(0, urlString.length() - 1); // shave
																				// off
																				// "."
				}
			} catch (MalformedURLException e) {
				// Exception-based control flow FTW
			}

		return urlString;
	}
}
