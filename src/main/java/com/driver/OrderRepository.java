package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        orderMap.put(order.getId(),order);
    }

    public void savePartner(String partnerId){
        partnerMap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if (orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)) {
            System.out.println("Before adding order " + orderId + " to partner " + partnerId + ":");
            System.out.println("Partner's current number of orders: " + partnerMap.get(partnerId).getNumberOfOrders());

            partnerToOrderMap.putIfAbsent(partnerId, new HashSet<>());
            partnerToOrderMap.get(partnerId).add(orderId);
            orderToPartnerMap.put(orderId, partnerId);
            partnerMap.get(partnerId).setNumberOfOrders(partnerToOrderMap.get(partnerId).size());

            System.out.println("After adding order " + orderId + " to partner " + partnerId + ":");
            System.out.println("Partner's updated number of orders: " + partnerMap.get(partnerId).getNumberOfOrders());
        }
    }

    public Order findOrderById(String orderId){
        return orderMap.get(orderId);
    }

    public DeliveryPartner findPartnerById(String partnerId){
        return partnerMap.get(partnerId);
    }

    public Integer findOrderCountByPartnerId(String partnerId){
        return partnerToOrderMap.getOrDefault(partnerId,new HashSet<>()).size();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        return new ArrayList<>(partnerToOrderMap.getOrDefault(partnerId,new HashSet<>()));
    }

    public List<String> findAllOrders(){
        return new ArrayList<>(orderMap.keySet());
    }

    public void deletePartner(String partnerId){
        if(partnerMap.containsKey(partnerId)){
            HashSet<String> orders = partnerToOrderMap.getOrDefault(partnerId, new HashSet<>());
            for(String orderId : orders){
                orderToPartnerMap.remove(orderId);
            }
            partnerToOrderMap.remove(partnerId);
            partnerMap.remove(partnerId);
        }
    }

    public void deleteOrder(String orderId){
        orderMap.remove(orderId);
        if(orderToPartnerMap.containsKey(orderId)){
            String partnerId = orderToPartnerMap.get(orderId);
            orderToPartnerMap.remove(orderId);
            HashSet<String> orders = partnerToOrderMap.get(partnerId);
            if(orders != null){
                orders.remove(orderId);
                partnerMap.get(partnerId).setNumberOfOrders(orders.size());
            }
        }
    }

    public Integer findCountOfUnassignedOrders(){
        return orderMap.size() - orderToPartnerMap.size();
    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        int count = 0;
        int time = Integer.parseInt(timeString.split(":")[0]) * 60 + Integer.parseInt(timeString.split(":")[1]);
        for(String orderId : partnerToOrderMap.getOrDefault(partnerId, new HashSet<>())){
            if(orderMap.get(orderId).getDeliveryTime() > time){
                count++;
            }
        }
        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        int maxTime = 0;
        for(String orderId : partnerToOrderMap.getOrDefault(partnerId, new HashSet<>())){
            int deliveryTime = orderMap.get(orderId).getDeliveryTime();
            if (deliveryTime > maxTime) {
                maxTime = deliveryTime;
            }
        }
        int hours = maxTime / 60;
        int minutes = maxTime % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

}