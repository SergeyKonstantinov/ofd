package com.sergey.tasks;
import com.sergey.tasks.servlets.HttpServlet;

import java.io.FileInputStream;
import java.util.Properties;

public class HttpServer {

    public static void main(String[] args) throws Throwable {

        Properties props = new Properties();
        props.load(HttpServer.class.getClassLoader().getResourceAsStream("server.properties"));
        //props.load(new FileInputStream("src/main/resources/server.properties"));

        int port = Integer.valueOf(props.getProperty("server.port", "8080"));
        int nthreads = Integer.valueOf(props.getProperty("server.nthreads", "10"));
        String url = props.getProperty("server.url", "/");

        HttpServlet httpServlet = new HttpServlet(port,url,nthreads);
        httpServlet.start();


    }
}

