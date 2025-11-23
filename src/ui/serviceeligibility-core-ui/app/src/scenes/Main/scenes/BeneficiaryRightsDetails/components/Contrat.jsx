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
import CommonSpinner from '../../../../../common/components/CommonSpinner/CommonSpinner';
import style from '../style.module.scss';
import PrestationServiceUtils from '../../BeneficiaryDetails/components/PrestationServiceUtils';
import DetailAMCEchange from './components/DetailAMCEchange';
import HistoriqueCodePeriodeContract from './components/HistoriqueCodePeriodeContract';
import HistoriquePeriodeContract from './components/HistoriquePeriodeContract';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class Contrat extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isOpen: !!props.open,
            modalAMCEchange: false,
            detailAMCEchange: null,
            modalCMU: false,
            historiqueCMU: null,
            modalContratResponsable: false,
            historiqueContratResponsable: null,
        };
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

    renderModalAMCEchange(detailAMCEchange) {
        if (detailAMCEchange && detailAMCEchange.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() => this.toggleAMCEchange(detailAMCEchange));
        }
        return null;
    }

    renderAMCEchange() {
        const { numerosAMCEchanges, contrat } = this.props;
        return (
            <span>
                <span>{contrat.numAMCEchange ? contrat.numAMCEchange : '-'}</span>
                <span>{this.renderModalAMCEchange(numerosAMCEchanges)}</span>
            </span>
        );
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

    @autobind
    renderFirstColonne() {
        const { t, amcName, amcNumber, contrat, conventions } = this.props;
        const numAmcEchange = this.renderAMCEchange();
        const conventionItem = utils.getConvention(conventions, contrat.typeConvention);

        return (
            <Col className="m-0 p-0">
                <LabelValuePresenter
                    id="contract-declarant"
                    label={t(`beneficiaryRightsDetails.idDeclarant`)}
                    value={`${amcNumber || '-'} - ${amcName || '-'}`}
                />
                <LabelValuePresenter
                    id="contract-numAMCEchange"
                    label={t('beneficiaryRightsDetails.numAMCEchange')}
                    value={numAmcEchange}
                />
                <LabelValuePresenter
                    id="contract-manager"
                    label={t(`beneficiaryRightsDetails.gestionnaire`)}
                    value={contrat.gestionnaire}
                />
                <LabelValuePresenter
                    id="contract-numeroAdherent"
                    label={t(`beneficiaryRightsDetails.numeroAdherent`)}
                    value={contrat.numeroAdherent}
                />
                <LabelValuePresenter
                    id="contract-convention-type"
                    label={t(`beneficiaryRightsDetails.typeConvention`)}
                    value={conventionItem ? conventionItem.libelle : ''}
                />
                <LabelValuePresenter
                    id="contract-numOperateur"
                    label={t('consolidatedContract.numOperateur')}
                    value={contrat.numOperateur}
                />
            </Col>
        );
    }

    @autobind
    renderSecondColonne() {
        const { t, contrat } = this.props;
        const souscripteurValue = `${contrat.civilitePorteur || '-'} ${contrat.prenomPorteur || '-'} ${
            contrat.nomPorteur || '-'
        }`;

        return (
            <Col className="m-0 p-0">
                <LabelValuePresenter
                    id="contract-subscriber"
                    label={t(`beneficiaryRightsDetails.souscripteur`)}
                    value={souscripteurValue}
                />
                <LabelValuePresenter
                    id="contract-date-subscriber"
                    label={t(`beneficiaryRightsDetails.dateSouscription`)}
                    value={contrat.dateSouscription}
                />
                <LabelValuePresenter
                    id="contract-criteria"
                    label={t(`beneficiaryRightsDetails.critereSecondaire`)}
                    value={contrat.critereSecondaire}
                />
                <LabelValuePresenter
                    id="contract-secondary-criteria-detailed"
                    label={t(`beneficiaryRightsDetails.critereSecondaireDetaille`)}
                    value={contrat.critereSecondaireDetaille}
                />
                <LabelValuePresenter
                    id="contract-group-insured"
                    label={t(`beneficiaryRightsDetails.groupeAssure`)}
                    value={contrat.groupeAssures}
                />
            </Col>
        );
    }

    @autobind
    renderThirdColonne() {
        const { t, contrat, debutDroit, finDroit, isHTP } = this.props;
        const { periodeCMUOuverts, periodeResponsableOuverts, contratCMUC2S, isContratCMU, isContratResponsable } =
            contrat;
        let cmuValue;
        if (contratCMUC2S !== null && contratCMUC2S !== undefined) {
            switch (contratCMUC2S) {
                case '1':
                    cmuValue = t(`beneficiaryRightsDetails.contratCMUC2SSansParticipation`);
                    break;
                case '2':
                    cmuValue = t(`beneficiaryRightsDetails.contratCMUC2SAvecParticipation`);
                    break;
                default:
                    cmuValue = t(isContratCMU ? 'yes' : 'no');
            }
        }
        let isCMU;
        let isResponsable;
        if (!isHTP) {
            isCMU = t(isContratCMU ? 'yes' : 'no');
            isResponsable = t(isContratResponsable ? 'yes' : 'no');
        }

        return (
            <Col className="m-0">
                <LabelValuePresenter
                    id="contract-ext-num"
                    label={t(`beneficiaryRightsDetails.numeroContratExterne`)}
                    value={contrat.numeroExterneContratIndividuel}
                />
                <LabelValuePresenter
                    id="contract-external-collective-number"
                    label={t(`beneficiaryRightsDetails.numeroExterneContratCollectif`)}
                    value={contrat.numeroExterneContratCollectif}
                />
                <LabelValuePresenter
                    id="contract-contratCMUC2S"
                    label={t(`consolidatedContract.contratCMUC2S`)}
                    value={cmuValue}
                />
                <LabelValuePresenter
                    id="contract-cmu"
                    label={t(`consolidatedContract.contratCMU`)}
                    value={
                        isHTP ? (
                            <div className="d-flex justify-item-center align-items-center">
                                {utils.getActivePeriodFromData(periodeCMUOuverts, debutDroit, finDroit, t)}
                                {this.renderModalCMU(periodeCMUOuverts)}
                            </div>
                        ) : (
                            isCMU
                        )
                    }
                />

                <LabelValuePresenter
                    id="contract-responsible"
                    label={t(`consolidatedContract.contratResponsable`)}
                    value={
                        isHTP ? (
                            <div className="d-flex justify-item-center align-items-center">
                                {utils.getActivePeriod(periodeResponsableOuverts, debutDroit, finDroit, t)}
                                {this.renderModalContratResponsable(periodeResponsableOuverts)}
                            </div>
                        ) : (
                            isResponsable
                        )
                    }
                />
            </Col>
        );
    }

    render() {
        const { t, conventions, contrat } = this.props;
        const {
            isOpen,
            modalAMCEchange,
            detailAMCEchange,
            modalCMU,
            historiqueCMU,
            modalContratResponsable,
            historiqueContratResponsable,
        } = this.state;
        const canRender = contrat && conventions;
        if (!canRender) {
            return <CommonSpinner />;
        }
        const pageTitle = t('beneficiaryRightsDetails.contrat');
        const contratCollectif = `${t('beneficiaryRightsDetails.contratCollectif')}${t('colon')}${
            contrat.numeroContratCollectif || '-'
        }`;
        const typeContrat = `${t('beneficiaryRightsDetails.typeContrat')}${t('colon')}${t(
            utils.getI18nLabelTypeContrat(contrat.qualification),
        )}`;
        const context = `${contratCollectif} / ${typeContrat}`;
        const panelHeader = <PanelHeader title={`${pageTitle}${t('colon')}${contrat.numero}`} context={context} />;

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
                {detailAMCEchange && (
                    <Modal
                        size="md"
                        isOpen={modalAMCEchange}
                        isOutsideModalClicked={false}
                        toggle={() => this.toggleAMCEchange()}
                        backdrop="static"
                        className={style['modal-in-modal']}
                    >
                        <ModalHeader toggle={() => this.toggleAMCEchange()}>
                            {t('beneficiaryRightsDetails.detailAMCEchangeHeader')}
                        </ModalHeader>
                        <ModalBody>
                            <DetailAMCEchange
                                numEchangeTDB={contrat.numAMCEchange}
                                detailAMCEchange={detailAMCEchange}
                            />
                        </ModalBody>
                    </Modal>
                )}
                {historiqueCMU && (
                    <Modal
                        size="md"
                        isOpen={modalCMU}
                        toggle={() => this.toggleCMU()}
                        backdrop="static"
                        className={style['modal-in-modal']}
                    >
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
                        className={style['modal-in-modal']}
                    >
                        <ModalHeader toggle={() => this.toggleContratResponsable()}>
                            {t('historiqueContratResponsable.header')}
                        </ModalHeader>
                        <ModalBody>
                            <HistoriquePeriodeContract historique={historiqueContratResponsable} />
                        </ModalBody>
                    </Modal>
                )}
            </ControlledPanel>
        );
    }
}

Contrat.propTypes = {
    t: PropTypes.func,
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    isHTP: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Contrat;
