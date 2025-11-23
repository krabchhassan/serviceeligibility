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
class TrackingBreadcrumb extends Component {
    render() {
        const { t } = this.props;
        return <DesktopBreadcrumbPart label={t('breadcrumb.tracking')} />;
    }
}
TrackingBreadcrumb.propTypes = {
    t: PropTypes.func,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TrackingBreadcrumb;
