package com.sergey.tasks.servlets;

import com.sergey.tasks.entity.objects.CommandEntity;
import com.sergey.tasks.entity.objects.ResultCode;
import com.sergey.tasks.parsers.HttpRequestParser;
import com.sergey.tasks.parsers.exceptions.RequestParserEcxeption;
import com.sergey.tasks.processors.RequestProcessor;
import com.sergey.tasks.processors.ResponseProcessor;
import com.sergey.tasks.utils.DataBaseManager;

import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.Socket;

/**
 * Created by Sergey on 10.02.2018.
 */
public class SocketProcessor implements Runnable{

    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private String url;

    private DataSource dataSource;

    public SocketProcessor(Socket socket, DataSource dataSource, String url) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
        this.dataSource = dataSource;
        this.url = url;
    }

    private void writeResponse(HttpServlet.ResponseEntity responseEntity)  {

        ResponseProcessor responseProcessor = new ResponseProcessor(responseEntity);
        String response = null;
        try {
            response = responseProcessor.process();
            os.write(response.getBytes("UTF-8"));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private HttpRequestParser.HttpRequest readRequest() throws IOException, RequestParserEcxeption {

        HttpRequestParser httpRequestParser = new HttpRequestParser(is);
        HttpRequestParser.HttpRequest httpRequest = httpRequestParser.parse();

        return httpRequest;

    }

    @Override
    public void run() {

        HttpServlet.ResponseEntity responseEntity = null;

        boolean isOurRequest = false;
        try {
            HttpRequestParser.HttpRequest httpRequest = readRequest();
            if (httpRequest.getMethod().equals("POST") && httpRequest.getUrl().equals(url)){
                isOurRequest = true;
                RequestProcessor requestProcessor = new RequestProcessor(httpRequest);
                CommandEntity commandEntity = requestProcessor.process();
                DataBaseManager dataBaseManager = new DataBaseManager(dataSource,commandEntity);
                responseEntity = dataBaseManager.executeCommand();
            }
        } catch (Exception e)
        {
            responseEntity = new HttpServlet.ResponseEntity();
            responseEntity.setResultCode(ResultCode.ERROR);
            e.printStackTrace();
        }


        if (isOurRequest)
            writeResponse(responseEntity);

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
