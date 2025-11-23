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
class SituationsParticulieres extends Component {
    getColumns() {
        return [
            this.generateCommonColumn('code', 150),
            this.generateCommonColumn('debut', 150),
            this.generateCommonColumn('fin', 150),
        ];
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`situationsParticulieres.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
        };
    }

    render() {
        const { situationsParticulieres } = this.props;
        const data = situationsParticulieres.map((item) => ({
            ...item,
            debut: DateUtils.formatDisplayDate(item.periode.debut),
            fin: DateUtils.formatDisplayDate(item.periode.fin),
        }));
        return (
            <Row>
                <Col className="ml-3 pl-3 mr-3 mb-1">
                    <Row>
                        <CgdTable
                            id="situationsParticulieresTable"
                            data={data}
                            columns={this.getColumns()}
                            showPageSizeOptions={(situationsParticulieres || []).length > 5}
                            showPagination={(situationsParticulieres || []).length > 5}
                            initialPageSize={
                                (situationsParticulieres || []).length > 5 ? 5 : (situationsParticulieres || []).length
                            }
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
SituationsParticulieres.propTypes = propTypes;
// Add default props
SituationsParticulieres.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default SituationsParticulieres;
