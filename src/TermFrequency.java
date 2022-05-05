

import org.apache.lucene.search.similarities.TFIDFSimilarity;
public class TermFrequency extends TFIDFSimilarity {
         // idf is set to 1
        @Override
        public float idf(long docFreq, long docCount) {
            return (float) 1.0;
        }

       @Override
        public float tf(float freq) {
            return (float) Math.sqrt(freq);
        }
    
       
        @Override
        public float lengthNorm(int length) {
            return (float) (1.0 / Math.sqrt(length));
        }
        
    }
