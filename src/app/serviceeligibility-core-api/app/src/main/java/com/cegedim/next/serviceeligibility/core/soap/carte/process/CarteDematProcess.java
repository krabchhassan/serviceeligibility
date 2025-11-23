package com.cegedim.next.serviceeligibility.core.soap.carte.process;

import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeRequest;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeResponse;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeV2Response;

public interface CarteDematProcess {

  /**
   * Execute une recherche de cartes
   *
   * @param request request envoyé à la base de droit
   * @param reponse reponse renvoyé par la base de droit
   */
  void execute(CarteDematerialiseeRequest request, CarteDematerialiseeResponse reponse);

  void execute(CarteDematerialiseeRequest request, CarteDematerialiseeV2Response reponse);
}
