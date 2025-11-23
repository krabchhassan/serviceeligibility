/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    PanelHeader,
    ControlledPanel,
    Modal,
    ModalHeader,
    ModalBody,
    PanelSection,
    Row,
    Col,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import utils from '../../../../../common/utils/businessUtils';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import Communication from './components/Communication';
import CommonSpinner from '../../../../../common/components/CommonSpinner/CommonSpinner';
import style from '../style.module.scss';
import PrestationServiceUtils from '../../BeneficiaryDetails/components/PrestationServiceUtils';
import HistoriqueCodePeriodeContract from './components/HistoriqueCodePeriodeContract';
import HistoriquePeriodeContract from './components/HistoriquePeriodeContract';
import HistoriqueAffiliationROContract from './components/HistoriqueAffiliationRoContract';
import HistoriqueTeletransmissionContract from './components/HistoriqueTeletransmissionContract';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapLink = (communication) => {
    const googleMap = 'https://www.google.com/maps?q=';
    let mapParams = '';

    if (communication.lignesAdresse[3]) {
        mapParams += `${communication.lignesAdresse[3]} `;
    }

    if (communication.codePostal) {
        mapParams += communication.codePostal;
    }

    return googleMap + mapParams;
};

const getLibConvention = (listeConventions, code) => {
    const trouve = listeConventions.find((convention) => convention.code === code);
    return trouve ? trouve.libelle : '';
};

const handleFormatNumber = (phoneNumber) => {
    const phoneWithoutEmptySpaces = (phoneNumber || '').replace(/\s/g, '');
    return (phoneWithoutEmptySpaces || '').replace(/(\d{2})(\d{2})(\d{2})(\d{2})(\d{2})/, '$1 $2 $3 $4 $5');
};

// method recover from the angular.js original app
const communicationControl = (contrat, conventions, t) => {
    const returnedContract = { ...contrat };
    returnedContract.libConvention = getLibConvention(conventions, contrat.typeConvention);
    returnedContract.communicationAD = {};
    returnedContract.communicationEN = {};
    returnedContract.communicationGE = {};

    contrat.communications.forEach((item) => {
        const communication = {
            lignesAdresse: item.lignesAdresse.slice(),
            telephone: handleFormatNumber(item.telephone),
            email: item.email,
        };

        switch (item.type) {
            case 'AD': {
                communication.mapLink = mapLink(item);
                returnedContract.communicationAD = communication;
                break;
            }
            case 'EN': {
                returnedContract.communicationEN = communication;
                break;
            }
            case 'GE': {
                returnedContract.communicationGE = communication;
                break;
            }
            default:
                break;
        }
    });
    const communicationLabel = 'beneficiaryRightsDetails.communication';
    returnedContract.communicationAD.titre = `${t(communicationLabel)} ${t('beneficiaryRightsDetails.typeComAD')}`;
    returnedContract.communicationEN.titre = `${t(communicationLabel)} ${t('beneficiaryRightsDetails.typeComEN')}`;
    returnedContract.communicationGE.titre = `${t(communicationLabel)} ${t('beneficiaryRightsDetails.typeComGE')}`;

    return returnedContract;
};

