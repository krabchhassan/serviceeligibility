package com.cegedim.scenario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.cegedim.next.e2e.core.annotation.E2EScenario;
import com.cegedim.next.e2e.core.scenario.Scenario;
import com.cegedim.objects.Garantie;
import com.cegedim.objects.Prestation;
import com.cegedim.pages.SuiviDroitAssureDetailPage;
import com.cegedim.pages.SuiviDroitAssureSearchPage;
import com.cegedim.utils.AuthUtils;
import com.cegedim.utils.ImportUtils;

@E2EScenario(name = "Droit Assuré")
public class SearchDroitAssureScenario extends Scenario {
    @Override
    public void doScenario() {
        // Import des données de test
        ImportUtils.doVolumetrieFluxDeclarationImport();

        // Login
        AuthUtils.login(this);

        // Url de test de l'UI
        goTo(initialUrl + "/#/beneficiary_rights_tracking");

        //
        // Scenarii
        //
        enterScenarioSection("Recherche de droits assuré");
        /*
         * // Titre de la page enterScenarioItem("Titre de la page");
         * scenarioTitrePage();
         * 
         * // Test des champs de recherche
         * enterScenarioItem("Droit assuré / Recherche"); scenarioChampsRecherche();
         */
        // Test du résultat de recherche
        enterScenarioItem("Droit assuré / Résultat de recherche");
        scenarioResultatRecherche();

        // Détail du 1er assuré
        enterScenarioItem("Droit assuré / Détail droit");
        scenarioDetailRecherche();
    }

    public void scenarioTitrePage() {
        SuiviDroitAssureSearchPage searchPage = new SuiviDroitAssureSearchPage(getWebDriver(), getScenarioReport());
        searchPage.getPageTitle().hasContent("Suivi des droits de l'assuré");
    }

