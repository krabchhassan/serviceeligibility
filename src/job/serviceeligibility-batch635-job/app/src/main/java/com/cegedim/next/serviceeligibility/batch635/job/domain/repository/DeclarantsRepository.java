package com.cegedim.next.serviceeligibility.batch635.job.domain.repository;

import com.cegedim.next.serviceeligibility.batch635.job.domain.model.Declarants;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeclarantsRepository extends MongoRepository<Declarants, String> {
  Optional<Declarants> findById(String id);
}
