package com.cegedim.next.serviceeligibility.core.model.kafka;

import lombok.Data;

@Data
public class Contact {
  private String fixe;
  private String mobile;
  private String email;

  public Contact(String fixe, String mobile, String email) {
    this.fixe = fixe;
    this.mobile = mobile;
    this.email = email;
  }

  public Contact() {}

  public void setFixe(String newFixe) {
    if (newFixe != null) {
      this.fixe = newFixe;
    }
  }

  public void setMobile(String newMobile) {
    if (newMobile != null) {
      this.mobile = newMobile;
    }
  }

  public void setEmail(String newEmail) {
    if (newEmail != null) {
      this.email = newEmail;
    }
  }
}
