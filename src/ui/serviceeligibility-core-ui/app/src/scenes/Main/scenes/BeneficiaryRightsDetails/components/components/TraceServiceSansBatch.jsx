/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Panel, PanelHeader, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import CommonSpinner from '../../../../../../common/components/CommonSpinner/CommonSpinner';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class TraceServiceSansBatch extends Component {
    render() {
        const { service, t } = this.props;
        const canRender = service;
        if (!canRender) {
            return <CommonSpinner />;
        }

        return (
            <Panel
                panelTheme="secondary"
                header={
                    <PanelHeader
                        title={service.codeService}
                        context={t('beneficiaryRightsDetails.trace.serviceActive')}
                    />
                }
            />
        );
    }
}

TraceServiceSansBatch.propTypes = {
    t: PropTypes.func,
    service: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TraceServiceSansBatch;
