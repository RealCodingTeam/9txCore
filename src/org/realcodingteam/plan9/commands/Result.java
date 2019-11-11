package org.realcodingteam.plan9.commands;

import net.md_5.bungee.api.ChatColor;

public final class Result {
    
    public static final Result SUCCESS = new Result(Status.SUCCESS);
    public static final Result NO_PERMISSION = new Result(Status.NO_PERMISSION);
    public static final Result INVALID_SYNTAX = new Result(Status.INVALID_SYNTAX);
    public static final Result ERROR = new Result(Status.ERROR);
    
    public final Status status;
    public final String message;
    
    public Result(Status status) {
        this.status = status;
        
        switch(status) {
            case INVALID_SYNTAX:
                this.message = ChatColor.RED + "Invalid command syntax. See patch help command for help.";
                return;
            case NO_PERMISSION:
                this.message = ChatColor.RED + "You lack permission.";
                return;
            case ERROR:
                this.message = ChatColor.RED + "Something went wrong executing this command. Please inform an admin of this issue.";
                return;
            default:
            case SUCCESS:
                this.message = "";
                return;
        }
    }
    
    public Result(Status status, String message) {
        this.status = status;
        this.message = message;
    }
    
    public static enum Status {
        SUCCESS,
        NO_PERMISSION,
        INVALID_SYNTAX,
        ERROR,
    }
}
