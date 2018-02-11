package com.sergey.tasks.entity.dao.impl;

import com.sergey.tasks.entity.objects.ResultCode;
import com.sergey.tasks.entity.dao.CommandDAO;
import com.sergey.tasks.entity.objects.Command;
import com.sergey.tasks.servlets.HttpServlet;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

/**
 * Created by Sergey on 10.02.2018.
 */
public class CommadDAOImpl implements CommandDAO {

    private DataSource dataSource;

    public CommadDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public HttpServlet.ResponseEntity addPerson(String login, String password, BigDecimal balance) {

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        HttpServlet.ResponseEntity responseEntity = new HttpServlet.ResponseEntity();
        responseEntity.setCommand(Command.CREATE_AGT);

        try {

            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT login FROM persons WHERE login = ?");
            preparedStatement.setString(1,login);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                responseEntity.setResultCode(ResultCode.USER_EXIST);
            }
            else {

                connection.setAutoCommit(false);
                preparedStatement = connection.prepareStatement("INSERT INTO persons (login,password) VALUES(?,?)");
                preparedStatement.setString(1,login);
                preparedStatement.setString(2,password);
                int rows = preparedStatement.executeUpdate();
                if (rows>0) {
                    preparedStatement = connection.prepareStatement("SELECT id FROM persons WHERE login = ?");
                    preparedStatement.setString(1,login);
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()){
                        int idPerson = resultSet.getInt(1);
                        preparedStatement = connection.prepareStatement("INSERT INTO balance (idperson,balance) VALUES(?,?)");
                        preparedStatement.setInt(1,idPerson);
                        preparedStatement.setBigDecimal(2,balance);
                        int rowsBalance = preparedStatement.executeUpdate();
                        if (rowsBalance>0){
                            responseEntity.setResultCode(ResultCode.OK);
                            connection.commit();
                        }
                    }
                }

            }

        } catch (SQLException e) {
            responseEntity.setResultCode(ResultCode.ERROR);
        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseEntity;
    }

    @Override
    public HttpServlet.ResponseEntity getBalance(String login, String password)  {

        HttpServlet.ResponseEntity responseEntity = new HttpServlet.ResponseEntity();
        responseEntity.setCommand(Command.GET_BALANCE);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {

            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement("SELECT balance, persons.password FROM balance\n" +
                    "inner join persons on balance.idperson = persons.id\n" +
                    "WHERE persons.login = ?");

            preparedStatement.setString(1,login);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String passwordPerson = resultSet.getString(2);
                if (passwordPerson.equals(password)){
                    BigDecimal balance = resultSet.getBigDecimal(1);
                    responseEntity.setBalance(balance);
                    responseEntity.setResultCode(ResultCode.OK);
                }
                else {
                    responseEntity.setResultCode(ResultCode.WRONG_PASSWORD);
                }
            }
            else
                responseEntity.setResultCode(ResultCode.USER_DOESNT_EXIST);

        } catch (SQLException e) {
            responseEntity.setResultCode(ResultCode.ERROR);
        } finally {

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return responseEntity;

    }
}
