package com.cegedim.next.common.excel.businesscomponent.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/** This class defines the metadata of a document. */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {

  /** The metadata name which defines the Excel file name. */
  @Getter @Setter private String name;

  /** The metadata sheets which contains the description of informations. */
  @Getter @Setter private List<ExcelSheet> sheets;

  /** Default constructor. */
  public Metadata() {
    this.sheets = new ArrayList<>();
  }

  /**
   * Return the sheets number. If {@link #getSheets()} is <code>NULL</code> then it returns <code>0
   * </code> else the size of {@link #getSheets()}.
   *
   * @return an instance of {@link Integer}.
   */
  public Integer getSheetsNumber() {
    return this.sheets != null ? this.sheets.size() : 0;
  }
}
