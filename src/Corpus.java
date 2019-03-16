import java.io.*;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.stream.*;

public class Corpus extends ProcessXML {

    private int create;
    private XMLStreamWriter out;
    private ArrayList<String> words_filter;
    private String currentTitle;

    public Corpus(String xmlin){
        super(xmlin);
        create = 0;
    }

    public int generate(String xmlout, ArrayList<String> words_filter) {
        currentTitle = "";
        File file = new File(xmlout);
        this.words_filter = words_filter;
        try {
            OutputStream outputStream = new FileOutputStream(file);
            out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(outputStream, "utf-8"));
            out.writeStartDocument();
            out.writeStartElement("pages");
            super.process();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return create;

    }

    @Override
    public void processTitle(String title) {
        currentTitle = title;
    }

    //TODO filtrer avec les words_filter
    @Override
    public void processText(String text) {
        text = normalize(text);
        ArrayList<String> textSplit = new ArrayList<>(Arrays.asList(splitWords(text)));
        try{
            if(intersectionNonNul(textSplit) || words_filter.isEmpty()){
                create++;
                out.writeStartElement("page");
                out.writeStartElement("title");
                out.writeCharacters(currentTitle);
                out.writeEndElement();
                out.writeStartElement("text");
                out.writeCharacters(text);
                out.writeEndElement();
                out.writeEndElement();
            }
        }catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void processEnd() {
        try{
            out.writeEndElement();
            out.writeEndDocument();
            out.close();
        }catch (XMLStreamException e){
            e.printStackTrace();
        }
    }

    private boolean intersectionNonNul(ArrayList<String> words){
        for (String word : words_filter) {
            if (words.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private String normalize(String word){
        word = Normalizer.normalize(word, Normalizer.Form.NFD);
        word = word.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return word.toLowerCase();
    }

    private String[] splitWords(String words){
        return Arrays.asList(words.replaceAll("[^A-Za-z]", " ")
                .split(" ")).stream().filter(x -> x.length()>1).toArray(String[]::new);
    }
}
