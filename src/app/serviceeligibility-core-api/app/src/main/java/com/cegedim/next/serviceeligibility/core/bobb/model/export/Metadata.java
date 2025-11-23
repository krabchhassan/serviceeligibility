package com.cegedim.next.serviceeligibility.core.bobb.model.export;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** This class defines the metadata of a document. */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {

  /** The metadata name which defines the Excel file name. */
  private String name;

  /** The metadata sheets which contains the description of informations. */
  private List<ExcelSheet> sheets;

  private int sheetsNumber;

  /** Default constructor. */
  public Metadata() {
    this.sheets = new ArrayList<>();
  }

  public void addSheet(ExcelSheet excelSheet) {
    this.sheets.add(excelSheet);
  }
}
