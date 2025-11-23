package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import lombok.Data;

/** DeclarationErreurDto contient les erreurs générées lors de la recherche d'une déclaration */
@Data
public class DeclarationErreurDto {
  String codeErreur;
  String libelleErreur;
}
