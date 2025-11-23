package com.cegedim.next.serviceeligibility.core.soap.consultation.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddResponse;
import com.cegedimassurances.norme.commun.TypeCodeReponse;
import com.cegedimassurances.norme.commun.TypeHeaderIn;
import com.cegedimassurances.norme.commun.TypeHeaderOut;
import javax.xml.datatype.XMLGregorianCalendar;

/** Interface. */
public interface MapperBaseDeDroitToWebService {

  TypeHeaderOut mapHeaderOut(TypeHeaderIn headerIn);

  TypeCodeReponse createCodeReponseOK();

  void mapInfoBddResponse(
      GetInfoBddResponse getInfoBddResponse,
      DeclarationDto declaration,
      boolean isDemandeBeneficiaire,
      XMLGregorianCalendar dateFin,
      boolean isConsultationVersion3,
      boolean mapperWithAdresse);
}
