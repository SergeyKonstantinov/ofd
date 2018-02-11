package com.sergey.tasks.parsers;

import com.sergey.tasks.entity.objects.Command;
import com.sergey.tasks.entity.objects.ResultCode;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.sergey.tasks.servlets.HttpServlet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

/**
 * Created by Sergey on 11.02.2018.
 */
public class HttpResponseParser {
    private HttpServlet.ResponseEntity responseEntity;

    public HttpResponseParser(HttpServlet.ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }
    public String parse() throws ParserConfigurationException, TransformerException {

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        // root elements
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("response");
        doc.appendChild(rootElement);

        Element resultCode = doc.createElement("result-code");
        resultCode.setTextContent(String.valueOf(responseEntity.getResultCode().ordinal()));
        rootElement.appendChild(resultCode);

        if (responseEntity.getCommand()== Command.GET_BALANCE && responseEntity.getResultCode() == ResultCode.OK){
            Element extra = doc.createElement("extra");
            extra.setTextContent(String.valueOf(responseEntity.getBalance()));
            Attr attr = doc.createAttribute("name");
            attr.setValue("balance");
            extra.setAttributeNode(attr);
            rootElement.appendChild(extra);
        }

        doc .setXmlStandalone(true);

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();

        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(doc), new StreamResult(writer));
        String output = writer.getBuffer().toString();
        return output;

    }
}
