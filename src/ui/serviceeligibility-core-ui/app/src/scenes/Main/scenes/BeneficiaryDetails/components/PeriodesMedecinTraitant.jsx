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
class PeriodesMedecinTraitant extends Component {
    getColumns() {
        return [this.generateCommonColumn('debut', 150), this.generateCommonColumn('fin', 150)];
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`periodesMedecinTraitant.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
        };
    }

    render() {
        const { periodesMedecinTraitant } = this.props;
        const data = periodesMedecinTraitant.map((item) => ({
            ...item,
            debut: DateUtils.formatDisplayDate(item.debut),
            fin: DateUtils.formatDisplayDate(item.fin),
        }));
        return (
            <Row>
                <Col className="ml-3 pl-3 mr-3 mb-1">
                    <Row>
                        <CgdTable
                            id="periodesMedecinTraitantTable"
                            data={data}
                            columns={this.getColumns()}
                            showPageSizeOptions={(periodesMedecinTraitant || []).length > 5}
                            showPagination={(periodesMedecinTraitant || []).length > 5}
                            initialPageSize={
                                (periodesMedecinTraitant || []).length > 5 ? 5 : (periodesMedecinTraitant || []).length
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
PeriodesMedecinTraitant.propTypes = propTypes;
// Add default props
PeriodesMedecinTraitant.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default PeriodesMedecinTraitant;
