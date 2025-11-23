package com.cegedim.next.common.excel.features.writedocument.command;

import java.io.File;
import lombok.Getter;
import lombok.Setter;

/** This class defines the result of building Excel {@link File}. */
public class ResultBuildingExcelFile {

  /** {@link File} name. */
  @Getter @Setter private String fileName;

  /** The content in byte of the Excel {@link File}. */
  @Getter @Setter private byte[] content;
}
