package com.cegedim.next.serviceeligibility.core.bobb.dao;

import com.cegedim.next.serviceeligibility.core.bobb.GuaranteeViewContexts;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GuaranteeViewContextRepository
    extends MongoRepository<GuaranteeViewContexts, String> {
  Optional<GuaranteeViewContexts> findByContextKey(String contextKey);
}
