package service;

import data.Element;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class XmlParserServiceTest {

    private XmlParserService xmlParserService;

    @Before
    public void setUp() {
        xmlParserService = new XmlParserService();
    }

    @Test
    public void shouldParseXmlWithOneRoot() {
        xmlParserService.parseXmlFile(new File("src/test/java/service/xml/correct_one_element"));
        List<Element> elements = xmlParserService.getElements();
        String elementName = elements.get(0).getName();
        assertEquals("root", elementName);
    }

    @Test
    public void shouldParseXmlWithTwoRoots() {
        xmlParserService.parseXmlFile(new File("src/test/java/service/xml/correct_two_elements"));
        List<Element> elements = xmlParserService.getElements();
        String firstElementName = elements.get(0).getName();
        String secondElementName = elements.get(1).getName();

        assertEquals("root", firstElementName);
        assertEquals("first", secondElementName);
    }

    @Test
    public void shouldParseXmlWithChildElement() {
        xmlParserService.parseXmlFile(new File("src/test/java/service/xml/correct_child_elements"));
        List<Element> elements = xmlParserService.getElements();

        String rootElement = elements.get(0).getName();
        assertEquals("root", rootElement);

        String firstElementName = elements.get(1).getName();
        assertEquals("first", firstElementName);

        String childElementName = elements.get(2).getName();
        assertEquals("child", childElementName);

        String secondElementName = elements.get(3).getName();
        assertEquals("second", secondElementName);


        Element element = elements.get(1);
        Element childElement = element.getElementList().get(0);
        assertEquals("child", childElement.getName());
    }

    @Test(expected = RuntimeException.class)
    public void throwExceptionThenElementNotClosed() {
        try {
            xmlParserService.parseXmlFile(new File("src/test/java/service/xml/wrong_element_not_closed"));
        } catch (RuntimeException ex) {
            String expectedMessage = "Element with name: test must be closed";
            assertEquals(expectedMessage, ex.getMessage());
            throw ex;
        }
    }

    @Test
    public void shouldParseElementWithCloseTag() {
        xmlParserService.parseXmlFile(new File("src/test/java/service/xml/correct_close_element"));
        List<Element> elements = xmlParserService.getElements();
        Element element = elements.get(0);
        assertEquals("root", element.getName());
    }

    @Test
    @Ignore("TODO implementing <element/>")
    public void shouldParseXml(){
        xmlParserService.parseXmlFile(new File("src/test/java/service/xml/correct"));
        List<Element> elements = xmlParserService.getElements();
        assertEquals(4, elements.size());
    }
}
