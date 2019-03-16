import java.io.File;
import java.io.FileNotFoundException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
//import java.org.tartarus.snowball.ext.*;



public class Dictionnaire extends ProcessXML{

    private static final int TAILLE = 700;
    private static ArrayList<String> dictionnaire;
    private LinkedHashMap<String, Integer> occurences;
    
    private ArrayList<String> stopWords;

    public Dictionnaire(String xml){
    	
        super(xml);
        occurences = new LinkedHashMap<>();
        dictionnaire = new ArrayList<>();
        
    	Scanner sc;
		try {
			sc = new Scanner(new File("./stopwords_fr.txt"));
			stopWords = new ArrayList<String>();
			
			while (sc.hasNextLine()) {
	    		stopWords.add(sc.next());
			}
	    	sc.close();
	    	
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
    }

    public ArrayList<String> generate(){
        try {
            super.process();
            occurences = occurences.entrySet().stream().sorted(Map.Entry.comparingByValue()).collect(Collectors.toMap(
                    Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
            String words[] = Arrays.copyOfRange(occurences.keySet()
                    .toArray(new String[occurences.size()]), occurences.keySet().size()-TAILLE, occurences.keySet().size());
            dictionnaire.addAll(Arrays.asList(words));
            dictionnaire.sort(String::compareToIgnoreCase);
            //System.out.println(dictionnaire.size());
            //System.out.println(dictionnaire);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dictionnaire;
    }

    //TODO prendre la racine du mot
    private String normalize(String word){
        word = Normalizer.normalize(word, Normalizer.Form.NFD);
       // System.out.println(word);
        word = word.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
       // System.out.println(word);
        return word.toLowerCase();
    }
    
    private String racineMots(String word) {
    	

		return word;
  
    }
    

    //TODO filtre stopwords
    private String[] splitWords(String words){
    	
        return Arrays.asList(words.replaceAll("[^A-Za-z]", " ")
                .split(" ")).stream().filter(x -> x.length()>2 && !stopWords.contains(x)).toArray(String[]::new);
    }

    @Override
    protected void processTitle(String title){
        String w = normalize(title);
        for(String word: splitWords(w)){
            if(!occurences.containsKey(word))
                occurences.put(word,1);
            else occurences.put(word,occurences.get(word)+1);
        }
    }

    @Override
    protected void processText(String text) {
    	
    	String w = normalize(text);
    	
    	//String s = stem.w;
    	
    	String rootWord = racineMots(w);
    	
        for(String word: splitWords(w)){
            if(!occurences.containsKey(word))
                occurences.put(word,1);
            else occurences.put(word,occurences.get(word)+1);
        }
    }

    @Override
    protected void processEnd() {

    }
}
