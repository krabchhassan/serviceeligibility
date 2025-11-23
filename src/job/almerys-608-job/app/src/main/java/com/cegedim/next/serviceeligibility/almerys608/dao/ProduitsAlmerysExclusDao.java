package com.cegedim.next.serviceeligibility.almerys608.dao;

import com.cegedim.next.serviceeligibility.almerys608.model.ProduitsAlmerysExclus;
import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import java.util.List;

public interface ProduitsAlmerysExclusDao extends IMongoGenericDao<ProduitsAlmerysExclus> {
  List<ProduitsAlmerysExclus> findByKey(String idDeclarant, String critereSecondaireDetaille);
}
