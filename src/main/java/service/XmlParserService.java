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
            elementParser(builder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void elementParser(String content) {
        char[] charArray = content.toCharArray();
        String rootElementName = "";
        String elementName = "";

        rootElementName = getElement(rootElementName, charArray);
        Element rootElement = new Element();
        rootElement.setName(rootElementName);
        String rootContent = getElementContent(rootElementName, charArray);

        if(rootContent.length() != 0){
            getElementChilds(rootContent);
        }


        System.out.println(rootElementName);

    }

    private void getElementChilds(String rootContent) {
        char[] charArray = rootContent.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if(charArray[i] == '<'){

            }
        }
    }

    private String getElementContent(String rootElementName, char[] charArray) {
        String elements = String.valueOf(charArray);
        String content = "";
        String tagOpenPrefix = "<" + rootElementName + ">";
        String tagCloseSuffix = "</" + rootElementName + ">";
        if (elements.startsWith(tagOpenPrefix) && elements.endsWith(tagCloseSuffix)) {
            content = elements.replace(tagOpenPrefix, "");
            content = content.replace(tagCloseSuffix, "");
        }
        return content;
    }

    private String getElement(String rootElementName, char[] chars) {
        if (chars[0] == '<') {
            for (int i = 1; i < chars.length; i++) {
                char ch = chars[i];
                if (ch != '>') {
                    rootElementName = rootElementName + ch;
                } else {
                    break;
                }
            }
        }
        return rootElementName;
    }

    private String getElementName(String elementName, char c) {
        if (c != '>') {
            elementName = elementName + c;
        }
        return elementName;
    }

    private String getElementChild(Element element) {
        String elementName = element.getName();
        StringBuilder childElementName = new StringBuilder();
        char[] charArray = elementName.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] != '>') {
                childElementName.append(charArray[i]);
            }
        }
        return childElementName.toString();
    }


    private Element getElement(char ch) {
        Element element = new Element();
        StringBuilder elementNameBuilder = new StringBuilder();
        if (ch != '>') {
            elementNameBuilder.append(ch);
        }
        element.setName(elementNameBuilder.toString());
        return element;
    }


}
