package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.Data;

@Data
public class ContractTPPeriode {

  String contratPeriodeDebut;
  String contratPeriodeFin;
  String contratPeriodeFermetureFin;

  LocalDate contratPeriodeDebutFormatDate;
  LocalDate contratPeriodeFinFormatDate;
  LocalDate contratPeriodeFermetureFinFormatDate;

  public ContractTPPeriode(PeriodeDroitContractTP periodeContrat, DateTimeFormatter formatter) {
    this.contratPeriodeDebut = periodeContrat.getPeriodeDebut();
    this.contratPeriodeFin = periodeContrat.getPeriodeFin();
    this.contratPeriodeFermetureFin = periodeContrat.getPeriodeFinFermeture();

    this.contratPeriodeDebutFormatDate = LocalDate.parse(this.contratPeriodeDebut, formatter);
    if (this.contratPeriodeFin != null) {
      this.contratPeriodeFinFormatDate = LocalDate.parse(this.contratPeriodeFin, formatter);
    }
    if (this.contratPeriodeFermetureFin != null) {
      this.contratPeriodeFermetureFinFormatDate =
          LocalDate.parse(this.contratPeriodeFermetureFin, formatter);
    }
  }
}
