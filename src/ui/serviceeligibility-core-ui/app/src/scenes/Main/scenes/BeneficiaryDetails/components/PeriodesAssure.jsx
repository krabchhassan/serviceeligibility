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
class PeriodesAssure extends Component {
    getColumns() {
        return [this.generateCommonColumn('debut', 150), this.generateCommonColumn('fin', 150)];
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`periodesAssure.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
        };
    }

    render() {
        const { periodesAssure } = this.props;
        const data = periodesAssure.map((item) => ({
            ...item,
            debut: DateUtils.formatDisplayDate(item.debut),
            fin: DateUtils.formatDisplayDate(item.fin),
        }));
        return (
            <Row>
                <Col className="ml-3 pl-3 mr-3 mb-1">
                    <Row>
                        <CgdTable
                            id="periodesAssureTable"
                            data={data}
                            columns={this.getColumns()}
                            showPageSizeOptions={(periodesAssure || []).length > 5}
                            showPagination={(periodesAssure || []).length > 5}
                            initialPageSize={(periodesAssure || []).length > 5 ? 5 : (periodesAssure || []).length}
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
PeriodesAssure.propTypes = propTypes;
// Add default props
PeriodesAssure.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default PeriodesAssure;
