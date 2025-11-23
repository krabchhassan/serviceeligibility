package com.cegedim.next.serviceeligibility.core.features.volumetry.xlsutilities.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** This class defines a document which contains metadata and data. */
@Data
public class Document {

  /** The metadata. */
  private Metadata metadata;

  /** The sheet contents. */
  private List<Content> content;

  /** Default constructor. */
  public Document() {
    this.content = new ArrayList<>();
  }
}
