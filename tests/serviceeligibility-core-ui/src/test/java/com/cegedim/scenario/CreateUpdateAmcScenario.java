package com.cegedim.scenario;

import java.util.Date;

import com.cegedim.next.e2e.core.annotation.E2EScenario;
import com.cegedim.next.e2e.core.element.find.strategies.WaitingStrategyType;
import com.cegedim.next.e2e.core.scenario.Scenario;
import com.cegedim.pages.AmcConsultPage;
import com.cegedim.pages.AmcCreatePage;
import com.cegedim.pages.AmcSearchPage;
import com.cegedim.utils.AuthUtils;
import com.cegedim.utils.ImportUtils;
import com.cegedim.utils.ServiceEnum;

@E2EScenario(name = "Création, Mise à jour et duplication d'AMC", strategyTypes = WaitingStrategyType.DOCUMENT)
public class CreateUpdateAmcScenario extends Scenario {

    @Override
    public void doScenario() {
        // Import des données de test
        ImportUtils.doDeclarantsImport();

        // Connexion
        AuthUtils.login(this);

        //
        // Scenarii
        //
        enterScenarioSection("AMC Création / Modification / Duplication");

        // Annulation de la creation d'AMC
        enterScenarioItem("Annulation de la création");
        scenarioAnnulationCreation();

        // Création d'une nouvelle AMC
        enterScenarioItem("Création d'une nouvelle AMC");
        scenarioCreationAMC();

        // Consultation du nouvel AMC
        enterScenarioItem("Visualisation de la nouvelle AMC");
        scenarioVisuNewAmc();

        /*
         * // Modification de l'AMC enterScenarioItem("Update a new AMC");
         * scenarioUpdateNewAmc();
         */

        // Duplication du nouvel AMC
        enterScenarioItem("Duplication de la nouvelle AMC");
        scenarioDuplicateAmc();

    }

    // Test du titre de la page
    public void scenarioAnnulationCreation() {
        AmcSearchPage amcSearchPage = new AmcSearchPage(getWebDriver(), getScenarioReport());
        AmcCreatePage amcCreatePage = new AmcCreatePage(getWebDriver(), getScenarioReport());

        amcSearchPage.getButtonCreateAMC().click();
        amcCreatePage.getButtonCancel().click();
        amcSearchPage.getButtonCreateAMC().click();
        amcCreatePage.getItemNumeroAMC("Create").fill("1234567890");
        amcCreatePage.getButtonCancel().click();
        amcCreatePage.getButtonResterPageCreation().click();
        amcCreatePage.getButtonCancel().click();
        amcCreatePage.getButtonConfirmeAnnulation().click();
        amcSearchPage.getComboSearchNumeroAMC().isVisible();
    }

