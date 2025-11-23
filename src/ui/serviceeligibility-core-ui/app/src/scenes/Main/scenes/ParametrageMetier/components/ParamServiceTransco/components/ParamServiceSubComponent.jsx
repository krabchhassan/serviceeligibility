/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import ReactTable from 'react-table';
import 'react-table/react-table.css';
import { Panel, PanelSection, PanelHeader, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import ContextSubComponent from './ParamContextSubComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* eslint-disable camelcase */
const beneficiaireCodes = {
    beneficiaire_affiliation_qualite: { col: 'TDB13', label: 'Qualité bénéficiaire' },
    beneficiaire_affiliation_regimeOD1: { col: 'TDB15', label: "Code organisme de l'ouvrant droit 1" },
    beneficiaire_nirBeneficiaire: { col: 'TDC1B05', label: 'NIR bénéficiaire' },
    beneficiaire_affiliation_regimeParticulier: { col: 'TDC1B16', label: 'Régime particulier' },
    beneficiaire_affiliation_isBeneficiaireACS: { col: 'TDC1B17', label: " Bénéficiaire de l'ACS" },
    beneficiaire_affiliation_periodeDebut: { col: 'TDC1B19', label: "Début d'affiliation RO" },
    beneficiaire_affiliation_typeAssure: { col: 'TDC1B21', label: "Type d'assuré" },
    beneficiaire_affiliation_isTeleTransmission: { col: 'TDC1B22', label: 'Télétransmission' },
    beneficiaire_affiliation_hasMedecinTraitant: { col: 'TDC1B23', label: 'Médecin traitant' },
    beneficiaire_refExternePersonne: { col: 'TDC2B10', label: 'Référence externe personne' },
    beneficiaire_adresses_typeAdresse: { col: 'TDC3B05', label: "Type d'adresse" },
    beneficiaire_adresses_numero: { col: 'TDC3B06', label: 'Numéro de la voie' },
    beneficiaire_adresses_ligne: { col: 'TDC3B07', label: "Complément d'adresse" },
    beneficiaire_adresses_codePostal: { col: 'TDC3B08', label: 'Code postal' },
};
const contratCodes = {
    isCarteTPaEditer: { col: 'TDC4B05', label: 'Edition de la carte TP' },
    contrat_numero: { col: 'TDB06', label: 'Numéro du contrat' },
    contrat_critereSecondaireDetaille: { col: 'TDC1B07', label: 'Critère secondaire détaillé' },
    contrat_critereSecondaire: { col: 'TDC1B08', label: 'Critère secondaire' },
    contrat_numeroCarte: { col: '', label: '' },
    contrat_individuelOuCollectif: { col: 'TDC1B11', label: 'Type d’adhésion' },
    contrat_numeroContratCollectif: { col: 'TDC1B12', label: 'N° de contrat collectif' },
    contrat_dateSouscription: { col: 'TDC1B14', label: 'Date souscription' },
    contrat_isContratResponsable: { col: 'TDC1B15', label: 'Contrat responsable' },
    contrat_isContratCMU: { col: 'TDC1B18', label: 'Bénéficiaire de la CMU' },
    contrat_rangAdministratif: { col: 'TDC1B20', label: 'Rang administratif du bénéficiaire dans le contrat' },
    contrat_modePaiementPrestations: { col: 'TDC1B24', label: 'Mode de paiement prestations' },
    contrat_numeroIndividuel: { col: '', label: '' },
    contrat_numeroIndividuelExterne: { col: '', label: '' },
    contrat_numeroContratCollectifExterne: { col: 'TDC1B13', label: 'N° externe du contrat collectif' },
    contrat_nomPorteur: { col: 'TDC2B05', label: 'Nom du souscripteur' },
    contrat_prenomPorteur: { col: 'TDC2B06', label: 'Prénom du souscripteur' },
    contrat_civilitePorteur: { col: 'TDC2B07', label: 'Civilité du souscripteur' },
    contrat_destinataire: { col: 'TDC4B07', label: 'Destinataire communication' },
    contrat_gestionnaire: { col: 'TDC2B08', label: 'Gestionnaire du contrat' },
    contrat_groupeAssures: { col: 'TDC2B09', label: 'Groupe d’assurés souscripteur du contrat' },
    contrat_editeurCarte: { col: 'TDC4B06', label: 'Editeur de la carte' },
    contrat_numAMCDetaille: { col: '', label: '' },
    contrat_numOperateur: { col: 'TDC1B26', label: "Numéro de l'opérateur" },
    contrat_fondCarte: { col: 'TDC4B08', label: 'Fond de page carte TP' },
    contrat_annexe1: { col: 'TDC4B09', label: 'Annexe 1 de la carte TP' },
    contrat_annexe2: { col: 'TDC4B10', label: 'Annexe 2 de la carte TP' },
};
const domaineDroitsCodes = {
    domaineDroits_periodeDroit_periodeFin: { col: 'TDD06', label: 'Date de fin de droits ' },
    domaineDroits_codeProduit: { col: 'TDCD05', label: 'Code produit' },
    domaineDroits_libelleProduit: { col: 'TDCD06', label: 'Libellé code produit' },
    domaineDroits_codeOptionMutualiste: { col: 'TDCD07', label: 'Option mutualiste' },
    domaineDroits_libelleOptionMutualiste: { col: 'TDCD08', label: 'Libellé option mutualiste' },
    domaineDroits_codeGarantie: { col: 'TDCD09', label: 'Code garantie' },
    domaineDroits_libelleGarantie: { col: 'TDCD10', label: 'Libellé garantie' },
    domaineDroits_prioriteDroit_code: { col: 'TDCD11', label: 'Priorité droit' },
    domaineDroits_prioriteDroit_typeDroit: { col: 'TDCD12', label: 'Type de droit' },
    domaineDroits_periodeDroit_modeObtention: { col: 'TDCD13', label: 'Code mouvement' },
    domaineDroits_periodeDroit_motifEvenement: { col: 'TDCD14', label: 'Motif évènement' },
    domaineDroits_periodeDroit_libelleEvenement: { col: 'TDCD15', label: 'Libellé évènement' },
    domaineDroits_isEditable: { col: 'TDCD16', label: 'Domaine éditable' },
    domaineDroits_isSuspension: { col: 'TDCD17', label: 'Indicateur suspension' },
    domaineDroits_dateAdhesionCouverture: { col: 'TDCD18', label: "Date d'adhésion à la couverture" },
    domaineDroits_origineDroits: { col: 'TDCD19', label: 'Origine des déclarations de droits' },
    domaineDroits_codeProfil: { col: 'TDCD06', label: 'Code profil ' },
    domaineDroits_libelleCodeRenvoi: { col: 'TDCD08', label: 'Libellé renvoi' },
    domaineDroits_conventionnements_typeConventionnement_code: {
        col: 'TDCD09',
        label: 'Convention couvrant le domaine de prestations',
    },
    domaineDroits_conventionnements_priorite: { col: 'TDCD10', label: 'Priorité de la convention' },
    domaineDroits_codeExterneProduit: { col: 'TDCD11', label: 'Référence externe produit' },
    domaineDroits_referenceCouverture: { col: 'TDCD13', label: 'Référence couverture' },
    prestation_code_controle: { col: 'TDG05', label: 'Code Prestation' },
};

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class ParamServiceSubComponent extends Component {
    render() {
        const { t, data: parentData } = this.props;
        const { beneficiaire, contrat, domaineDroits } = parentData.controleContextuel || {};
        const beneficiaireList = Object.keys(beneficiaire || {}).map((key) => ({
            name: key,
            active: beneficiaire[key],
        }));
        const contratList = Object.keys(contrat || {}).map((key) => ({ name: key, active: contrat[key] }));
        const domaineDroitsList = Object.keys(domaineDroits || {}).map((key) => ({
            name: key,
            active: domaineDroits[key],
        }));
        const rowsData = [
            {
                type: t('parameters.champsBenef'),
                data: beneficiaireList,
                codes: beneficiaireCodes,
            },
            {
                type: t('parameters.champsContract'),
                data: contratList,
                codes: contratCodes,
            },
            {
                type: t('parameters.champsDomaine'),
                data: domaineDroitsList,
                codes: domaineDroitsCodes,
            },
        ];
        const columns = [
            {
                Header: t('parameters.controlType'),
                accessor: 'type',
            },
        ];

        return (
            <Panel header={<PanelHeader title={t('parameters.controlContextuel')} />}>
                <PanelSection>
                    <ReactTable
                        data={rowsData}
                        columns={columns}
                        className="-striped -highlight"
                        showPageSizeOptions={false}
                        showPagination={false}
                        defaultPageSize={3}
                        previousText={t('table.pagination.previous')}
                        loadingText={t('table.loading')}
                        rowsText={t('table.pagination.rows')}
                        noDataText={t('table.noData')}
                        SubComponent={(data) => <ContextSubComponent data={data.original} />}
                    />
                </PanelSection>
            </Panel>
        );
    }
}

ParamServiceSubComponent.propTypes = {
    t: PropTypes.func,
    data: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParamServiceSubComponent;
