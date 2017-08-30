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

        if (rootContent.length() != 0) {
            Element elementChild = new Element();
            List<String> childElementsByContent = getElementsByContent(rootContent);
            for (String childContent: childElementsByContent) {
                if (getElementsByContent(childContent).size() != 0) {
                    elementParser(childContent);
                } else {
                  /*  String[] splitElementName = childContent.split("/");
                    if(splitElementName[0].equals(splitElementName[1])){
                        elementChild.setName(splitElementName[0]);
                        elements.add(elementChild);
                    }*/
                }
            }
        }
        rootElement.setElementList(elements);
        System.out.println(elements);
        System.out.println(rootElementName);

    }

    private List<String> getElementsByContent(String rootContent) {
       List<String> names = new ArrayList<String>();
        String elementName = "";
        char[] charArray = rootContent.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] == '<') {
                for (int j = i + 1; j < charArray.length; j++) {
                    if (charArray[j] != '>') {
                        elementName = elementName + charArray[j];
                    }
                    if(charArray[j] == '>'){
                        names.add(elementName);
                        elementName = "";
                        break;
                    }
                }
            }
        }
        return names;
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
}
