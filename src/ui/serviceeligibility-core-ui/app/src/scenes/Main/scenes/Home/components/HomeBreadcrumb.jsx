/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import {
    BreadcrumbPart,
    DesktopBreadcrumbPart,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['breadcrumb'])
class HomeBreadcrumb extends Component {
    render() {
        return <BreadcrumbPart parentPart={<DesktopBreadcrumbPart showBreadcrumbHomeIcon path="/" />} />;
    }
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HomeBreadcrumb;
