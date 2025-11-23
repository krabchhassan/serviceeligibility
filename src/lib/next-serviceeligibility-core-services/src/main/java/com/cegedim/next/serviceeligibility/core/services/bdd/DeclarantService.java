package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.dao.DeclarantDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.ValidationException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeclarantService {

  private final DeclarantDao dao;

  /**
   * Savoir si l'idClientBO est compatible avec une AMC
   *
   * @param idClientBO identifiant du client BackOffice
   * @param idDeclarant identifiant de l'AMC
   * @return indique si l'IdClientBO est associé à l'AMC
   */
  @ContinueSpan(log = "isCompatible declarant")
  public Boolean isCompatible(String idClientBO, String idDeclarant) {
    return dao.isCompatible(idClientBO, idDeclarant);
  }

  /**
   * Récupérer l'idClientBo d'une AMC
   *
   * @param idDeclarant Identifiant du déclarant
   * @return l'idClientBO de l'amc
   * @throws ValidationException Exception retournée si l'AMC est inexistante ou que l'idClientBo
   *     n'est pas valorisé
   */
  @ContinueSpan(log = "getIdClientBo")
  public String getIdClientBo(String idDeclarant) throws ValidationException {
    return dao.getIdClientBo(idDeclarant);
  }

  /** Retourne une liste d _id de tous les declarants dans la collection declarant */
  @ContinueSpan(log = "getAllDeclarantIDs")
  public List<String> getAllDeclarantIDs() {
    return dao.getAllDeclarantIDs();
  }

  @ContinueSpan(log = "findById")
  public Declarant findById(String codeAmc) {
    return dao.findById(codeAmc);
  }

  @ContinueSpan(log = "getDeclarantsByCodeService")
  public List<Declarant> getDeclarantsByCodeService(
      List<String> codeServices, String couloirClient) {
    return dao.getDeclarantsByCodeService(codeServices, couloirClient);
  }

  @ContinueSpan(log = "getDeclarantByCodeService")
  public Declarant getDeclarantByCodeService(String idDeclarant, List<String> codeServices) {
    return dao.getDeclarantByCodeService(idDeclarant, codeServices);
  }
}
