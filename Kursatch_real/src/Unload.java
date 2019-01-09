import com.opencsv.CSVWriter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

//выгрузка данных в файл
public class Unload{
    private static String[] moduleHeaders = {"ID", "Name", "Serial", "Install_date", "Max_Temperature", "Max_Frequency", "Max_Memory"};

    public void toXML(List<String> names ) {
        List<Object[]> data = ServerQuery.queryPerform(new Message("ReadModules")).getTableArguments();
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            doc = docBuilder.newDocument();
            Element root = doc.createElement("Modules");
            doc.appendChild(root);
            for (int j = 0; j < names.size(); j++) {
                Element moduleName = doc.createElement(replaceSpaces(names.get(j)));
                root.appendChild(moduleName);
                int k;
                for (k = 0; k < data.size(); k++){
                    if (data.get(k)[1].toString().equals(names.get(j))) break;
                }
                for (int i = 2; i < 7; i++) {
                    Element attribute = doc.createElement(moduleHeaders[i]);
                    moduleName.appendChild(attribute);
                    attribute.appendChild(doc.createTextNode(data.get(k)[i].toString()));
                }
            }

        } catch (ParserConfigurationException p){}

        //set up a transformer
        TransformerFactory transfac = TransformerFactory.newInstance();
        Transformer trans = null;
        try { trans = transfac.newTransformer();}
        catch (TransformerConfigurationException e){ System.err.println("Failed "+e); }
        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");

        //create string from xml tree
        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        try { trans.transform(source, result); }
        catch (TransformerException e) { System.err.println("Failed "+e); }
        String xmlString = sw.toString();

        System.out.println("Here's the xml:\n\n" + xmlString);
        try {
            File newTextFile = new File("C:\\Projects\\Modules.xml");
            FileWriter fw = new FileWriter(newTextFile);
            fw.write(xmlString);
            fw.close();
        } catch (IOException iox) {
            //do stuff with exception
            iox.printStackTrace();
        }

    }

    public void toXSL(List<String> names){
        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet("Modules");
        List<Object[]> data = ServerQuery.queryPerform(new Message("ReadModules")).getTableArguments();
        Row headers = sheet.createRow(0);
        for(int j = 0; j < 7; j++){
            try {
                headers.createCell(j).setCellValue(moduleHeaders[j]);
            } catch (NullPointerException ex) {
                System.err.println("Failed "+ex);
            }
        }

        for (int i = 1; i <= names.size(); i++){
            Row row = sheet.createRow(i);
            int k;
            for (k = 0; k < data.size(); k++){
                if (data.get(k)[1].toString().equals(names.get(i-1))) break;
            }

            for(int j = 0; j < 7; j++){
                try {
                    row.createCell(j).setCellValue(data.get(k)[j].toString());
                } catch (NullPointerException ex) {
                    System.err.println("Failed "+ex);
                }
            }
        }
        try {
            book.write(new FileOutputStream("C:\\Projects\\Modules.xsl"));
            book.close();
        } catch (IOException e) { System.err.println("Failed "+e); }
    }

    public void toCSV(List<String> names){
        try {
            List<String[]> list = new ArrayList<>();
            list.add(moduleHeaders);

            //Create record
            List<Object[]> data = ServerQuery.queryPerform(new Message("ReadModules")).getTableArguments();
            for (int i = 0; i < names.size(); i++){
                int k;
                for (k = 0; k < data.size(); k++){
                    if (data.get(k)[1].toString().equals(names.get(i))) break;
                }
                String[] record = new String[7];
                for(int j = 0; j < 7; j++){
                    try {
                        record[j] = data.get(k)[j].toString();
                    } catch (NullPointerException ex) {
                        System.err.println("Failed "+ex);
                    }
                }
                list.add(record);
            }

            try {
                FileOutputStream fos = new FileOutputStream("C:\\Projects\\Modules.csv");
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                CSVWriter writer = new CSVWriter(osw);
                writer.writeAll(list);
                writer.close();
            } catch (FileNotFoundException e) {}
        }
        catch (IOException e) {System.err.println("Failed "+e);}
    }

    private String replaceSpaces(String s) {
        s = s.replace(" ", "_");
        return s;
    }
}
