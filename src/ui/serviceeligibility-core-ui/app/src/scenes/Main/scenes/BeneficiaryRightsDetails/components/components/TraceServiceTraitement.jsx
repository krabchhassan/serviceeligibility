/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import { Panel, PanelHeader, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import CommonSpinner from '../../../../../../common/components/CommonSpinner/CommonSpinner';
import TraceServiceHeader from './components/TraceServiceHeader';
import businessUtils from '../../../../../../common/utils/businessUtils';
import TraceRejet from './components/TraceRejet';
import NoDataPresenter from './components/NoDataPresenter';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class TraceServiceTraitement extends Component {
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

    renderTreatement(traitement, index) {
        const { t } = this.props;
        const { erreur, dateExecution, nbPrioOrigineDifferent, nomFichierARL } = traitement;
        const dateExecutionLabel = businessUtils.formatServiceTime('T', dateExecution, t);
        const rejetLabel = erreur
            ? `${t('beneficiaryRightsDetails.trace.reject')} ${erreur.code} - ${erreur.libelle}`
            : `${t('beneficiaryRightsDetails.trace.noReject')}`;
        const priorityLabel =
            nbPrioOrigineDifferent > 0
                ? `${nbPrioOrigineDifferent} ${t('beneficiaryRightsDetails.trace.nbPriority')}`
                : t('beneficiaryRightsDetails.trace.noPriority');
        const context = `${dateExecutionLabel} / ${rejetLabel} / ${priorityLabel}`;

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

        const { traitements } = service;
        if (!traitements || !traitements.length) {
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
                    {traitements.map((traitement, index) => this.renderTreatement(traitement, index))}
                </PanelSection>
            </Panel>
        );
    }
}

TraceServiceTraitement.propTypes = {
    t: PropTypes.func,
    service: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TraceServiceTraitement;
