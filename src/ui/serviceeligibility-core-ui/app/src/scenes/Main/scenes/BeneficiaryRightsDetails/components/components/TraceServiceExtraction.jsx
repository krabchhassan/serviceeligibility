/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import { PanelHeader, Panel, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import CommonSpinner from '../../../../../../common/components/CommonSpinner/CommonSpinner';
import TraceServiceExtractionItem from './components/TraceServiceExtractionItem';
import TraceServiceHeader from './components/TraceServiceHeader';
import businessUtils from '../../../../../../common/utils/businessUtils';
import NoDataPresenter from './components/NoDataPresenter';
import TraceRejet from './components/TraceRejet';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class TraceServiceExtraction extends Component {
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

    buildExtraction() {
        const { isAlsoConso, service } = this.props;
        const { extractions } = service;
        return extractions.map((extraction, index) => (
            // eslint-disable-next-line  react/no-array-index-key
            <TraceServiceExtractionItem key={index} extraction={extraction} isAlsoConso={isAlsoConso} />
        ));
    }

    buildConsolidation() {
        const { service } = this.props;
        const { consolidations } = service;
        return consolidations.map((consolidation, index) => this.renderConso(consolidation, index));
    }

    renderConso(consolidation, consoIndex) {
        const { isAlsoConso, t } = this.props;
        const { extractions, dateExecution, erreur, nomFichierARL } = consolidation;
        const context = businessUtils.buildContextHeaderSubPanel(dateExecution, erreur, 'C', t);

        return (
            <Panel key={consoIndex} header={<PanelHeader context={context} />}>
                <PanelSection>
                    {extractions?.length > 0
                        ? extractions.map((extraction, index) => (
                              // eslint-disable-next-line react/no-array-index-key
                              <React.Fragment key={index}>
                                  {erreur && <TraceRejet rejet={erreur} arl={nomFichierARL} noExtraction />}
                                  <TraceServiceExtractionItem extraction={extraction} isAlsoConso={isAlsoConso} />
                              </React.Fragment>
                          ))
                        : erreur && (
                              <PanelSection>
                                  <TraceRejet rejet={erreur} arl={nomFichierARL} noExtraction />
                              </PanelSection>
                          )}
                </PanelSection>
            </Panel>
        );
    }

    render() {
        const { service, isAlsoConso, t } = this.props;
        const { expanded } = this.state;
        const canRender = service;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const { consolidations, extractions, codeService } = service;

        if ((isAlsoConso && !consolidations) || (!isAlsoConso && !extractions)) {
            const displayedLabel = isAlsoConso
                ? t('beneficiaryRightsDetails.trace.noConsolidation')
                : t('beneficiaryRightsDetails.trace.noExtraction');
            return (
                <NoDataPresenter
                    codeService={codeService}
                    noDataLabel={
                        codeService === 'CARTE-TP' ? t('beneficiaryRightsDetails.trace.noTreatments') : displayedLabel
                    }
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
                <PanelSection>{isAlsoConso ? this.buildConsolidation() : this.buildExtraction()}</PanelSection>
            </Panel>
        );
    }
}

TraceServiceExtraction.propTypes = {
    t: PropTypes.func,
    service: PropTypes.shape(),
    isAlsoConso: PropTypes.bool,
};

TraceServiceExtraction.defaultProps = {
    isAlsoConso: false,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TraceServiceExtraction;
