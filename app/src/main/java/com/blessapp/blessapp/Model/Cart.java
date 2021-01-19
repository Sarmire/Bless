package com.blessapp.blessapp.Model;

public class Cart {
    String pid, name, price, amount, discount, imageUrl;

    public Cart(){

    }

    public Cart(String pid, String name, String price, String amount, String discount, String imageUrl) {
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.discount = discount;
        this.imageUrl = imageUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