    public void scenarioCreationAMC() {
        AmcSearchPage amcSearchPage = new AmcSearchPage(getWebDriver(), getScenarioReport());
        AmcCreatePage amcCreatePage = new AmcCreatePage(getWebDriver(), getScenarioReport());

        amcSearchPage.getButtonCreateAMC().click();
        amcCreatePage.getItemNumeroAMC("Create").isEnabled();
        amcCreatePage.getItemLibCourt("Create").isEnabled();
        amcCreatePage.getItemLibLong("Create").isEnabled();
        amcCreatePage.getItemCodePartenaire("Create").isEnabled();
        amcCreatePage.getComboCircuit().isEnabled();
        amcCreatePage.getItemEmetteurDroits("Create").isNotEnabled();
        amcCreatePage.getComboOperateurPrincipal().isEnabled();
        amcCreatePage.getItemSiret("Create").isEnabled();
        amcCreatePage.getButtonSubmit().isNotEnabled();

        // Test des messages de validation
        // => N° AMC
        amcCreatePage.getMsgValidationNumAMC("Create").isVisible();
        amcCreatePage.getMsgValidationNumAMC("Create").hasContent("Requis");
        amcCreatePage.getItemNumeroAMC("Create").append("154548484");
        amcCreatePage.getMsgValidationNumAMC("Create").isVisible();
        amcCreatePage.getMsgValidationNumAMC("Create").hasContent("Format invalide : 10 caractères requis");
        amcCreatePage.getItemNumeroAMC("Create").append("11");
        amcCreatePage.getMsgValidationNumAMC("Create").isVisible();
        amcCreatePage.getMsgValidationNumAMC("Create").hasContent("Format invalide : 10 caractères requis");
        // => Libellé Court
        amcCreatePage.getMsgValidationLibCourt("Create").isVisible();
        amcCreatePage.getMsgValidationLibCourt("Create").hasContent("Requis");
        // => Code partenaire
        amcCreatePage.getMsgValidationCodePartenaire("Create").isVisible();
        amcCreatePage.getMsgValidationCodePartenaire("Create").hasContent("Requis");
        // => Code circuit
        amcCreatePage.getMsgValidationCircuitAMC().isVisible();
        amcCreatePage.getMsgValidationCircuitAMC().hasContent("Requis");
        // => Opérateur principal
        amcCreatePage.getMsgValidationOperateurPrincipal().isVisible();
        amcCreatePage.getMsgValidationOperateurPrincipal().hasContent("Requis");

        // Création de l'entête de l'AMC
        amcCreatePage.getItemNumeroAMC("Create").fill("0775627391");
        amcCreatePage.getItemLibCourt("Create").fill("APREVA");
        amcCreatePage.getItemLibLong("Create").fill("APREVA MUTUELLE");
        amcCreatePage.getItemCodePartenaire("Create").fill("AES");
        // Saisie dépendante Circuit/Emetteur
        amcCreatePage.getComboCircuit().select("01 - Droits hébergés BDD : AMC ==> BDD ==> TPG");
        amcCreatePage.getItemEmetteurDroits("Create").hasValue("AMC");
        amcCreatePage.getComboCircuit().select("02 - Droits hébergés BDD : AMC ==> BDD ==> ALMERYS");
        amcCreatePage.getItemEmetteurDroits("Create").hasValue("AMC");
        amcCreatePage.getComboCircuit().select("03 - Droits hébergés BDD : AMC ==> BDD");
        amcCreatePage.getItemEmetteurDroits("Create").hasValue("AMC");
        amcCreatePage.getComboCircuit().select("04 - Droits hébergés TPG : AMC ==> TPG ==> BDD");
        amcCreatePage.getItemEmetteurDroits("Create").hasValue("TPG");
        amcCreatePage.getComboOperateurPrincipal().select("IS - iSanté");
        amcCreatePage.getItemSiret("Create").fill("12502310500015");

        Date dateActivationService = new Date();

        // Activation des services
        for (int i = 0; i <= 8; i++) {
            ServiceEnum serviceEnum = ServiceEnum.getByOrdre(i);

            if (serviceEnum != null) {
                switch (serviceEnum) {
                case PRIORISATION:
                    amcCreatePage.getRadioActiveService(i).select("Oui").hasValue("Oui");
                    amcCreatePage.getItemPeriodeOuvertureService(i).fill(dateActivationService);
                    amcCreatePage.getItemPeriodeSynchroService(i).fill(dateActivationService);
                    amcCreatePage.getItemCouloirClientService(i).fill("TSTE2E");
                    break;
                case VISIODROIT:
                    amcCreatePage.getRadioActiveService(i).select("Oui").hasValue("Oui");
                    amcCreatePage.getItemPeriodeOuvertureService(i).fill(dateActivationService);
                    System.err.println("VISIODROIT - Genere fichier comptable ");
                    amcCreatePage.getRadioActiveFichierService(i).select("Oui").hasValue("Oui");
                    System.err.println("VISIODROIT - code comptable ");
                    amcCreatePage.getItemCodeComptableService(i).isEnabled();
                    amcCreatePage.getItemCodeComptableService(i).fill("65491");
                    break;
                case SEL_TP:
                    /*
                     * amcCreatePage.getRadioActiveService(i).select("Oui"). hasValue ("Oui");
                     * amcCreatePage.getItemPeriodeOuvertureService(i).fill( dateActivationService);
                     */
                    break;
                case DCLBEN:
                    amcCreatePage.getRadioActiveService(i).select("Oui").hasValue("Oui");
                    amcCreatePage.getItemPeriodeOuvertureService(i).fill(dateActivationService);
                    amcCreatePage.getItemPeriodeSynchroService(i).fill(dateActivationService);
                    amcCreatePage.getItemCouloirClientService(i).fill("TSTE2E");
                    amcCreatePage.getComboTypeConvention("DCLBEN").select("IS - iSanté");
                    amcCreatePage.getItemNumDebutFichierService(i).fill("3");
                    amcCreatePage.getItemNumEmetteurService(i).fill("3210364");
                    amcCreatePage.getItemVersionNormeService(i).fill("004");
                    amcCreatePage.getItemCodeClientService(i).fill("637");
                    break;
                case TPG_IS:
                    amcCreatePage.getRadioActiveService(i).select("Oui").hasValue("Oui");
                    amcCreatePage.getItemPeriodeOuvertureService(i).fill(dateActivationService);
                    amcCreatePage.getItemPeriodeSynchroService(i).fill(dateActivationService);
                    amcCreatePage.getItemCouloirClientService(i).fill("TSTE2E");
                    amcCreatePage.getComboTypeConvention("TPG-IS").select("IS - iSanté");
                    amcCreatePage.getItemNumDebutFichierService(i).fill("3");
                    amcCreatePage.getItemNumEmetteurService(i).fill("3210364");
                    amcCreatePage.getItemVersionNormeService(i).fill("004");
                    amcCreatePage.getItemCodeClientService(i).fill("637");
                    amcCreatePage.getRadioTousDomaineService(i).select("Oui").hasValue("Oui");
                    break;
                case TPG_SP:
                    /*
                     * amcCreatePage.getRadioActiveService(i).select("Oui"). hasValue ("Oui");
                     * amcCreatePage.getItemPeriodeOuvertureService(i).fill( dateActivationService);
                     * amcCreatePage.getItemPeriodeSynchroService(i).fill( dateActivationService);
                     * amcCreatePage.getItemCouloirClientService(i).fill( "TSTE2E");
                     * amcCreatePage.getComboTypeConvention("TPG-SP").
                     * select("SP - Santé-Pharma / SP santé");
                     * amcCreatePage.getItemNumDebutFichierService(i).fill("3");
                     * amcCreatePage.getItemNumEmetteurService(i).fill("3210364" );
                     * amcCreatePage.getItemVersionNormeService(i).fill("004");
                     * amcCreatePage.getItemCodeClientService(i).fill("637");
                     * amcCreatePage.getRadioTousDomaineService(i).select("Oui") . hasValue("Oui");
                     */
                    break;
                case ALMV3:
                    amcCreatePage.getRadioActiveService(i).select("Oui").hasValue("Oui");
                    amcCreatePage.getButtonAjoutRegroupement().click();
                    amcCreatePage.getItemRegroupementDetailleService(i).fill("951");
                    amcCreatePage.getItemRegroupementService(i).fill("753");
                    amcCreatePage.getItemPeriodeOuvertureService(i).fill(dateActivationService);
                    amcCreatePage.getItemPeriodeSynchroService(i).fill(dateActivationService);
                    amcCreatePage.getItemCouloirClientService(i).fill("TSTE2E");
                    amcCreatePage.getComboTypeConvention("ALMV3").select("AL - Almerys");
                    amcCreatePage.getItemNumDebutFichierService(i).fill("6");
                    amcCreatePage.getItemNumEmetteurService(i).fill("6655823");
                    amcCreatePage.getItemNumClientService(i).fill("654948");
                    amcCreatePage.getItemNomClientService(i).fill("Groupe AESIO");
                    amcCreatePage.getItemVersionNormeService(i).fill("006");
                    amcCreatePage.getItemTypeFichierService(i).fill("CSV");
                    amcCreatePage.getItemCodePerimetreService(i).fill("LOC");
                    amcCreatePage.getItemNomPerimetreService(i).fill("Local");
                    amcCreatePage.getItemTypeGestionnaireContratService(i).fill("B.O. PREMIUM");
                    amcCreatePage.getItemLibGestionnaireContratService(i).fill("Activ Premium");
                    amcCreatePage.getItemCodeClientService(i).fill("APR");
                    amcCreatePage.getRadioContratIndividuelService(i).select("Interne").hasValue("Interne");
                    amcCreatePage.getRadioContratCollectifService(i).select("Interne").hasValue("Interne");
                    break;
                case CARTE_TP:
                    /*
                     * amcCreatePage.getRadioActiveService(i).select("Oui"). hasValue ("Oui");
                     * amcCreatePage.getItemPeriodeOuvertureService(i).fill( dateActivationService);
                     * amcCreatePage.getItemPeriodeSynchroService(i).fill( dateActivationService);
                     * amcCreatePage.getItemCouloirClientService(i).fill( "TSTE2E");
                     * amcCreatePage.getItemDebutPeriodeReferenceService(i). fill(
                     * dateActivationService);
                     * amcCreatePage.getItemFinPeriodeReferenceService(i).fill(
                     * dateActivationService); amcCreatePage.getItemNumEmetteurService(i).fill(
                     * "852369741"); amcCreatePage.getItemCodeClientService(i).fill("MOILIER") ;
                     */
                    break;
                case CARTE_DEMATERIALISEE:
                    /*
                     * On laisse ce service désactivé pour l'activer en MAJ
                     * amcCreatePage.getRadioActiveService(i).select("Oui"). hasValue ("Oui");
                     * amcCreatePage.getItemPeriodeOuvertureService(i).fill( dateActivationService);
                     * amcCreatePage.getItemPeriodeSynchroService(i).fill( dateActivationService);
                     * amcCreatePage.getItemCouloirClientService(i).fill( "TSTE2E");
                     */
                    break;
                }
            }
        }

        // Validation de l'AMC
        amcCreatePage.getButtonSubmit().click();
    }

