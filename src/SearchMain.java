import java.util.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.MessageFormat;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.store.Directory;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;


import org.apache.lucene.queryparser.classic.QueryParser;

public class SearchMain {
    public static String corpus = "ohsumed.88-91";
    public static String query = "query.ohsu.1-63";
    public static DataParser parse = new DataParser();
    public static void main(String args[]) throws IOException
    {
        SearchMain searchAlgorithms=new SearchMain();
        Directory index = parse.returnIndex(corpus, false);

        //searchAlgorithms.search(index, "Boolean");
        //searchAlgorithms.search(index, "TF");
        //searchAlgorithms.search(index, "TFIDF");
        //searchAlgorithms.search(index, "PseudoRelevance");
        searchAlgorithms.search(index, "Custom");
    }
    
    public  void search(Directory index, String algorithmType)
    {
     
     try {
        
        DirectoryReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        String model = "BM25";
        int retreival=20;
        if(algorithmType.equals("Boolean"))
        {
              searcher.setSimilarity(new BooleanSimilarity());model="Boolean";
        }

        else if(algorithmType.equals("TF"))
        {
             searcher.setSimilarity(new TermFrequency());model="TF";
        }

        else if(algorithmType.equals("TFIDF"))
        {
             searcher.setSimilarity(new ClassicSimilarity());model="TFIDF";
        }

         else if(algorithmType.equals("PseudoRelevance"))
         {
            searcher.setSimilarity(new BM25Similarity());
            index = parse.returnIndex(corpus, true);
            reader = DirectoryReader.open(index);
            searcher=new IndexSearcher(reader);
            retreival=25;
            model="PseudoRelevance";
        }

        else if(algorithmType.equals("Custom"))
         {
            searcher.setSimilarity(new CustomAlgorithm());
            model="TFIDFCustom";
            index = parse.returnIndex(corpus, true);

        }        
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(System.getProperty("user.dir")+"/resources/"+algorithmType),StandardOpenOption.TRUNCATE_EXISTING);
        final Analyzer analyzer = new EnglishAnalyzer();
        QueryParser queryParser = new MultiFieldQueryParser(new String[]{"title", "source", "abstract", "author", "publication", "keywords"}, analyzer);
        long startTime=System.currentTimeMillis();
        List<QueryListParser> queries = QueryListParser.parseQueries(query);
        int count = 0;
        
        for (QueryListParser query : queries) {
                    Query descpart = queryParser.parse(QueryParser.escape(query.description));
                    TopDocs topDocs = searcher.search(descpart, retreival);
                    for (final ScoreDoc scoreDoc : topDocs.scoreDocs) {
                        writer.write(MessageFormat.format("{0}\tQ0\t{1}\t{2}\t{3}\t{4}\n",
                        query.getNumber(), searcher.doc(scoreDoc.doc).getField(parse.MEDLINE_UI_FIELD).stringValue(),
                        count, scoreDoc.score, model));
                        count+=1;
                    }                
                }
          long endTime=System.currentTimeMillis();
          System.out.println("Time taken:"+(endTime-startTime)+"ms");
          writer.close();
    } 
    
    catch (Exception e) {
        e.printStackTrace();
    }
  } 
}

    
