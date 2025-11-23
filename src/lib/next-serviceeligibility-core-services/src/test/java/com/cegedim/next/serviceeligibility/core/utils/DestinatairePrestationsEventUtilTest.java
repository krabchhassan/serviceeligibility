package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.kafka.ModePaiement;
import com.cegedim.next.serviceeligibility.core.model.kafka.RibAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.mongodb.assertions.Assertions;
import org.junit.jupiter.api.Test;

class DestinatairePrestationsEventUtilTest {

  @Test
  void newRecipientNeedEventJira6666() {
    ModePaiement modePaiement = new ModePaiement();
    modePaiement.setCode("VIR");

    DestinatairePrestations newDestinataire = new DestinatairePrestations();
    newDestinataire.setModePaiementPrestations(modePaiement);
    newDestinataire.setRib(new RibAssure("bic", "iban"));

    DestinatairePrestations oldDestinataire = new DestinatairePrestations();
    oldDestinataire.setModePaiementPrestations(modePaiement);

    Assertions.assertTrue(
        DestinatairePrestationsEventUtil.newRecipientNeedEvent(newDestinataire, oldDestinataire));
  }
}
