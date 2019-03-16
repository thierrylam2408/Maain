import java.io.*;
import java.text.Normalizer;
import java.util.*;

public class Main {
	
	public static void main(String[] args) {
	    //FILE
		//String inputXML = "/Volumes/Lexar/frwiki-20190120-pages-articles-multistream.xml";
		//String inputXML = "frwiki-debut.xml";
		String inputXML = "frwiki-ex.xml";
		String outputXML = "clean.xml";

		//CORPUS
		ArrayList<String> words_filter = new ArrayList<>(Arrays.asList("algorithme", "complexité", "calculer", "temps"));
		Corpus corpus = new Corpus(inputXML);
		//System.out.println(corpus.generate(outputXML, words_filter));

        //DICO
		Dictionnaire dico = new Dictionnaire(inputXML);
		ArrayList<String> dictionnaire = dico.generate();
		System.out.println("Dico:"+dictionnaire);

		//CLI_RMP
		CLI_RMP cliRMP = new CLI_RMP(inputXML, dictionnaire);
		CLI_RMP_Data CLI_RMP_data = cliRMP.generate();
		System.out.println("C:"+CLI_RMP_data.contenu);
		System.out.println("L:"+CLI_RMP_data.ligne);
		System.out.println("I:"+CLI_RMP_data.index);
		System.out.println("Mot->(Page,Frequence): "+CLI_RMP_data.mots_pages);

		//PageRank
        PageRank pg = new PageRank(CLI_RMP_data, dictionnaire,0.001, 0.85, 3	); //k ~ 1000 en vrai
        TreeMap<Double, ArrayList<Integer>> rank = pg.generate();
        System.out.println("Rank->Page: "+rank);

        HashMap<Integer, ArrayList<Integer>> result = new HashMap<>();
        //indice mot -> indices pages ordonnées par rank et frequence important
		for(Integer indice_mot: CLI_RMP_data.mots_pages.keySet()){
		    ArrayList<Integer> pages = new ArrayList<>();
		    for(Double r: rank.keySet()){
		        for(Integer indice_page: rank.get(r)){
                    //if(CLI_RMP_data.mots_pages.get(indice_mot).containsKey(indice_page))
                        //System.out.println(CLI_RMP_data.mots_pages.get(indice_mot).get(indice_page));
		            if(CLI_RMP_data.mots_pages.get(indice_mot).containsKey(indice_page) &&
                            CLI_RMP_data.mots_pages.get(indice_mot).get(indice_page) >= 1/5000){
                        pages.add(indice_page);
                    }
                }
            }
            result.put(indice_mot, pages);
		}
		System.out.println("Mot->Pages ordonnees par Rank:"+result);

        //Final output
        HashMap<Integer, String> indice_page_title = new HashMap<>();
        for(Map.Entry<String, Integer> entry : CLI_RMP_data.numero_pages.entrySet()){
            indice_page_title.put(entry.getValue(), entry.getKey());
        }
        FinalOutput out = new FinalOutput(result, indice_page_title, dictionnaire);
        ObjectOutputStream oos = null;
        try{
            final FileOutputStream fichier = new FileOutputStream("file.ser");
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(out);
            oos.flush();
        }catch(final IOException ex){
            ex.printStackTrace();
        }


        //TEST SERVER
        ObjectInputStream ois = null;
        try{
            final FileInputStream fin = new FileInputStream("file.ser");
            ois = new ObjectInputStream(fin);
            final FinalOutput res = (FinalOutput) ois.readObject();
            String requete1 = "abstraite algebre";
            String[] words = splitWords(requete1);
            ArrayList<ArrayList<Integer>> pages_requete = new ArrayList<>();
            for(String word: words){
                pages_requete.add(res.result.get(res.mots.indexOf(normalize(word))));
            }
            System.out.println("Pages par mot:"+pages_requete);
            //Faire l'intersection des pages
            ArrayList<Integer> intersection = intersection(pages_requete);
            System.out.println("Intersection:"+intersection);
        }catch(final IOException ex ){
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    private static String normalize(String word){
        word = Normalizer.normalize(word, Normalizer.Form.NFD);
        word = word.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return word.toLowerCase();
    }

    private static String[] splitWords(String words){
        return Arrays.asList(words.replaceAll("[^A-Za-z]", " ")
                .split(" ")).stream().filter(x -> x.length()>1).toArray(String[]::new);
    }

    private static ArrayList<Integer> intersection(ArrayList<ArrayList<Integer>> pages){
	    //TODO
	    return null;
    }

}
