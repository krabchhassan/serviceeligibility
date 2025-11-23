package com.cegedim.scenario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cegedim.next.e2e.core.annotation.E2EScenario;
import com.cegedim.next.e2e.core.scenario.Scenario;
import com.cegedim.pages.SuiviFluxPage;
import com.cegedim.utils.AuthUtils;
import com.cegedim.utils.ImportUtils;

@E2EScenario(name = "Suivi Flux")
public class SuiviFluxScenario extends Scenario {

    public Date setDate(String date) {
        Date theDate = null;
        try {
            theDate = (new SimpleDateFormat("d/M/y")).parse(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return theDate;
    }

    @Override
    public void doScenario() {
        ImportUtils.doVolumetrieFluxDeclarationImport();

        AuthUtils.login(this);

        goTo(initialUrl + "/#/file_tracking");

        enterScenarioSection("Suivi de flux");
        /*
         * // Test du titre de la page enterScenarioItem("Titre de la page");
         * scenarioTitrePage();
         */

        /*
         * // Test des champs de recherche enterScenarioItem("Critère de recherche");
         * scenarioSearch();
         */
        // Test du resultat de recherche
        enterScenarioItem("Résultat de recherche");
        scenarioResult();
    }

    // Test du titre de la page
    public void scenarioTitrePage() {
        SuiviFluxPage fluxPage = new SuiviFluxPage(getWebDriver(), getScenarioReport());
        fluxPage.getPageTitle().hasContent("Suivi des Flux");
    }

    // Test des champs de recherche
    public void scenarioSearch() {
        SuiviFluxPage fluxPage = new SuiviFluxPage(getWebDriver(), getScenarioReport());

        fluxPage.getButtonMoreLessCriteria().click();
        fluxPage.getSearchPartenaire().isVisible();

        // Recherche par date de début
        Date dateFichier = setDate("29/10/2019");
        fluxPage.getSearchDateDebut().fill(dateFichier);
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        /*
         * dateFichier = setDate("30/10/2019");
         * fluxPage.getSearchDateDebut().fill(dateFichier);
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         */
        fluxPage.getButtonClearDateDebut().click();

        // Recherche par date de fin
        dateFichier = setDate("30/10/2019");
        fluxPage.getSearchDateFin().fill(dateFichier);
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("14 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("11 résultat(s) correspondant aux critères");
        /*
         * dateFichier = setDate("28/10/2016");
         * fluxPage.getSearchDateFin().fill(dateFichier);
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         */
        fluxPage.getButtonClearDateFin().click();

        // Recherche par N° AMC
        fluxPage.getSearchNumeroAMC().select("0038912994");
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("2 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        /*
         * fluxPage.getSearchNumeroAMC().select("0044317250");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         */
        fluxPage.getButtonClearCombo("N° AMC").click();

        // Recherche par Processus
        fluxPage.getSearchProcessus().select("Peuplement");
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("14 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        /*
         * fluxPage.getSearchProcessus().select("Almerys");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("4 résultat(s) correspondant aux critères");
         */
        fluxPage.getSearchProcessus().select("Tous");

        // Recherche par Partenaire
        fluxPage.getSearchPartenaire().fill("CLR");
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        /*
         * fluxPage.getSearchPartenaire().fill("TSTE2E");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         */
        fluxPage.getSearchPartenaire().fill("");

        // Recherche par Emetteur
        fluxPage.getSearchEmetteur().select("AMC");
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("11 résultat(s) correspondant aux critères");
        /*
         * fluxPage.getSearchEmetteur().select("TPG");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         */
        fluxPage.getSearchEmetteur().select("Tous");

        // Recherche par Nom d'AMC
        fluxPage.getSearchNomAMC().select("MACIF3");
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        /*
         * fluxPage.getSearchNomAMC().select("MACIF5");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         */
        fluxPage.getButtonClearCombo("Nom AMC").click();

        // Recherche par n° de fichier
        fluxPage.getSearchNumeroFichier().fill("21");
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("0 résultat(s) correspondant aux critères");
        /*
         * fluxPage.getSearchNumeroFichier().fill("12191");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("1 résultat(s) correspondant aux critères");
         */
        fluxPage.getSearchNumeroFichier().fill("");

        // Recherche par Type de fichier
        fluxPage.getSearchTypeFichier().select("TFD");
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("14 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("0 résultat(s) correspondant aux critères");
        /*
         * fluxPage.getSearchTypeFichier().select("ARL");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("1 résultat(s) correspondant aux critères");
         * fluxPage.getSearchTypeFichier().select("Extraction");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         */
        fluxPage.getSearchTypeFichier().select("Tous");

        // Recherche par circuit
        fluxPage.getSearchCircuit().select("02 - Droits hébergés BDD : AMC ==> BDD ==> ALMERYS");
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("5 résultat(s) correspondant aux critères");
        /*
         * fluxPage.getSearchCircuit().
         * select("01 - Droits hébergés BDD : AMC ==> BDD ==> TPG");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         */
        fluxPage.getSearchCircuit().select("Tous");

        // Recherche par nom de fichier
        fluxPage.getSearchNomFichier().fill("TEST_600.OK");
        fluxPage.getButtonSearch().click();
        fluxPage.getFichiersRecusNbResultSearch().hasContent("1 résultat(s) correspondant aux critères");
        fluxPage.getFichiersEmisNbResultSearch().hasContent("0 résultat(s) correspondant aux critères");
        /*
         * fluxPage.getSearchNomFichier().fill(
         * "ARL_606_CLR_0038912994_AL_20191029-094951306.csv");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("1 résultat(s) correspondant aux critères");
         * fluxPage.getSearchNomFichier().fill("AUCUN_FICHIER_TROUVE.TEST");
         * fluxPage.getButtonSearch().click();
         * fluxPage.getFichiersRecusNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         * fluxPage.getFichiersEmisNbResultSearch().
         * hasContent("0 résultat(s) correspondant aux critères");
         */
        fluxPage.getSearchNomFichier().fill("");
    }

    // Test du résultat de la recherche
    public void scenarioResult() {
        goTo(initialUrl + "/#/file_tracking");

        SuiviFluxPage fluxPage = new SuiviFluxPage(getWebDriver(), getScenarioReport());
        fluxPage.getButtonSearch().click();
        fluxPage.getButtonExpandFichierRecu(1).click();
        fluxPage.getButtonExpandFichierEmis(1).click();

        // On ne peut pas tester une valeur contenant l'heure à cause de la Time
        // Zone
        // fluxPage.getContenuCelluleFichierRecu(1, 2).hasContent("29/10/2019
        // 10:49");
        fluxPage.getContenuCelluleFichierRecu(1, 3).hasContent("CLR");
        fluxPage.getContenuCelluleFichierRecu(1, 4).hasContent("0038912994 - MACIF3");
        fluxPage.getContenuCelluleFichierRecu(1, 5).hasContent("Peuplement");
        fluxPage.getContenuCelluleFichierRecu(1, 6).hasContent("TFD");
        fluxPage.getContenuCelluleFichierRecu(1, 7).hasContent("TEST_600.OK");
        fluxPage.getContenuCelluleFichierRecu(1, 8).hasContent("21");
        fluxPage.getContenuCelluleFichierRecu(1, 10).hasContent("10");
        fluxPage.getContenuCelluleFichierRecu(1, 11).hasContent("10");
        fluxPage.getContenuCelluleFichierRecu(1, 12).hasContent("0");

        fluxPage.getDetailInfoFichierRecu(1, "Numéro AMC échanges").hasContent("-");
        fluxPage.getDetailInfoFichierRecu(1, "Opérateur principal").hasContent("AL");
        fluxPage.getDetailInfoFichierRecu(1, "Emetteur de droits").hasContent("AMC");
        fluxPage.getDetailInfoFichierRecu(1, "Batch").hasContent("606");
        fluxPage.getDetailInfoFichierRecu(1, "Version").hasContent("V02");
        fluxPage
                .getDetailInfoFichierRecu(1, "Nom fichier ARL")
                    .hasContent("ARL_606_CLR_0038912994_AL_20191029-094951306.csv");
        fluxPage
                .getDetailInfoFichierRecu(1, "Circuit")
                    .hasContent("02 - Droits hébergés BDD : AMC ==> BDD ==> ALMERYS");
        fluxPage.getDetailInfoFichierRecu(1, "Rejet").hasContent("-");
        fluxPage.getDetailInfoFichierRecu(1, "Motif").hasContent("-");

        // On ne peut pas tester une valeur contenant l'heure à cause de la Time
        // Zone
        // fluxPage.getContenuCelluleFichierEmis(1, 2).hasContent("29/10/2019
        // 10:49");
        fluxPage.getContenuCelluleFichierEmis(1, 3).hasContent("CLR");
        fluxPage.getContenuCelluleFichierEmis(1, 4).hasContent("0038912994 - MACIF3");
        fluxPage.getContenuCelluleFichierEmis(1, 5).hasContent("Peuplement");
        fluxPage.getContenuCelluleFichierEmis(1, 6).hasContent("ARL");
        fluxPage.getContenuCelluleFichierEmis(1, 7).hasContent("ARL_606_CLR_0038912994_AL_20191029-094951306.csv");
        fluxPage.getContenuCelluleFichierEmis(1, 9).hasContent("11");
        fluxPage.getContenuCelluleFichierEmis(1, 10).hasContent("0.00 %");

        fluxPage.getDetailInfoFichierEmis(1, "Batch").hasContent("606");
        fluxPage.getDetailInfoFichierEmis(1, "Version").hasContent("-");
        fluxPage.getDetailInfoFichierEmis(1, "Nom fichier ARL").hasContent("-");
        fluxPage
                .getDetailInfoFichierEmis(1, "Circuit")
                    .hasContent("02 - Droits hébergés BDD : AMC ==> BDD ==> ALMERYS");
        fluxPage.getDetailInfoFichierEmis(1, "Critère secondaire").hasContent("-");
        fluxPage.getDetailInfoFichierEmis(1, "Critère secondaire détaillé").hasContent("-");
    }
}
