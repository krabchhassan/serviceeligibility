/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { CgdTable, Col, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import StringUtils from '../../../../../common/utils/StringUtils';
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
class HistoriqueDateNaissance extends Component {
    getColumns() {
        return [this.generateCommonColumn('dateNaissance', 100), this.generateCommonColumn('rangNaissance', 100)];
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`historiqueDateNaissance.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
        };
    }

    render() {
        const { historique } = this.props;
        const data = historique.map((item) => ({
            dateNaissance: DateUtils.formatDisplayDate(item.dateNaissance),
            rangNaissance: item.rangNaissance,
        }));
        return (
            <Row>
                <Col className="ml-3 pl-3 mr-3 mb-1">
                    <Row>
                        <CgdTable
                            id="domainTable"
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
HistoriqueDateNaissance.propTypes = propTypes;
// Add default props
HistoriqueDateNaissance.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HistoriqueDateNaissance;
