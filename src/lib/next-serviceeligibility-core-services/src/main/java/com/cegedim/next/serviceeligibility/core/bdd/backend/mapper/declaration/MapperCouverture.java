package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.CouvertureDto;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceFormatDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MapperCouverture {

  private static final Logger LOGGER = LoggerFactory.getLogger(MapperCouverture.class);

  public DomaineDroit dtoToEntity() {
    return null;
  }

  public CouvertureDto entityToDto(DomaineDroit domaineDroit) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat sdfDefault = new SimpleDateFormat("dd/MM/yyyy");

    CouvertureDto couverture = new CouvertureDto();
    couverture.setProduit(domaineDroit.getCodeProduit());
    couverture.setProduitLibelle(domaineDroit.getLibelleProduit());
    couverture.setProduitExterne(domaineDroit.getCodeExterneProduit());
    couverture.setProduitExterneLibelle(domaineDroit.getLibelleExterne());
    couverture.setReferenceCouverture(domaineDroit.getReferenceCouverture());
    if (StringUtils.isNotBlank(domaineDroit.getDateAdhesionCouverture())) {
      try {
        Date dateCouverture = sdf.parse(domaineDroit.getDateAdhesionCouverture());
        couverture.setDateAdhesion(sdfDefault.format(dateCouverture));
      } catch (ParseException e) {
        LOGGER.error("MapperCouvertureImpl#Erreur lors de la conversion de date", e);
        throw new ExceptionServiceFormatDate();
      }
    }
    return couverture;
  }
}
