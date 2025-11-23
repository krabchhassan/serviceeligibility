package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.dao.CarteDematDao;
import com.cegedim.next.serviceeligibility.core.dao.CartePapierDao;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkActions;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejection;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsConsolidations;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.mongodb.client.ClientSession;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service("cartesService620")
public class CartesService {

  private final CarteDematDao carteDematDao;

  private final CartePapierDao cartePapierDao;

  private final DeclarationService declarationService;

  public CartesService(
      CarteDematDao carteDematDao,
      @Autowired CartePapierDao cartePapierDao,
      @Autowired DeclarationService declarationService) {
    this.carteDematDao = carteDematDao;
    this.cartePapierDao = cartePapierDao;
    this.declarationService = declarationService;
  }

  public void createRejectionsFromDeclarationConsolideeList(
      List<DeclarationConsolide> consoByContrat,
      Date today,
      ConstantesRejetsConsolidations codeRejet,
      BulkActions bulkActions) {
    createRejectionsFromDeclarationConsolideeList(
        consoByContrat, today, codeRejet.toString(), codeRejet.getService(), bulkActions);
  }

  public void createRejectionsFromDeclarationConsolideeList(
      List<DeclarationConsolide> consoByContrat,
      Date today,
      String rejet,
      int service,
      BulkActions bulkActions) {
    for (DeclarationConsolide declarationConsolide : consoByContrat) {
      String declarationId = declarationConsolide.getIdDeclarations();
      Declaration declaration = declarationService.getDeclarationById(declarationId);
      for (String codeService : declarationConsolide.getCodeServices()) {
        if (service == 2
            || (codeService.equals(Constants.CARTE_TP) && service == 1)
            || (codeService.equals(Constants.CARTE_DEMATERIALISEE) && service == 0)) {
          if (declaration == null) {
            log.error(
                "Création du rejet ignorée : la déclaration {} n'existe pas !", declarationId);
          } else {
            bulkActions.reject(new Rejection(rejet, declaration, today, codeService));
          }
        }
      }
    }
  }

  public int saveCartesDemat(List<CarteDemat> demats, ClientSession session) {
    return carteDematDao.insertAll(demats, session);
  }

  public Collection<CartePapierEditique> saveCartesPapiers(
      List<CartePapierEditique> papiers, ClientSession session) {
    return cartePapierDao.insertAll(papiers, session);
  }

  public List<CarteDemat> findCartesDematByDeclarantAndContrat(
      String idDeclarant, String numeroContrat) {
    return carteDematDao.findCartesDematByDeclarantAndContrat(idDeclarant, numeroContrat);
  }

  public List<CarteDemat> findAllCartesDematByDeclarantAndAMCContrat(
      String idDeclarant, String numeroContrat, String numeroPersonne) {
    return carteDematDao.findAllCartesDematByDeclarantAndAMCContrat(
        idDeclarant, numeroContrat, numeroPersonne);
  }

  public int findCartesDematByDeclarantAndDateExec(String idDeclarant, String dateExec) {
    return carteDematDao.findCartesDematByDeclarantAndDateExec(idDeclarant, dateExec);
  }
}
