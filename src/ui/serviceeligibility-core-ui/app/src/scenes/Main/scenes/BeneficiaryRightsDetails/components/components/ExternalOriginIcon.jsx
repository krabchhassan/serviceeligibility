/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Tooltip, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class ExternalOriginIcon extends Component {
    render() {
        const { tooltipId, size, t } = this.props;

        return (
            <Fragment>
                <CgIcon id={tooltipId} name="shuffle" size={size || '2x'} />
                <Tooltip target={tooltipId} placement="top">
                    {t(`beneficiaryRightsDetails.tooltipOrigineExterne`)}
                </Tooltip>
            </Fragment>
        );
    }
}

ExternalOriginIcon.propTypes = {
    t: PropTypes.func,
    tooltipId: PropTypes.string,
    size: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ExternalOriginIcon;
