/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { CgdTable, Col, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import businessUtils from '../../../../../common/utils/businessUtils';
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
class InformationsAssure extends Component {
    getColumns() {
        return [
            this.generateCommonColumn('nir', 120),
            this.generateCommonColumn('debut', 80),
            this.generateCommonColumn('fin', 80),
            this.generateCommonColumn('caisse', 100),
        ];
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`informationsAssure.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
        };
    }

    render() {
        const { t, informations } = this.props;
        const data = (informations.affiliationsRO || []).map((item) => ({
            nir: `${businessUtils.formatRO(item.nir.code)} / ${item.nir.cle}`,
            debut: DateUtils.formatDisplayDate(item.periode.debut),
            fin: DateUtils.formatDisplayDate(item.periode.fin),
            caisse: `${(item.rattachementRO || {}).codeRegime || ''} - ${(item.rattachementRO || {}).codeCaisse || ''}`,
        }));
        const nirbenefiaire = informations.nir
            ? `${businessUtils.formatRO(informations.nir.code)} / ${informations.nir.cle}`
            : '-';
        return (
            <Row>
                <Col className="ml-3 pl-3 mr-3 mb-1">
                    <Row>
                        <div>
                            <LabelValuePresenter
                                id="nirbeneficiaire"
                                label={t('informationsAssure.nirBeneficiaire')}
                                value={nirbenefiaire}
                                inline
                            />
                        </div>
                    </Row>
                    {(informations.affiliationsRO || []).length > 0 && (
                        <Fragment>
                            <br />
                            <Row className="cgd-comment">{t('informationsAssure.affiliationsRO')}</Row>
                            <Row>
                                <CgdTable
                                    id="domainTable"
                                    data={data}
                                    columns={this.getColumns()}
                                    showPageSizeOptions={(informations.affiliationsRO || []).length > 5}
                                    showPagination={(informations.affiliationsRO || []).length > 5}
                                    initialPageSize={
                                        (informations.affiliationsRO || []).length > 5
                                            ? 5
                                            : (informations.affiliationsRO || []).length
                                    }
                                />
                            </Row>
                        </Fragment>
                    )}
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
InformationsAssure.propTypes = propTypes;
// Add default props
InformationsAssure.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default InformationsAssure;
