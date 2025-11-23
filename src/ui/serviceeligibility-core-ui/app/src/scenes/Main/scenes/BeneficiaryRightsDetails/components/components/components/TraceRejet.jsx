/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    Col,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import CommonSpinner from '../../../../../../../common/components/CommonSpinner/CommonSpinner';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class TraceRejet extends Component {
    render() {
        const { t, rejet, arl, noExtraction } = this.props;
        const canRender = rejet;
        if (!canRender) {
            return <CommonSpinner />;
        }
        const { motif, typeErreur, niveauErreur } = rejet;
        const rejetLabel = `${t('beneficiaryRightsDetails.trace.reject')} ${rejet.code} - ${rejet.libelle}`;
        const errorLvlLabel = niveauErreur
            ? `${niveauErreur} - ${t(`beneficiaryRightsDetails.trace.levelReject${niveauErreur}`)}`
            : '';
        return (
            <Panel header={noExtraction ? null : <PanelHeader context={rejetLabel} />}>
                <PanelSection>
                    <Row>
                        <Col>
                            <LabelValuePresenter
                                id="reject-arl-file"
                                label={t('beneficiaryRightsDetails.trace.fichierARL')}
                                value={arl}
                            />
                        </Col>
                        <Col>
                            <LabelValuePresenter
                                id="reject-level-error"
                                label={t(`beneficiaryRightsDetails.trace.levelReject${niveauErreur}`)}
                                value={errorLvlLabel}
                            />
                        </Col>
                    </Row>
                    <Row>
                        <Col>
                            <LabelValuePresenter
                                id="reject-reason-error"
                                label={t('beneficiaryRightsDetails.trace.reason')}
                                value={motif}
                            />
                        </Col>
                        <Col>
                            <LabelValuePresenter
                                id="reject-type-error"
                                label={t('beneficiaryRightsDetails.trace.type')}
                                value={typeErreur}
                            />
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }
}

TraceRejet.propTypes = {
    t: PropTypes.func,
    rejet: PropTypes.shape(),
    arl: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TraceRejet;
