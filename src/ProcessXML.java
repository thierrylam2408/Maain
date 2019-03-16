import javax.xml.XMLConstants;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLResolver;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import java.io.*;

public abstract class ProcessXML {

    
	private static final XMLResolver NO_OP_XML_RESOLVER = null;

	protected abstract void processTitle(String title);
    protected abstract void processText(String text);
    protected abstract void processEnd();
    private String xml;

    public ProcessXML(String xml){
        this.xml = xml;
    }

    protected void process(){
    	
        XMLInputFactory factory = XMLInputFactory.newInstance();
        
         factory.setProperty("javax.xml.stream.isCoalescing", true);
         //factory.setFeature(, false);
         factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
         
         //factory.setFeature("dbvdg",false);

         //factory.setXMLResolver(NO_OP_XML_RESOLVER);
        
        
        File filein = new File(xml);
        try {
            XMLEventReader xmlEventReader = factory.createXMLEventReader(new FileReader(filein));
            while (xmlEventReader.hasNext()) {
                XMLEvent xmlEvent = xmlEventReader.nextEvent();
                if(xmlEvent.isStartElement()){
                    StartElement startElement = xmlEvent.asStartElement();
                    if(startElement.getName().getLocalPart().equals("title")){
                        xmlEvent = xmlEventReader.nextEvent();
                        if(xmlEvent instanceof Characters){
                            processTitle(xmlEvent.asCharacters().getData());
                        }
                    }else if(startElement.getName().getLocalPart().equals("text")){
                        xmlEvent = xmlEventReader.nextEvent();
                        if(xmlEvent instanceof Characters) {
                            processText(xmlEvent.asCharacters().getData());
                        }
                    }
                }
                else if(xmlEvent.isEndDocument()){
                    processEnd();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
