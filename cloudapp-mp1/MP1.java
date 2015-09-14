import java.io.BufferedReader;
import java.io.File;
import java.lang.reflect.Array;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class MP1 {
    Random generator;
    String userName;
    String inputFileName;
    String delimiters = " \t,;.?!-:@[](){}_*/";
    String[] stopWordsArray = {"i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours",
            "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its",
            "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that",
            "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
            "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while",
            "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before",
            "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again",
            "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each",
            "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than",
            "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};

    void initialRandomGenerator(String seed) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        messageDigest.update(seed.toLowerCase().trim().getBytes());
        byte[] seedMD5 = messageDigest.digest();

        long longSeed = 0;
        for (int i = 0; i < seedMD5.length; i++) {
            longSeed += ((long) seedMD5[i] & 0xffL) << (8 * i);
        }

        this.generator = new Random(longSeed);
    }

    Integer[] getIndexes() throws NoSuchAlgorithmException {
        Integer n = 10000;
        Integer number_of_lines = 50000;
        Integer[] ret = new Integer[n];
        this.initialRandomGenerator(this.userName);
        for (int i = 0; i < n; i++) {
            ret[i] = generator.nextInt(number_of_lines);
        }
        return ret;
    }

    public MP1(String userName, String inputFileName) {
        this.userName = userName;
        this.inputFileName = inputFileName;
    }

    public String[] process() throws Exception {
        String[] ret = new String[20];
        List<String> stopWordsList = Arrays.asList(stopWordsArray);

        File file = new File(inputFileName);
        List<String> lines = new ArrayList<String>();
        try {
            FileReader reader = new FileReader(file);
            BufferedReader buffReader = new BufferedReader(reader);
            String s;
            while ((s = buffReader.readLine()) != null) {
                lines.add(s);
            }
        } catch (IOException e) {
            System.exit(0);
        }

        Map<String, Integer> resultsMap = new HashMap<String, Integer>();
        Integer[] indexes = getIndexes();

        for (Integer index : indexes) {
            String s = lines.get(index);
            StringTokenizer tokens = new StringTokenizer(s, delimiters, false);
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken().trim().toLowerCase();
                if (!stopWordsList.contains(token)) {
                    int previousValue = resultsMap.get(token) == null ? 0 : resultsMap.get(token);
                    resultsMap.put(token, ++previousValue);
                }
            }
        }
        // Convert Map to List
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(resultsMap.entrySet());

        // Sort list with comparator, to compare the Map values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                int returnValue = o2.getValue().compareTo(o1.getValue());
                return returnValue == 0 ? o1.getKey().compareTo(o2.getKey()) : returnValue;
            }
        });
        int i = 0;
        for (Map.Entry<String, Integer> entry : list) {
            ret[i] = entry.getKey();
            if (++i >= ret.length) {
                break;
            }
        }
        return ret;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("MP1 <User ID>");
        } else {
            String userName = args[0];
            String inputFileName = "./input.txt";
            MP1 mp = new MP1(userName, inputFileName);
            String[] topItems = mp.process();
            for (String item : topItems) {
                System.out.println(item);
            }
        }
    }
}
