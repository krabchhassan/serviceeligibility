/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import 'react-table/react-table.css';
import { Row, Col, Panel, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import fluxUtils from '../../../fluxUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class ResultSearchFilesSubComponent extends Component {
    render() {
        const { data, circuits, isReceivedFiles, t } = this.props;
        const { numAMCEchange, batch, infoFichier, operateurPrincipal, emetteurDroits, codeCircuit } = data || {};
        const circuitLabel = fluxUtils.getCircuitLibelle(circuits, codeCircuit);
        const safeInfoFichier = infoFichier || {};

        return (
            <Panel>
                <PanelSection>
                    <Row>
                        <Col xs={4}>
                            <LabelValuePresenter
                                id="resul-search-modal-amc-exchange"
                                label={t('fileTrackingResult.modal.amcExchange')}
                                value={numAMCEchange}
                            />
                        </Col>
                        <Col xs={5}>
                            <LabelValuePresenter
                                id="resul-search-modal-batch"
                                label={t('fileTrackingResult.modal.batch')}
                                value={batch}
                            />
                        </Col>
                        {isReceivedFiles ? (
                            <Col>
                                <LabelValuePresenter
                                    id="resul-search-modal-reject"
                                    label={t('fileTrackingResult.modal.reject')}
                                    value={safeInfoFichier.codeRejet}
                                />
                            </Col>
                        ) : (
                            <Col>
                                <LabelValuePresenter
                                    id="resul-search-modal-secondary-criteria"
                                    label={t('fileTrackingResult.modal.secondaryCriteria')}
                                    value={safeInfoFichier.critereSecondaire}
                                />
                            </Col>
                        )}
                    </Row>
                    <Row>
                        <Col xs={4}>
                            <LabelValuePresenter
                                id="resul-search-modal-operator"
                                label={t('fileTrackingResult.modal.mainOperator')}
                                value={operateurPrincipal}
                            />
                        </Col>
                        <Col xs={5}>
                            <LabelValuePresenter
                                id="resul-search-modal-version"
                                label={t('fileTrackingResult.modal.version')}
                                value={safeInfoFichier.versionFichier}
                            />
                        </Col>
                        {isReceivedFiles ? (
                            <Col>
                                <LabelValuePresenter
                                    id="resul-search-modal-reason"
                                    label={t('fileTrackingResult.modal.reason')}
                                    value={safeInfoFichier.messageRejet}
                                />
                            </Col>
                        ) : (
                            <Col>
                                <LabelValuePresenter
                                    id="resul-search-modal-detailed-secondary-criteria"
                                    label={t('fileTrackingResult.modal.detailedSecondaryCriteria')}
                                    value={safeInfoFichier.critereSecondaireDetaille}
                                />
                            </Col>
                        )}
                    </Row>
                    <Row>
                        <Col xs={4}>
                            <LabelValuePresenter
                                id="resul-search-modal-rights-emitter"
                                label={t('fileTrackingResult.modal.rightsEmitter')}
                                value={emetteurDroits}
                            />
                        </Col>
                        <Col xs={5}>
                            <LabelValuePresenter
                                id="resul-search-modal-arl-name-file"
                                label={t('fileTrackingResult.modal.arlNameFile')}
                                value={safeInfoFichier.nomFichierARL}
                            />
                            <LabelValuePresenter
                                id="resul-search-modal-circuit"
                                label={t('fileTrackingResult.modal.circuit')}
                                value={circuitLabel}
                            />
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }
}

ResultSearchFilesSubComponent.propTypes = {
    t: PropTypes.func,
    data: PropTypes.shape(),
    circuits: PropTypes.arrayOf(PropTypes.shape()),
    isReceivedFiles: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ResultSearchFilesSubComponent;
