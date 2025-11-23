package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.dao.BeneficiaryDao;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TestBeneficiaryService {
  private final BeneficiaryDao beneficiaryDao;

  public BenefAIV5 createBeneficiary(BenefAIV5 beneficiaires) {
    return beneficiaryDao.save(beneficiaires);
  }

  public List<BenefAIV5> getBeneficiaires() {
    return beneficiaryDao.getAll();
  }

  public void dropCollections() {
    beneficiaryDao.dropCollections();
  }
}
