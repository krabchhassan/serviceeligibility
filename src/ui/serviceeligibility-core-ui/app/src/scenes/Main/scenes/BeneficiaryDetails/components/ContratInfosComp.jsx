import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Col,
    Modal,
    ModalHeader,
    ModalBody,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import PrestationServiceUtils from './PrestationServiceUtils';
import HistoriqueCMU from './HistoriqueCMU';
import HistoriqueContratResponsable from './HistoriqueContratResponsable';
import HistoriqueSuspension from './HistoriqueSuspension';
import DateUtils from '../../../../../common/utils/DateUtils';
import businessUtils from '../../../../../common/utils/businessUtils';

@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class ContratInfosComp extends Component {
    constructor(props) {
        super(props);

        this.state = {
            expanded: false,
            modalCMU: false,
            modalSuspension: false,
            historiqueCMU: null,
            historiqueContratResponsable: null,
            historiqueSuspension: null,
        };
    }

    onCollapseClick() {
        const { expanded } = this.state;
        this.setState({ expanded: !expanded });
    }

    isCartePapier(contexteTiersPayant) {
        const { t } = this.props;
        let isCartePapier = '-';
        if (
            contexteTiersPayant &&
            contexteTiersPayant.isCartePapier !== undefined &&
            contexteTiersPayant.isCartePapier !== null
        ) {
            isCartePapier = t(contexteTiersPayant.isCartePapier ? 'yes' : 'no');
        }
        return isCartePapier;
    }

    isCarteDemat(contexteTiersPayant) {
        const { t } = this.props;
        let isCarteDematerialisee = '-';
        if (
            contexteTiersPayant &&
            contexteTiersPayant.isCarteDematerialisee !== undefined &&
            contexteTiersPayant.isCarteDematerialisee !== null
        ) {
            isCarteDematerialisee = t(contexteTiersPayant.isCarteDematerialisee ? 'yes' : 'no');
        }
        return isCarteDematerialisee;
    }

    toggleCMU(historiqueCMU) {
        const { modalCMU } = this.state;
        this.setState({ modalCMU: !modalCMU, historiqueCMU });
    }

    toggleContratResponsable(historiqueContratResponsable) {
        const { modalContratResponsable } = this.state;
        this.setState({ modalContratResponsable: !modalContratResponsable, historiqueContratResponsable });
    }

    toggleSuspension(historiqueSuspension) {
        const { modalSuspension } = this.state;
        this.setState({ modalSuspension: !modalSuspension, historiqueSuspension });
    }

    renderModalCMU(periodesContrat) {
        return PrestationServiceUtils.renderButtonModal(() => this.toggleCMU(periodesContrat));
    }

    renderModalContratResponsable(periodesContrat) {
        return PrestationServiceUtils.renderButtonModal(() => this.toggleContratResponsable(periodesContrat));
    }

    renderModalSuspension(periodesSuspension) {
        if (periodesSuspension && periodesSuspension.length > 0) {
            return PrestationServiceUtils.renderButtonModal(() => this.toggleSuspension(periodesSuspension));
        }
        return null;
    }

    renderCartePapier(isCartePapier) {
        const { t } = this.props;
        return (
            <Fragment>
                <div>
                    <LabelValuePresenter
                        id="cartePapier"
                        label={t('beneficiaryDetails.servicePrestation.cartePapier')}
                        value={isCartePapier}
                        inline
                    />
                </div>
            </Fragment>
        );
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
                {lastPeriodeSuspension && (
                    <Fragment>
                        <div>
                            <LabelValuePresenter
                                id="debutSuspension"
                                label={t('beneficiaryDetails.servicePrestation.debutSuspension')}
                                value={DateUtils.transformDateForDisplay(
                                    ((lastPeriodeSuspension || {}).periode || {}).debut,
                                )}
                                inline
                            />
                        </div>
                        <div>
                            <LabelValuePresenter
                                id="finSuspension"
                                label={t('beneficiaryDetails.servicePrestation.finSuspension')}
                                value={DateUtils.transformDateForDisplay(
                                    ((lastPeriodeSuspension || {}).periode || {}).fin,
                                )}
                                inline
                            />
                        </div>
                    </Fragment>
                )}
            </Fragment>
        );
    }

    render() {
        const { t, item, endContractDate } = this.props;
        const {
            expanded,
            modalCMU,
            modalContratResponsable,
            historiqueCMU,
            historiqueContratResponsable,
            modalSuspension,
            historiqueSuspension,
        } = this.state;
        const { contexteTiersPayant } = item;
        const isCartePapier = this.isCartePapier(contexteTiersPayant);
        const isCarteDematerialisee = this.isCarteDemat(contexteTiersPayant);

        const codeCMUActif = businessUtils.returnCodeOfActivePeriod(item.periodesContratCMUOuvert, endContractDate, t);
        const isResponsable = businessUtils.isActivePeriod(item.periodesContratResponsableOuvert, endContractDate, t);

        const valueContratResponsable = (
            <Fragment>
                <span>{isResponsable}</span>
                <span>
                    {item.periodesContratResponsableOuvert != null &&
                        item.periodesContratResponsableOuvert.length > 0 &&
                        this.renderModalContratResponsable(item.periodesContratResponsableOuvert)}
                </span>
            </Fragment>
        );

        const valueCMU = (
            <Fragment>
                <span>{codeCMUActif}</span>
                <span>
                    {item.periodesContratCMUOuvert != null &&
                        item.periodesContratCMUOuvert.length > 0 &&
                        this.renderModalCMU(item.periodesContratCMUOuvert)}
                </span>
            </Fragment>
        );

        let lastPeriodeSuspension = null;
        if (item.periodesSuspension && item.periodesSuspension.length > 0) {
            lastPeriodeSuspension = item.periodesSuspension[0];
        }

        return (
            <Panel
                onCollapseClick={() => this.onCollapseClick()}
                expanded={expanded}
                className="mt-3"
                header={<PanelHeader title={t('beneficiaryDetails.servicePrestation.infosComp')} />}
            >
                <PanelSection>
                    <Row>
                        <Col xs="12" md="4">
                            <div>
                                <LabelValuePresenter
                                    id="critereSecondaire"
                                    label={t('beneficiaryDetails.servicePrestation.critereSecondaire')}
                                    value={(item || {}).critereSecondaire}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="critereSecondaireDetaille"
                                    label={t('beneficiaryDetails.servicePrestation.critereSecondaireDetaille')}
                                    value={(item || {}).critereSecondaireDetaille}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="typeContrat"
                                    label={t('beneficiaryDetails.servicePrestation.typeContrat')}
                                    value={valueContratResponsable}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="cmu"
                                    label={t('beneficiaryDetails.servicePrestation.cmu')}
                                    value={valueCMU}
                                    inline
                                />
                            </div>
                        </Col>
                        <Col xs="12" md="4">
                            <div>
                                <LabelValuePresenter
                                    id="numeroAdherentComplet"
                                    label={t('beneficiaryDetails.servicePrestation.numeroAdherentComplet')}
                                    value={(item || {}).numeroAdherentComplet}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="numeroExterne"
                                    label={t('beneficiaryDetails.servicePrestation.numeroExterne')}
                                    value={(item || {}).numeroExterne}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="gestionnaire"
                                    label={t('beneficiaryDetails.servicePrestation.gestionnaire')}
                                    value={(item || {}).gestionnaire}
                                    inline
                                />
                            </div>
                            <div>
                                <LabelValuePresenter
                                    id="apporteurAffaire"
                                    label={t('beneficiaryDetails.servicePrestation.apporteurAffaire')}
                                    value={(item || {}).apporteurAffaire}
                                    inline
                                />
                            </div>
                            {this.renderPeriodeSuspension(lastPeriodeSuspension, item.periodesSuspension)}
                        </Col>
                        <Col xs="12" md="4">
                            <div>
                                <LabelValuePresenter
                                    id="carteDematerialisee"
                                    label={t('beneficiaryDetails.servicePrestation.carteDematerialisee')}
                                    value={isCarteDematerialisee}
                                    inline
                                />
                            </div>
                            {this.renderCartePapier(isCartePapier)}
                            <div>
                                <LabelValuePresenter
                                    id="dateRestitutionCarte"
                                    label={t('beneficiaryDetails.servicePrestation.dateRestitutionCarte')}
                                    value={DateUtils.transformDateForDisplay(
                                        (contexteTiersPayant || {}).dateRestitutionCarte,
                                    )}
                                    inline
                                />
                            </div>
                        </Col>
                    </Row>
                    {historiqueCMU && (
                        <Modal size="md" isOpen={modalCMU} toggle={() => this.toggleCMU()} backdrop="static">
                            <ModalHeader toggle={() => this.toggleCMU()}>{t('historiqueCMU.header')}</ModalHeader>
                            <ModalBody>
                                <HistoriqueCMU historique={historiqueCMU} />
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
                                <HistoriqueContratResponsable historique={historiqueContratResponsable} />
                            </ModalBody>
                        </Modal>
                    )}
                    {historiqueSuspension && (
                        <Modal
                            size="full-width"
                            isOpen={modalSuspension}
                            toggle={() => this.toggleSuspension()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.toggleSuspension()}>
                                {t('historiqueSuspension.header')}
                            </ModalHeader>
                            <ModalBody>
                                <HistoriqueSuspension historique={historiqueSuspension} />
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    endContractDate: PropTypes.string,
};
// Default props
const defaultProps = {};

ContratInfosComp.propTypes = propTypes;
// Add default props
ContratInfosComp.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ContratInfosComp;
