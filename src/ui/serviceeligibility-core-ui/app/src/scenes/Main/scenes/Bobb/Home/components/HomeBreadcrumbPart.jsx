/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { PureComponent } from 'react';
import { DesktopBreadcrumbPart } from '@beyond-framework/common-uitoolkit-beyond';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
class HomeBreadcrumbPart extends PureComponent {
    render() {
        return <DesktopBreadcrumbPart path="/" showBreadcrumbIcon />;
    }
}

// Add prop types
HomeBreadcrumbPart.propTypes = {};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HomeBreadcrumbPart;
