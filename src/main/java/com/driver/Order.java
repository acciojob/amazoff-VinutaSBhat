package com.driver;

public class Order {

    private String id;
    private int deliveryTime;

    public Order(String id, String deliveryTime) {

        this.id = id;
        String[] parts = deliveryTime.split(":");
        this.deliveryTime = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
    }

    public String getId() {
        return id;
    }

    public int getDeliveryTime() {return deliveryTime;}
}
