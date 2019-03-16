import java.util.ArrayList;
import java.util.HashMap;

public class FinalOutput implements java.io.Serializable {

    public HashMap<Integer, ArrayList<Integer>> result;
    public HashMap<Integer, String> pages;
    public ArrayList<String> mots;
    public ArrayList<Integer> order_pages;

    public FinalOutput(HashMap<Integer, ArrayList<Integer>> result, HashMap<Integer, String> pages, ArrayList<String> mots, ArrayList<Integer> order_pages){
        this.result = result;
        this.pages = pages;
        this.mots = mots;
        this.order_pages = order_pages;
    }


}