    public void scenarioChampsRecherche() {
        SuiviDroitAssureSearchPage searchPage = new SuiviDroitAssureSearchPage(getWebDriver(), getScenarioReport());
        /*
         * Recherche NIR incompatible avec Selenium // NIR
         * searchPage.getItemSearchNIR().fill("2790613055093");
         * searchPage.getButtonSearch().isEnabled();
         * searchPage.getButtonClearSearch().click();
         */

        // AMC + N° adhérent
        searchPage.getComboSearchNumeroAMC().select("0780508073 - CCMO");
        searchPage.getButtonSearch().isNotEnabled();
        searchPage.getItemSearchNumeroAdherent().fill("00000027");
        searchPage.getButtonSearch().isEnabled();
        searchPage.getButtonClearSearch().click();
        searchPage.getButtonSearch().isNotEnabled();
        // AMC + N° Contrat
        searchPage.getComboSearchNumeroAMC().select("0780508073 - CCMO");
        searchPage.getItemSearchNumeroContrat().fill("900000027");
        searchPage.getButtonSearch().isEnabled();
        searchPage.getButtonClearSearch().click();
        // Autres critères
        searchPage.getItemSearchNumeroAMCEchange().fill("0060005615");
        searchPage.getItemSearchNumeroAdherent().fill("00000204");
        searchPage.getItemSearchNumeroContrat().fill("900000204");

        Date dateNaissance = null;
        try {
            dateNaissance = (new SimpleDateFormat("d/M/y")).parse("13/03/2013");
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        searchPage.getItemSearchDateNaissance().fill(dateNaissance);
        searchPage.getItemSearchRang().fill("2");
        searchPage.getButtonSearch().isNotEnabled();
        searchPage.getButtonClearSearch().click();
    }

    public void scenarioResultatRecherche() {
        SuiviDroitAssureSearchPage searchPage = new SuiviDroitAssureSearchPage(getWebDriver(), getScenarioReport());
        searchPage.getButtonClearSearch().click();
        searchPage.getComboSearchNumeroAMC().select("0780508073 - CCMO");
        searchPage.getItemSearchNumeroContrat().fill("900000027");
        searchPage.getButtonSearch().click();
        searchPage.getNbResultSearch().hasContent("(1)");
        searchPage.getResultSearch("numeroContrat").isVisible();
        searchPage.getValueResultSearch("numeroContrat").hasContent("900000027");
        searchPage.getResultSearch("numeroOuNomAMC").isVisible();
        searchPage.getValueResultSearch("numeroOuNomAMC").hasContent("0780508073");
    }

    public void scenarioDetailRecherche() {
        SuiviDroitAssureSearchPage searchPage = new SuiviDroitAssureSearchPage(getWebDriver(), getScenarioReport());
        SuiviDroitAssureDetailPage detailPage = new SuiviDroitAssureDetailPage(getWebDriver(), getScenarioReport());

        searchPage.getButtonClearSearch().click();
        searchPage.getComboSearchNumeroAMC().select("0780508073 - CCMO");
        searchPage.getItemSearchNumeroContrat().fill("900000027");
        searchPage.getButtonSearch().click();

        searchPage.getButtonOpenAssure(1).click();
        detailPage.getButtonExpandPanels().click();
        // On ne peut pas tester une valeur contenant l'heure à cause de la Time
        // Zone
        // detailPage.getPageTitle().hasContent("Déclaration des droits du
        // 21/02/2019 à 01:03 pour l'assuré PALOT Alain");

        // Panel : Identification Assuré
        detailPage
                .getPanelComment("indentification-panel")
                    .hasContent("Né(e) le 13/08/1977 - 1 / N° RO 1 77 08 13 056 052 42");

        // Assuré
        detailPage.getSousPanelInfo("insured-member").hasContent("00000027");
        detailPage.getSousPanelInfo("insured-number-member-complet").hasContent("-");
        detailPage.getSousPanelInfo("insured-number-person").hasContent("00005210");
        detailPage.getSousPanelInfo("insured-external-person-number").hasContent("00005210");
        detailPage.getSousPanelInfo("insured-family-name").hasContent("PALOT");
        detailPage.getSousPanelInfo("insured-marital-name").hasContent("PALOT");
        // Assureur
        detailPage.getSousPanelInfo("insurer-number-amc").hasContent("0780508073 - CCMO");
        detailPage.getSousPanelInfo("insurer-number-amc-exchange").hasContent("0060005615");
        detailPage.getSousPanelInfo("insurer-number-operator").hasContent("-");
        // Affiliation
        detailPage.getSousPanelInfo("affiliation-nir").hasContent("1 77 08 13 056 052 42");
        detailPage.getSousPanelInfo("affiliation-ro2").hasContent("-");
        detailPage.getSousPanelInfo("affiliation-date-start").hasContent("01/05/2009");
        detailPage.getSousPanelInfo("affiliation-subdivision-ro1").hasContent("01 131 -");
        detailPage.getSousPanelInfo("affiliation-subdivision-ro2").hasContent("- - -");
        detailPage.getSousPanelInfo("affiliation-specific-subdivision").hasContent("-");

        // Panel : Contrat
        detailPage.getPanelComment("contract-panel").hasContent("Contrat collectif : 8037-01 / Type contrat : Base");
        // Assuré
        detailPage.getSousPanelInfo("contract-ext-num").hasContent("-");
        detailPage.getSousPanelInfo("contract-criteria").hasContent("-");
        detailPage.getSousPanelInfo("contract-manager").hasContent("MIAIHM");
        detailPage.getSousPanelInfo("contract-subscriber").hasContent("M Alain PALOT");
        detailPage.getSousPanelInfo("contract-date-subscriber").hasContent("01/05/2010");
        detailPage.getSousPanelInfo("contract-external-collective-number").hasContent("-");
        detailPage.getSousPanelInfo("contract-secondary-criteria-detailed").hasContent("-");
        detailPage.getSousPanelInfo("contract-convention-type").hasContent("Gestion séparée avec DRE / MF");
        detailPage.getSousPanelInfo("contract-group-insured").hasContent("8037");
        detailPage.getSousPanelInfo("contract-insured-type").hasContent("ASSURE");
        detailPage.getSousPanelInfo("contract-cmu").hasContent("Non");
        detailPage.getSousPanelInfo("contract-responsible").hasContent("Oui");
        detailPage.getSousPanelInfo("contract-acs").hasContent("Non");
        detailPage.getSousPanelInfo("contract-remote-transmission").hasContent("Oui");
        detailPage.getSousPanelInfo("contract-payment-method").hasContent("V");

        // Panel Droit
        detailPage.getSousPanelInfo("rights-panel").hasContent("11 domaine(s) de droits - 1 garantie(s)");

        ArrayList<Prestation> prestations = new ArrayList<>();
        ArrayList<Garantie> garanties = new ArrayList<>();
        prestations.add(new Prestation("DEF", "02A", "3 - 100.00"));
        garanties.add(new Garantie("AUXM", "0.MHS0100 - MIAIHM", "01", "16/01/2019", "31/12/2018", "100", prestations));
        /*
         * garanties.add(new Garantie("COSL", "0.MHS0100 - MIAIHM", "01", "16/01/2019",
         * "31/12/2018", "100", prestations));
         * 
         * prestations = new ArrayList<>(); prestations.add(new Prestation("DEF", "099",
         * "Formule sans paramètre")); garanties.add(new Garantie("DEOR",
         * "0.MHS0100 - MIAIHM", "01", "16/01/2019", "31/12/2018", "OUI", prestations));
         * 
         * garanties.add(new Garantie("DEPR", "0.MHS0100 - MIAIHM", "01", "16/01/2019",
         * "31/12/2018", "OUI", prestations));
         * 
         * prestations = new ArrayList<>(); prestations.add(new Prestation("DEF", "02A",
         * "3 - 000.00")); garanties.add(new Garantie("EXTE", "0.MHS0100 - MIAIHM",
         * "01", "16/01/2019", "31/12/2018", "100", prestations));
         * 
         * prestations = new ArrayList<>(); prestations.add(new Prestation("DEF", "099",
         * "Formule sans paramètre")); garanties.add(new Garantie("HOSC",
         * "0.MHS0100 - MIAIHM", "01", "16/01/2019", "31/12/2018", "OUI", prestations));
         * 
         * prestations = new ArrayList<>(); prestations.add(new Prestation("DEF", "02A",
         * "3 - 100.00")); garanties.add(new Garantie("LABO", "0.MHS0100 - MIAIHM",
         * "01", "16/01/2019", "31/12/2018", "100", prestations));
         * 
         * prestations = new ArrayList<>(); prestations.add(new Prestation("DEF", "099",
         * "Formule sans paramètre")); garanties.add(new Garantie("OPTI",
         * "0.MHS0100 - MIAIHM", "01", "16/01/2019", "31/12/2018", "OUI", prestations));
         * 
         * prestations = new ArrayList<>(); prestations.add(new Prestation("B", "02A",
         * "3 - 100.00")); prestations.add(new Prestation("CPH", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("HD4", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("HD7", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("HDA", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("HDE", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("HDR", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("HG4", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("HG7", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("MHU", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("MPI", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("PG4", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("PG7", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("PH1", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("PH4", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("PH7", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("PHA", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("PHN", "02A", "3 - 000.00"));
         * prestations.add(new Prestation("PM4", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("PMH", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("PMR", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("PPI", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("SNG", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("TNS", "02A", "3 - 100.00"));
         * prestations.add(new Prestation("UPH", "02A", "3 - 100.00"));
         * garanties.add(new Garantie("PHAR", "0.MHS0100 - MIAIHM", "01", "16/01/2019",
         * "31/12/2018", "100", prestations));
         */
        prestations = new ArrayList<>();
        prestations.add(new Prestation("HD2", "02A", "3 - 100.00"));
        prestations.add(new Prestation("HG2", "02A", "3 - 100.00"));
        prestations.add(new Prestation("PH2", "02A", "3 - 100.00"));
        prestations.add(new Prestation("PM2", "02A", "3 - 100.00"));
        garanties.add(new Garantie("PHOR", "0.MHS0100 - MIAIHM", "01", "16/01/2019", "31/12/2018", "100", prestations));
        /*
         * prestations = new ArrayList<>(); prestations.add(new Prestation("DEF", "02A",
         * "3 - 100.00")); garanties.add(new Garantie("RADL", "0.MHS0100 - MIAIHM",
         * "01", "16/01/2019", "31/12/2018", "100", prestations));
         */
        for (Garantie garantie : garanties) {
            detailPage
                    .getSousPanelInfo("right-item-" + garantie.getCode() + "-garantie")
                        .hasContent(garantie.getLibelle());
            detailPage
                    .getSousPanelInfo("right-item-" + garantie.getCode() + "-priority")
                        .hasContent(garantie.getPriorite());
            detailPage.getDateDebutGarantie(garantie.getCode()).hasContent(garantie.getDateDebut());
            detailPage.getDateFinGarantie(garantie.getCode()).hasContent(garantie.getDateFin());
            detailPage.getSousPanelInfo("right-item-" + garantie.getCode() + "-rate").hasContent(garantie.getTaux());
            // Vérification du détail des prestations
            detailPage.getButtonPrestations(garantie.getCode()).click();
            int indicePrestation = 0;
            for (Prestation prest : garantie.getPrestations()) {
                indicePrestation++;
                detailPage.getPrestationCode(indicePrestation).hasContent(prest.getCode());
                detailPage.getPrestationFormule(indicePrestation).hasContent(prest.getFormule());
                detailPage.getPrestationParametre(indicePrestation).hasContent(prest.getParametre());
            }
            // Fermeture de la fenêtre de détail des prestations
            detailPage.getButtonClosePrestations().click();
        }

        // Retour à la page d'acceuil
        detailPage.getUrlRetour().click();
        detailPage.getPageTitle().hasContent("Suivi des droits de l'assuré");

    }
}
