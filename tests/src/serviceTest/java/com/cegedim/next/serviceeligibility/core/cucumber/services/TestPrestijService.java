package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.model.kafka.Trace;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.PrestIJ;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestPrestijService {
  private final MongoTemplate template;

  public void emptyCollection() {
    template.remove(new Query(), Constants.PRESTIJ);
  }

  public PrestIJ savePrestij(PrestIJ prestIJ) {
    return template.save(prestIJ, Constants.PRESTIJ);
  }

  public Trace getPrestijTrace(String id) {
    return template.findById(id, Trace.class, Constants.PRESTIJ_TRACE);
  }
}
