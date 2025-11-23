package com.cegedim.next.common.excel.util.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.NonNull;

/** This class defines utility methods on file. */
public final class FileUtility {

  /** Private constructor. */
  private FileUtility() {}

  /**
   * Allows to create a file in a generated temporary directory.
   *
   * @param fileName a file name [REQUIRED NOT NULL].
   * @return an instance of {@link File}.
   * @throws IOException
   */
  public static File createlFileInATemporaryDirectory(@NonNull final String fileName)
      throws IOException {
    final Path pathTempDirectory = Files.createTempDirectory(null);

    Path filePath = Paths.get(pathTempDirectory.toString() + File.separatorChar + fileName);

    return Files.createFile(filePath).toFile();
  }
}
