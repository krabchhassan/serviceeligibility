package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.kafka.exception.IdClientBoException;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdClientBOService {

  private final DeclarantService declarantService;

  public void controlIdClientBO(String idDeclarant, String idDeclarantFlux, String keycloakUser)
      throws IdClientBoException {
    // Contrôle que l'idClientBo a le droit de créer/modifier des
    // données de l'AMC
    if (!idDeclarant.equals(idDeclarantFlux)) {
      throw new IdClientBoException("Incohérence entre les déclarants (Contrat/URL)");
    } else {
      try {
        String idClientBO = declarantService.getIdClientBo(idDeclarant);
        if (!idClientBO.equals(keycloakUser)) {
          throw new IdClientBoException(
              "L'identifiant Back Office "
                  + keycloakUser
                  + " ne permet pas d'accéder aux données du déclarant "
                  + idDeclarant);
        }
      } catch (ValidationException e) {
        throw new IdClientBoException(e.getMessage());
      }
    }
  }
}
