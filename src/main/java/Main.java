import data.Element;
import service.XmlParserService;

import java.io.File;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        XmlParserService xmlParserService = new XmlParserService();
        File file = new File("C:\\Users\\CodeAcademy\\IdeaProjects\\Romas Noreika\\codeacademy-xmlparser\\src\\main\\java\\xmlparser.xml");
        xmlParserService.parseXmlFile(file);
        List<Element> elements = xmlParserService.getElements();
        elements.stream().forEach(element -> System.out.println(element));
    }
}
