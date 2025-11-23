package com.cegedim.next.consumer.api.service;

import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import java.util.ArrayList;

class DeclarantServiceTest {

  public static IdentiteContrat getIdentiteContrat() {
    IdentiteContrat identite = new IdentiteContrat();
    Nir nir = new Nir();
    nir.setCode("2160631412621");
    nir.setCle("41");
    identite.setNir(nir);
    identite.setDateNaissance("20161101");
    identite.setRangNaissance("1");
    identite.setNumeroPersonne("12345678");
    ArrayList<NirRattachementRO> affiliationsRO = new ArrayList<>();
    NirRattachementRO nirRattachementRO = new NirRattachementRO();
    nirRattachementRO.setNir(nir);
    Periode periode = new Periode();
    periode.setDebut("2022-01-01");
    periode.setFin("2022-12-31");
    nirRattachementRO.setPeriode(periode);
    RattachementRO rattachementRO = new RattachementRO();
    rattachementRO.setCodeRegime("Regime1");
    rattachementRO.setCodeCentre("Centre1");
    rattachementRO.setCodeCaisse("Caisse1");
    nirRattachementRO.setRattachementRO(rattachementRO);
    affiliationsRO.add(nirRattachementRO);
    identite.setAffiliationsRO(affiliationsRO);
    return identite;
  }
}
