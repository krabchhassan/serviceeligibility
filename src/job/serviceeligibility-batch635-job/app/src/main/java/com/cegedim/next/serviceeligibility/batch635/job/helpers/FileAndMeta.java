package com.cegedim.next.serviceeligibility.batch635.job.helpers;

import java.nio.file.Path;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileAndMeta {
  private Path filePath;
  private Path metaFilePath;
}
