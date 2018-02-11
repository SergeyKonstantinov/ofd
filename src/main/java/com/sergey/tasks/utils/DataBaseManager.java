package com.sergey.tasks.utils;

import com.sergey.tasks.entity.dao.CommandDAO;
import com.sergey.tasks.entity.dao.impl.CommadDAOImpl;
import com.sergey.tasks.entity.objects.Command;
import com.sergey.tasks.entity.objects.CommandEntity;
import com.sergey.tasks.servlets.HttpServlet;

import javax.sql.DataSource;

/**
 * Created by Sergey on 10.02.2018.
 */
public class DataBaseManager {

    private CommandEntity commandEntity;
    private DataSource dataSource;

    public DataBaseManager(DataSource dataSource, CommandEntity commandEntity) {
        this.dataSource = dataSource;
        this.commandEntity = commandEntity;
    }

    public CommandEntity getCommandEntity() {
        return commandEntity;
    }

    public void setCommandEntity(CommandEntity commandEntity) {
        this.commandEntity = commandEntity;
    }

    public HttpServlet.ResponseEntity executeCommand()  {

        CommandDAO commandDAO = new CommadDAOImpl(dataSource);

        HttpServlet.ResponseEntity responseEntity = null;
        if (commandEntity.getCommand() == Command.CREATE_AGT){
            responseEntity = commandDAO.addPerson(commandEntity.getLogin(),commandEntity.getPassword(),commandEntity.getBalance());
        }else if (commandEntity.getCommand() == Command.GET_BALANCE)
            responseEntity = commandDAO.getBalance(commandEntity.getLogin(),commandEntity.getPassword());

        return responseEntity;

    }
}
