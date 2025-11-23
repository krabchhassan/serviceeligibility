/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Col, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';

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
class Address extends Component {
    render() {
        const { t, address } = this.props;
        const isAddressEmpty = !address || (!address.ligne1 && !address.ligne4 && !address.ligne6);
        return (
            <Row>
                <Col xs="auto" className="col pr-1">
                    <CgIcon fixedWidth name="map-marker" size="xs" className="text-default lt" id="adress" />
                </Col>
                <Col className="pl-0">
                    {isAddressEmpty && <div>{t('beneficiaryDetails.servicePrestation.adresseManquante')}</div>}
                    {!isAddressEmpty && address.ligne1 && <div>{address.ligne1}</div>}
                    {!isAddressEmpty && address.ligne2 && <div>{address.ligne2}</div>}
                    {!isAddressEmpty && address.ligne3 && <div>{address.ligne3}</div>}
                    {!isAddressEmpty && address.ligne4 && <div>{address.ligne4}</div>}
                    {!isAddressEmpty && address.ligne5 && <div>{address.ligne5}</div>}
                    {!isAddressEmpty && address.ligne6 && <div>{address.ligne6}</div>}
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
Address.propTypes = propTypes;
// Add default props
Address.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Address;
