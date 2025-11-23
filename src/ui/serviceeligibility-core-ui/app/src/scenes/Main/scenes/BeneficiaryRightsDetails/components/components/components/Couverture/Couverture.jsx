/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    Col,
    Modal,
    ModalHeader,
    ModalBody,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import utils from '../../../../../../../../common/utils/businessUtils';
import Constants from '../../../../../../../../common/utils/Constants';
import PrestationServiceUtils from '../../../../../BeneficiaryDetails/components/PrestationServiceUtils';
import HistoPeriodePrioriteDroits from './HistoPeriodePrioriteDroits';
import HistoPeriodeRemboursement from './HistoPeriodeRemboursement';
import DateRangePresenter from '../../../../../../../../common/utils/DateRangePresenter';
import DateUtils from '../../../../../../../../common/utils/DateUtils';
import dateConstants from '../../../../Constants';
import styleCss from '../style.module.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const formatUniteTaux = (code) => {
    if (!code || code === 'XX') {
        return '';
    }
    return `typeTaux_${code}`;
};
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class Couverture extends Component {
    constructor(props) {
        super(props);

        this.state = {
            modalPrioriteDroit: false,
            historiquePrioriteDroits: null,
            modalRemboursement: false,
            historiqueRemboursements: null,
        };
    }

    togglePrioriteDroits(priorites) {
        const { modalPrioriteDroit } = this.state;
        this.setState({
            modalPrioriteDroit: !modalPrioriteDroit,
            historiquePrioriteDroits: priorites,
        });
    }

    toggleRemboursements(remboursements) {
        const { modalRemboursement } = this.state;
        this.setState({
            modalRemboursement: !modalRemboursement,
            historiqueRemboursements: remboursements,
        });
    }

    renderModalPrioriteDroits(priorites) {
        return PrestationServiceUtils.renderButtonModal(() => this.togglePrioriteDroits(priorites));
    }

    renderModalRemboursements(remboursements) {
        return PrestationServiceUtils.renderButtonModal(() => this.toggleRemboursements(remboursements));
    }

    render() {
        const { domainCode, domain, t, clientType, selectedStitch, TPOnlineSelected, domainesFromHistoConsoByMaille } =
            this.props;
        const {
            codeGarantie,
            libelleGarantie,
            codeProduit,
            libelleProduit,
            codeExterneProduit,
            libelleExterneProduit,
            naturePrestation,
            prioritesDroit,
            referenceCouverture,
            masqueFormule,
            remboursements,
        } = domain;
        const {
            modalPrioriteDroit,
            historiquePrioriteDroits,
            modalRemboursement,
            historiqueRemboursements,
            tauxValue,
        } = this.state;
        const doNotDisplayGuaranty = selectedStitch === 'mailleDomaineTP';
        const doNotDisplayProduct = doNotDisplayGuaranty || selectedStitch === 'mailleGarantie';
        const doNotDisplayCoverRef = doNotDisplayProduct || selectedStitch === 'mailleProduit';
        let severalPrioritePeriods = false;
        let severalRemboursementPeriods = false;
        if (prioritesDroit.length > 1) {
            severalPrioritePeriods = true;
        }
        if (remboursements.length > 1) {
            severalRemboursementPeriods = true;
        }
        const [firstPriorite] = prioritesDroit || {};
        const [firstRemboursement] = remboursements || {};

        return (
            <Panel
                header={<PanelHeader title={t('beneficiaryRightsDetails.coverRights.title')} />}
                panelTheme="secondary"
                border={false}
            >
                <PanelSection>
                    {!doNotDisplayGuaranty && (
                        <LabelValuePresenter
                            id="right-cover-garanties-comp"
                            label={t('beneficiaryRightsDetails.coverRights.garantiesComp')}
                            value={utils.getLibelleConverter(codeGarantie, libelleGarantie)}
                        />
                    )}
                    {clientType !== Constants.TOPOLOGY_INSURER && !doNotDisplayCoverRef && (
                        <LabelValuePresenter
                            id="right-cover-ref"
                            label={t('beneficiaryRightsDetails.coverRights.refCover')}
                            value={referenceCouverture}
                            defaultValue="-"
                        />
                    )}
                    {!doNotDisplayProduct && (
                        <LabelValuePresenter
                            id="right-cover-product"
                            label={t('beneficiaryRightsDetails.coverRights.product')}
                            value={utils.getLibelleConverter(codeProduit, libelleProduit)}
                        />
                    )}
                    {!doNotDisplayProduct && (
                        <LabelValuePresenter
                            id="right-cover-ref"
                            label={t('beneficiaryRightsDetails.coverRights.externalProduct')}
                            value={utils.getLibelleConverter(codeExterneProduit, libelleExterneProduit)}
                        />
                    )}
                    {naturePrestation && clientType === Constants.TOPOLOGY_INSURER && (
                        <LabelValuePresenter
                            id="right-nature-of-service"
                            label={t('beneficiaryRightsDetails.coverRights.natureOfPrestation')}
                            value={naturePrestation}
                        />
                    )}
                    {firstPriorite &&
                        firstPriorite.priorites.map((priorite) => (
                            <div key={priorite.code}>
                                <LabelValuePresenter
                                    id={`right-item-${domainCode}-priority`}
                                    label={t('beneficiaryRightsDetails.coverRights.priority')}
                                    value={priorite.code}
                                />
                            </div>
                        ))}
                    {firstPriorite && firstPriorite.periode != null && (
                        <div>
                            <Row className="ml-4">
                                {domainesFromHistoConsoByMaille && <Col xs={2} />}
                                <Col
                                    xs={domainesFromHistoConsoByMaille ? 5 : 9}
                                    className={domainesFromHistoConsoByMaille ? 'pt-1 mr-4' : 'pt-1'}
                                >
                                    <DateRangePresenter
                                        start={DateUtils.formatDisplayDate(
                                            firstPriorite.periode.debut,
                                            dateConstants.DEFAULT_DATE,
                                        )}
                                        end={DateUtils.formatDisplayDate(
                                            firstPriorite.periode.fin,
                                            dateConstants.DEFAULT_DATE,
                                        )}
                                        inline
                                    />
                                </Col>
                                {severalPrioritePeriods && (
                                    <Col xs={1}>{this.renderModalPrioriteDroits(prioritesDroit)}</Col>
                                )}
                            </Row>
                        </div>
                    )}
                    {(!TPOnlineSelected || clientType !== Constants.TOPOLOGY_INSURER) &&
                        (masqueFormule != null ? (
                            <LabelValuePresenter
                                id="right-formula-mask"
                                label={t('beneficiaryRightsDetails.formulaMask')}
                                value={masqueFormule}
                            />
                        ) : (
                            <div>
                                {firstRemboursement &&
                                    firstRemboursement.remboursements.map((remboursement) => (
                                        <LabelValuePresenter
                                            id={`right-item-${domainCode}-rate`}
                                            label={t('beneficiaryRightsDetails.coverRights.taux')}
                                            value={utils.getLibelleConverterForRate(
                                                remboursement.tauxRemboursement,
                                                formatUniteTaux(remboursement.uniteTauxRemboursement),
                                            )}
                                        />
                                    ))}
                                {firstRemboursement && firstRemboursement.periode != null && (
                                    <div>
                                        <Row className="ml-4">
                                            {domainesFromHistoConsoByMaille && <Col xs={2} />}
                                            <Col
                                                xs={domainesFromHistoConsoByMaille ? 5 : 9}
                                                className={domainesFromHistoConsoByMaille ? 'pt-1 mr-4' : 'pt-1'}
                                            >
                                                <DateRangePresenter
                                                    start={DateUtils.formatDisplayDate(
                                                        firstRemboursement.periode.debut,
                                                        dateConstants.DEFAULT_DATE,
                                                    )}
                                                    end={DateUtils.formatDisplayDate(
                                                        firstRemboursement.periode.fin,
                                                        dateConstants.DEFAULT_DATE,
                                                    )}
                                                    inline
                                                />
                                            </Col>
                                            {severalRemboursementPeriods && (
                                                <Col xs={1}>{this.renderModalRemboursements(remboursements)}</Col>
                                            )}
                                        </Row>
                                    </div>
                                )}
                            </div>
                        ))}
                    {historiquePrioriteDroits && (
                        <Modal
                            size="md"
                            isOpen={modalPrioriteDroit}
                            toggle={() => this.togglePrioriteDroits()}
                            backdrop="static"
                            className={styleCss['modal-border']}
                        >
                            <ModalHeader toggle={() => this.togglePrioriteDroits()}>
                                {t('historiquePrioriteDroits.header')}
                            </ModalHeader>
                            <ModalBody>
                                <HistoPeriodePrioriteDroits historique={historiquePrioriteDroits} />
                            </ModalBody>
                        </Modal>
                    )}
                    {historiqueRemboursements && (
                        <Modal
                            size="md"
                            isOpen={modalRemboursement}
                            toggle={() => this.toggleRemboursements()}
                            backdrop="static"
                            className={styleCss['modal-border']}
                        >
                            <ModalHeader toggle={() => this.toggleRemboursements()}>
                                {t('historiqueRemboursements.header', { code: tauxValue })}
                            </ModalHeader>
                            <ModalBody>
                                <HistoPeriodeRemboursement historique={historiqueRemboursements} />
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

Couverture.propTypes = {
    t: PropTypes.func,
    domain: PropTypes.shape(),
    clientType: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Couverture;
