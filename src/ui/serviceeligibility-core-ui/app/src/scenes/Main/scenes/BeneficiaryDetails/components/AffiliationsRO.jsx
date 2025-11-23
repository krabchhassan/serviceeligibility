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
class AffiliationsRO extends Component {
    getColumns() {
        return [
            this.generateCommonColumn('nirCode', 120),
            this.generateCommonColumn('nirCle', 40),
            this.generateCommonColumn('debut', 90),
            this.generateCommonColumn('fin', 90),
            this.generateCommonColumn('rattachementROCodeRegime', 60),
            this.generateCommonColumn('rattachementROCodeCaisse', 50),
            this.generateCommonColumn('rattachementROCodeCentre', 50),
        ];
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`affiliationsRO.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
        };
    }

    render() {
        const { list } = this.props;
        const data = list.map((item) => ({
            nirCode: (item.nir || {}).code,
            nirCle: (item.nir || {}).cle,
            debut: DateUtils.formatDisplayDate(item.periode.debut),
            fin: DateUtils.formatDisplayDate(item.periode.fin),
            rattachementROCodeRegime: (item.rattachementRO || {}).codeRegime,
            rattachementROCodeCaisse: (item.rattachementRO || {}).codeCaisse,
            rattachementROCodeCentre: (item.rattachementRO || {}).codeCentre,
        }));
        return (
            <Row>
                <Col className="ml-3 pl-3 mr-3 mb-1">
                    <Row>
                        <CgdTable
                            id="domainTable"
                            data={data}
                            columns={this.getColumns()}
                            showPageSizeOptions={(list || []).length > 5}
                            showPagination={(list || []).length > 5}
                            initialPageSize={(list || []).length > 5 ? 5 : (list || []).length}
                        />
                    </Row>
                </Col>
            </Row>
        );
    }
}

// Prop types
const propTypes = {
    list: PropTypes.arrayOf(PropTypes.shape()),
};
// Default props
const defaultProps = {};

// Add prop types
AffiliationsRO.propTypes = propTypes;
// Add default props
AffiliationsRO.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default AffiliationsRO;
