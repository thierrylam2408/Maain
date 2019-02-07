import java.util.ArrayList;
import java.util.Arrays;

public class Main {
	
	public static void main(String[] args) {
	    //FILE
		//String inputXML = "/Volumes/Lexar/frwiki-20190120-pages-articles-multistream.xml";
		String inputXML = "frwiki.xml";
		String outputXML = "clean.xml";

		//CORPUS
		ArrayList<String> words_filter = new ArrayList<>(Arrays.asList("algorithme", "complexité", "calculer", "temps"));
		Corpus corpus = new Corpus(inputXML);
		//System.out.println(corpus.generate(outputXML, words_filter));

        //DICO
		Dictionnaire dico = new Dictionnaire(inputXML);
		dico.generate();

		//CLI
		CLI cli = new CLI(inputXML);
		cli.generate();
	}

}
