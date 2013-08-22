import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, SAXException {
        final File xmlFile = new File("./data/demo.xml");

        if (xmlFile.isFile()) {


            if (xmlFile.exists()) {

                InputSource inputSource = new InputSource(xmlFile.toURI().toString());

                inputSource.setCharacterStream(new FileReader(xmlFile));

                XMLReader xmlReader = XMLReaderFactory.createXMLReader();

                DefaultHandler defaultHandler = new DefaultHandler();


                int b = 0;


            }


        }






    }
}
