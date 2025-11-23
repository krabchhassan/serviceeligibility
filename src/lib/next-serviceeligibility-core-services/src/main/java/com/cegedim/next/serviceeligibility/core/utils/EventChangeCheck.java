package com.cegedim.next.serviceeligibility.core.utils;

import lombok.Data;

@Data
public class EventChangeCheck {
  boolean paymentChange = false;
  boolean benefitChange = false;

  public void setOrPaymentChange(boolean value) {
    paymentChange = paymentChange || value;
  }

  public void setOrBenefitChange(boolean value) {
    benefitChange = benefitChange || value;
  }
}
