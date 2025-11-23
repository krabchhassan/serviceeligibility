/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Col, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';

/* eslint-disable no-plusplus */
/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class DetailAMCEchange extends Component {
    render() {
        const { t, numEchangeTDB, detailAMCEchange } = this.props;

        return (
            <Row>
                <Col className="ml-3 pl-3 mr-3 mb-1">
                    <LabelValuePresenter
                        id="contract-num-echange(-tdb)"
                        label={t(`beneficiaryRightsDetails.numEchangeTDB`)}
                        value={numEchangeTDB}
                    />
                    <LabelValuePresenter
                        id="contract-detail-amc-echange"
                        label={t(`beneficiaryRightsDetails.detailAMCEchange`)}
                        value={detailAMCEchange.join(', ')}
                    />
                </Col>
            </Row>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

// Add prop types
DetailAMCEchange.propTypes = propTypes;
// Add default props
DetailAMCEchange.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default DetailAMCEchange;
