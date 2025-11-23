package com.cegedim.next.serviceeligibility.facturation.htp;

import static org.junit.jupiter.api.Assertions.*;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.common.omu.helper.OmuHelperImpl;
import com.cegedim.common.omu.helper.exception.SecuredAnalyzerException;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import com.cegedim.next.serviceeligibility.facturation.htp.services.Processor;
import io.micrometer.tracing.Tracer;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CommandLineEntryPointTest {

  private CommandLineEntryPoint commandLineEntryPoint;

  @Mock private Tracer tracer;

  private OmuHelper omuHelper = new OmuHelperImpl();

  private Processor processor;

  private CrexProducer crexProducer = new CrexProducer();

  @Mock private DeclarantService declarantService;

  @Mock private ContractService contractService;

  private static final LocalDate CURRENT_DATE = LocalDate.now();

  private static final String MOIS = "--MOIS";

  private static final String ANNEE = "--ANNEE";

  @BeforeEach
  void setUp() {
    processor = new Processor(contractService, declarantService);
    commandLineEntryPoint =
        new CommandLineEntryPoint(
            omuHelper, processor, crexProducer, tracer, "", "", "", "INSURER");
  }

  @Test
  void readParameterEmpty() throws SecuredAnalyzerException {
    String[] emptyArgs = new String[0];
    LocalDate checkDate = CURRENT_DATE.minusMonths(1);
    LocalDate shouldReturn =
        YearMonth.of(checkDate.getYear(), checkDate.getMonthValue()).atEndOfMonth();
    shouldReturn(shouldReturn, emptyArgs);

    emptyArgs = new String[] {"--MOIS", "", "--ANNEE", ""};
    shouldReturn(shouldReturn, emptyArgs);
  }

  /**
   * Case 1 : mois demande apres current mois -> le calcul est donc demande pour l annee precedente
   * || Case 2 : mois demande avant current mois -> le calcul est donc demande pour l annee en cours
   * sauf si mois courant janvier
   */
  @Test
  void readParameterMonth() throws SecuredAnalyzerException {
    // Case 1
    int targetMonth = 12;
    String[] args = new String[] {MOIS, Integer.toString(targetMonth)};
    LocalDate shouldReturn = YearMonth.of(CURRENT_DATE.getYear() - 1, targetMonth).atEndOfMonth();
    shouldReturn(shouldReturn, args);

    // Case 2
    targetMonth = 1;
    args = new String[] {MOIS, Integer.toString(targetMonth)};
    int targetYear =
        CURRENT_DATE.getYear() - (Month.JANUARY.equals(CURRENT_DATE.getMonth()) ? 1 : 0);
    shouldReturn = YearMonth.of(targetYear, targetMonth).atEndOfMonth();
    shouldReturn(shouldReturn, args);
  }

  /**
   * Si seulement l annee est renseignee alors on a le meme fonctionnement que si on donne aucun
   * argument
   */
  @Test
  void readParameterYear() throws SecuredAnalyzerException {
    String[] args = new String[] {ANNEE, "2999"};
    LocalDate checkDate = CURRENT_DATE.minusMonths(1);
    LocalDate shouldReturn =
        YearMonth.of(checkDate.getYear(), checkDate.getMonthValue()).atEndOfMonth();
    shouldReturn(shouldReturn, args);
  }

  /** Si on donne les deux parametres alors on fait le calcul pour l annee et le mois renseignes */
  @Test
  void readParameterMonthYear() throws SecuredAnalyzerException {
    int targetYear = 2999;
    int targetMonth = 3;
    String[] args =
        new String[] {ANNEE, Integer.toString(targetYear), MOIS, Integer.toString(targetMonth)};
    LocalDate shouldReturn = YearMonth.of(targetYear, targetMonth).atEndOfMonth();
    shouldReturn(shouldReturn, args);
  }

  /** Si les parametres passes sont pas au format YYYY pour --ANNEE et MM pour --MOIS on throw */
  @Test
  void badParameterType() {
    String[] argsAnnee = new String[] {ANNEE, "blabla"};
    assertThrows(NumberFormatException.class, () -> shouldReturn(CURRENT_DATE, argsAnnee));

    String[] argsMois = new String[] {MOIS, "blabla"};
    assertThrows(NumberFormatException.class, () -> shouldReturn(CURRENT_DATE, argsMois));
  }

  void shouldReturn(LocalDate shouldReturn, String... args) throws SecuredAnalyzerException {
    ApplicationArguments arguments = new DefaultApplicationArguments(args);
    int returnCode = commandLineEntryPoint.readParameters(arguments);
    assertEquals(0, returnCode);
    assertEquals(shouldReturn, commandLineEntryPoint.getDateCalcul());
  }
}
