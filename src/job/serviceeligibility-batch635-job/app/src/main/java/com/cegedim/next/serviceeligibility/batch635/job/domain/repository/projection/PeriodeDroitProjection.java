package com.cegedim.next.serviceeligibility.batch635.job.domain.repository.projection;

import static com.cegedim.next.serviceeligibility.batch635.job.helpers.Helper.PROJECTION_DATE_FORMATTER;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PeriodeDroitProjection {
  private String periodeDebut;
  private String periodeFin;
  private String numeroAdherent;
  private String numeroContrat;
  private String nirOd1;
  private String cleNirOd1;
  private String dateNaissance;
  private String rangNaissance;
  private String numeroPersonne;
  private String nom;
  private String prenom;

  private LocalDate periodeDebutParsed;
  private LocalDate periodeFinParsed;

  public LocalDate getPeriodeDebutParsed() {
    if (periodeDebutParsed == null) {
      periodeDebutParsed = LocalDate.parse(periodeDebut, PROJECTION_DATE_FORMATTER);
    }
    return periodeDebutParsed;
  }

  public LocalDate getPeriodeFinParsed() {
    if (periodeFinParsed == null) {
      periodeFinParsed = LocalDate.parse(periodeFin, PROJECTION_DATE_FORMATTER);
    }
    return periodeFinParsed;
  }
}
