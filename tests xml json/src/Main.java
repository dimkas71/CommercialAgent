import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.File;
import java.io.FileReader;
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


    public static void main(String[] args) throws IOException, SAXException, ClassNotFoundException {

//        List<Person> persons = getPersonsFromXML("./data/demo.xml");
//        savePersonsToSQLite(persons);

        List<Person> persons = getPersonsFromSQLite();

        for (Person person : persons) {
            System.out.println(person);
        }


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

    private static void savePersonsToXML(List<Person> persons) {
        //TODO: write algorithm for saving list of persons to xml file ("./data/saved.xml")
    }
}
