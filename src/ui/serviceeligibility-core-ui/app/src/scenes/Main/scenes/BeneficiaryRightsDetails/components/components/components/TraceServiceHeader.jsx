/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Status, PanelHeader, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import Constants from '../../../Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const labelContextByserviceType = {
    [Constants.typeService.extraction]: 'beneficiaryRightsDetails.trace.nbExtractions',
    [Constants.typeService.traitement]: 'beneficiaryRightsDetails.trace.nbTreatments',
    [Constants.typeService.consolidationExtraction]: 'beneficiaryRightsDetails.trace.nbConsolidations',
    [Constants.typeService.consolidation]: 'beneficiaryRightsDetails.trace.nbConsolidations',
};

const getSize = (service) => {
    const { typeService, traitements, extractions, consolidations } = service;
    switch (typeService) {
        case Constants.typeService.traitement: {
            return (traitements || []).length;
        }
        case Constants.typeService.extraction: {
            return (extractions || []).length;
        }
        case Constants.typeService.consolidation:
        case Constants.typeService.consolidationExtraction: {
            return (consolidations || []).length;
        }
        default: {
            return 0;
        }
    }
};
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class TraceServiceHeader extends Component {
    render() {
        const { service, t, ...rest } = this.props;
        const { lastExecutionOk, codeService, typeService } = service;
        const labelStatus = t(`beneficiaryRightsDetails.trace.traitement${lastExecutionOk ? 'OK' : 'KO'}`);
        const iconLabel = lastExecutionOk ? 'check-circle-o' : 'warning';
        const context = `${getSize(service)} ${t(labelContextByserviceType[typeService])}`;
        const behavior = lastExecutionOk ? 'success' : 'default';

        return (
            <PanelHeader
                id={`header-${codeService}-${typeService}`}
                title={codeService === 'CARTE-TP' ? 'CARTE-PAPIER' : codeService}
                status={<Status label={labelStatus} behavior={behavior} icon={iconLabel} />}
                context={context}
                {...rest}
            />
        );
    }
}

TraceServiceHeader.propTypes = {
    t: PropTypes.func,
    service: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TraceServiceHeader;
