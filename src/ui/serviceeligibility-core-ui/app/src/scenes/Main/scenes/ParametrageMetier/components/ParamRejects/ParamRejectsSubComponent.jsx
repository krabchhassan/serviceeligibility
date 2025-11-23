/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import 'react-table/react-table.css';
import { Row, Col, Panel, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class ParamRejectsSubComponent extends Component {
    render() {
        const { data, t } = this.props;
        const { motif, niveauErreur, typeErreur } = data;
        const errorLevels = {
            F: t('parameters.errorLevel.file'),
            B: t('parameters.errorLevel.beneficiary'),
            D: t('parameters.errorLevel.grants'),
        };

        const errorTypes = {
            S: t('parameters.errorType.structure'),
            R: t('parameters.errorType.repository'),
            M: t('parameters.errorType.matrix'),
        };

        return (
            <Panel>
                <PanelSection>
                    <Row>
                        <Col xs={4} />
                        <Col xs={8}>
                            <LabelValuePresenter
                                id="resul-modal-reject-cause"
                                label={t('parameters.cause')}
                                value={motif}
                                labelTextRight
                                labelPortion={3}
                            />
                        </Col>
                    </Row>
                    <Row>
                        <Col xs={4}>
                            <LabelValuePresenter
                                id="resul-modal-reject-error-level"
                                label={t('parameters.errorLevel.name')}
                                value={errorLevels[niveauErreur]}
                                labelTextRight
                                labelPortion={3}
                            />
                        </Col>
                        <Col xs={8}>
                            <LabelValuePresenter
                                id="resul-modal-reject-error-type"
                                label={t('parameters.errorType.name')}
                                value={errorTypes[typeErreur]}
                                labelTextRight
                                labelPortion={3}
                            />
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }
}

ParamRejectsSubComponent.propTypes = {
    t: PropTypes.func,
    data: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParamRejectsSubComponent;
