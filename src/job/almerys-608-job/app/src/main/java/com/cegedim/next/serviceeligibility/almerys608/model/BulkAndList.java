package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejet;
import java.util.List;
import java.util.Map;
import org.springframework.data.util.Pair;

public record BulkAndList(
    List<Rejet> rejetNonBloquantsList,
    List<ProduitToCheck> produitsToCheckList,
    List<Produit> produitsCheckedList,
    List<Rejet> rejetBloquantsList,
    List<Rejet> rejetsProduitsExclusList,
    List<Beneficiaire> beneficiariesList,
    Map<String, BulkObject> serviceTPsContrat,
    List<InfosSouscripteur> infosSouscripteurList,
    Map<Pair<String, String>, BulkObject> carencesList,
    List<String> numeroPersonneList,
    BulkList<BulkObject> bulkList) {}
