package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDao;
import com.cegedim.next.serviceeligibility.core.model.entity.RestitutionCarte;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestRestitutionCarteService {

  private final RestitutionCarteDao restitutionCarteDao;

  public List<RestitutionCarte> getRestitutionCarte(
      String idDeclarant, String numeroPersonne, String numeroContrat, String numeroAdherent) {
    return restitutionCarteDao.findRestitutionByIdDeclarantBenefContrat(
        idDeclarant,
        numeroPersonne,
        numeroContrat,
        numeroAdherent,
        null,
        Constants.MAX_ITEMS_PER_LOAD_UI);
  }
}
