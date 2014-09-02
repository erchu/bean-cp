package com.github.erchu.beancp.tutorial.objects1;

import java.math.BigDecimal;

public class OrderOverviewDto {
    
   private long id;
    
   private String customerFullName;
    
   private BigDecimal totalAmount;

   public long getId() {
       return id;
   }

   public void setId(long id) {
       this.id = id;
   }

   public String getCustomerFullName() {
       return customerFullName;
   }

   public void setCustomerFullName(String customerFullName) {
       this.customerFullName = customerFullName;
   }

   public BigDecimal getTotalAmount() {
       return totalAmount;
   }

   public void setTotalAmount(BigDecimal totalAmount) {
       this.totalAmount = totalAmount;
   }
}
