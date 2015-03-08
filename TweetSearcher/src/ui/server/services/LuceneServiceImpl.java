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
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.TopFieldDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

import ui.client.services.LuceneService;
import ui.shared.Constants;
import ui.shared.Tweet;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;

import org.apache.lucene.search.SortField.Type;

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

	public List<Tweet> getTweets(String query, String type) {
		List<Tweet> tweets = new ArrayList<Tweet>();
		try {
			if (isearcher == null)
				initSearcher();
			ScoreDoc[] hits = null;

			if (query.length() == 0)
				hits = getRecentTweets();
			else {
				String[] separate = query.split(" ");
				if (type.equals(Constants.GENERAL))
					hits = generalSearh(separate);
				else if (type.equals(Constants.HASHTAGS)) {
					hits = getHashTags(separate);
				} else if (type.equals(Constants.USER)) {
					hits = getUser(separate);
				}
			}

			// BooleanQuery bq = new BooleanQuery();
			// String[] fields = new String[] { "user", "text" };
			//
			// StandardAnalyzer analyzer = new
			// StandardAnalyzer(Version.LUCENE_40);
			// MultiFieldQueryParser mfqp = new MultiFieldQueryParser(
			// Version.LUCENE_40, fields, analyzer);
			// Query q = null;
			//
			// q = new TermQuery(new Term("text", searchText));
			// bq.add(q, Occur.SHOULD);
			// q = new TermQuery(new Term("user", searchText));
			// bq.add(q, Occur.SHOULD);
			//
			// TopScoreDocCollector collector = TopScoreDocCollector.create(10,
			// true);
			// isearcher.search(bq, collector);
			// ScoreDoc[] hits = collector.topDocs().scoreDocs;
			//

			for (ScoreDoc hit : hits) {
				Document d = isearcher.doc(hit.doc);
				tweets.add(newTweet(d));
			}

		} catch (IOException | java.text.ParseException | ParseException e) {
			e.printStackTrace();
		}

		return tweets;
	}

	private ScoreDoc[] generalSearh(String[] query) throws IOException,
			ParseException {
		BooleanQuery bq = new BooleanQuery();
		Query q = null;
		String[] fields = new String[] { "text", "hashtags", "user" };
		StandardAnalyzer sa = new StandardAnalyzer(Version.LUCENE_40);
		MultiFieldQueryParser mfqp = new MultiFieldQueryParser(
				Version.LUCENE_40, fields, sa);

		for (String s : query) {

			System.out.println("Looking for general: " + s);

			// q = mfqp.parse(mfqp.escape(s));
			// bq.add(q, BooleanClause.Occur.SHOULD);
			q = new TermQuery(new Term("text", s));
			bq.add(q, Occur.SHOULD);
			q = new TermQuery(new Term("user", s));
			q.setBoost(5f);
			bq.add(q, Occur.SHOULD);
			q = new TermQuery(new Term("hashtags", s));
			q.setBoost(10f);
			bq.add(q, Occur.SHOULD);
		}
		return getScoreDoc(bq);
	}

	private ScoreDoc[] getRecentTweets() throws IOException {
		BooleanQuery bq = new BooleanQuery();
		Query q = null;
		q = NumericRangeQuery.newIntRange("retweets", 0, 100, true, true);
		bq.add(q, Occur.MUST);
		System.out.println("Getting recent tweets");
		Sort sorter = new Sort();
		SortField sf = new SortField("created_at", Type.LONG, true);
		sorter.setSort(sf);
		TopFieldDocs tfd = isearcher.search(bq, 10, sorter);
		return tfd.scoreDocs;
	}

	private ScoreDoc[] getUser(String[] users) throws IOException,
			ParseException {
		BooleanQuery bq = new BooleanQuery();
		Query q = null;
		// String[] fields = new String[] { "text", "user" };
		// StandardAnalyzer sa = new StandardAnalyzer(Version.LUCENE_40);
		// MultiFieldQueryParser mfqp = new MultiFieldQueryParser(
		// Version.LUCENE_40, fields, sa);

		for (String s : users) {
			System.out.println("Looking for User: " + s);
			// q = mfqp.parse(mfqp.escape(s));
			// bq.add(q, BooleanClause.Occur.SHOULD);

			q = new TermQuery(new Term("user", s));
			bq.add(q, Occur.SHOULD);
			// s = "@" + s;
			// System.out.println("looking for text: " + s);
			// q = new TermQuery(new Term("text", s));
			// bq.add(q, Occur.SHOULD);

			// if (s.contains("@"))
			// s = s.replaceAll("@", "");
			// q = new TermQuery(new Term("user", s));
			// bq.add(q, Occur.SHOULD);
			// s = "@" + s;
			// q = new TermQuery(new Term("text", s));
			// bq.add(q, Occur.SHOULD);
		}
		return getScoreDoc(bq);
	}

	private ScoreDoc[] getHashTags(String[] tags) throws IOException,
			ParseException {
		BooleanQuery bq = new BooleanQuery();
		Query q = null;
		String[] fields = new String[] { "hashtags" };
		StandardAnalyzer sa = new StandardAnalyzer(Version.LUCENE_40);
		MultiFieldQueryParser mfqp = new MultiFieldQueryParser(
				Version.LUCENE_40, fields, sa);
		for (String s : tags) {
			System.out.println("Looking for HashTags: " + s);
			q = mfqp.parse(mfqp.escape(s));
			bq.add(q, BooleanClause.Occur.SHOULD);
			// if (s.contains("#"))
			// s = s.replaceAll("#", "");
			// q = new TermQuery(new Term("hashtags", s));
			// bq.add(q, Occur.SHOULD);
		}
		return getScoreDoc(bq);
	}

	private ScoreDoc[] getScoreDoc(BooleanQuery bq) throws IOException {
		TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
		isearcher.search(bq, collector);
		return collector.topDocs().scoreDocs;
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
