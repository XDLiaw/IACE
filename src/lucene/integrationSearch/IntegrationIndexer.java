package lucene.integrationSearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IntegrationIndexer {
	public final static Object lock = new Object();
	public final static String FIELD_ID = "id";
	public final static String FIELD_CLASS_NAME = "className";
	public final static String FIELD_CONTENT = "content";
	public final static Analyzer ANALYZER = new StandardAnalyzer();
	

	public static Directory openDirectory(String luceneIndexFolder) throws IOException {
		Directory indexDirectory = FSDirectory.open(new File(luceneIndexFolder).toPath());
		return indexDirectory;
	}
	
	public static IndexWriter createIndexWriter(Directory indexDirectory) throws IOException {
		IndexWriterConfig config = new IndexWriterConfig(ANALYZER);
		IndexWriter writer = new IndexWriter(indexDirectory, config);
		return writer;
	}
	
	public static IndexReader createIndexReader(Directory indexDirectory) throws IOException {
		IndexReader reader = DirectoryReader.open(indexDirectory);
		return reader;
	}

	public static void addDoc(IndexWriter writer, long id, String className, String content) throws IOException {
		Document doc = new Document();
		doc.add(new StringField(FIELD_ID, String.valueOf(id), Store.YES));
		doc.add(new TextField(FIELD_CLASS_NAME, className, Store.YES));
		doc.add(new TextField(FIELD_CONTENT, content, Store.NO));
		writer.addDocument(doc);
	}

	public static void addDoc(IndexWriter writer, long id, Class<?> cls, String content) throws IOException {
		addDoc(writer, id, cls.getName(), content);
	}

	public static void deleteDoc(IndexWriter writer, long id, String className) throws ParseException, IOException {
		String queryStr = IntegrationIndexer.FIELD_ID + ":" + id + " AND " + IntegrationIndexer.FIELD_CLASS_NAME + ":" + className;
		String[] queryFields = { IntegrationIndexer.FIELD_ID, IntegrationIndexer.FIELD_CLASS_NAME };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(queryFields, IntegrationIndexer.ANALYZER);
		parser.setDefaultOperator(QueryParser.Operator.OR);
		Query query = parser.parse(queryStr);
		writer.deleteDocuments(query);
	}
	
	public static void deleteDoc(IndexWriter writer, long id, Class<?> cls) throws ParseException, IOException {
		deleteDoc(writer, id, cls.getName());
	}

	public static void updateDoc(IndexWriter writer, long id, String className, String content) throws ParseException, IOException {
		deleteDoc(writer, id, className);
		addDoc(writer, id, className, content);
	}

	public static void updateDoc(IndexWriter writer, long id, Class<?> cls, String content) throws ParseException, IOException {
		deleteDoc(writer, id, cls);
		addDoc(writer, id, cls, content);
	}

	public static List<Document> search(IndexReader reader, IntegrationSearchModel arg) throws ParseException, IOException {
		String queryStr = IntegrationIndexer.FIELD_CONTENT + ":(" + arg.getSearchText() + ")";
		if (StringUtils.isNotBlank(arg.getSearchText())) {
			queryStr += " AND " + IntegrationIndexer.FIELD_CLASS_NAME + ":" + arg.getClassName();
		}

		String[] queryFields = { IntegrationIndexer.FIELD_CLASS_NAME, IntegrationIndexer.FIELD_CONTENT };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(queryFields, IntegrationIndexer.ANALYZER);
		parser.setDefaultOperator(QueryParser.Operator.OR);
		Query query = parser.parse(queryStr);
		IndexSearcher searcher = new IndexSearcher(reader);
		int topN = arg.getPageSize() * (arg.getPageIndex() + 1);
		TopDocs docs = searcher.search(query, topN);
		ScoreDoc[] scoreDocs = docs.scoreDocs;

		List<Document> resultList = new ArrayList<Document>();
		for (int i=arg.getPageSize() * arg.getPageIndex(); i < scoreDocs.length; i++) {
			int docId = scoreDocs[i].doc;
			Document d = searcher.doc(docId);
			resultList.add(d);			
		}
		
		return resultList;
	}
	
	public static int existCount(IndexReader reader, long id, String className) throws ParseException, IOException {
		String queryStr = IntegrationIndexer.FIELD_ID + ":" + id + " AND " + IntegrationIndexer.FIELD_CLASS_NAME + ":" + className;
		String[] queryFields = { IntegrationIndexer.FIELD_CLASS_NAME, IntegrationIndexer.FIELD_ID };
		MultiFieldQueryParser parser = new MultiFieldQueryParser(queryFields, IntegrationIndexer.ANALYZER);
		parser.setDefaultOperator(QueryParser.Operator.OR);
		Query query = parser.parse(queryStr);
		
		IndexSearcher searcher = new IndexSearcher(reader);
		int docCount = searcher.count(query);
		return docCount;
	}

}
