package com.netlab.spaceapp.model;

/**
 * Sebagai model untuk Item
 */
public class Item {
    private int id;
    private String name;
    private String description;
    private int quantity;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }


    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return name ;
    }
}
