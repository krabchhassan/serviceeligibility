package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import jakarta.validation.ValidationException;
import java.util.List;

public interface DeclarantDao {
  /**
   * Savoir si l'idClientBO est compatible avec une AMC
   *
   * @param idClientBO identifiant du client BackOffice
   * @param idDeclarant identifiant de l'AMC
   * @return indique si l'IdClientBO est associé à l'AMC
   */
  Boolean isCompatible(String idClientBO, String idDeclarant);

  /**
   * Retourne l'idClientBo d'un déclarant
   *
   * @param idDeclarant L'identifiant du déclarant
   * @return l'idClientBo
   * @throws ValidationException Exception soulevée si le déclarant est inexistant ou si la valeur
   *     idClientBO n'est pas renseignée
   */
  String getIdClientBo(String idDeclarant) throws ValidationException;

  /**
   * @return liste des "_id" dans la collection "declarants"
   */
  List<String> getAllDeclarantIDs();

  Declarant findById(String id);

  List<Declarant> findAll();

  Declarant findByAmcEchange(String amcEchange);

  List<Declarant> getDeclarantsByCodeService(List<String> codeService, String couloirClient);

  Declarant getDeclarantByCodeService(String idDeclarant, List<String> codeServices);

  void create(Declarant declarant);

  void dropCollection();
}