    public void scenarioVisuNewAmc() {
        AmcConsultPage amcConsultPage = new AmcConsultPage(getWebDriver(), getScenarioReport());

        // Bascule sur la page de consultation
        amcConsultPage.getPageTitle().hasContent("Consultation d'une AMC");
        amcConsultPage.getContenu("Numéro AMC").hasContent("0775627391");
        amcConsultPage.getContenu("Libellé court").hasContent("APREVA");
        amcConsultPage.getContenu("Libellé long").hasContent("APREVA MUTUELLE");
        amcConsultPage.getContenu("Code partenaire").hasContent("AES");
        amcConsultPage.getContenu("Code circuit").hasContent("04 - Droits hébergés TPG : AMC ==> TPG ==> BDD");
        amcConsultPage.getContenu("Emetteur des droits").hasContent("TPG");
        amcConsultPage.getContenu("Opérateur principal").hasContent("IS - iSanté");
        amcConsultPage.getContenu("SIRET").hasContent("12502310500015");

        // Test des services
        amcConsultPage.getStatutService("PRIORISATION").hasContent("Ouvert");
        amcConsultPage.getStatutService("VISIODROIT").hasContent("Ouvert");
        amcConsultPage.getStatutService("SEL-TP").hasContent("Fermé");
        amcConsultPage.getStatutService("DCLBEN").hasContent("Ouvert");
        amcConsultPage.getStatutService("TPG-IS").hasContent("Ouvert");
        amcConsultPage.getStatutService("TPG-SP").hasContent("Fermé");
        amcConsultPage.getStatutService("ALMV3").hasContent("Ouvert");
        amcConsultPage.getStatutService("CARTE-TP").hasContent("Fermé");
        amcConsultPage.getStatutService("CARTE-DEMATERIALISEE").hasContent("Fermé");
    }

