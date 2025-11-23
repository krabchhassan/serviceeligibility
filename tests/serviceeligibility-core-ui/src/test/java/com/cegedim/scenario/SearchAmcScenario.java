package com.cegedim.scenario;

import com.cegedim.next.e2e.core.annotation.E2EScenario;
import com.cegedim.next.e2e.core.scenario.Scenario;
import com.cegedim.pages.AmcSearchPage;
import com.cegedim.utils.AuthUtils;
import com.cegedim.utils.ImportUtils;

@E2EScenario(name = "Recherche d'AMC")
public class SearchAmcScenario extends Scenario {

    @Override
    public void doScenario() {
        // Import du jeu de test
        ImportUtils.doDeclarantsImport();

        // Connexion
        AuthUtils.login(this);

        //
        // Scenarii
        //
        enterScenarioSection("AMC - Page de recherche");

        // Recherche par n° d'AMC
        enterScenarioItem("Recherche d'AMC par identifiant");
        scenarioSearchByAmcNumber();

        // Réinitialisation de la recherche
        enterScenarioItem("Réinitialisation de la recherche");
        scenarioClearSearch();

        // Recherche par libelle court
        enterScenarioItem("Recherche par Libellé Court");
        scenarioSearchByLibelleCourt();

        // Recherche par Couloir client et service
        enterScenarioItem("Recherche par Couloir client et Service");
        scenarioSearchByCouloirClientAndService();

        // Recherche par service
        enterScenarioItem("Recherche par Service uniquement");
        scenarioSearchByService();
    }

    public void scenarioSearchByAmcNumber() {
        AmcSearchPage amcPage = new AmcSearchPage(getWebDriver(), getScenarioReport());

        amcPage.getButtonClearSearch().click();
        amcPage.getComboSearchNumeroAMC().select("0775678584");
        amcPage.getComboSearchLibelleCourt().isNotEnabled();
        amcPage.getComboSearchService().isNotEnabled();
        amcPage.getItemSearchCouloir().isNotEnabled();
        amcPage.getButtonSearch().click();
        amcPage.getNbResultSearch().hasContent("(1)");
        amcPage.getCritereRecherche(1).hasContent("Numéro AMC :");
        amcPage.getValeurRecherche(1).hasContent("0775678584");
        amcPage.getValeurPremierAMC().hasContent("0775678584 - MNT CTER - MNT COMPLEMEN'TER");
    }

    public void scenarioSearchByLibelleCourt() {
        AmcSearchPage amcPage = new AmcSearchPage(getWebDriver(), getScenarioReport());

        amcPage.getButtonClearSearch().click();
        amcPage.getComboSearchLibelleCourt().select("GRAS SAVOYE");
        amcPage.getComboSearchNumeroAMC().isNotEnabled();
        amcPage.getComboSearchService().isNotEnabled();
        amcPage.getItemSearchCouloir().isNotEnabled();
        amcPage.getButtonSearch().click();
        amcPage.getNbResultSearch().hasContent("(1)");
        amcPage.getCritereRecherche(1).hasContent("Libellé court :");
        amcPage.getValeurRecherche(1).hasContent("GRAS SAVOYE");
        amcPage.getValeurPremierAMC().hasContent("0000401026 - GRAS SAVOYE - GRAS SAVOYE");
    }

    public void scenarioSearchByCouloirClientAndService() {
        AmcSearchPage amcPage = new AmcSearchPage(getWebDriver(), getScenarioReport());

        amcPage.getButtonClearSearch().click();
        amcPage.getItemSearchCouloir().append("IS");
        amcPage.getComboSearchNumeroAMC().isNotEnabled();
        amcPage.getComboSearchLibelleCourt().isNotEnabled();
        amcPage.getComboSearchService().select("DCLBEN");
        amcPage.getButtonSearch().click();
        amcPage.getNbResultSearch().hasContent("(11)");
        amcPage.getCritereRecherche(1).hasContent("Couloir client :");
        amcPage.getValeurRecherche(1).hasContent("IS");
        amcPage.getCritereRecherche(2).hasContent("Service :");
        amcPage.getValeurRecherche(2).hasContent("DCLBEN");
        amcPage.getValeurPremierAMC().hasContent("0000401182 - AON - AON");
    }

    public void scenarioSearchByService() {
        AmcSearchPage amcPage = new AmcSearchPage(getWebDriver(), getScenarioReport());

        amcPage.getButtonClearSearch().click();
        amcPage.getComboSearchService().select("DCLBEN");
        amcPage.getComboSearchNumeroAMC().isNotEnabled();
        amcPage.getComboSearchLibelleCourt().isNotEnabled();
        amcPage.getItemSearchCouloir().isEnabled();
        amcPage.getButtonSearch().click();
        amcPage.getNbResultSearch().hasContent("(13)");
        amcPage.getCritereRecherche(1).hasContent("Service :");
        amcPage.getValeurRecherche(1).hasContent("DCLBEN");
        amcPage.getValeurPremierAMC().hasContent("0000401182 - AON - AON");
    }

    public void scenarioClearSearch() {
        AmcSearchPage amcPage = new AmcSearchPage(getWebDriver(), getScenarioReport());

        amcPage.getComboSearchNumeroAMC().select("0775678584");
        amcPage.getButtonClearSearch().click();
        amcPage.getComboSearchLibelleCourt().isEnabled();
        amcPage.getComboSearchNumeroAMC().isEnabled();
        amcPage.getComboSearchService().isEnabled();
        amcPage.getItemSearchCouloir().isEnabled();
    }

}
