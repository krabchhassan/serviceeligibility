/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    Modal,
    ModalHeader,
    ModalBody,
    PanelHeader,
    Panel,
    PanelSection,
    Row,
    Col,
    LoadingSpinner,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import autobind from 'autobind-decorator';
import LabelValuePresenter from '../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import Address from '../../../../../../common/components/Address/Address';
import DateUtils from '../../../../../../common/utils/DateUtils';
import businessUtils from '../../../../../../common/utils/businessUtils';
import Constants from '../../Constants';
import PrestationServiceUtils from '../../../BeneficiaryDetails/components/PrestationServiceUtils';
import HistoriqueCodePeriodeContract from './HistoriqueCodePeriodeContract';
import HistoriquePeriodeContract from './HistoriquePeriodeContract';
import HistoriqueTeletransmissionContract from './HistoriqueTeletransmissionContract';
import HistoriqueAffiliationROContract from './HistoriqueAffiliationRoContract';
import DateRangeFromToPresenter from '../../../../../../common/utils/DateRangeFromToPresenter';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class BenefData extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isOpenBenef: true,
            isOpenAffiliationAndAddress: false,
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

    @autobind
    toggleCollapseBenef() {
        const { isOpenBenef } = this.state;
        this.setState({ isOpenBenef: !isOpenBenef });
    }

    @autobind
    toggleCollapseAffiliationAndAdresses() {
        const { isOpenAffiliationAndAddress } = this.state;
        this.setState({ isOpenAffiliationAndAddress: !isOpenAffiliationAndAddress });
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

    renderBenefColumn1() {
        const { t, benefToDisplay } = this.props;

        return (
            <Col>
                <div>
                    <LabelValuePresenter
                        id="numeroPersonne"
                        label={t('consolidatedContract.numeroPersonne')}
                        value={benefToDisplay.numeroPersonne}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="refExternePersonne"
                        label={t(`consolidatedContract.refExternePersonne`)}
                        value={benefToDisplay.refExternePersonne}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="numeroAdhesionIndividuelle"
                        label={t(`consolidatedContract.numeroAdhesionIndividuelle`)}
                        value={benefToDisplay.numeroAdhesionIndividuelle}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="dateAdhesionMutuelle"
                        label={t(`consolidatedContract.dateAdhesionMutuelle`)}
                        value={DateUtils.formatDisplayDate(benefToDisplay.dateAdhesionMutuelle, Constants.DEFAULT_DATE)}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="dateDebutAdhesionIndividuelle"
                        label={t(`consolidatedContract.dateDebutAdhesionIndividuelle`)}
                        value={DateUtils.formatDisplayDate(
                            benefToDisplay.dateDebutAdhesionIndividuelle,
                            Constants.DEFAULT_DATE,
                        )}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-dateRadiation"
                        label={t(`consolidatedContract.dateRadiation`)}
                        value={DateUtils.formatDisplayDate(benefToDisplay.dateRadiation, Constants.DEFAULT_DATE)}
                        inline
                    />
                </div>
            </Col>
        );
    }

    renderBenefColumn2() {
        const { t, benefToDisplay } = this.props;

        return (
            <Col>
                <div>
                    <LabelValuePresenter
                        id="dateNaissance"
                        label={t('consolidatedContract.dateNaissance')}
                        value={DateUtils.formatDisplayDate(benefToDisplay.dateNaissance, Constants.DEFAULT_DATE)}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="rangNaissance"
                        label={t('consolidatedContract.rangNaissance')}
                        value={benefToDisplay.rangNaissance}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="rangAdministratif"
                        label={t('consolidatedContract.rangAdministratif')}
                        value={benefToDisplay.rangAdministratif}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="nirBeneficiaire"
                        label={t(`consolidatedContract.nirBeneficiaire`)}
                        value={
                            benefToDisplay.nirBeneficiaire && benefToDisplay.cleNirBeneficiaire
                                ? `${businessUtils.formatRO(benefToDisplay.nirBeneficiaire)} ${
                                      benefToDisplay.cleNirBeneficiaire
                                  }`
                                : '-'
                        }
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="nirOd1"
                        label={t(`consolidatedContract.nirOd1`)}
                        value={
                            benefToDisplay.nirOd1 && benefToDisplay.cleNirOd1
                                ? `${businessUtils.formatRO(benefToDisplay.nirOd1)} ${benefToDisplay.cleNirOd1}`
                                : '-'
                        }
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="nirOd2"
                        label={t(`consolidatedContract.nirOd2`)}
                        value={
                            benefToDisplay.nirOd2 && benefToDisplay.cleNirOd2
                                ? `${businessUtils.formatRO(benefToDisplay.nirOd2)} ${benefToDisplay.cleNirOd2}`
                                : '-'
                        }
                        inline
                    />
                </div>
            </Col>
        );
    }

    renderBenefColumn3() {
        const { benefToDisplay, endContractDate, t } = this.props;
        const { situationsParticulieres, periodesMedecinTraitant } = benefToDisplay;
        const periodeMedecinTraitantActive = businessUtils.isActivePeriod(periodesMedecinTraitant, endContractDate, t);
        const situationParticuliereActive = businessUtils.returnCodeOfActivePeriod(
            situationsParticulieres,
            endContractDate,
            t,
        );

        return (
            <Col>
                <div>
                    <LabelValuePresenter
                        id="categorieSociale"
                        label={t(`consolidatedContract.categorieSociale`)}
                        value={benefToDisplay.categorieSociale}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="situationParticuliere"
                        label={t(`consolidatedContract.situationParticuliere`)}
                        value={situationParticuliereActive}
                        inline
                    />
                    {this.renderModalSituationsParticulieres(situationsParticulieres)}
                </div>
                <div>
                    <LabelValuePresenter
                        id="modePaiementPrestations"
                        label={t(`consolidatedContract.modePaiementPrestations`)}
                        value={benefToDisplay.modePaiementPrestations}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="medecinTraitant"
                        label={t(`consolidatedContract.medecinTraitant`)}
                        value={periodeMedecinTraitantActive}
                        inline
                    />
                    {this.renderModalPeriodesMedecinTraitant(periodesMedecinTraitant)}
                </div>
            </Col>
        );
    }

    renderAffiliationAndAdresses() {
        const { t, benefToDisplay, endContractDate } = this.props;
        const { isOpenAffiliationAndAddress } = this.state;

        const { affiliation, affiliationsRO, adresses, regimesParticuliers, teletransmissions } = benefToDisplay;

        const affiliationName = affiliation
            ? `${affiliation.civilite ? affiliation.civilite : ''} ${affiliation.nom ? affiliation.nom : ''} ${
                  affiliation.prenom ? affiliation.prenom : ''
              }`
            : '';

        const context = affiliation ? (
            <DateRangeFromToPresenter
                start={DateUtils.formatDisplayDate(affiliation.periodeDebut)}
                end={DateUtils.formatDisplayDate(affiliation.periodeFin, Constants.DEFAULT_DATE)}
            />
        ) : (
            ''
        );

        const affiliationROActive = businessUtils.returnCodeOfActivePeriod(affiliationsRO, endContractDate, t);
        const teletransmissionActive = businessUtils.returnCodeOfActivePeriod(teletransmissions, endContractDate, t);
        const regimeParticulierActif = businessUtils.returnCodeOfActivePeriod(regimesParticuliers, endContractDate, t);

        return (
            <Panel
                id="affiliation-panel"
                header={<PanelHeader title={t('consolidatedContract.affiliation')} context={context} />}
                expanded={isOpenAffiliationAndAddress}
                onCollapseClick={this.toggleCollapseAffiliationAndAdresses}
            >
                <PanelSection>
                    <Row>
                        <Col xs={4}>
                            <div>
                                <LabelValuePresenter
                                    id="affiliationName"
                                    label={t('consolidatedContract.affiliationName')}
                                    value={affiliationName}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="qualite"
                                    label={t(`consolidatedContract.qualite`)}
                                    value={(affiliation || {}).qualite}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="typeAssure"
                                    label={t(`consolidatedContract.typeAssure`)}
                                    value={(affiliation || {}).typeAssure}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="typeAssure"
                                    label={t(`consolidatedContract.affiliationRO`)}
                                    value={affiliationROActive}
                                    inline
                                />
                                {this.renderModalAffiliationsRO(affiliationsRO)}
                            </div>
                        </Col>
                        <Col xs={2} className="pl-1">
                            <div>
                                <LabelValuePresenter
                                    id="regimeOD1"
                                    label={t(`consolidatedContract.regimeOD1`)}
                                    value={(affiliation || {}).regimeOD1}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="caisseOD1"
                                    label={t(`consolidatedContract.caisseOD1`)}
                                    value={(affiliation || {}).caisseOD1}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="centreOD1"
                                    label={t(`consolidatedContract.centreOD1`)}
                                    value={(affiliation || {}).centreOD1}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="regimeOD2"
                                    label={t(`consolidatedContract.regimeOD2`)}
                                    value={(affiliation || {}).regimeOD2}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="caisseOD2"
                                    label={t(`consolidatedContract.caisseOD2`)}
                                    value={(affiliation || {}).caisseOD2}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="centreOD2"
                                    label={t(`consolidatedContract.centreOD2`)}
                                    value={(affiliation || {}).centreOD2}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs={3}>
                            <div>
                                <LabelValuePresenter
                                    id="contract-acs"
                                    label={t(`consolidatedContract.beneficiaireACS`)}
                                    value={t((affiliation || {}).isBeneficiaireACS ? 'yes' : 'no')}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="regimeParticulier"
                                    label={t('consolidatedContract.regimeParticulier')}
                                    value={regimeParticulierActif}
                                    inline
                                />
                                {this.renderModalRegimesParticuliers(regimesParticuliers)}
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="contract-remote-transmission"
                                    label={t(`consolidatedContract.teletransmission`)}
                                    value={teletransmissionActive}
                                    inline
                                />
                                {this.renderModalTeletransmissions(teletransmissions)}
                            </div>
                        </Col>
                        <Col xs={3}>{adresses && <Address address={adresses[0]} />}</Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    render() {
        const { t, benefToDisplay } = this.props;
        const {
            isOpenBenef,
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
        if (!benefToDisplay) {
            return <LoadingSpinner iconType="circle-o-notch" behavior="primary" />;
        }

        return (
            <Panel
                id="beneficiary-panel"
                header={<PanelHeader title={t('consolidatedContract.infosBenef')} />}
                expanded={isOpenBenef}
                onCollapseClick={this.toggleCollapseBenef}
            >
                <PanelSection>
                    <Row>
                        {this.renderBenefColumn1()}
                        {this.renderBenefColumn2()}
                        {this.renderBenefColumn3()}
                    </Row>
                    {this.renderAffiliationAndAdresses()}
                    {historiqueRegimesParticuliers && (
                        <Modal
                            size="md"
                            isOpen={modalRegimesParticuliers}
                            toggle={() => this.toggleRegimesParticuliers()}
                            backdrop="static"
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
                        >
                            <ModalHeader toggle={() => this.toggleTeletransmissions()}>
                                {t('historiqueTeletransmission.header')}
                            </ModalHeader>
                            <ModalBody>
                                <HistoriqueTeletransmissionContract historique={historiqueTeletransmissions} />
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

BenefData.propTypes = {
    t: PropTypes.func,
    benefToDisplay: PropTypes.shape(),
    endContractDate: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default BenefData;
