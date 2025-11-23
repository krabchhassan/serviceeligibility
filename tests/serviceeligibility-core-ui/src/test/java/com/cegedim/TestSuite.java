package com.cegedim;

import org.junit.runners.Suite.SuiteClasses;

import com.cegedim.next.e2e.core.annotation.E2ESuite;
import com.cegedim.next.e2e.core.suite.Suite;
import com.cegedim.scenario.CreateUpdateAmcScenario;
import com.cegedim.scenario.ParametrageScenario;
import com.cegedim.scenario.SearchAmcScenario;
import com.cegedim.scenario.SearchDroitAssureScenario;
import com.cegedim.scenario.SuiviFluxScenario;
import com.cegedim.scenario.TranscodageScenario;
import com.cegedim.scenario.VolumetrieScenario;

@SuiteClasses({ ParametrageScenario.class, CreateUpdateAmcScenario.class, SearchAmcScenario.class,
        SearchDroitAssureScenario.class, SuiviFluxScenario.class, TranscodageScenario.class, VolumetrieScenario.class })

@E2ESuite(name = "Service Eligibility")
public class TestSuite extends Suite {

    // @BeforeClass
    // public static void before() {
    // ImportUtils.doDeclarantsImport();
    // }
}
