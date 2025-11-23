package com.cegedim.next.serviceeligibility.core.business.carte.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.CarteDematDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import java.util.List;

/** Interface de la classe d'accès aux services lies aux {@code CarteDemat}. */
public interface CarteDematService extends GenericService<CarteDemat> {

  /**
   * Methode permettant de renvoyer la liste des cartes dématérialisées correspondant à la demande
   * de l'utilisateur.
   *
   * @param demande demande reçue par web service
   * @param isV2 est-ce une demande de type V2
   * @return la liste des cartes dématérialisées correspondant à la demande de l'utilisateur.
   */
  List<CarteDematDto> getCartesDemat(final DemandeCarteDemat demande, boolean isV2);
}
