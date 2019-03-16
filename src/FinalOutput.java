import java.util.ArrayList;
import java.util.HashMap;

public class FinalOutput implements java.io.Serializable {

    public HashMap<Integer, ArrayList<Integer>> result;
    public HashMap<Integer, String> pages;
    public ArrayList<String> mots;

    public FinalOutput(HashMap<Integer, ArrayList<Integer>> result, HashMap<Integer, String> pages, ArrayList<String> mots){
        this.result = result;
        this.pages = pages;
        this.mots = mots;
    }


}
