import java.util.ArrayList;
import java.util.HashMap;

public class CLI_RMP_Data {
    ArrayList<Double> contenu;
    ArrayList<Integer> ligne;
    ArrayList<Integer> index;
    HashMap<Integer, HashMap<Integer, Double>> mots_pages;
    HashMap<String, Integer> numero_pages;
    int nbPage;

    public CLI_RMP_Data(ArrayList<Double> c, ArrayList<Integer> l, ArrayList<Integer> i, HashMap<Integer,
            HashMap<Integer, Double>> mp, HashMap<String, Integer> np, int nbp){
        contenu = c;
        ligne = l;
        index = i;
        mots_pages = mp;
        numero_pages = np;
        nbPage = nbp;
    }
}
