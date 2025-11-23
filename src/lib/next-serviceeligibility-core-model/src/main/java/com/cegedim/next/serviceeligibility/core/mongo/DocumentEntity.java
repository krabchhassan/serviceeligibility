package com.cegedim.next.serviceeligibility.core.mongo;

import org.springframework.data.annotation.Id;

/** Classe la plus haute représentant un document */
public class DocumentEntity implements IDocumentEntity {

  private static final long serialVersionUID = 6343834010459158663L;

  @Id private String _id;

  /** Constructeur. */
  public DocumentEntity() {}

  /**
   * @return L'identifiant technique de l'entité
   */
  @Override
  public String get_id() {
    return this._id;
  }

  /**
   * @param _id L'identifiant technique de l'entité manipulée
   */
  @Override
  public void set_id(String _id) {
    this._id = _id;
  }
}
