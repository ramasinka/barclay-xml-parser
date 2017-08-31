package service;

import data.Element;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class XmlParserService {

    List<Element> elements = new ArrayList<Element>();

    public void parseXmlFile(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String content = getContent(bufferedReader);
            parseXmlElements(content, null);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found", e);
        }
    }

    private String getContent(BufferedReader bufferedReader) {
        int c;
        StringBuilder builderContent = new StringBuilder();
        try {
            while ((c = bufferedReader.read()) != -1) {
                char ch = (char) c;
                builderContent.append(ch);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builderContent.toString();
    }

    private List<Element> parseXmlElements(String content, Element element) {
        char[] contentCharArray = content.toCharArray();
        if (element == null) {
            element = createElement(content);
        }
        String elementName = element.getName();
        elements.add(element);
        if (elementName != null) {
            String elementContent = getElementContent(elementName, contentCharArray);
            String lastCharacter = elementName.substring(elementName.length() - 1);
            if (lastCharacter.equals("/")) {
                element.setName(elementName.replace("/", ""));
            }
            if (elementContent.length() != 0) {
                List<Element> childElements = getChildElements(elementContent);
                if (!childElements.isEmpty()) {
                    element.setElementList(childElements);
                    for (Element el : childElements) {
                        parseXmlElements(content, el);
                    }
                }
            }
        }
        return elements;
    }

    private List<Element> getChildElements(String elementContent) {
        List<Element> childElements = new ArrayList<Element>();
        String elementName = "";
        String childContent = "";
        char[] charArray = elementContent.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '<') {
                for (int j = i + 1; j < charArray.length; j++) {
                    if (charArray[j] != '>') {
                        elementName = elementName + charArray[j];
                    } else {

                        if (!elementName.contains("/")) {
                            if (elementContent.contains("/" + elementName)) {
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
                                checkIsElementClosed(elementName, elementContent);
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

    private void checkIsElementClosed(String elementName, String elementContent) {
        if (!elementContent.contains("/" + elementName)) {
            throw new RuntimeException("Element with name: " + elementName + " must be closed");
        }
    }

    private String getElementContent(String elementName, char[] charArray) {
        String elements = String.valueOf(charArray);
        String elementContent = "";
        String tagOpenPrefix = "<" + elementName + ">";
        String tagCloseSuffix = "</" + elementName + ">";
        if (!elements.contains(tagCloseSuffix) && elements.contains(tagOpenPrefix) && elementName.substring(elementName.length() - 1).equals("/")) {
            return "";
        }
        elementContent = elements.substring(elements.indexOf(tagOpenPrefix) + tagOpenPrefix.length(), elements.indexOf(tagCloseSuffix));
        return elementContent;
    }

    private Element createElement(String content) {
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
