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
class HistoriqueSuspension extends Component {
    getColumns() {
        return [
            this.generateCommonColumn('debut', 100),
            this.generateCommonColumn('fin', 100),
            this.generateCommonColumn('typeSuspension', 100),
            this.generateCommonColumn('motifSuspension'),
            this.generateCommonColumn('motifLeveeSuspension'),
        ];
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`historiqueSuspension.${key}`), false),
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
            ...item,
            debut: DateUtils.formatDisplayDate(item.periode.debut),
            fin: DateUtils.formatDisplayDate(item.periode.fin),
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
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

// Add prop types
HistoriqueSuspension.propTypes = propTypes;
// Add default props
HistoriqueSuspension.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HistoriqueSuspension;
