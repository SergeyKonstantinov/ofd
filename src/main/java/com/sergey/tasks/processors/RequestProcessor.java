package com.sergey.tasks.processors;

import com.sergey.tasks.entity.objects.Command;
import com.sergey.tasks.entity.objects.CommandEntity;
import com.sergey.tasks.entity.exceptions.BadCommandNameException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.sergey.tasks.parsers.HttpRequestParser;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;

/**
 * Created by Sergey on 10.02.2018.
 */
public class RequestProcessor {
    private HttpRequestParser.HttpRequest httpRequest;

    public RequestProcessor(HttpRequestParser.HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public CommandEntity process() throws ParserConfigurationException, IOException, SAXException, BadCommandNameException {

        CommandEntity commandEntity = new CommandEntity();
        commandEntity.setBalance(new BigDecimal(0));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(httpRequest.getBodyRequest())));

        Element root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i=0;i<nodeList.getLength();i++){
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {

                Element element = (Element)node;

                if (element.getTagName().equals("request-type"))
                    commandEntity.setCommand(Command.getCommandByName(element.getTextContent()));

                if (element.getTagName().equals("extra"))
                    if (element.hasAttribute("name"))
                        if(element.getAttribute("name").equals("login"))
                            commandEntity.setLogin(element.getTextContent());
                        if(element.getAttribute("name").equals("password"))
                            commandEntity.setPassword(element.getTextContent());
            }
        }
        return commandEntity;
    }
}
