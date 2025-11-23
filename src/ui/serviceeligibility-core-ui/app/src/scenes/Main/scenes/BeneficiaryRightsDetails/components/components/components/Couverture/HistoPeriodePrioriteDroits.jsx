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

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class HistoriquePeriode extends Component {
    getColumns() {
        const { t } = this.props;
        return [
            {
                Header: StringUtils.splitIntoDivs(t('historiquePrioriteDroits.periode'), false),
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
                Header: StringUtils.splitIntoDivs(t('historiquePrioriteDroits.priorites'), false),
                accessor: 'priorites',
                id: 'priorites',
                Cell: ({ value }) => (
                    <div>
                        {value.map((priorite) => (
                            <div>
                                <div>
                                    <span className="cgd-comment">
                                        {t('beneficiaryRightsDetails.coverRights.priority')} :
                                    </span>{' '}
                                    {priorite.code}
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
            priorites: item.priorites,
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
