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
class TriggerRecycleHistory extends Component {
    getColumns() {
        return [this.generateCommonColumn('debut', 100), this.generateCommonColumn('fin', 100)];
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`recycleHistory.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
            Cell: (props) => DateUtils.formatDisplayDateTime(props.value),
        };
    }

    render() {
        const { trigger } = this.props;
        const data = ((trigger || {}).periodes || []).reverse().map((item) => ({
            debut: (item.periode || {}).debut,
            fin: (item.periode || {}).fin,
        }));
        return (
            <Row>
                <Col className="ml-3 pl-3 mr-3 mb-1">
                    <Row>
                        <CgdTable
                            id="domainTable"
                            data={data}
                            columns={this.getColumns()}
                            showPageSizeOptions={(data || []).length > 5}
                            showPagination={(data || []).length > 5}
                            initialPageSize={(data || []).length > 5 ? 5 : (data || []).length}
                        />
                    </Row>
                </Col>
            </Row>
        );
    }
}

// Prop types
const propTypes = {
    trigger: PropTypes.shape(),
};
// Default props
const defaultProps = {};

// Add prop types
TriggerRecycleHistory.propTypes = propTypes;
// Add default props
TriggerRecycleHistory.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TriggerRecycleHistory;
