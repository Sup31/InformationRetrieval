import org.apache.lucene.search.similarities.TFIDFSimilarity;
public class CustomAlgorithm extends TFIDFSimilarity {
        @Override
        public float idf(long docFreq, long docCount) {
            return (float)(Math.log((docCount+1)/(double)(docFreq+1)) + 1.0);
        }

       @Override
        public float tf(float freq) {
            return (float) (1+ Math.log10(freq));
        }
    
       
        @Override
        public float lengthNorm(int length) {
            return (float) (1.0 / Math.sqrt(length));
        }
        
    }
