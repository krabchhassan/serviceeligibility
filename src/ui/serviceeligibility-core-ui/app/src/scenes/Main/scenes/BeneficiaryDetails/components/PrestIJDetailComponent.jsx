/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import autobind from 'autobind-decorator';
import PrestIJRightItem from './PrestIJRightItem';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class PrestIJDetailComponent extends Component {
    @autobind
    selectAssure() {
        const { prestIJDetails, nirCode, nirCle } = this.props;
        const detailsToDisplay = [];
        (prestIJDetails.assures || []).map((assure) =>
            nirCode === assure.nir.code && nirCle === assure.nir.cle ? detailsToDisplay.push(assure) : null,
        );
        return detailsToDisplay;
    }

    @autobind
    renderAssure(assure) {
        const { prestIJDetails, nirCode } = this.props;
        return (
            <Fragment>
                {(assure.droits || []).map((droit) => (
                    <PrestIJRightItem prestIJDetails={prestIJDetails} droit={droit} nirCode={nirCode} />
                ))}
            </Fragment>
        );
    }

    render() {
        const detailsToDisplay = this.selectAssure();
        return <Fragment>{(detailsToDisplay || []).map((detail) => this.renderAssure(detail))}</Fragment>;
    }
}
PrestIJDetailComponent.propTypes = {
    prestIJDetails: PropTypes.shape(),
    nirCode: PropTypes.string,
    nirCle: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default PrestIJDetailComponent;
