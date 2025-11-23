package com.cegedim.next.serviceeligibility.core.model.entity;

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "tmpPurgeDeclarations")
public class DeclarationLight {
  @Id private String _id;

  private Date dateCreation;

  public DeclarationLight(Declaration declaration) {
    this._id = declaration.get_id();
    this.dateCreation = declaration.getDateCreation();
  }
}
