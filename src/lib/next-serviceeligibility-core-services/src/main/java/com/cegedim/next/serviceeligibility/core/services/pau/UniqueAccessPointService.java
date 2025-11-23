package com.cegedim.next.serviceeligibility.core.services.pau;

import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequest;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointResponse;

public interface UniqueAccessPointService {

  String LOG_BENEFICIAIRE_DEBUT = "{} : Recherche des bénéficiaires";

  String LOG_BENEFICIAIRE_RESULTAT = "Id Beneficiaire {}, numeroPersonne {}";

  String LOG_ADHERENT_DEBUT = "{} : Détermination de l’adhérent";

  String LOG_CONTRAT_DEBUT = "{} : Recherche des contrats";

  String LOG_CONTRAT_TRI_DEBUT = "{} : Tri des contrats";

  String LOG_CONTRAT_RESTITUTION_DEBUT = "{} : Restitution des contrats";

  UniqueAccessPointResponse execute(UniqueAccessPointRequest requete);
}
