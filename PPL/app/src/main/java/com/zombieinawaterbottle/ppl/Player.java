package com.zombieinawaterbottle.ppl;

import java.util.Comparator;

public class Player implements Comparable {
    private String name;
    private String battype;
    private int price;
    private String country;
    private String type;
    private String bowltype;
    private int batovr;
    private int ballovr;
    private String sellingPrice;
    private String date;



    public Player(){

    }

    public int getBatovr() {
        return batovr;
    }

    public int getBallovr() {
        return ballovr;
    }

    public String getSellingPrice() {
        return sellingPrice;
    }

    public String getName() {
        return name;
    }

    public String getBattype() {
        return battype;
    }

    public int getPrice() {
        return price;
    }

    public String getCountry() {
        return country;
    }

    public String getType() {
        return type.toUpperCase();
    }

    public String getBowltype() {
        return bowltype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setSellingPrice(String sellingPrice) {
        this.sellingPrice = sellingPrice;
    }


    @Override
    public int compareTo(Object o) {
        Player player = (Player)o;
        if (this.getPrice() == player.getPrice()) return 0;
        else if (this.getPrice() < player.getPrice()) return 1;
        else return -1;

    }
}
