package com.cegedim.next.serviceeligibility.consolidationcontract.config;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.EFFET_DEBUT;

import com.cegedim.next.serviceeligibility.consolidationcontract.bean.DeclarationKey;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class DeclarationKeyReaderConverter implements Converter<Document, DeclarationKey> {

  private static final Logger logger = LoggerFactory.getLogger(DeclarationKeyReaderConverter.class);

  @Override
  public DeclarationKey convert(Document declaration) {
    SimpleDateFormat readerConverterFormat = DateUtils.getReaderConverterFormat();
    DeclarationKey result = new DeclarationKey();
    result.set_id(declaration.getObjectId("_id").toHexString());
    result.setIdDeclarant(declaration.get("idDeclarant", String.class));
    Document contrat = declaration.get("contrat", Document.class);
    result.setContratNumero(contrat.get("numero", String.class));
    result.setNumeroAdherent(contrat.get("numeroAdherent", String.class));
    if (declaration.get(EFFET_DEBUT) instanceof Date) {
      result.setEffetDebut(declaration.get(EFFET_DEBUT, Date.class));
    } else {
      try {
        result.setEffetDebut(readerConverterFormat.parse(declaration.getString(EFFET_DEBUT)));
      } catch (ParseException e) {
        logger.error(
            "Impossible de convertir la chaine en data: {}", declaration.getString(EFFET_DEBUT));
        result.setEffetDebut(null);
      }
    }
    return result;
  }
}
