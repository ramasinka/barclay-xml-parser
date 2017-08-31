package service;

import data.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XmlParserService {

    List<Element> elements = new ArrayList<Element>();

    public void handleFile(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            handleCharacter(bufferedReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleCharacter(BufferedReader bufferedReader) {
        int c;
        StringBuilder builder = new StringBuilder();
        try {
            while ((c = bufferedReader.read()) != -1) {
                char ch = (char) c;
                builder.append(ch);
            }
            handleElementParser(builder.toString(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Element> handleElementParser(String content, Element rootElement) {
        char[] charArray = content.toCharArray();
        if (rootElement == null) {
            rootElement = createRootElement(content);
        }
        String rootElementName = rootElement.getName();
        elements.add(rootElement);
        if (rootElementName != null) {
            String rootContent = getElementContent(rootElementName, charArray);
            List<Element> childRootElements = getChildRootElements(rootContent);
            if (childRootElements.size() != 0) {
                rootElement.setElementList(childRootElements);
                for (Element element : childRootElements) {
                    handleElementParser(content, element);
                }
            }
        }
        return elements;
    }

    private List<Element> getChildRootElements(String rootContent) {
        List<Element> childElements = new ArrayList<Element>();
        String elementName = "";
        String childContent = "";
        char[] charArray = rootContent.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '<') {
                for (int j = i + 1; j < charArray.length; j++) {
                    if (charArray[j] != '>') {
                        elementName = elementName + charArray[j];
                    }
                    if (charArray[j] == '>') {
                        if (!elementName.contains("/") && rootContent.contains("/" + elementName)) {
                            if (childContent.contains(elementName)) {
                                elementName = "";
                                break;
                            } else {
                                childContent = getElementContent(elementName, charArray);
                                Element element = new Element();
                                element.setName(elementName);
                                childElements.add(element);
                                elementName = "";
                            }
                        } else {
                            elementName = "";
                        }
                        break;
                    }
                }
            }
        }
        return childElements;
    }

    private String getElementContent(String rootElementName, char[] charArray) {
        String elements = String.valueOf(charArray);
        String tagOpenPrefix = "<" + rootElementName + ">";
        String tagCloseSuffix = "</" + rootElementName + ">";
        String result = elements.substring(elements.indexOf(tagOpenPrefix) + tagOpenPrefix.length(), elements.indexOf(tagCloseSuffix));
        return result;
    }

    private Element createRootElement(String content) {
        Element element = new Element();
        String elementName = content.substring(content.indexOf("<") + 1, content.indexOf(">"));
        element.setName(elementName);
        return element;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
