package com.cegedim.next.serviceeligibility.core.services.pojo;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.util.CollectionUtils;

@Data
public class PAPNatureTags {
  String nature;
  List<String> tags;

  public PAPNatureTags() {
    /* empty constructor */ }

  public PAPNatureTags(PAPNatureTags source) {
    this.nature = source.getNature();
    if (!CollectionUtils.isEmpty(source.getTags())) {
      this.tags = new ArrayList<>();
      this.tags.addAll(source.getTags());
    }
  }
}
