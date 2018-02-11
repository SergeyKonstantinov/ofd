package com.sergey.tasks.entity.objects;

import com.sergey.tasks.entity.exceptions.BadCommandNameException;

/**
 * Created by Sergey on 10.02.2018.
 */
public enum Command {
    CREATE_AGT,
    GET_BALANCE;

    public static Command getCommandByName(String commandName) throws BadCommandNameException {
        if (commandName.equals("CREATE-AGT"))
            return CREATE_AGT;
        else if (commandName.equals("GET-BALANCE"))
            return GET_BALANCE;
        else throw new BadCommandNameException("Wrong command: "+commandName);
    }
}
