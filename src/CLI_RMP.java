
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLI_RMP extends ProcessXML {

    private ArrayList<ArrayList<String>> liensExternes;
    private HashMap<String, Integer> numero_pages; //place dans le fichier xml
    private Pattern pattern = Pattern.compile("\\[\\[([^\\]]*)\\|[^\\]]*\\]\\]"); //ou sans "|" voir l328 de frwiki-ex
    private ArrayList<String> dictionnaire;
    private HashMap<Integer, HashMap<Integer, Double>> mots_pages;
    //Indice du mot ds le dictionnaire -> (Indice de la page ds numero_page, frequence du mot ds la page)
    private String lastTitle;
    private int nbPage;

    public CLI_RMP(String xml, ArrayList<String> dictionnaire){
        super(xml);
        liensExternes = new ArrayList<>();
        numero_pages = new HashMap<>();
        this.dictionnaire = dictionnaire;
        this.mots_pages = new HashMap<>();
        this.nbPage = 0;
    }

    public CLI_RMP_Data generate(){
        process();

        ArrayList<Double> contenu = new ArrayList<>();
        ArrayList<Integer> ligne = new ArrayList<>();
        ArrayList<Integer> index = new ArrayList<>();
        ligne.add(0);
        for(int i=0; i<liensExternes.size(); i++){
            ArrayList<Integer> indexLiens = new ArrayList<>();
            for(int j=0; j<liensExternes.get(i).size(); j++){
                if(numero_pages.containsKey(liensExternes.get(i).get(j))){
                    indexLiens.add(numero_pages.get(liensExternes.get(i).get(j)));
                }
            }
            for(int k=0; k<indexLiens.size(); k++) {
                contenu.add((double)1/indexLiens.size());
                index.add(indexLiens.get(k));
            }
            ligne.add(ligne.get(i) + indexLiens.size());
        }
        return new CLI_RMP_Data(contenu, ligne, index, mots_pages, numero_pages, nbPage);
    }



    @Override
    protected void processTitle(String title) {
        title = title.toLowerCase();
        numero_pages.put(title, numero_pages.size());
        lastTitle = title;
        nbPage++;
    }

    @Override
    protected void processText(String text) {
        Matcher matcher = pattern.matcher(text);
        ArrayList<String> liensPagei = new ArrayList<>();
        while(matcher.find()){
            String lien = matcher.group(1);
            liensPagei.add(lien.toLowerCase());
        }
        liensExternes.add(liensPagei);

        int indicePage = numero_pages.get(lastTitle);
        ArrayList<String> words = new ArrayList<String>(Arrays.asList(text.split(" ")));
        for(String word: words){
            //TODO normaliser word
            if(dictionnaire.contains(word)){
                int indiceWord = dictionnaire.indexOf(word);
                if(mots_pages.containsKey(indiceWord)){
                    if(mots_pages.get(indiceWord).containsKey(indicePage)){
                        mots_pages.get(indiceWord).put(indicePage, mots_pages.get(indiceWord).get(indicePage)+1);
                    }else{
                        mots_pages.get(indiceWord).put(indicePage, 1.0);
                    }
                }else{
                    HashMap<Integer, Double> page = new HashMap<>();
                    page.put(indicePage, 1.0);
                    mots_pages.put(indiceWord, page);
                }
            }
        }
        for(Integer indiceMot: mots_pages.keySet()){
            for(Integer indiceP: mots_pages.get(indiceMot).keySet()){
                if(indiceP == nbPage-1) {
                    mots_pages.get(indiceMot).put(indiceP, mots_pages.get(indiceMot).get(indiceP)/words.size());
                }
            }
        }
    }

    @Override
    protected void processEnd() {
    }

}
