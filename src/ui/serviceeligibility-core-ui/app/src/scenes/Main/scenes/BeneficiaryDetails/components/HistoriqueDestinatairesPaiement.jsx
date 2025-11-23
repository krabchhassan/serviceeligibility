/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    Card,
    CardBlock,
    CardHeader,
    CardText,
    Col,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import DestinatairePaiement from './DestinatairePaiement';
import DateUtils from '../../../../../common/utils/DateUtils';

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
class HistoriqueDestinatairesPaiement extends Component {
    render() {
        const { t, historique } = this.props;
        return historique.map((item, index) => {
            const { periode } = item || {};
            const key = `destinatairePaiement${index}`;
            return (
                <Col md="6" key={key}>
                    <Card className="primary mb-4" key={key}>
                        <CardHeader>
                            <span>
                                <span>{t('beneficiaryDetails.servicePrestation.destPaiement')} </span>
                                <span className="cgd-comment">
                                    {' '}
                                    {t('beneficiaryDetails.servicePrestation.periodeGarantieDebut')}{' '}
                                    {DateUtils.transformDateForDisplay((periode || {}).debut)}{' '}
                                </span>
                                <span className="cgd-comment">
                                    {t('beneficiaryDetails.servicePrestation.periodeGarantieFin')}{' '}
                                    {DateUtils.transformDateForDisplay((periode || {}).fin)}
                                </span>
                            </span>
                        </CardHeader>
                        <CardBlock>
                            <CardText>
                                <DestinatairePaiement firstDestinatairesPaiements={item} />
                            </CardText>
                        </CardBlock>
                    </Card>
                </Col>
            );
        });
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    historique: PropTypes.arrayOf(PropTypes.shape()),
};
// Default props
const defaultProps = {};

HistoriqueDestinatairesPaiement.propTypes = propTypes;
// Add default props
HistoriqueDestinatairesPaiement.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HistoriqueDestinatairesPaiement;
