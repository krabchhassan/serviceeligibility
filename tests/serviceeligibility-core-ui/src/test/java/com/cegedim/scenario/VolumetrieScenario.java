package com.cegedim.scenario;

import java.util.ArrayList;

import com.cegedim.next.e2e.core.annotation.E2EScenario;
import com.cegedim.next.e2e.core.scenario.Scenario;
import com.cegedim.objects.Volumetrie;
import com.cegedim.pages.VolumetriePage;
import com.cegedim.utils.AuthUtils;
import com.cegedim.utils.ImportUtils;

@E2EScenario(name = "Volumétrie")
public class VolumetrieScenario extends Scenario {

    @Override
    public void doScenario() {
        ImportUtils.doVolumetrieFluxDeclarationImport();

        AuthUtils.login(this);

        goTo(initialUrl + "/#/volumetry");

        enterScenarioSection("Recherche de volumétrie");
        /*
         * // Test du titre de la page enterScenarioItem("Titre de la page");
         * scenarioTitrePage();
         */

        // Test des champs de recherche
        enterScenarioItem("Critère de recherche");
        scenarioSearch();

        // Test du resultat de recherche
        enterScenarioItem("Affichage détail");
        scenarioAffichageDetail();
        /*
         * // Test du tri enterScenarioItem("Tri"); scenarioTri();
         */
    }

    // Test du titre de la page
    public void scenarioTitrePage() {
        VolumetriePage volumetriePage = new VolumetriePage(getWebDriver(), getScenarioReport());
        volumetriePage.getPageTitle().hasContent("Volumétrie");
    }

    // Test de la recherche
    public void scenarioSearch() {
        VolumetriePage volumetriePage = new VolumetriePage(getWebDriver(), getScenarioReport());
        volumetriePage.getSearchAMC().fill("MACIF");
        volumetriePage.getSearchPartenaire().isNotEnabled();
        volumetriePage.getButtonRechercher().click();
        volumetriePage.getContenuCelluleTotal(3).hasContent("2219777");
        volumetriePage.getContenuCelluleTotal(4).hasContent("967670");
        volumetriePage.getContenuCelluleTotal(5).hasContent("810939");
        volumetriePage.getContenuCelluleTotal(6).hasContent("156731");
        volumetriePage.getSearchAMC().fill("");
        volumetriePage.getSearchPartenaire().fill("CLR");
        volumetriePage.getSearchAMC().isNotEnabled();
        volumetriePage.getButtonRechercher().click();
        volumetriePage.getContenuCelluleTotal(3).hasContent("512847");
        volumetriePage.getContenuCelluleTotal(4).hasContent("221786");
        volumetriePage.getContenuCelluleTotal(5).hasContent("178201");
        volumetriePage.getContenuCelluleTotal(6).hasContent("43585");
    }

    // Initialisation des données à tester
    private ArrayList<Volumetrie> initDataTest() {
        ArrayList<Volumetrie> volumetries = new ArrayList<>();
        volumetries.add(new Volumetrie(1, "BPC", "0009490004 - MACIF8", "487487", "208624", "163169", "45455"));
        // volumetries.add(new Volumetrie(2, "CLR", "0038912994 - MACIF3",
        // "30551", "16654", "14481", "2173"));
        // volumetries.add(new Volumetrie(3, "CLR", "0044317250 - MACIF5",
        // "92829", "26350", "9476", "16874"));
        volumetries.add(new Volumetrie(16, "MPR", "0002420016 - RCBF", "30914", "25390", "24120", "1270"));
        return volumetries;
    }

    // Test du résultat de la recherche
    public void scenarioAffichageDetail() {
        VolumetriePage volumetriePage = new VolumetriePage(getWebDriver(), getScenarioReport());
        volumetriePage.getSearchPartenaire().fill("");
        volumetriePage.getButtonRechercher().click();

        ArrayList<Volumetrie> volumetries = initDataTest();
        for (Volumetrie vol : volumetries) {
            volumetriePage.getContenuCelluleResultat(vol.getIndiceLigne(), 1).hasContent(vol.getCodePartenaire());
            volumetriePage.getContenuCelluleResultat(vol.getIndiceLigne(), 2).hasContent(vol.getAmc());
            volumetriePage.getContenuCelluleResultat(vol.getIndiceLigne(), 3).hasContent(vol.getNbDeclaration());
            volumetriePage.getContenuCelluleResultat(vol.getIndiceLigne(), 4).hasContent(vol.getNbPersonne());
            volumetriePage.getContenuCelluleResultat(vol.getIndiceLigne(), 5).hasContent(vol.getNbPersDroitOuvert());
            volumetriePage.getContenuCelluleResultat(vol.getIndiceLigne(), 6).hasContent(vol.getNbPersDroitFerme());
        }

    }

    // Test du tri par colonne
    public void scenarioTri() {
        goTo(initialUrl + "/#/volumetry");
        VolumetriePage volumetriePage = new VolumetriePage(getWebDriver(), getScenarioReport());
        // Tri par AMC ascendant
        volumetriePage.getButtonTri("AMC").click();
        volumetriePage.getContenuCelluleResultat(1, 2).hasContent("0000401141 - FILHET-ALLARD");
        volumetriePage.getContenuCelluleResultat(16, 2).hasContent("0079001962 - MACIF2");
        // Tri par AMC descendant
        volumetriePage.getButtonTri("AMC").click();
        volumetriePage.getContenuCelluleResultat(1, 2).hasContent("0079001962 - MACIF2");
        volumetriePage.getContenuCelluleResultat(16, 2).hasContent("0000401141 - FILHET-ALLARD");
        // Tri par Nombre de déclarations ascendant
        volumetriePage.getButtonTri("Nombre de déclarations").click();
        volumetriePage.getContenuCelluleResultat(1, 3).hasContent("174");
        volumetriePage.getContenuCelluleResultat(16, 3).hasContent("994588");
        // Tri par Nombre de déclarations descendant
        volumetriePage.getButtonTri("Nombre de déclarations").click();
        volumetriePage.getContenuCelluleResultat(1, 3).hasContent("994588");
        volumetriePage.getContenuCelluleResultat(16, 3).hasContent("174");
        // Tri par Nombre de personnes ascendant
        volumetriePage.getButtonTri("Nombre de personnes").click();
        volumetriePage.getContenuCelluleResultat(1, 4).hasContent("152");
        volumetriePage.getContenuCelluleResultat(16, 4).hasContent("491941");
        // Tri par Nombre de personnes descendant
        volumetriePage.getButtonTri("Nombre de personnes").click();
        volumetriePage.getContenuCelluleResultat(1, 4).hasContent("491941");
        volumetriePage.getContenuCelluleResultat(16, 4).hasContent("152");
        // Tri par Nombre de personnes droits ouverts ascendant
        volumetriePage.getButtonTri("Nombre de personnes droits ouverts").click();
        volumetriePage.getContenuCelluleResultat(1, 5).hasContent("151");
        volumetriePage.getContenuCelluleResultat(16, 5).hasContent("428832");
        // Tri par Nombre de personnes droits ouverts descendant
        volumetriePage.getButtonTri("Nombre de personnes droits ouverts").click();
        volumetriePage.getContenuCelluleResultat(1, 5).hasContent("428832");
        volumetriePage.getContenuCelluleResultat(16, 5).hasContent("151");
        // Tri par Nombre de personnes droits fermés ascendant
        volumetriePage.getButtonTri("Nombre de personnes droits fermés").click();
        volumetriePage.getContenuCelluleResultat(1, 6).hasContent("1");
        volumetriePage.getContenuCelluleResultat(16, 6).hasContent("63109");
        // Tri par Nombre de personnes droits fermés descendant
        volumetriePage.getButtonTri("Nombre de personnes droits fermés").click();
        volumetriePage.getContenuCelluleResultat(1, 6).hasContent("63109");
        volumetriePage.getContenuCelluleResultat(16, 6).hasContent("1");
        // Tri par Partenaire ascendant
        volumetriePage.getButtonTri("Partenaire").click();
        volumetriePage.getContenuCelluleResultat(1, 1).hasContent("BPC");
        volumetriePage.getContenuCelluleResultat(16, 1).hasContent("MPR");
        // Tri par Partenaire descendant
        volumetriePage.getButtonTri("Partenaire").click();
        volumetriePage.getContenuCelluleResultat(1, 1).hasContent("MPR");
        volumetriePage.getContenuCelluleResultat(16, 1).hasContent("BPC");

    }
}
