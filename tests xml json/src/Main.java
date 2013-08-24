import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends DefaultHandler {

    public List<Person> persons = new ArrayList<Person>();

    public Main () {
        super();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);

        if (localName.equals("person")) {

            persons.add(new Person(attributes.getValue("name"), Integer.parseInt(attributes.getValue("age"))));

        }

    }


    public static void main(String[] args) throws IOException, SAXException, ClassNotFoundException, XmlPullParserException {


        //final String xmlPathIn = "./data/demo.xml";
        final String xmlPathIn = "./data/saved.xml";

        List<Person> persons = getPersonsFromXML(xmlPathIn);
        savePersonsToSQLite(persons);

/*
        List<Person> persons = getPersonsFromSQLite();

        for (Person person : persons) {
            System.out.println(person);
        }

        savePersonsToXML(persons);
*/

    }

    private static void savePersonsToSQLite(List<Person> persons) throws ClassNotFoundException {
        //TODO: load persons to sqlite3-type persons.db
        Class.forName("org.sqlite.JDBC");

        Connection connection = null;

        try {

            connection = DriverManager.getConnection("jdbc:sqlite:./data/simple.db");

            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (name TEXT, age INTEGER)");



            //TODO: delete this code in production
            /*
            for (Person person : saxHandler.persons) {
                //insert values into database....
                String sqlStmt = "insert into person values (" +"'" + person.getName() + "', " + String.format("%d", person.getAge()) + ");";
                statement.addBatch(sqlStmt);
            }

            statement.executeBatch();

            */


            PreparedStatement preparedStatement = connection.prepareStatement("insert into person values(?, ?);");



            for (Person person : persons) {

                preparedStatement.setString(1, person.getName());
                preparedStatement.setInt(2, person.getAge());

                preparedStatement.addBatch();

            }

            preparedStatement.executeBatch();

            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    private static List<Person> getPersonsFromXML(String xmlPath) throws SAXException, IOException {

        Main saxHandler = new Main();
        final File xmlFile = new File(xmlPath);

        if (xmlFile.isFile()) {


            if (xmlFile.exists()) {

                XMLReader xmlReader = XMLReaderFactory.createXMLReader();

                xmlReader.setContentHandler(saxHandler);
                xmlReader.setErrorHandler(saxHandler);

                xmlReader.parse(new InputSource(new FileReader(xmlFile)));

            }

        }
        return saxHandler.persons;
    }

    private static List<Person> getPersonsFromSQLite() throws ClassNotFoundException {

        List<Person> persons = new ArrayList<Person>();


        Class.forName("org.sqlite.JDBC");

        Connection connection = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:./data/simple.db");

            PreparedStatement preparedStatement = connection.prepareStatement("select name, age from person;");

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                persons.add(new Person(resultSet.getString("name"), resultSet.getInt("age")));
            }



        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }



        return persons;
    }

    private static void savePersonsToXML(List<Person> persons) throws XmlPullParserException, IOException {
        //TODO: write algorithm for saving list of persons to xml file ("./data/saved.xml")


        //region code below don't worked, because of it needs to have a factory to create XmlSerializer...
       /* XmlSerializer xmlSerializer = XmlPullParserFactory.newInstance().newSerializer();


        xmlSerializer.setOutput(new FileWriter(new File("./data/saved.xml")));

        xmlSerializer.startDocument("<?xml version=\"1.0\" encoding=\"utf-8\"?>", true);

        xmlSerializer.startTag("", "persons")
            .startTag("", "person")
            .attribute("", "name", "Dmitriy")
            .endTag("", "person")
            .endTag("", "persons")
            .endDocument();

        xmlSerializer.flush();*/
        //endregion



        final String startDocument = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";


        File xmlFile = new File("./data/saved.xml");
        xmlFile.createNewFile();

        FileWriter fileWriter = new FileWriter(xmlFile);

        fileWriter.write(startDocument);

        fileWriter.write("<persons>" + "\n"); //start tag persons
        for (Person person : persons) {
            StringBuilder sb = new StringBuilder();
            sb.append("\t<person name=")
                    .append('"')
                    .append(person.getName())
                    .append('"')
                    .append(" age=")
                    .append('"')
                    .append(String.format("%d", person.getAge()))
                    .append('"')
                    .append(" />");
            fileWriter.write(sb.toString() + "\n");

        }

        fileWriter.write("</persons>" + "\n"); //end tag persons

        fileWriter.close();





    }
}
