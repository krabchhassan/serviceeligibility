/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { BreadcrumbPart, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['breadcrumb'])
class ParametrageBreadcrumb extends Component {
    render() {
        const { t } = this.props;
        return <BreadcrumbPart label={t('breadcrumb.parameters')} />;
    }
}
ParametrageBreadcrumb.propTypes = {
    t: PropTypes.func,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParametrageBreadcrumb;
