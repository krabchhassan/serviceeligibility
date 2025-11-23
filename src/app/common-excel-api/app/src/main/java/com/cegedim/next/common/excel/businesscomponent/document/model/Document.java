package com.cegedim.next.common.excel.businesscomponent.document.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** This class defines a document which contains metadata and data. */
public class Document {

  /** The metadata. */
  @Getter @Setter private Metadata metadata;

  /** The sheet contents. */
  @Getter
  @Setter
  @JsonProperty(value = "content")
  private List<SheetContent> sheetContents;

  /** Default constructor. */
  public Document() {
    this.sheetContents = new ArrayList<>();
  }
}
