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
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import CommonSpinner from '../../../../../../../common/components/CommonSpinner/CommonSpinner';
import BasicTable from '../../../../../../../common/components/BasicTable/BasicTable';
import businessUtils from '../../../../../../../common/utils/businessUtils';
import TraceRejet from './TraceRejet';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class TraceServiceExtractionItem extends Component {
    buildReturnsTable(retours) {
        if (!retours) {
            return null;
        }

        const { t } = this.props;
        const headers = [
            t('beneficiaryRightsDetails.trace.dateTreatment'),
            t('beneficiaryRightsDetails.trace.fileName'),
            t('beneficiaryRightsDetails.trace.reject'),
        ];

        const rowsData = retours.map((retour, index) => ({
            key: index,
            values: [retour.dateExecution, retour.nomFichier, retour.rejet],
        }));

        return <BasicTable headers={headers} rowsData={rowsData} />;
    }

    buildEmitterCustomerLine() {
        const { t, extraction, isAlsoConso } = this.props;
        const { emetteur, client } = extraction;
        return (
            <Fragment>
                {isAlsoConso && (
                    <Col>
                        <LabelValuePresenter
                            id="trace-extraction-emitter"
                            label={t('beneficiaryRightsDetails.trace.client')}
                            value={client}
                        />
                    </Col>
                )}
                <Col>
                    <LabelValuePresenter
                        id="trace-extraction-emitter"
                        label={t('beneficiaryRightsDetails.trace.numEmetteur')}
                        value={emetteur}
                    />
                </Col>
            </Fragment>
        );
    }

    buildFileDataLine() {
        const { t, extraction } = this.props;
        const { nomFichier, numeroFichier } = extraction;

        return (
            <Fragment>
                <Col>
                    <LabelValuePresenter
                        id="trace-extraction-file-name"
                        label={t('beneficiaryRightsDetails.trace.fileName')}
                        value={nomFichier}
                    />
                </Col>
                <Col>
                    <LabelValuePresenter
                        id="trace-extraction-name-file"
                        label={t('beneficiaryRightsDetails.trace.fileNumber')}
                        value={numeroFichier}
                    />
                </Col>
            </Fragment>
        );
    }

    render() {
        const { t, isAlsoConso, extraction } = this.props;
        const canRender = extraction;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const { erreur, dateExecution, retours, nomFichierARL } = extraction;
        const context = businessUtils.buildContextHeaderSubPanel(dateExecution, erreur, 'E', t);
        const panelBehavior = isAlsoConso ? 'secondary' : 'main';

        return (
            <Panel panelTheme={panelBehavior} header={<PanelHeader context={context} />}>
                <PanelSection>
                    <Row>
                        {this.buildEmitterCustomerLine()}
                        {!isAlsoConso && this.buildFileDataLine()}
                    </Row>
                    {isAlsoConso && <Row>{this.buildFileDataLine()}</Row>}
                </PanelSection>
                {retours && !isAlsoConso && (
                    <PanelSection title={t('beneficiaryRightsDetails.trace.returns').toUpperCase()}>
                        {this.buildReturnsTable(retours)}
                    </PanelSection>
                )}
                {erreur && (
                    <PanelSection>
                        <TraceRejet rejet={erreur} arl={nomFichierARL} />
                    </PanelSection>
                )}
            </Panel>
        );
    }
}

TraceServiceExtractionItem.propTypes = {
    t: PropTypes.func,
    extraction: PropTypes.shape(),
    isAlsoConso: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TraceServiceExtractionItem;
