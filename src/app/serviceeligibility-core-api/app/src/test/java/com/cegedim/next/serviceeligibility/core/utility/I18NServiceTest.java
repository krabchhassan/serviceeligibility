package com.cegedim.next.serviceeligibility.core.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class I18NServiceTest {

  @Test
  void should_get_key() {
    I18NService service = new I18NService();
    String message = service.getMessage(6000);
    Assertions.assertEquals("Paramètres recherche BDD incorrects", message);
  }
}
