package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrestationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.Data;

/** Classe DTO */
@Data
public class BeneficiaireCouvertureDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String codeDomaine;
  private String tauxRemboursement;
  private String uniteTauxRemboursement;
  private XMLGregorianCalendar periodeDebut;
  private XMLGregorianCalendar periodeFin;
  private String codeExterneProduit;
  private String codeOptionMutualiste;
  private String libelleOptionMutualiste;
  private String codeProduit;
  private String libelleProduit;
  private String codeGarantie;
  private String libelleGarantie;
  private String prioriteDroits;
  private String origineDroits;
  private XMLGregorianCalendar dateAdhesionCouverture;
  private String libelleCodeRenvoi;
  private String categorieDomaine;
  /* DOCUMENTS EMBEDDED */
  private List<PrestationDto> prestationDtos;
  private String libelleCodeRenvoiAdditionnel;
}
