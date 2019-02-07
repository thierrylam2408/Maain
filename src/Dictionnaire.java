import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;

/*
virer les stopwords une fois tous les mots fréquents faire un xml/json
 */

public class Dictionnaire extends ProcessXML{

    private static final int TAILLE = 700;
    private static ArrayList<String> dictionnaire;
    private LinkedHashMap<String, Integer> occurences;

    public Dictionnaire(String xml){
        super(xml);
        occurences = new LinkedHashMap<>();
        dictionnaire = new ArrayList<>();
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
            System.out.println(dictionnaire.size());
            System.out.println(dictionnaire);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dictionnaire;
    }

    //TODO prendre la racine du mot
    private String normalize(String word){
        word = Normalizer.normalize(word, Normalizer.Form.NFD);
        word = word.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return word.toLowerCase();
    }

    //TODO filtre stopwords
    private String[] splitWords(String words){
        return Arrays.asList(words.replaceAll("[^A-Za-z]", " ")
                .split(" ")).stream().filter(x -> x.length()>1).toArray(String[]::new);
    }

    @Override
    protected void processTitle(String title) {
        for(String word: splitWords(title)){
            String w = normalize(word);
            if(!occurences.containsKey(w))
                occurences.put(w,1);
            else occurences.put(w,occurences.get(w)+1);
        }
    }

    @Override
    protected void processText(String text) {
        for(String word: splitWords(text)){
            String w = normalize(word);
            if(!occurences.containsKey(w))
                occurences.put(w,1);
            else occurences.put(w,occurences.get(w)+1);
        }
    }

    @Override
    protected void processEnd() {

    }
}
