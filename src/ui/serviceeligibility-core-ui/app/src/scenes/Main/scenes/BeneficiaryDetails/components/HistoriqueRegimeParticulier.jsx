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
class HistoriqueRegimeParticulier extends Component {
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
            Header: StringUtils.splitIntoDivs(t(`historiqueRegime.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
        };
    }

    render() {
        const { informations } = this.props;
        const data = informations.map((item) => ({
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
                            showPageSizeOptions={(informations || []).length > 5}
                            showPagination={(informations || []).length > 5}
                            initialPageSize={(informations || []).length > 5 ? 5 : (informations || []).length}
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
HistoriqueRegimeParticulier.propTypes = propTypes;
// Add default props
HistoriqueRegimeParticulier.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default HistoriqueRegimeParticulier;
