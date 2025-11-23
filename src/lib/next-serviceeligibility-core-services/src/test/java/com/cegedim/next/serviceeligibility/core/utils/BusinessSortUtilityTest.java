package com.cegedim.next.serviceeligibility.core.utils;

import static org.junit.jupiter.api.Assertions.*;

import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.RattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.Source;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class BusinessSortUtilityTest {

  @Test
  void should_not_scratch_service_prestation_prest_ij() {
    IdentiteContrat newIC = createIdentiteContrat();
    IdentiteContrat oldIC = createIdentiteContrat();
    oldIC.getAffiliationsRO().get(0).setPeriode(new Periode("2012-01-01", null));
    newIC.getAffiliationsRO().get(0).setPeriode(new Periode("2018-01-01", null));

    BusinessSortUtility.setAffiliationsRO(newIC, oldIC, Source.PREST_IJ);

    assertEquals(1, oldIC.getAffiliationsRO().size());
    assertEquals("2012-01-01", oldIC.getAffiliationsRO().get(0).getPeriode().getDebut());
    assertNull(oldIC.getAffiliationsRO().get(0).getPeriode().getFin());
  }

  @Test
  void should_not_scratch_autre_declaration() {
    IdentiteContrat newIC = createIdentiteContrat();
    IdentiteContrat oldIC = createIdentiteContrat();
    oldIC.getAffiliationsRO().get(0).setPeriode(new Periode("2012-01-01", null));
    newIC.getAffiliationsRO().get(0).setPeriode(new Periode("2018-01-01", null));

    BusinessSortUtility.setAffiliationsRO(newIC, oldIC, Source.AUTRE);

    assertEquals(1, oldIC.getAffiliationsRO().size());
    assertEquals("2012-01-01", oldIC.getAffiliationsRO().get(0).getPeriode().getDebut());
    assertNull(oldIC.getAffiliationsRO().get(0).getPeriode().getFin());
  }

  @Test
  void should_modify_tbd() {
    IdentiteContrat newIC = createIdentiteContrat();
    IdentiteContrat oldIC = createIdentiteContrat();
    oldIC.getAffiliationsRO().get(0).setPeriode(new Periode("2012-01-01", null));
    newIC.getAffiliationsRO().get(0).setPeriode(new Periode("2018-01-01", null));

    BusinessSortUtility.setAffiliationsRO(newIC, oldIC, Source.TDB_DECLARATION);

    assertEquals(1, newIC.getAffiliationsRO().size());
    assertEquals("2012-01-01", newIC.getAffiliationsRO().get(0).getPeriode().getDebut());
    assertNull(newIC.getAffiliationsRO().get(0).getPeriode().getFin());
  }

  @Test
  void should_modify_service_prestation() {
    IdentiteContrat newIC = createIdentiteContrat();
    IdentiteContrat oldIC = createIdentiteContrat();
    oldIC.getAffiliationsRO().get(0).setPeriode(new Periode("2012-01-01", "2017-12-31"));
    newIC.getAffiliationsRO().get(0).setPeriode(new Periode("2018-01-01", null));

    BusinessSortUtility.setAffiliationsRO(newIC, oldIC, Source.SERVICE_PRESTATION);

    assertEquals(1, newIC.getAffiliationsRO().size());
    assertEquals("2012-01-01", newIC.getAffiliationsRO().get(0).getPeriode().getDebut());
    assertNull(newIC.getAffiliationsRO().get(0).getPeriode().getFin());
  }

  @Test
  void should_inverse_service_prestation() {
    IdentiteContrat newIC = createIdentiteContrat();
    IdentiteContrat oldIC = createIdentiteContrat();
    oldIC.getAffiliationsRO().get(0).setPeriode(new Periode("2012-01-01", "2017-12-31"));
    newIC.getAffiliationsRO().get(0).setPeriode(new Periode("2012-01-01", "2011-12-31"));

    BusinessSortUtility.setAffiliationsRO(newIC, oldIC, Source.SERVICE_PRESTATION);

    assertEquals(1, newIC.getAffiliationsRO().size());
    assertEquals("2012-01-01", newIC.getAffiliationsRO().get(0).getPeriode().getDebut());
    assertEquals("2017-12-31", newIC.getAffiliationsRO().get(0).getPeriode().getFin());
  }

  @Test
  void should_reduce_service_prestation() {
    IdentiteContrat newIC = createIdentiteContrat();
    IdentiteContrat oldIC = createIdentiteContrat();
    oldIC.getAffiliationsRO().get(0).setPeriode(new Periode("2012-01-01", "2017-12-31"));
    newIC.getAffiliationsRO().get(0).setPeriode(new Periode("2015-01-01", "2014-12-31"));

    BusinessSortUtility.setAffiliationsRO(newIC, oldIC, Source.SERVICE_PRESTATION);

    assertEquals(1, newIC.getAffiliationsRO().size());
    assertEquals("2012-01-01", newIC.getAffiliationsRO().get(0).getPeriode().getDebut());
    assertEquals("2017-12-31", newIC.getAffiliationsRO().get(0).getPeriode().getFin());
  }

  private IdentiteContrat createIdentiteContrat() {
    Nir nir = new Nir();
    nir.setCode("1");
    nir.setCle("1");
    IdentiteContrat newIC = new IdentiteContrat();
    NirRattachementRO newNir = new NirRattachementRO();
    RattachementRO rattRO = new RattachementRO();
    rattRO.setCodeCaisse("1");
    rattRO.setCodeRegime("1");
    List<NirRattachementRO> nirRattachementROS = new ArrayList<>();
    nirRattachementROS.add(newNir);
    newIC.setAffiliationsRO(nirRattachementROS);
    newNir.setRattachementRO(rattRO);
    newNir.setNir(nir);

    return newIC;
  }
}
