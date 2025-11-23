/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';

import { BreadcrumbPart, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import HomeBreadcrumb from '../../Home/components/HomeBreadcrumb';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['breadcrumb'])
class DeclarantBreadcrumb extends Component {
    render() {
        const { breadcrumb } = this.props;
        return <BreadcrumbPart label={breadcrumb} parentPart={<HomeBreadcrumb />} />;
    }
}

DeclarantBreadcrumb.propTypes = {
    breadcrumb: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default DeclarantBreadcrumb;
