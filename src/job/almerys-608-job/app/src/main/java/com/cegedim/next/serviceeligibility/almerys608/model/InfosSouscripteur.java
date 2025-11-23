package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.Rejet;
import java.util.List;

public record InfosSouscripteur(
    Boolean isSouscripteur,
    String qualite,
    String codeEtat,
    String dateRadiation,
    List<String> dateFinDroit,
    String rangAdministratif,
    String numeroPersonne,
    String numeroContrat,
    String typeAssure,
    Rejet rejet) {}
