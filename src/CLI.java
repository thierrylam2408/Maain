
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CLI extends ProcessXML {

    private ArrayList<ArrayList<String>> liensExternes;
    private HashMap<String, Integer> numero_pages;
    private Pattern pattern = Pattern.compile("\\[\\[([^\\]]*)\\|[^\\]]*\\]\\]");

    public CLI(String xml){
        super(xml);
        liensExternes = new ArrayList<>();
        numero_pages = new HashMap<>();
    }

    public void generate(){
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
        System.out.println("C"+contenu);
        System.out.println("L"+ligne);
        System.out.println("I"+index);
    }



    @Override
    protected void processTitle(String title) {
        numero_pages.put(title, numero_pages.size());
    }

    @Override
    protected void processText(String text) {
        Matcher matcher = pattern.matcher(text);
        ArrayList<String> liensPagei = new ArrayList<>();
        while(matcher.find()){
            String lien = matcher.group(1);
            liensPagei.add(lien);
        }
        liensExternes.add(liensPagei);
    }

    @Override
    protected void processEnd() {

    }

}
