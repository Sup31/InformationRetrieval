import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;

public class DataParser {
     String SEQ_ID = ".I";
     String MEDLINE_UI = ".U";
     String MEDLINE_UI_FIELD = "universal_identifier";
     String MESH_TERMS = ".M";
     String MESH_TERMS_FIELD = "keywords";
     String TITLE = ".T";
     String TITLE_FIELD = "title";

     String PUBLICATION_TYPE = ".P";
     String PUBLICATION_TYPE_FIELD = "publication";
     String ABSTRACT = ".W";
     String ABSTRACT_FIELD = "abstract";
     String AUTHOR = ".A";
     String AUTHOR_FIELD = "author";
     String SOURCE = ".S";
     String SOURCE_FIELD = "source";

    public Directory returnIndex(String filename, Boolean feedback) throws IOException{
        StandardAnalyzer analyzer = new StandardAnalyzer();
        Directory index = new ByteBuffersDirectory();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/resources/"+filename));
        IndexWriter writer = new IndexWriter(index, config);
        try
        {
          
          String line = reader.readLine();
          Document curr = null;
          String field = null;
          Field.Store store = Field.Store.NO;
          while(line != null)
          {

            if (line.startsWith(SEQ_ID)) {
                if (curr != null) {
                    writer.addDocument(curr);
                }
                curr = new Document();

            } else if (line.startsWith(MEDLINE_UI)) {
                field = MEDLINE_UI_FIELD;
                store = Field.Store.YES;
            } else if (line.startsWith(MESH_TERMS)) {
                field = MESH_TERMS_FIELD;
                if(feedback = true)
                  {store = Field.Store.YES;}
                else
                  {store = Field.Store.NO;}

            } else if (line.startsWith(TITLE)) {
                field = TITLE_FIELD;
                store = Field.Store.NO;

            } else if (line.startsWith(PUBLICATION_TYPE)) {
                field = PUBLICATION_TYPE_FIELD;
                store = Field.Store.NO;


            } else if (line.startsWith(ABSTRACT)) {
                field = ABSTRACT_FIELD;
                store = Field.Store.NO;

            } else if (line.startsWith(SOURCE)) {
                field = SOURCE_FIELD;
                store = Field.Store.NO;

            } else {
                curr.add(new TextField(field, line, store));
            }
            line = reader.readLine();
            }
        writer.addDocument(curr);
        writer.commit();
        }

        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            reader.close();
            writer.close();
        }
        return index;

    }
}

