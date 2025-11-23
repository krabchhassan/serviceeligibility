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
import DateUtils from '../../../../../common/utils/DateUtils';
import DestinatairePrestation from './DestinatairePrestation';

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
class HistoriqueDestinatairesPrestation extends Component {
    render() {
        const { t, historique } = this.props;
        return historique.map((item, index) => {
            const { periode } = item || {};
            const key = `destinatairePrestation${index}`;
            return (
                <Col md="6" key={key}>
                    <Card className="primary primary mb-4" key={key}>
                        <CardHeader>
                            <span>
                                <span>{t('beneficiaryDetails.servicePrestation.destRelevePrestation')} </span>
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
                                <DestinatairePrestation firstDestinatairesRelevePrestations={item} />
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

// Add prop types
HistoriqueDestinatairesPrestation.propTypes = propTypes;
// Add default props
HistoriqueDestinatairesPrestation.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HistoriqueDestinatairesPrestation;
