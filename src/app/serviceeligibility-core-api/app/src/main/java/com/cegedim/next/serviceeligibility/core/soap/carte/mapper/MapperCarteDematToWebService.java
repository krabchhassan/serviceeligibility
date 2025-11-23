package com.cegedim.next.serviceeligibility.core.soap.carte.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.CarteDematDto;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeResponse;
import com.cegedimassurances.norme.cartedemat.beans.CarteDematerialiseeV2Response;
import com.cegedimassurances.norme.cartedemat.beans.CodeReponse;
import com.cegedimassurances.norme.cartedemat.beans.Commentaires;
import com.cegedimassurances.norme.cartedemat.beans.TypeHeaderIn;
import com.cegedimassurances.norme.cartedemat.beans.TypeHeaderOut;

/** Interface. */
public interface MapperCarteDematToWebService {

  TypeHeaderOut mapHeaderOut(TypeHeaderIn headerIn);

  CodeReponse createCodeReponseOK();

  Commentaires createListeCommentairesVide();

  void mapCarteDematResponse(CarteDematerialiseeResponse response, CarteDematDto carteDematDto);

  void mapCarteDematResponse(CarteDematerialiseeV2Response response, CarteDematDto carteDematDto);
}
