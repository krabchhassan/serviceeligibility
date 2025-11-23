package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.Carence;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.BeneficiaireAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.CarenceAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.DomaineDroitAlmerys;
import com.cegedim.next.serviceeligibility.core.model.entity.Transcodage;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import config.Test608Configuration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Pair;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = Test608Configuration.class)
class MappersAlmerysTest {
  @Autowired MappersAlmerys mappersAlmerys;

  @Test
  void carenceTest() {
    TmpObject2 tmpObject2 = new TmpObject2();

    BeneficiaireAlmerys beneficiaire = new BeneficiaireAlmerys();
    beneficiaire.setNumeroPersonne("456");
    beneficiaire.setRefExternePersonne("456");

    Contrat contrat = new Contrat();
    contrat.setNumero("C1");
    contrat.setNumeroAdherent("C1");
    contrat.setModePaiementPrestations("ESP");

    DomaineDroitAlmerys domaineDroit = new DomaineDroitAlmerys();
    domaineDroit.setCodeExterneProduit("Prod_Alm_1");

    tmpObject2.setBeneficiaire(beneficiaire);
    tmpObject2.setContrat(contrat);
    tmpObject2.setDomaineDroit(domaineDroit);

    Map<Pair<String, String>, BulkObject> carencesList = new HashMap<>();
    List<Transcodage> domaineDroitsTranscoList = new ArrayList<>();
    Transcodage transco = new Transcodage();
    transco.setCle(List.of("PHAR"));
    transco.setCodeTransco("PH");
    domaineDroitsTranscoList.add(transco);

    mappersAlmerys
        .mapperAlmerysCarence()
        .mapCarences(tmpObject2, carencesList, domaineDroitsTranscoList);
    Assertions.assertEquals(0, carencesList.size());

    CarenceAlmerys carence = new CarenceAlmerys();
    carence.setDomaineDroitCarence("PHAR");
    carence.setPeriodeDebutCarence("2025/04/01");
    carence.setPeriodeFinCarence("2025/04/30");

    domaineDroit.setCarences(List.of(carence));

    mappersAlmerys
        .mapperAlmerysCarence()
        .mapCarences(tmpObject2, carencesList, domaineDroitsTranscoList);
    Assertions.assertEquals(1, carencesList.size());
    Assertions.assertEquals("456", carencesList.keySet().stream().toList().get(0).getFirst());
    Assertions.assertEquals(
        "Prod_Alm_1", carencesList.keySet().stream().toList().get(0).getSecond());

    Pair<String, String> key = Pair.of("456", "Prod_Alm_1");
    Carence carence1 = (Carence) carencesList.get(key);
    Assertions.assertEquals("C1-C1", carence1.getNumeroContrat());
    Assertions.assertEquals("456", carence1.getRefInterneOS());
    Assertions.assertEquals("Prod_Alm_1", carence1.getRefProduit());
    Assertions.assertEquals(1, carence1.getCarenceInfos().size());
    Assertions.assertEquals("PH", carence1.getCarenceInfos().get(0).getIdentColonne());
    Assertions.assertEquals("2025/04/01", carence1.getCarenceInfos().get(0).getDateDebutEffet());
    Assertions.assertEquals("2025/04/30", carence1.getCarenceInfos().get(0).getDateFinEffet());
  }
}
