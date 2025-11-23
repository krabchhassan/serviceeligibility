package com.cegedim.next.serviceeligibility.batch635.job.domain.service;

import static org.mockito.Mockito.*;

import com.cegedim.next.serviceeligibility.batch635.job.domain.model.Declarants;
import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.DeclarantsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DeclarantsServiceTest {

  @Mock DeclarantsRepository declarantsRepository;

  @InjectMocks DeclarantsServiceImpl declarantsService;

  @Test
  void shouldCheckIfAmcExists_whenCallToServiceIsOk() {
    String amc = "5239893746";
    when(declarantsRepository.existsById(amc)).thenReturn(true);
    boolean res = declarantsService.declarantExistsById(amc);

    Assertions.assertTrue(res);
    verify(declarantsRepository, times(1)).existsById(amc);
  }

  @Test
  void shouldGetIdentityByDeclarants_whenCallToServiceIsOk() {
    String amc = "5239893746";
    Declarants declarants = getDeclarants(amc);

    String res = declarantsService.getIdentityByDeclarants(declarants);

    Assertions.assertTrue(res.contains(declarants.getEmetteurDroits()));
  }

  Declarants getDeclarants(String amc) {
    Declarants declarants = new Declarants();
    declarants.setCodeCircuit("03");
    declarants.setCodePartenaire("MAA");
    declarants.setEmetteurDroits("AMC");
    return declarants;
  }
}
