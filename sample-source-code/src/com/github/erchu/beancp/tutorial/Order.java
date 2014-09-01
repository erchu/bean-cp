package com.github.erchu.beancp.tutorial;

import java.math.BigDecimal;

public class Order {
    
   private long id;
    
   private Customer customer;
    
   private BigDecimal totalAmount;

   public long getId() {
       return id;
   }

   public void setId(long id) {
       this.id = id;
   }

   public Customer getCustomer() {
       return customer;
   }

   public void setCustomer(Customer customer) {
       this.customer = customer;
   }

   public BigDecimal getTotalAmount() {
       return totalAmount;
   }

   public void setTotalAmount(BigDecimal totalAmount) {
       this.totalAmount = totalAmount;
   }
}
