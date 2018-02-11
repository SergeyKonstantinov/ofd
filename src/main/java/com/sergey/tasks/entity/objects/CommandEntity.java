package com.sergey.tasks.entity.objects;

import java.math.BigDecimal;

/**
 * Created by Sergey on 10.02.2018.
 */

public class CommandEntity {

    private String login;
    private String password;
    private BigDecimal balance;

    private Command command;

    public CommandEntity() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "CommandEntity{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", command=" + command +
                '}';
    }
}
