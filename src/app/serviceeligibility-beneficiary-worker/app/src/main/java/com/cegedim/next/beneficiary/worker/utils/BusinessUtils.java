package com.cegedim.next.beneficiary.worker.utils;

import com.cegedim.next.serviceeligibility.core.model.kafka.Contact;
import com.cegedim.next.serviceeligibility.core.model.kafka.NomAssure;
import org.apache.commons.lang3.StringUtils;

public final class BusinessUtils {

  public static NomAssure updateName(NomAssure newNomAssure, NomAssure existingNomAssure) {
    if (newNomAssure == null) {
      newNomAssure = existingNomAssure;
    } else if (existingNomAssure != null) {
      if (StringUtils.isBlank(newNomAssure.getCivilite())) {
        newNomAssure.setCivilite(existingNomAssure.getCivilite());
      }
      if (StringUtils.isBlank(newNomAssure.getNomFamille())) {
        newNomAssure.setNomFamille(existingNomAssure.getNomFamille());
      }
      if (StringUtils.isBlank(newNomAssure.getNomUsage())) {
        newNomAssure.setNomUsage(existingNomAssure.getNomUsage());
      }
      if (StringUtils.isBlank(newNomAssure.getPrenom())) {
        newNomAssure.setPrenom(existingNomAssure.getPrenom());
      }
    }
    return newNomAssure;
  }

  public static Contact updateContact(Contact newContact, Contact existingContact) {
    if (newContact == null) {
      newContact = existingContact;

    } else {
      if (existingContact != null) {
        if (StringUtils.isBlank(newContact.getEmail())) {
          newContact.setEmail(existingContact.getEmail());
        }
        if (StringUtils.isBlank(newContact.getMobile())) {
          newContact.setMobile(existingContact.getMobile());
        }
        if (StringUtils.isBlank(newContact.getFixe())) {
          newContact.setFixe(existingContact.getFixe());
        }
      }
    }
    return newContact;
  }

  /** /** Private constructor. */
  private BusinessUtils() {}
}
