/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    Panel,
    PanelSection,
    Row,
    Col,
    Button,
    Status,
    Modal,
    ModalHeader,
    ModalBody,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import DateUtils from '../../../../../../../common/utils/DateUtils';
import CommonSpinner from '../../../../../../../common/components/CommonSpinner/CommonSpinner';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import DateRangePresenter from '../../../../../../../common/utils/DateRangePresenter';
import AttestationContractDetail from './AttestationContractDetail';
import styleCss from './style.module.scss';
import Constants from '../../../Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class AttestationContract extends Component {
    constructor(props) {
        super(props);

        this.state = {
            modalAttestation: false,
            attestationToOpen: null,
        };
    }

    getPeriodeDroits() {
        const { attestation } = this.props;
        const { periodeDebut, periodeFin } = attestation;

        return (
            <DateRangePresenter
                start={DateUtils.formatDisplayDate(periodeDebut, Constants.DEFAULT_DATE)}
                end={DateUtils.formatDisplayDate(periodeFin, Constants.DEFAULT_DATE)}
            />
        );
    }

    toggleAttestation(attestationToOpen) {
        const { modalAttestation } = this.state;
        this.setState({ modalAttestation: !modalAttestation, attestationToOpen });
    }

    renderModalTitle(status, periodeDroits) {
        const { t, attestation } = this.props;
        const title = t('consolidatedContract.modalAttestationHeader');

        return (
            <Row>
                <Col xs={3} className="pr-0">{`${title}`}</Col>
                <Col xs={4} className="pl-0 pr-0">
                    {periodeDroits}
                </Col>
                {attestation.isLastCarteDemat && (
                    <Col className="pl-0" xs={5}>
                        {status}
                    </Col>
                )}
            </Row>
        );
    }

    renderCarteDemat(status) {
        const { t, attestation } = this.props;
        const title = attestation.isCarteDemat ? t('yes') : t('no');

        return (
            <Row className="no-gutters align-items-center">
                <Col xs="auto" className="pl-4">
                    <span className="d-inline-block">{title}</span>
                </Col>

                {attestation.isLastCarteDemat && (
                    <Col xs="auto" className="pl-2">
                        {status}
                    </Col>
                )}
            </Row>
        );
    }

    render() {
        const { modalAttestation, attestationToOpen } = this.state;
        const { conventions, attestation, t, showItelisCode } = this.props;
        const canRender = attestation && conventions;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const pageTitle = t('consolidatedContract.periode');

        const { details } = attestation;
        const disabledButton = details === undefined;
        const isCartePapier = attestation.isCartePapier ? t('yes') : t('no');
        const status = (
            <Status
                label={t('consolidatedContract.valide')}
                behavior="success"
                tooltip={t('consolidatedContract.validTooltip')}
            />
        );
        const carteDemat = this.renderCarteDemat(status);

        const periodeDroits = this.getPeriodeDroits();

        const dc = DateUtils.parseDate(attestation.dateCreation).format('DD/MM/YYYY HH:mm');
        const dateCreation = dc.slice(0, 10);
        const timeCreation = dc.slice(11, 16);

        return (
            <Panel id="certificate-panel" label={pageTitle} border={false} wrapperClassName="pt-0 pb-0">
                <PanelSection className="pt-0 pb-0">
                    <Row className="ml-0 align-items-center g-0">
                        <Col xs={2} className="p-0 pt-1">
                            {t('consolidatedContract.dateCreationAttestationTP', {
                                date: dateCreation,
                                time: timeCreation,
                            })}
                        </Col>

                        <Col xs={3} className="p-0 pr-4">
                            <LabelValuePresenter
                                id="certification-periodeDroits"
                                label={t('consolidatedContract.periodeDroits')}
                                value={periodeDroits}
                            />
                        </Col>

                        <Col xs={1} className="p-0" />

                        <Col xs={2} className="p-0 text-nowrap">
                            <LabelValuePresenter
                                id="certification-cartePapier"
                                label={t('consolidatedContract.cartePapier')}
                                value={isCartePapier}
                            />
                        </Col>

                        <Col xs={1} className="p-0 text-muted font-italic">
                            {t('consolidatedContract.carteDemat')}:
                        </Col>

                        <Col xs={2} className="p-0 d-flex align-items-center flex-nowrap">
                            <span className=" text-nowrap">{carteDemat}</span>
                        </Col>

                        <Col xs={1} className="p-0 d-flex justify-content-end">
                            <Button
                                id="open-button"
                                outlineNoBorder
                                behavior="primary"
                                disabled={disabledButton}
                                onClick={() => this.toggleAttestation(attestation)}
                            >
                                {`${t('open')} `}
                                <CgIcon name="right-scroll" />
                            </Button>
                        </Col>
                    </Row>

                    {attestationToOpen && (
                        <Modal
                            size="full-width"
                            isOpen={modalAttestation}
                            toggle={() => this.toggleAttestation()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.toggleAttestation()} className={styleCss['modal-header']}>
                                {this.renderModalTitle(status, periodeDroits)}
                            </ModalHeader>
                            <ModalBody>
                                <AttestationContractDetail
                                    attestation={attestationToOpen}
                                    conventions={conventions}
                                    isCartePapier={isCartePapier}
                                    carteDemat={attestation.isCarteDemat ? t('yes') : t('no')}
                                    showItelisCode={showItelisCode}
                                />
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

AttestationContract.propTypes = {
    t: PropTypes.func,
    attestation: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default AttestationContract;
