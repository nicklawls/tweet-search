package com.tweetsearch;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.apache.lucene.document.Field;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class IndexBuilder {

	public static void main(String[] args)  {
		
		String[] fields = {"user", "text", "created_at", "geo_location", "linkTitle", "hasBadLink" };
		
		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory directory = null;
		
		// TODO verify args
		Path indexDir  = Paths.get(args[0]);
		Path tweetsDir = Paths.get(args[1]);
		
		
		try {
		    directory = FSDirectory.open(indexDir);
		    if (directory.listAll().length >= 1) {
		    	System.err.println("Index directory is occupied, please provide an empty directory");
		    	System.exit(1);
		    }
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to read directory: " + e.getMessage());
			System.exit(1);
		}
		

		
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter iwriter = null;
		try {
			iwriter = new IndexWriter(directory, config);
		} catch (IOException e) {
			System.err.println("Failed to write to index: " + e.getMessage());
			System.exit(1);
		}
		
		JSONParser jsonParser = new JSONParser();
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(tweetsDir, "tweets[0-3].json") ) {
			
			for (Path tweetFile : stream) {
			
				Stream<String> lines = Files.lines(tweetFile);
				
				Document[] docs = (Document[]) lines
					.map((line) -> { 
						try {
							return (JSONObject) jsonParser.parse(line);
						} catch (ParseException e) {
							System.err.println("JSON parse error: " + e.getMessage()) ;
						}
						return null;
					})
					.filter(a -> a != null)
					.map((json) -> {
						Document doc = new Document();
						
						for (String field: fields) {
							
							if (json.containsKey(field)) {
								String content = json.get(field).toString();
								doc.add(new Field(field, content, TextField.TYPE_STORED));
							} 
						}
						return doc;
						
					}).toArray(Document[]::new); 
				
				for (Document doc : docs) {
					iwriter.addDocument(doc);
				}
				lines.close();
			}
		} catch(IOException e) {
			System.err.println("Failed to finish directory stream: " + e.getMessage());
			System.exit(1);
		}
		
		
		try {
			iwriter.close();
			directory.close();
		} catch (IOException e) {
			System.err.println("Failed to close index: " + e.getMessage());
			System.exit(1);
		}

	}

}
