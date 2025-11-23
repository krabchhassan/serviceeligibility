package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;

public class CouvertureDto implements GenericDto {
  /** */
  private static final long serialVersionUID = 1303190417395506389L;

  private String produit;
  private String produitLibelle;
  private String produitExterne;
  private String produitExterneLibelle;
  private String referenceCouverture;
  private String dateAdhesion;

  public String getProduit() {
    return produit;
  }

  public void setProduit(String produit) {
    this.produit = produit;
  }

  public String getProduitExterne() {
    return produitExterne;
  }

  public String getProduitLibelle() {
    return produitLibelle;
  }

  public void setProduitLibelle(String produitLibelle) {
    this.produitLibelle = produitLibelle;
  }

  public void setProduitExterne(String produitExterne) {
    this.produitExterne = produitExterne;
  }

  public String getProduitExterneLibelle() {
    return produitExterneLibelle;
  }

  public void setProduitExterneLibelle(String produitExterneLibelle) {
    this.produitExterneLibelle = produitExterneLibelle;
  }

  public String getReferenceCouverture() {
    return referenceCouverture;
  }

  public void setReferenceCouverture(String referenceCouverture) {
    this.referenceCouverture = referenceCouverture;
  }

  public String getDateAdhesion() {
    return dateAdhesion;
  }

  public void setDateAdhesion(String dateAdhesion) {
    this.dateAdhesion = dateAdhesion;
  }
}
