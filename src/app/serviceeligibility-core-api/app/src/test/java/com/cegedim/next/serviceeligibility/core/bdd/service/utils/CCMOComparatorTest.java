package com.cegedim.next.serviceeligibility.core.bdd.service.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AffiliationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.BeneficiaireDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.CCMOComparator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CCMOComparatorTest {

  @Test
  void should_compare_declaration() {
    DeclarationDto d1 = new DeclarationDto();
    DeclarationDto d2 = new DeclarationDto();
    BeneficiaireDto benef1 = new BeneficiaireDto();
    BeneficiaireDto benef2 = new BeneficiaireDto();
    AffiliationDto affiliation1 = new AffiliationDto();
    AffiliationDto affiliation2 = new AffiliationDto();
    d1.setBeneficiaire(benef1);
    benef1.setAffiliation(affiliation1);

    d2.setBeneficiaire(benef2);
    benef2.setAffiliation(affiliation2);

    benef1.setDateNaissance("2000/01/01");
    benef1.setRangNaissance("1");
    benef2.setDateNaissance("2000/01/02");
    benef2.setRangNaissance("1");

    affiliation1.setQualite("ENFANT");
    affiliation2.setQualite("ENFANT");

    CCMOComparator comparator = new CCMOComparator();
    int result = comparator.compare(d1, d2);
    Assertions.assertEquals(-1, result);

    int result2 = comparator.compare(d2, d1);
    Assertions.assertEquals(1, result2);
  }
}
