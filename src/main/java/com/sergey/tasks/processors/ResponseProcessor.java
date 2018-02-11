package com.sergey.tasks.processors;

import com.sergey.tasks.parsers.HttpResponseParser;
import com.sergey.tasks.servlets.HttpServlet;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * Created by Sergey on 11.02.2018.
 */
public class ResponseProcessor {
    private HttpServlet.ResponseEntity responseEntity;

    public ResponseProcessor(HttpServlet.ResponseEntity responseEntity) {
        this.responseEntity = responseEntity;
    }
    public String process() throws TransformerException, ParserConfigurationException {

        StringBuilder stringBuilder = new StringBuilder();
        HttpResponseParser httpResponseParser = new HttpResponseParser(responseEntity);
        String responseBody = httpResponseParser.parse();

        stringBuilder.append("HTTP/1.1 200 OK\r\n");
        stringBuilder.append("Access-Control-Allow-Origin: *\r\n");
        stringBuilder.append("Content-Length: "+responseBody.length()+"\r\n");
        stringBuilder.append("Content-type: text/xml; charset=UTF-8\r\n\r\n");
        stringBuilder.append(responseBody);

        return stringBuilder.toString();
    }
}
