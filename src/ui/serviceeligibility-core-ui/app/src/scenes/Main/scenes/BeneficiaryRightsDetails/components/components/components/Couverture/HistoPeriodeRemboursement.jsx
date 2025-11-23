/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { CgdTable, Col, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import StringUtils from '../../../../../../../../common/utils/StringUtils';
import DateUtils from '../../../../../../../../common/utils/DateUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const formatUniteTaux = (code) => {
    if (!code || code === 'XX') {
        return '';
    }
    return `typeTaux_${code}`;
};
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class HistoriquePeriode extends Component {
    getColumns() {
        const { t } = this.props;
        return [
            {
                Header: StringUtils.splitIntoDivs(t('historiqueRemboursements.periode'), false),
                accessor: 'periode',
                id: 'periode',
                Cell: ({ value }) => (
                    <div>
                        {DateUtils.formatDisplayDate(value.debut)} - {DateUtils.formatDisplayDate(value.fin)}
                    </div>
                ),
                width: 100,
                disableSortBy: true,
            },
            {
                Header: StringUtils.splitIntoDivs(t('historiqueRemboursements.remboursements'), false),
                accessor: 'remboursements',
                id: 'remboursements',
                Cell: ({ value }) => (
                    <div>
                        {value.map((remboursement) => (
                            <div>
                                <div>
                                    <span className="cgd-comment">
                                        {t('beneficiaryRightsDetails.coverRights.taux')} :
                                    </span>{' '}
                                    {`${remboursement.tauxRemboursement} ${formatUniteTaux(
                                        remboursement.uniteTauxRemboursement,
                                    )}`}
                                </div>
                            </div>
                        ))}
                    </div>
                ),
                width: 100,
                disableSortBy: true,
            },
        ];
    }

    render() {
        const { historique } = this.props;
        const data = historique.map((item) => ({
            periode: item.periode,
            remboursements: item.remboursements,
        }));
        return (
            <Row>
                <Col className="ml-3 pl-3 mr-3 mb-1">
                    <Row>
                        <CgdTable
                            id="historiquePeriodeTable"
                            data={data}
                            columns={this.getColumns()}
                            showPageSizeOptions={(historique || []).length > 5}
                            showPagination={(historique || []).length > 5}
                            initialPageSize={(historique || []).length > 5 ? 5 : (historique || []).length}
                        />
                    </Row>
                </Col>
            </Row>
        );
    }
}

// Prop types
const propTypes = {
    historique: PropTypes.arrayOf(PropTypes.shape()),
};
// Default props
const defaultProps = {};

// Add prop types
HistoriquePeriode.propTypes = propTypes;
// Add default props
HistoriquePeriode.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HistoriquePeriode;
