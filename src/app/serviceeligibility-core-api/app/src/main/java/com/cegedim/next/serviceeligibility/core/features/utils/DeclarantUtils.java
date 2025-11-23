package com.cegedim.next.serviceeligibility.core.features.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.CodeRenvoiTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.ConventionTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.FondCarteTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.RegroupementDomainesTPDto;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.List;

public class DeclarantUtils {
  private DeclarantUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static boolean checkUnicityConventionOK(List<ConventionTPDto> conventions) {
    int nbConventions = conventions.size();
    for (int i = 0; i < nbConventions; i++) {
      for (int j = i + 1; j < nbConventions; j++) {
        ConventionTPDto conventionA = conventions.get(i);
        ConventionTPDto conventionB = conventions.get(j);
        if (DateUtils.isPeriodeValide(conventionA.getDateDebut(), conventionA.getDateFin())
            && DateUtils.isPeriodeValide(conventionB.getDateDebut(), conventionB.getDateFin())
            && isConventionTPEqual(conventionA, conventionB)
            && DateUtils.isOverlapping(
                conventionA.getDateDebut(),
                conventionA.getDateFin(),
                conventionB.getDateDebut(),
                conventionB.getDateFin())) {
          return false;
        }
      }
    }
    return true;
  }

  public static boolean checkUnicityCodesRenvoiOK(List<CodeRenvoiTPDto> codesRenvoi) {
    for (int i = 0; i < codesRenvoi.size() - 1; i++) {
      for (int j = i + 1; j < codesRenvoi.size(); j++) {
        CodeRenvoiTPDto codeRenvoiA = codesRenvoi.get(i);
        CodeRenvoiTPDto codeRenvoiB = codesRenvoi.get(j);
        if (DateUtils.isPeriodeValide(codeRenvoiA.getDateDebut(), codeRenvoiA.getDateFin())
            && DateUtils.isPeriodeValide(codeRenvoiB.getDateDebut(), codeRenvoiB.getDateFin())
            && isCodeRenvoiEqual(codeRenvoiA, codeRenvoiB)
            && DateUtils.isOverlapping(
                codeRenvoiA.getDateDebut(),
                codeRenvoiA.getDateFin(),
                codeRenvoiB.getDateDebut(),
                codeRenvoiB.getDateFin())) {
          return false;
        }
      }
    }
    return true;
  }

  public static boolean checkUnicityRegroupementsDomainesTPOK(
      List<RegroupementDomainesTPDto> regroupementsDomainesTP) {
    int nbRegroupements = regroupementsDomainesTP.size();
    for (int i = 0; i < nbRegroupements; i++) {
      for (int j = i + 1; j < nbRegroupements; j++) {
        RegroupementDomainesTPDto regroupementDomaineA = regroupementsDomainesTP.get(i);
        RegroupementDomainesTPDto regroupementDomaineB = regroupementsDomainesTP.get(j);
        if (DateUtils.isPeriodeValide(
                regroupementDomaineA.getDateDebut(), regroupementDomaineA.getDateFin())
            && DateUtils.isPeriodeValide(
                regroupementDomaineB.getDateDebut(), regroupementDomaineB.getDateFin())
            && isRegroupementDomainesTPEqual(regroupementDomaineA, regroupementDomaineB)
            && DateUtils.isOverlapping(
                regroupementDomaineA.getDateDebut(),
                regroupementDomaineA.getDateFin(),
                regroupementDomaineB.getDateDebut(),
                regroupementDomaineB.getDateFin())) {
          return false;
        }
      }
    }
    return true;
  }

  public static boolean checkUnicityFondCarteTPOK(List<FondCarteTPDto> fondsCartesTPD) {
    int nbFondsCartes = fondsCartesTPD.size();
    for (int i = 0; i < nbFondsCartes; i++) {
      for (int j = i + 1; j < nbFondsCartes; j++) {
        FondCarteTPDto fondCarteA = fondsCartesTPD.get(i);
        FondCarteTPDto fondCarteB = fondsCartesTPD.get(j);
        if (DateUtils.isPeriodeValide(fondCarteA.getDateDebut(), fondCarteA.getDateFin())
            && DateUtils.isPeriodeValide(fondCarteB.getDateDebut(), fondCarteB.getDateFin())
            && isFondCarteTPEqual(fondCarteA, fondCarteB)
            && DateUtils.isOverlapping(
                fondCarteA.getDateDebut(),
                fondCarteA.getDateFin(),
                fondCarteB.getDateDebut(),
                fondCarteB.getDateFin())) {
          return false;
        }
      }
    }
    return true;
  }

  public static boolean isConventionTPEqual(
      ConventionTPDto conventionA, ConventionTPDto conventionB) {
    if (conventionA != null && conventionB != null) {
      if (conventionA.getDomaineTP() != null && conventionB.getDomaineTP() != null) {
        return conventionA.getReseauSoin().equals(conventionB.getReseauSoin())
            && conventionA.getDomaineTP().equals(conventionB.getDomaineTP());
      }
      return conventionA.getReseauSoin().equals(conventionB.getReseauSoin())
          && (conventionA.getDomaineTP() == null && conventionB.getDomaineTP() == null);
    }
    return false;
  }

  private static boolean isCodeRenvoiEqual(
      CodeRenvoiTPDto codeRenvoiA, CodeRenvoiTPDto codeRenvoiB) {
    if (codeRenvoiA != null && codeRenvoiB != null) {
      if (codeRenvoiA.getReseauSoin() != null && codeRenvoiB.getReseauSoin() != null) {
        return codeRenvoiA.getDomaineTP().equals(codeRenvoiB.getDomaineTP())
            && codeRenvoiA.getReseauSoin().equals(codeRenvoiB.getReseauSoin());
      }
      return codeRenvoiA.getDomaineTP().equals(codeRenvoiB.getDomaineTP())
          && (codeRenvoiA.getReseauSoin() == null && codeRenvoiB.getReseauSoin() == null);
    }
    return false;
  }

  private static boolean isRegroupementDomainesTPEqual(
      RegroupementDomainesTPDto regroupementDomaineA,
      RegroupementDomainesTPDto regroupementDomaineB) {
    if (regroupementDomaineA != null && regroupementDomaineB != null) {
      return regroupementDomaineA
          .getDomaineRegroupementTP()
          .equals(regroupementDomaineB.getDomaineRegroupementTP());
    }
    return false;
  }

  private static boolean isFondCarteTPEqual(FondCarteTPDto fondCarteA, FondCarteTPDto fondCarteB) {
    if (fondCarteA != null && fondCarteB != null) {
      return fondCarteA.getReseauSoin().equals(fondCarteB.getReseauSoin());
    }
    return false;
  }
}