    public void scenarioUpdateNewAmc() {
        AmcConsultPage amcConsultPage = new AmcConsultPage(getWebDriver(), getScenarioReport());
        AmcSearchPage amcSearchPage = new AmcSearchPage(getWebDriver(), getScenarioReport());
        Date dateActivationService = new Date();

        amcConsultPage.getButtonUpdate().click();
        amcConsultPage.getLibLongAMC().fill("APREVA BY AESIO");
        amcConsultPage.getPanelService("CARTE-DEMATERIALISEE").click();
        amcConsultPage.getRadioActiveService(8).select("Oui").hasValue("Oui");
        amcConsultPage.getItemPeriodeOuvertureService(8).fill(dateActivationService);
        amcConsultPage.getItemPeriodeSynchroService(8).fill(dateActivationService);
        amcConsultPage.getItemCouloirClientService(8).fill("AES");
        // Désactivation du service TPG-SP
        amcConsultPage.getPanelService("TPG-SP").click();
        amcConsultPage.getRadioDesactiveService(5).select("Non").hasValue("Non");
        amcConsultPage.getButtonValidate().click();
        // Retour à la page d'acceuil
        amcConsultPage.getUrlRetour().click();
        amcSearchPage.getPageTitle().hasContent("Recherche AMC");

    }

    public void scenarioDuplicateAmc() {

        AmcConsultPage amcConsultPage = new AmcConsultPage(getWebDriver(), getScenarioReport());
        AmcSearchPage amcSearchPage = new AmcSearchPage(getWebDriver(), getScenarioReport());
        AmcCreatePage amcCreatePage = new AmcCreatePage(getWebDriver(), getScenarioReport());

        Date dateActivationService = new Date();

        goTo(initialUrl);

        // Recherche de l'AMC APREVA par sson id
        amcSearchPage.getComboSearchNumeroAMC().select("0775627391");
        amcSearchPage.getButtonSearch().click();

        amcSearchPage.getButtonOpenAMC("0775627391").click();

        amcConsultPage.getButtonDuplicate().click();

        amcCreatePage.getItemNumeroAMC("Edit").fill("0775627392");
        amcCreatePage.getItemLibCourt("Edit").fill("AEC");
        amcCreatePage.getItemLibLong("Edit").fill("Arc En Ciel");
        amcCreatePage.getItemSiret("Edit").fill("12502310500024");

        amcCreatePage.getRadioActiveService(2).select("Oui").hasValue("Oui");
        amcCreatePage.getItemPeriodeOuvertureService(2).fill(dateActivationService);

        // Validation de l'AMC
        amcCreatePage.getButtonSubmit().click();

        // Le nouvelle AMC a le bon n°
        amcConsultPage.getContenu("Numéro AMC").hasContent("0775627392");

    }
}
