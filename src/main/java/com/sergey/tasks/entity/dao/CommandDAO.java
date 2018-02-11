package com.sergey.tasks.entity.dao;

import com.sergey.tasks.servlets.HttpServlet;

import java.math.BigDecimal;

/**
 * Created by Sergey on 10.02.2018.
 */
public interface CommandDAO {
    HttpServlet.ResponseEntity addPerson(String login, String password, BigDecimal balance);
    HttpServlet.ResponseEntity getBalance(String login, String password);
}
