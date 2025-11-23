/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
    PanelHeader,
    Panel,
    PanelSection,
    Row,
    Col,
    Modal,
    ModalHeader,
    ModalBody,
    Status,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import autobind from 'autobind-decorator';
import LabelValuePresenter from '../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import CommonSpinner from '../../../../../../common/components/CommonSpinner/CommonSpinner';
import businessUtils from '../../../../../../common/utils/businessUtils';
import PrestationServiceUtils from '../../../BeneficiaryDetails/components/PrestationServiceUtils';
import HistoriqueSuspensionContract from './HistoriqueSuspensionContract';
import DateUtils from '../../../../../../common/utils/DateUtils';
import Constants from '../../Constants';
import DetailAMCEchange from './DetailAMCEchange';
import HistoriquePeriodeContract from './HistoriquePeriodeContract';
import HistoriqueCodePeriodeContract from './HistoriqueCodePeriodeContract';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class ContractData extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isOpen: true,
            isOpenCollectiveData: false,
            modalSuspension: false,
            historiqueSuspension: null,
            modalCMU: false,
            historiqueCMU: null,
            modalContratResponsable: false,
            historiqueContratResponsable: null,
            modalAMCEchange: false,
            detailAMCEchange: null,
        };
    }

    @autobind
    toggleCollapse() {
        const { isOpen } = this.state;
        this.setState({ isOpen: !isOpen });
    }

    @autobind
    toggleCollapseCollectiveData() {
        const { isOpenCollectiveData } = this.state;
        this.setState({ isOpenCollectiveData: !isOpenCollectiveData });
    }

    toggleSuspension(historiqueSuspension) {
        const { modalSuspension } = this.state;
        this.setState({ modalSuspension: !modalSuspension, historiqueSuspension });
    }

    toggleAMCEchange(detailAMCEchange) {
        const { modalAMCEchange } = this.state;
        this.setState({ modalAMCEchange: !modalAMCEchange, detailAMCEchange });
    }

    toggleCMU(historiqueCMU) {
        const { modalCMU } = this.state;
        this.setState({ modalCMU: !modalCMU, historiqueCMU });
    }

    toggleContratResponsable(historiqueContratResponsable) {
        const { modalContratResponsable } = this.state;
        this.setState({ modalContratResponsable: !modalContratResponsable, historiqueContratResponsable });
    }

    renderModalSuspension(periodesSuspension) {
        if (periodesSuspension && periodesSuspension.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() => this.toggleSuspension(periodesSuspension));
        }
        return null;
    }

    renderModalAMCEchange(detailAMCEchange) {
        if (detailAMCEchange && detailAMCEchange.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() => this.toggleAMCEchange(detailAMCEchange));
        }
        return null;
    }

    renderModalCMU(periodesCMU) {
        if (periodesCMU && periodesCMU.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() => this.toggleCMU(periodesCMU));
        }
        return null;
    }

    renderModalContratResponsable(periodesContratResponsable) {
        if (periodesContratResponsable && periodesContratResponsable.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() =>
                this.toggleContratResponsable(periodesContratResponsable),
            );
        }
        return null;
    }

    renderPeriodeSuspension(lastPeriodeSuspension, listeSuspension) {
        const { t } = this.props;
        return (
            <Fragment>
                <div>
                    <LabelValuePresenter
                        id="lastSuspension"
                        label={t('beneficiaryDetails.servicePrestation.lastSuspension')}
                        value={lastPeriodeSuspension ? t('yes') : t('no')}
                        inline
                    />
                    {this.renderModalSuspension(listeSuspension)}
                </div>
            </Fragment>
        );
    }

    renderContractColumn1() {
        const { t, contract, amcName, numerosAMCEchanges } = this.props;

        const souscripteurValue =
            contract.prenomPorteur || contract.nomPorteur
                ? `${contract.civilitePorteur || ''} ${contract.prenomPorteur || ''} ${contract.nomPorteur || ''}`
                : '-';

        const declarant = `${contract.idDeclarant || '-'} - ${amcName || '-'}`;
        return (
            <Col>
                <div>
                    <LabelValuePresenter
                        id="contract-declarant"
                        label={t(`consolidatedContract.idDeclarant`)}
                        value={declarant}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-numAMCEchange"
                        label={t('consolidatedContract.numAMCEchange')}
                        value={contract.numAMCEchange}
                        inline
                    />
                    {this.renderModalAMCEchange(numerosAMCEchanges)}
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-numeroContrat"
                        label={t(`consolidatedContract.numeroContrat`)}
                        value={contract.numeroContrat}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="numeroExterneContratIndividuel"
                        label={t('consolidatedContract.numeroExterneContratIndividuel')}
                        value={contract.numeroExterneContratIndividuel}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-numeroAdherent"
                        label={t(`consolidatedContract.numeroAdherent`)}
                        value={contract.numeroAdherent}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-numeroAdherentComplet"
                        label={t(`consolidatedContract.numeroAdherentComplet`)}
                        value={contract.numeroAdherentComplet}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-subscriber"
                        label={t(`consolidatedContract.souscripteur`)}
                        value={souscripteurValue}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-type"
                        label={t(`consolidatedContract.type`)}
                        value={contract.type}
                        inline
                    />
                </div>

                <div>
                    <LabelValuePresenter
                        id="typeContrat"
                        label={t('consolidatedContract.typeContrat')}
                        value={t(businessUtils.getI18nLabelTypeContrat(contract.qualification))}
                        inline
                    />
                </div>
            </Col>
        );
    }

    renderContractColumn2() {
        const { t, contract, conventions } = this.props;

        const conventionItem = businessUtils.getConvention(conventions, contract.typeConvention);
        const indOuColl =
            contract.individuelOuCollectif &&
            (contract.individuelOuCollectif === '1' || contract.individuelOuCollectif === '2')
                ? t(`consolidatedContract.individuelOuCollectif${contract.individuelOuCollectif}`)
                : '-';

        return (
            <Col>
                <div>
                    <LabelValuePresenter
                        id="contract-dateSouscription"
                        label={t(`consolidatedContract.dateSouscription`)}
                        value={DateUtils.formatDisplayDate(contract.dateSouscription, Constants.DEFAULT_DATE)}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-dateResiliation"
                        label={t(`consolidatedContract.dateResiliation`)}
                        value={DateUtils.formatDisplayDate(contract.dateResiliation, Constants.DEFAULT_DATE)}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-dateRestitution"
                        label={t(`consolidatedContract.dateRestitution`)}
                        value={DateUtils.formatDisplayDate(contract.dateRestitution, Constants.DEFAULT_DATE)}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-destinataire"
                        label={t(`consolidatedContract.destinataire`)}
                        value={contract.destinataire}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-individuelOuCollectif"
                        label={t(`consolidatedContract.individuelOuCollectif`)}
                        value={indOuColl}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-convention-type"
                        label={t(`consolidatedContract.typeConvention`)}
                        value={conventionItem ? conventionItem.libelle : ''}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-groupeAssures"
                        label={t(`consolidatedContract.groupeAssures`)}
                        value={contract.groupeAssures}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-manager"
                        label={t(`consolidatedContract.gestionnaire`)}
                        value={contract.gestionnaire}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-numOperateur"
                        label={t('consolidatedContract.numOperateur')}
                        value={contract.numOperateur}
                        inline
                    />
                </div>
            </Col>
        );
    }

    renderContractColumn3() {
        const { contract, t, endContractDate, isHTP } = this.props;

        let lastPeriodeSuspension = null;
        const periodesSuspension = contract.suspension?.periodesSuspension;
        if (periodesSuspension && periodesSuspension.length > 0) {
            lastPeriodeSuspension = periodesSuspension[0];
        }
        const { periodesContratCMU, periodesContratResponsable, isContratCMU, isContratResponsable } = contract;
        let codeCMUActif;
        let isResponsable;
        if (isHTP) {
            codeCMUActif = businessUtils.returnCodeOfActivePeriod(periodesContratCMU, endContractDate, t);
            isResponsable = businessUtils.isActivePeriod(periodesContratResponsable, endContractDate, t);
        } else {
            codeCMUActif = t(isContratCMU ? 'yes' : 'no');
            isResponsable = t(isContratResponsable ? 'yes' : 'no');
        }

        const cmuc2sLabel = t(`consolidatedContract.contratCMUC2S${contract.contratCMUC2S}`);

        return (
            <Col>
                <div>
                    <LabelValuePresenter
                        id="contract-ordrePriorisation"
                        label={t(`consolidatedContract.ordrePriorisation`)}
                        value={contract.ordrePriorisation}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-criteria"
                        label={t(`consolidatedContract.critereSecondaire`)}
                        value={contract.critereSecondaire}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-detailed-criteria"
                        label={t(`consolidatedContract.critereSecondaireDetaille`)}
                        value={contract.critereSecondaireDetaille}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-cmu"
                        label={t(`consolidatedContract.contratCMU`)}
                        value={codeCMUActif}
                        inline
                    />
                    {isHTP && this.renderModalCMU(periodesContratCMU)}
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-contratCMUC2S"
                        label={t(`consolidatedContract.contratCMUC2S`)}
                        value={cmuc2sLabel}
                        inline
                    />
                </div>
                <div>
                    <LabelValuePresenter
                        id="contract-responsible"
                        label={t(`consolidatedContract.contratResponsable`)}
                        value={isResponsable}
                        inline
                    />
                    {isHTP && this.renderModalContratResponsable(periodesContratResponsable)}
                </div>
                {isHTP && (
                    <div>
                        <LabelValuePresenter
                            id="contract-suspension"
                            label={t(`consolidatedContract.suspension`)}
                            value={(contract.suspension || {}).etatSuspension}
                            inline
                        />
                    </div>
                )}
                {this.renderPeriodeSuspension(lastPeriodeSuspension, periodesSuspension)}
            </Col>
        );
    }

    renderCollectiveData() {
        const { t, contract } = this.props;
        const { isOpenCollectiveData } = this.state;

        return (
            <Panel
                id="collective-contract-panel"
                header={<PanelHeader title={t('consolidatedContract.collectiveContract')} />}
                expanded={isOpenCollectiveData}
                onCollapseClick={this.toggleCollapseCollectiveData}
            >
                <PanelSection>
                    <Row>
                        <Col>
                            <div>
                                <LabelValuePresenter
                                    id="numeroContratCollectif"
                                    label={t('consolidatedContract.numeroContratCollectif')}
                                    value={contract.numeroContratCollectif}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="numeroExterneContratCollectif"
                                    label={t('consolidatedContract.numeroExterneContratCollectif')}
                                    value={contract.numeroExterneContratCollectif}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col>
                            <div>
                                <LabelValuePresenter
                                    id="identifiantCollectivite"
                                    label={t('consolidatedContract.identifiantCollectivite')}
                                    value={contract.identifiantCollectivite}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="raisonSociale"
                                    label={t('consolidatedContract.raisonSociale')}
                                    value={contract.raisonSociale}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col>
                            <div>
                                <LabelValuePresenter
                                    id="siret"
                                    label={t('consolidatedContract.siret')}
                                    value={contract.siret}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="groupePopulation"
                                    label={t('consolidatedContract.groupePopulation')}
                                    value={contract.groupePopulation}
                                    inline
                                />
                            </div>
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }

    render() {
        const { t, contract, conventions, status } = this.props;
        const {
            isOpen,
            modalSuspension,
            historiqueSuspension,
            modalAMCEchange,
            detailAMCEchange,
            modalCMU,
            historiqueCMU,
            modalContratResponsable,
            historiqueContratResponsable,
        } = this.state;

        const canRender = contract && conventions;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const pageTitle = t('consolidatedContract.droits');
        const panelHeader = (
            <PanelHeader
                title={t('consolidatedContract.infosContrat')}
                status={<Status label={t(`consolidatedContract.${status.status}`)} behavior={status.behavior} />}
                titleClassname="col-md-6"
                contextClassname="col-md-6"
            />
        );

        return (
            <Panel
                id="contract-panel"
                header={panelHeader}
                label={pageTitle}
                expanded={isOpen}
                onCollapseClick={this.toggleCollapse}
            >
                <PanelSection>
                    <Row>
                        {this.renderContractColumn1()}
                        {this.renderContractColumn2()}
                        {this.renderContractColumn3()}
                    </Row>
                    {this.renderCollectiveData()}
                    {historiqueSuspension && (
                        <Modal
                            size="md"
                            isOpen={modalSuspension}
                            toggle={() => this.toggleSuspension()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.toggleSuspension()}>
                                {t('historiqueSuspension.header')}
                            </ModalHeader>
                            <ModalBody>
                                <HistoriqueSuspensionContract historique={historiqueSuspension} />
                            </ModalBody>
                        </Modal>
                    )}
                    {detailAMCEchange && (
                        <Modal
                            size="md"
                            isOpen={modalAMCEchange}
                            toggle={() => this.toggleAMCEchange()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.toggleAMCEchange()}>
                                {t('beneficiaryRightsDetails.detailAMCEchangeHeader')}
                            </ModalHeader>
                            <ModalBody>
                                <DetailAMCEchange
                                    numEchangeTDB={contract.numAMCEchange}
                                    detailAMCEchange={detailAMCEchange}
                                />
                            </ModalBody>
                        </Modal>
                    )}
                    {historiqueCMU && (
                        <Modal size="md" isOpen={modalCMU} toggle={() => this.toggleCMU()} backdrop="static">
                            <ModalHeader toggle={() => this.toggleCMU()}>{t('historiqueCMU.header')}</ModalHeader>
                            <ModalBody>
                                <HistoriqueCodePeriodeContract historique={historiqueCMU} />
                            </ModalBody>
                        </Modal>
                    )}
                    {historiqueContratResponsable && (
                        <Modal
                            size="md"
                            isOpen={modalContratResponsable}
                            toggle={() => this.toggleContratResponsable()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.toggleContratResponsable()}>
                                {t('historiqueContratResponsable.header')}
                            </ModalHeader>
                            <ModalBody>
                                <HistoriquePeriodeContract historique={historiqueContratResponsable} />
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

ContractData.propTypes = {
    t: PropTypes.func,
    contract: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    endContractDate: PropTypes.string,
    isHTP: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ContractData;
