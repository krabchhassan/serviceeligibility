/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';

import { DesktopBreadcrumbPart, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['breadcrumb'])
class BeneficiariesBreadcrumb extends Component {
    render() {
        const { t } = this.props;
        return <DesktopBreadcrumbPart label={t('breadcrumb.beneficiariesTracking')} path="/beneficiary_tracking" />;
    }
}

BeneficiariesBreadcrumb.propTypes = {
    t: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default BeneficiariesBreadcrumb;