function formatNir(nir, cle, regime, caisse, centre) {
    if (!nir || !cle) return null;
    const base = `${utils.formatRO(nir)} / ${cle}`;

    if (!regime) return base;

    const details = [regime, caisse, centre].filter(Boolean).join(' - ');
    return `${base} (${details})`;
}

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class Beneficiaire extends Component {
    constructor(props) {
        super(props);
        this.state = {
            contrat: { numero: null },
            isOpen: !!props.open,
            modalRegimesParticuliers: false,
            historiqueRegimesParticuliers: null,
            modalPeriodesMedecinTraitant: false,
            historiquePeriodesMedecinTraitant: null,
            modalSituationsParticulieres: false,
            historiqueSituationsParticulieres: null,
            modalAffiliationsRO: false,
            historiqueAffiliationsRO: null,
            modalTeletransmissions: false,
            historiqueTeletransmissions: null,
        };
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (
            nextProps &&
            prevState &&
            nextProps.t &&
            nextProps.contrat &&
            prevState.contrat &&
            nextProps.contrat.numero !== prevState.contrat.numero
        ) {
            return { contrat: communicationControl(nextProps.contrat, nextProps.conventions, nextProps.t) };
        }
        return null;
    }

    toggleRegimesParticuliers(historiqueRegimesParticuliers) {
        const { modalRegimesParticuliers } = this.state;
        this.setState({ modalRegimesParticuliers: !modalRegimesParticuliers, historiqueRegimesParticuliers });
    }

    togglePeriodesMedecinTraitant(historiquePeriodesMedecinTraitant) {
        const { modalPeriodesMedecinTraitant } = this.state;
        this.setState({
            modalPeriodesMedecinTraitant: !modalPeriodesMedecinTraitant,
            historiquePeriodesMedecinTraitant,
        });
    }

    toggleSituationsParticulieres(historiqueSituationsParticulieres) {
        const { modalSituationsParticulieres } = this.state;
        this.setState({
            modalSituationsParticulieres: !modalSituationsParticulieres,
            historiqueSituationsParticulieres,
        });
    }

    toggleAffiliationsRO(historiqueAffiliationsRO) {
        const { modalAffiliationsRO } = this.state;
        this.setState({ modalAffiliationsRO: !modalAffiliationsRO, historiqueAffiliationsRO });
    }

    toggleTeletransmissions(historiqueTeletransmissions) {
        const { modalTeletransmissions } = this.state;
        this.setState({ modalTeletransmissions: !modalTeletransmissions, historiqueTeletransmissions });
    }

    renderModalRegimesParticuliers(periodesRegimesParticuliers) {
        if (periodesRegimesParticuliers && periodesRegimesParticuliers.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() =>
                this.toggleRegimesParticuliers(periodesRegimesParticuliers),
            );
        }
        return null;
    }

    renderModalPeriodesMedecinTraitant(periodesPeriodesMedecinTraitant) {
        if (periodesPeriodesMedecinTraitant && periodesPeriodesMedecinTraitant.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() =>
                this.togglePeriodesMedecinTraitant(periodesPeriodesMedecinTraitant),
            );
        }
        return null;
    }

    renderModalSituationsParticulieres(periodesSituationsParticulieres) {
        if (periodesSituationsParticulieres && periodesSituationsParticulieres.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() =>
                this.toggleSituationsParticulieres(periodesSituationsParticulieres),
            );
        }
        return null;
    }

    renderModalAffiliationsRO(periodesAffiliationsRO) {
        if (periodesAffiliationsRO && periodesAffiliationsRO.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() => this.toggleAffiliationsRO(periodesAffiliationsRO));
        }
        return null;
    }

    renderModalTeletransmissions(periodesTeletransmissions) {
        if (periodesTeletransmissions && periodesTeletransmissions.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() =>
                this.toggleTeletransmissions(periodesTeletransmissions),
            );
        }
        return null;
    }

    @autobind
    renderFirstColonne() {
        const { t, identification, domaines, debutDroit, finDroit } = this.props;
        const { affiliationsRO, situationsParticulieres, regimesParticuliers } = identification;
        const firstDomain = domaines[0] || null;
        const garantieBase = firstDomain === null ? '-' : firstDomain.garantieLibelle;
        const produit = firstDomain === null ? '-' : firstDomain.couverture.produit;

        return (
            <Col xs={4} className={style['first-col']}>
                <LabelValuePresenter
                    id="typeAssure"
                    label={t(`consolidatedContract.affiliationRO`)}
                    value={
                        <div className="d-flex justify-item-center align-items-center">
                            {utils.getActivePeriodFromData(affiliationsRO, debutDroit, finDroit, t)}
                            {this.renderModalAffiliationsRO(affiliationsRO)}
                        </div>
                    }
                />
                <LabelValuePresenter
                    id="garantiesBase"
                    label={t('beneficiaryRightsDetails.garantiesBase')}
                    value={garantieBase}
                />
                <LabelValuePresenter
                    id="product"
                    label={t('beneficiaryRightsDetails.coverRights.product')}
                    value={produit}
                />
                <LabelValuePresenter
                    id="situationParticuliere"
                    label={t(`consolidatedContract.situationParticuliere`)}
                    value={
                        <div className="d-flex justify-item-center align-items-center">
                            {utils.getActivePeriodFromData(situationsParticulieres, debutDroit, finDroit, t)}
                            {this.renderModalSituationsParticulieres(situationsParticulieres)}
                        </div>
                    }
                />
                <LabelValuePresenter
                    id="regimeParticulier"
                    label={t('consolidatedContract.regimeParticulier')}
                    value={
                        <div className="d-flex justify-item-center align-items-center">
                            {utils.getActivePeriodFromData(regimesParticuliers, debutDroit, finDroit, t)}
                            {this.renderModalRegimesParticuliers(regimesParticuliers)}
                        </div>
                    }
                />
            </Col>
        );
    }

    @autobind
    renderSecondColonne() {
        const { t, identification } = this.props;
        const { contrat } = this.state;
        const { affiliation, dateNaissance, rangNaissance } = identification;
        const {
            nirBeneficiaire,
            cleNirBeneficiaire,
            nirOd1,
            cleNirOd1,
            nirOd2,
            cleNirOd2,
            regimeOD1,
            caisseOD1,
            centreOD1,
            regimeOD2,
            caisseOD2,
            centreOD2,
        } = affiliation;

        return (
            <Col xs={5} className={style['second-col']}>
                <LabelValuePresenter
                    id="nirBeneficiare"
                    label={t(`consolidatedContract.nirBeneficiaire`)}
                    value={formatNir(nirBeneficiaire, cleNirBeneficiaire)}
                />
                <LabelValuePresenter
                    id="nirOd1"
                    label={t(`consolidatedContract.nirOd1`)}
                    value={formatNir(nirOd1, cleNirOd1, regimeOD1, caisseOD1, centreOD1)}
                />
                <LabelValuePresenter
                    id="nirOd2"
                    label={t(`consolidatedContract.nirOd2`)}
                    value={formatNir(nirOd2, cleNirOd2, regimeOD2, caisseOD2, centreOD2)}
                />
                <LabelValuePresenter
                    id="infosNaissance"
                    label={t(`consolidatedContract.infosNaissance`)}
                    value={`${dateNaissance} - ${rangNaissance}`}
                />
                <LabelValuePresenter
                    id="rangAdministratif"
                    label={t(`consolidatedContract.rangAdministratif`)}
                    value={contrat.rangAdministratif}
                />
            </Col>
        );
    }

    @autobind
    renderThirdColonne() {
        const { t, identification, debutDroit, finDroit } = this.props;
        const { contrat } = this.state;
        const { periodesMedecinTraitant, teletransmissions } = identification;

        return (
            <Col xs={4} className={style['third-col']}>
                <LabelValuePresenter
                    id="contract-acs"
                    label={t(`beneficiaryRightsDetails.beneficiaireACS`)}
                    value={t(contrat.isBeneficiaireACS ? 'yes' : 'no')}
                />
                <LabelValuePresenter
                    id="contract-payment-method"
                    label={t(`beneficiaryRightsDetails.modePaiementPrestation`)}
                    value={contrat.modePaiementPrestations}
                />
                <LabelValuePresenter
                    id="contract-remote-transmission"
                    label={t(`consolidatedContract.teletransmission`)}
                    value={
                        <div className="d-flex justify-item-center align-items-center">
                            {utils.getActivePeriodFromData(teletransmissions, debutDroit, finDroit, t)}
                            {this.renderModalTeletransmissions(teletransmissions)}
                        </div>
                    }
                />
                <LabelValuePresenter
                    id="medecinTraitant"
                    label={t(`consolidatedContract.medecinTraitant`)}
                    value={
                        <div className="d-flex justify-item-center align-items-center">
                            {utils.getActivePeriod(periodesMedecinTraitant, debutDroit, finDroit, t)}
                            {this.renderModalPeriodesMedecinTraitant(periodesMedecinTraitant)}
                        </div>
                    }
                />
            </Col>
        );
    }

    render() {
        const { t, conventions, identification } = this.props;
        const {
            contrat,
            isOpen,
            modalRegimesParticuliers,
            historiqueRegimesParticuliers,
            modalPeriodesMedecinTraitant,
            historiquePeriodesMedecinTraitant,
            modalSituationsParticulieres,
            historiqueSituationsParticulieres,
            modalAffiliationsRO,
            historiqueAffiliationsRO,
            modalTeletransmissions,
            historiqueTeletransmissions,
        } = this.state;
        const canRender = contrat && conventions;
        if (!canRender) {
            return <CommonSpinner />;
        }
        const pageTitle = t('beneficiaryRightsDetails.beneficiaire');
        const panelHeader = <PanelHeader title={`${pageTitle}${t('colon')}${identification.assure.numeroPersonne}`} />;
        const fullSizePanel = 'full-size-panel';

        return (
            <ControlledPanel
                id="contract-panel"
                header={panelHeader}
                label={pageTitle}
                separators={false}
                initialExpanded={isOpen}
            >
                <PanelSection>
                    <Row>
                        {this.renderFirstColonne()}
                        {this.renderSecondColonne()}
                        {this.renderThirdColonne()}
                    </Row>
                </PanelSection>
                <PanelSection>
                    <Row>
                        <Col className={style[fullSizePanel]}>
                            <Communication communication={contrat.communicationAD} />
                        </Col>
                        <Col className={style[fullSizePanel]}>
                            <Communication communication={contrat.communicationEN} />
                        </Col>
                        <Col className={style[fullSizePanel]}>
                            <Communication communication={contrat.communicationGE} />
                        </Col>
                    </Row>
                </PanelSection>
                {historiqueRegimesParticuliers && (
                    <Modal
                        size="md"
                        isOpen={modalRegimesParticuliers}
                        toggle={() => this.toggleRegimesParticuliers()}
                        backdrop="static"
                        className={style['modal-in-modal']}
                    >
                        <ModalHeader toggle={() => this.toggleRegimesParticuliers()}>
                            {t('historiqueRegime.header')}
                        </ModalHeader>
                        <ModalBody>
                            <HistoriqueCodePeriodeContract historique={historiqueRegimesParticuliers} />
                        </ModalBody>
                    </Modal>
                )}
                {historiquePeriodesMedecinTraitant && (
                    <Modal
                        size="md"
                        isOpen={modalPeriodesMedecinTraitant}
                        toggle={() => this.togglePeriodesMedecinTraitant()}
                        backdrop="static"
                        className={style['modal-in-modal']}
                    >
                        <ModalHeader toggle={() => this.togglePeriodesMedecinTraitant()}>
                            {t('periodesMedecinTraitant.header')}
                        </ModalHeader>
                        <ModalBody>
                            <HistoriquePeriodeContract historique={historiquePeriodesMedecinTraitant} />
                        </ModalBody>
                    </Modal>
                )}
                {historiqueSituationsParticulieres && (
                    <Modal
                        size="md"
                        isOpen={modalSituationsParticulieres}
                        toggle={() => this.toggleSituationsParticulieres()}
                        backdrop="static"
                        className={style['modal-in-modal']}
                    >
                        <ModalHeader toggle={() => this.toggleSituationsParticulieres()}>
                            {t('situationsParticulieres.header')}
                        </ModalHeader>
                        <ModalBody>
                            <HistoriqueCodePeriodeContract historique={historiqueSituationsParticulieres} />
                        </ModalBody>
                    </Modal>
                )}
                {historiqueAffiliationsRO && (
                    <Modal
                        size="full-width"
                        isOpen={modalAffiliationsRO}
                        toggle={() => this.toggleAffiliationsRO()}
                        backdrop="static"
                        className={style['modal-in-modal']}
                    >
                        <ModalHeader toggle={() => this.toggleAffiliationsRO()}>
                            {t('affiliationsRO.header')}
                        </ModalHeader>
                        <ModalBody>
                            <HistoriqueAffiliationROContract historique={historiqueAffiliationsRO} />
                        </ModalBody>
                    </Modal>
                )}
                {historiqueTeletransmissions && (
                    <Modal
                        size="md"
                        isOpen={modalTeletransmissions}
                        toggle={() => this.toggleTeletransmissions()}
                        backdrop="static"
                        className={style['modal-in-modal']}
                    >
                        <ModalHeader toggle={() => this.toggleTeletransmissions()}>
                            {t('historiqueTeletransmission.header')}
                        </ModalHeader>
                        <ModalBody>
                            <HistoriqueTeletransmissionContract historique={historiqueTeletransmissions} />
                        </ModalBody>
                    </Modal>
                )}
            </ControlledPanel>
        );
    }
}

Beneficiaire.propTypes = {
    t: PropTypes.func,
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Beneficiaire;
