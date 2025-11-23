/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import { PanelHeader, Panel, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import CommonSpinner from '../../../../../../common/components/CommonSpinner/CommonSpinner';
import TraceRejet from './components/TraceRejet';
import NoDataPresenter from './components/NoDataPresenter';
import TraceServiceHeader from './components/TraceServiceHeader';
import businessUtils from '../../../../../../common/utils/businessUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class TraceServiceConsolidation extends Component {
    constructor(props) {
        super(props);
        this.state = {
            expanded: false,
        };
    }

    @autobind
    onCollapseClick(event, isExpanded) {
        this.setState({ expanded: isExpanded });
    }

    renderConsolidationItem(consolidation, index) {
        const { t } = this.props;
        const { dateExecution, erreur, nomFichierARL } = consolidation;
        const context = businessUtils.buildContextHeaderSubPanel(dateExecution, erreur, 'C', t);

        return (
            <Panel key={index} header={<PanelHeader context={context} />}>
                {erreur && (
                    <PanelSection>
                        <TraceRejet rejet={erreur} arl={nomFichierARL} />
                    </PanelSection>
                )}
            </Panel>
        );
    }

    render() {
        const { service, t } = this.props;
        const { expanded } = this.state;
        const canRender = service;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const { consolidations } = service;
        if (!consolidations || !consolidations.length) {
            return (
                <NoDataPresenter
                    codeService={service.codeService}
                    noDataLabel={t('beneficiaryRightsDetails.trace.noTreatments')}
                />
            );
        }

        return (
            <Panel
                onCollapseClick={this.onCollapseClick}
                expanded={expanded}
                panelTheme="secondary"
                header={<TraceServiceHeader service={service} />}
            >
                <PanelSection>
                    {consolidations.map((consolidation, index) => this.renderConsolidationItem(consolidation, index))}
                </PanelSection>
            </Panel>
        );
    }
}

TraceServiceConsolidation.propTypes = {
    t: PropTypes.func,
    service: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TraceServiceConsolidation;
