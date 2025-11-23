/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Panel, Row, Col, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';

@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class GTLotSummaryComponent extends Component {
    render() {
        const { t, concatLots, concatGTs } = this.props;
        return (
            <Panel border={false} panelTheme="secondary">
                <h4 className="text-colored-mix-0 pl-2">{t('parametersTPContrat.lotGT_summary')}</h4>
                {this.props.concatLots && (
                    <Row className="pl-2">
                        <Col xs="12">
                            <LabelValuePresenter
                                id="selectedLots"
                                label={t('parametersTPContrat.selected_lots')}
                                value={concatLots}
                                inline
                            />
                        </Col>
                    </Row>
                )}
                {this.props.concatGTs && (
                    <Row className="pl-2">
                        <Col xs="12">
                            <LabelValuePresenter
                                id="selectedGTs"
                                label={t('parametersTPContrat.selected_gts')}
                                value={concatGTs}
                                inline
                            />
                        </Col>
                    </Row>
                )}
            </Panel>
        );
    }
}

GTLotSummaryComponent.propTypes = {
    t: PropTypes.func,
    concatLots: PropTypes.string,
    concatGTs: PropTypes.string,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default GTLotSummaryComponent;
