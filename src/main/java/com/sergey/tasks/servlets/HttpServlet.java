package com.sergey.tasks.servlets;



import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import com.sergey.tasks.entity.objects.Command;
import com.sergey.tasks.entity.objects.ResultCode;

import javax.naming.NamingException;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Sergey on 10.02.2018.
 */
public class HttpServlet {

    private ExecutorService executorService;
    private MysqlConnectionPoolDataSource dataSource;
    private int port;
    private String url;
    private int numOfThreads;

    public HttpServlet(int port, String url, int numOfThreads) {
        this.port = port;
        this.url = url;
        this.numOfThreads = numOfThreads;
    }

    public void start() throws IOException, NamingException, SQLException {

        Properties props = new Properties();
        props.load(HttpServlet.class.getClassLoader().getResourceAsStream("db.properties"));
        //props.load(new FileInputStream("src/main/resources/db.properties"));

        dataSource = new MysqlConnectionPoolDataSource();
        dataSource.setURL(props.getProperty("mysql.url"));
        dataSource.setUser(props.getProperty("mysql.username"));
        dataSource.setPassword(props.getProperty("mysql.password"));

        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            Thread thread = new Thread(new SocketProcessor(socket,dataSource,url));
            executorService.submit(thread);
        }

    }

    /**
     * Created by Sergey on 10.02.2018.
     */
    public static class ResponseEntity {
        ResultCode resultCode;
        BigDecimal balance;
        Command command;

        public Command getCommand() {
            return command;
        }

        public void setCommand(Command command) {
            this.command = command;
        }

        public ResponseEntity() {
        }

        public ResponseEntity(ResultCode resultCode, BigDecimal balance) {
            this.resultCode = resultCode;
            this.balance = balance;
        }

        public ResultCode getResultCode() {
            return resultCode;
        }

        public void setResultCode(ResultCode resultCode) {
            this.resultCode = resultCode;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
    }
}
