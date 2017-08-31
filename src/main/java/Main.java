import service.XmlParserService;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File file = new File("C:\\Users\\CodeAcademy\\IdeaProjects\\Romas Noreika\\codeacademy-xmlparser\\src\\main\\java\\xmlparser.xml");
        XmlParserService service = new XmlParserService();
        service.parseXmlFile(file);
    }
}
