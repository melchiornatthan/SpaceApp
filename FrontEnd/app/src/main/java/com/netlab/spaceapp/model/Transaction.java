package com.netlab.spaceapp.model;

/**
 * Sebagai model untuk Transaction
 */
public class Transaction {
    private int id;
    private String username;
    private int quant_sent;
    private String name;
    private Status status;

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public int getQuant_sent() {
        return quant_sent;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return  name + "            STATUS : " + status ;
    }
}
