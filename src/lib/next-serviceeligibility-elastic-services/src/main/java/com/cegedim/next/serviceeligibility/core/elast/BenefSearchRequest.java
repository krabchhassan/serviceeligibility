package com.cegedim.next.serviceeligibility.core.elast;

import lombok.Data;

@Data
public class BenefSearchRequest {
  private int perPage = 10;
  private int page = 0;

  private String name; // contrats.data.nom.nomFamille or contrats.data.nom.nomUsage
  private String firstName; // contrats.data.nom.prenom
  private String nir; // identite.nir.code or identite.affiliationsRO.nir.code
  private String declarantId; // amc.idDeclarant
  private String declarantLabel; // amc.libelle
  private String declarantIdOrLabel; // amc.idDeclarant or amc.libelle
  private String subscriberId; // contrats.numeroAdherent
  private String contractNumber; // contrats.numeroContrat
  private String subscriberIdOrContractNumber; // contrats.numeroAdherent or contrats.numeroContrat
  private String
      birthDate; // identite.dateNaissance or identite.historiqueDateRangNaissance.dateNaissance
  private String
      birthRank; // identite.rangNaissance or identite.historiqueDateRangNaissance.rangNaissance
  private String email; // contrats.data.contact.email
  private String bic; // contrats.data.destinatairesPaiements.rib.bic.keyword
  private String iban; // contrats.data.destinatairesPaiements.rib.iban.keyword
  private String city; // contrats.data.adresse.line6
  private String street; // contrats.data.adresse.line4
  private String postalCode; // contrats.data.adresse.codePostal
  private String subscriberNumber; // contrats.numeroAdherent
  private String issuingCompany; // contrats.societeEmettrice
  private String insurerExchangeId; // contrats.numeroAMCEchange
  private String nameOrSubscriberIdorContractNumber; // contrats.data.nom.nomFamille or
  // contrats.data.no.nomUsage or contrats.numeroAdherent or contrats.numeroContrat
  private String serviceMetier; // services
}
