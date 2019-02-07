import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import javax.xml.stream.*;

public class Corpus extends ProcessXML {

    private int create;
    private XMLStreamWriter out;
    private ArrayList<String> words_filter;

    public Corpus(String xmlin){
        super(xmlin);
        create = 0;
    }

    //TODO FILTER AVEC words_filter
    public int generate(String xmlout, ArrayList<String> words_filter) {
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
        try {
            create++;
            out.writeStartElement("page");
            out.writeStartElement("title");
            out.writeCharacters(title);
            out.writeEndElement();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processText(String text) {
        try{
            if(intersectionNonNul((ArrayList<String>)Arrays.asList(text.split(" ")), words_filter)){

            }
            out.writeStartElement("text");
            out.writeCharacters(text);
            out.writeEndElement();
            out.writeEndElement();
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

    private boolean intersectionNonNul(ArrayList<String> l1, ArrayList<String> l2){
        for (String aL1 : l1) {
            if (l2.contains(aL1)) {
                return true;
            }
        }
        return false;
    }
}
