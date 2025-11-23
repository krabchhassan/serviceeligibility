package com.cegedim.scenario;

import com.cegedim.next.e2e.core.annotation.E2EScenario;
import com.cegedim.next.e2e.core.scenario.Scenario;
import com.cegedim.pages.TranscodagePage;
import com.cegedim.utils.AuthUtils;
import com.cegedim.utils.ImportUtils;

@E2EScenario(name = "Transcodage")
public class TranscodageScenario extends Scenario {
    @Override
    public void doScenario() {
        ImportUtils.doDeclarantsImport();

        AuthUtils.login(this);

        goTo(initialUrl + "/#/transcoding");

        TranscodagePage transcoPage = new TranscodagePage(getWebDriver(), getScenarioReport());

        enterScenarioSection("Visualisation du transcodage");
        /*
         * enterScenarioItem("Titre de la page");
         * transcoPage.getPageTitle().hasContent("Transcodage");
         */

        //
        // Transcodage ALMV3 / Domaine de droits
        //
        enterScenarioItem("Transcodage ALMV3 / Domaine de droits");
        transcoPage.getComboService().select("ALMV3");
        transcoPage.getComboObjet().select("Domaines de droits");
        transcoPage.getColTableauTransco(1, 1).hasContent("ALMER");
        transcoPage.getColTableauTransco(1, 2).hasContent("ALMER");
        /*
         * transcoPage.getColTableauTransco(2, 1).hasContent("TPALM");
         * transcoPage.getColTableauTransco(2, 2).hasContent("ALMER");
         */
        enterScenarioItem("Transcodage ALMV3 / Code Mouvements");
        transcoPage.getComboObjet().select("Code Mouvement");
        transcoPage.getColTableauTransco(1, 1).hasContent("AD");
        transcoPage.getColTableauTransco(1, 2).hasContent("F");
        transcoPage.getColTableauTransco(1, 3).hasContent("0");
        transcoPage.getColTableauTransco(1, 4).hasContent("MS");
        /*
         * transcoPage.getColTableauTransco(2, 1).hasContent("AD");
         * transcoPage.getColTableauTransco(2, 2).hasContent("O");
         * transcoPage.getColTableauTransco(2, 3).hasContent("1");
         * transcoPage.getColTableauTransco(2, 4).hasContent("CC");
         */
        enterScenarioItem("Transcodage ALMV3 / Mode paiement / Création");
        transcoPage.getComboObjet().select("Mode paiement");
        transcoPage.getButtonAdd().click();
        transcoPage.getValeurATranscoder("Mode_paiement_prestations").fill("E");
        transcoPage.getValeurTranscodee().fill("ES");
        transcoPage.getButtonValidateTransco().click();
        transcoPage.getColTableauTransco(2, 1).hasContent("E");
        transcoPage.getColTableauTransco(2, 2).hasContent("ES");
        enterScenarioItem("Transcodage ALMV3 / Mode paiement / Suppression");
        transcoPage.getButtonDeleteViaValeurTransco("ES").click();
        transcoPage.getButtonConfirmDelete().click();
        transcoPage.getColTableauTransco(2, 1).hasContent("V");
    }

}
