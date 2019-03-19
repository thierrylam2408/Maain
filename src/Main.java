import java.io.*;
import java.text.Normalizer;
import java.util.*;

public class Main {
	
	public static void main(String[] args) {
	    /*
	    //FILE
		//String inputXML = "/Volumes/Lexar/frwiki-20190120-pages-articles-multistream.xml";
		String inputXML = "frwiki-debut.xml";
		//String inputXML = "frwiki-ex.xml";
		String outputXML = "clean.xml";

		//CORPUS
		ArrayList<String> words_filter = new ArrayList<>(Arrays.asList(splitWords(normalize("mathématiques études"))));
		Corpus corpus = new Corpus(inputXML);
		System.out.println(corpus.generate(outputXML, words_filter));

        //DICO
		Dictionnaire dico = new Dictionnaire(outputXML);
		ArrayList<String> dictionnaire = dico.generate();
        System.out.println("Dico:"+dictionnaire);
		System.out.println("taille Dico:"+dictionnaire.size());

		//CLI_RMP
		CLI_RMP cliRMP = new CLI_RMP(outputXML, dictionnaire);
		CLI_RMP_Data CLI_RMP_data = cliRMP.generate();
		System.out.println("C:"+CLI_RMP_data.contenu);
		System.out.println("L:"+CLI_RMP_data.ligne);
		System.out.println("I:"+CLI_RMP_data.index);
		System.out.println("Mot->(Page,Frequence): "+CLI_RMP_data.mots_pages);

		//PageRank
        PageRank pg = new PageRank(CLI_RMP_data, dictionnaire,0.01, 0.85, 50	);
        TreeMap<Double, ArrayList<Integer>> rank = pg.generate();
        System.out.println("Rank->Page: "+rank);
        ArrayList<Integer> pagesOrder = joinPageOrderByRank(rank);

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
        FinalOutput out = new FinalOutput(result, indice_page_title, dictionnaire, pagesOrder);
        ObjectOutputStream oos = null;
        try{
            final FileOutputStream fichier = new FileOutputStream("file.ser");
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(out);
            oos.flush();
        }catch(final IOException ex){
            ex.printStackTrace();
        }
        */
        System.out.println("Simulation Serveur");
        simuServeur();
    }

    private static ArrayList<Integer> joinPageOrderByRank(TreeMap<Double, ArrayList<Integer>> rank){
        ArrayList<Integer> join = new ArrayList<>();
        for(Double r: rank.keySet())
            join.addAll(rank.get(r));
        return join;
    }

    public static void simuServeur(){
        //TEST SERVER
        ObjectInputStream ois = null;
        try{
            final FileInputStream fin = new FileInputStream("file.ser");
            ois = new ObjectInputStream(fin);
            final FinalOutput res = (FinalOutput) ois.readObject();
            String requete1 = "science algebre";
            String[] words = splitWords(requete1);
            ArrayList<ArrayList<Integer>> pages_requete = new ArrayList<>();
            for(String word: words){
                if(res.result.get(res.mots.indexOf(normalize(word))) != null)
                    pages_requete.add(res.result.get(res.mots.indexOf(normalize(word))));
                else pages_requete.add(new ArrayList<>());
            }
            System.out.println("Pages par mot:"+pages_requete);
            //Faire l'intersection des pages
            ArrayList<Integer> intersection = intersection(pages_requete, res.order_pages);
            System.out.println("Intersection:"+intersection);
            for(Integer i: intersection){
                System.out.println("Titre: "+res.pages.get(i));
            }

            /*
            ArrayList<Integer> e1 = new ArrayList<>();e1.addAll(Arrays.asList(3,1,2));
            ArrayList<Integer> e2 = new ArrayList<>();e2.addAll(Arrays.asList(1));
            ArrayList<Integer> e3 = new ArrayList<>();e3.addAll(Arrays.asList());
            ArrayList<Integer> e4 = new ArrayList<>();e4.addAll(Arrays.asList(1,2));
            ArrayList<Integer> o = new ArrayList<>();o.addAll(Arrays.asList(3,1,2));
            ArrayList<ArrayList<Integer>> l = new ArrayList<>();
            l.add(e2);l.add(e1);
            System.out.println(l);
            System.out.println(intersection(l,o));
            System.out.println();
            */
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

    private static ArrayList<Integer> intersection(ArrayList<ArrayList<Integer>> requetes, ArrayList<Integer> order){
	    if(requetes.size() == 1){
	        return requetes.get(0);
        }
        ArrayList<Integer> p1 = requetes.get(0);
	    ArrayList<Integer> p2 = requetes.get(1);
        int i1 = 0;
	    int i2 = 0;
	    ArrayList<Integer> intersection = new ArrayList<>();
        System.out.println("order: "+order);
        while(i1 < p1.size() && i2 < p2.size()){
            if(p1.get(i1).equals(p2.get(i2))){
                intersection.add(p1.get(i1));
                i1++;
                i2++;
            }else if(order.indexOf(p1.get(i1)) < order.indexOf(p2.get(i2))){
                i1++;
            }else{ //order.indexOf(p1.get(i1)) > order.indexOf(p2.get(i2))
                i2++;
            }
        }
        requetes.remove(0);
        requetes.remove(0);
        requetes.add(0, intersection);
        return intersection(requetes, order);
    }

}
