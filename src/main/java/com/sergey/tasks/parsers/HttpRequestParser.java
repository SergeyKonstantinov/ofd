package com.sergey.tasks.parsers;

import com.sergey.tasks.parsers.exceptions.RequestParserEcxeption;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergey on 10.02.2018.
 */
public class HttpRequestParser {
    private InputStream inputStream;

    public HttpRequestParser(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public HttpRequest parse() throws IOException, RequestParserEcxeption {

        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        HttpRequest httpRequest = new HttpRequest();

        String caption = br.readLine();
        String[] patternsOfCaption = caption.split(" ");

        if (patternsOfCaption.length >= 2){
            String method = patternsOfCaption[0];
            String url = patternsOfCaption[1];
            httpRequest.setMethod(method);
            httpRequest.setUrl(url);
        }else throw new RequestParserEcxeption("Can't parse a caption of request");

        while(true) {
            String header = br.readLine();
            if(header == null || header.trim().length() == 0) {
                break;
            }
            String[] patternsOfHeader = header.split(": ");
            if (patternsOfHeader.length==2)
                httpRequest.addHeader(patternsOfHeader[0],patternsOfHeader[1].trim());
        }

        String contentLength = httpRequest.getHeader("Content-Length");
        if (contentLength.isEmpty())
            throw new RequestParserEcxeption("Content-Length header does not found");

        int bodyLength = Integer.valueOf(contentLength);
        char[]  buffer      = new char[bodyLength];
        String  postData    = "";
        br.read(buffer, 0, bodyLength);
        postData = new String(buffer, 0, buffer.length);
        httpRequest.setBodyRequest(postData);

        return httpRequest;

    }

    public static class HttpRequest{
        private String method;
        private String url;
        private HashMap<String,String> headers = new HashMap<>();
        private String bodyRequest;

        public void addHeader(String key, String value){
            headers.put(key,value);
        }
        public String getHeader(String key){
            return headers.getOrDefault(key,"");
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public HashMap<String, String> getHeaders() {
            return headers;
        }

        public void setHeaders(HashMap<String, String> headers) {
            this.headers = headers;
        }

        public String getBodyRequest() {
            return bodyRequest;
        }

        public void setBodyRequest(String bodyRequest) {
            this.bodyRequest = bodyRequest;
        }

        @Override
        public String toString() {

            StringBuilder result = new StringBuilder();

            result.append("method: "+method+"\n");
            result.append("url: "+url+"\n");

            for (Map.Entry<String,String> entry:headers.entrySet())
                result.append(entry.getKey()+": "+entry.getValue()+"\n");

            result.append("BODY\n"+ bodyRequest);

            return result.toString();
        }
    }

}
