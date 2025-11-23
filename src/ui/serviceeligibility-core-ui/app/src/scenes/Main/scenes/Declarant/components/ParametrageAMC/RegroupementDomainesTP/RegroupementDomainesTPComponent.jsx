/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    CgdTable,
    Col,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { FieldArray } from 'redux-form';
import Constants from './Constants';
import DateUtils from '../../../../../../../common/utils/DateUtils';
import RegroupementDomainesTPFieldArrayComponent from './RegroupementDomainesTPFieldArrayComponent';
import CreateRegroupementDomainesTPFieldArrayComponent from './CreateRegroupementDomainesTPFieldArrayComponent';
import ParametrageAMCUtils from '../ParametrageAMCUtils';
import StringUtils from '../../../../../../../common/utils/StringUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class RegroupementDomainesTPComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: `${Constants.REGROUPEMENT_DOMAINES_TP_FIELD_ARRAY_NAME}`,
        };
    }

    getColumns() {
        const { t } = this.props;
        return [
            ParametrageAMCUtils.generateCommonColumn('domaineRegroupementTP', '20%', t),
            ParametrageAMCUtils.generateCommonColumn('codesDomainesTP', '35%', t),
            ParametrageAMCUtils.generateBooleanCommonColumn('niveauRemboursementIdentique', '15%', t),
            ParametrageAMCUtils.generateCommonColumn('dateDebut', '15%', t),
            ParametrageAMCUtils.generateCommonColumn('dateFin', '15%', t),
        ];
    }

    getCodesDomainesList(codesDomainesTP) {
        const { domainList } = this.props;
        const codeLabels = Object.values(codesDomainesTP || []).map((codeDomaine) =>
            ParametrageAMCUtils.getElementLabel(codeDomaine, domainList),
        );
        return codeLabels.join(',');
    }

    generateRowsData() {
        const { regroupementDomainesTP, domainList } = this.props;

        const rowsData = Object.values(regroupementDomainesTP || []).map((item, index) => ({
            key: index,
            domaineRegroupementTP: ParametrageAMCUtils.getElementLabel(item.domaineRegroupementTP, domainList),
            codesDomainesTP: StringUtils.splitIntoDivsArrayJoin(this.getCodesDomainesList(item.codesDomainesTP)),
            niveauRemboursementIdentique: item.niveauRemboursementIdentique,
            dateDebut: DateUtils.formatDisplayDate(item.dateDebut),
            dateFin: item.dateFin,
        }));

        return rowsData;
    }

    renderPanelView() {
        const { t, regroupementDomainesTP } = this.props;

        const cols = this.getColumns();
        const rowsData = this.generateRowsData();
        if (regroupementDomainesTP && regroupementDomainesTP.length > 0) {
            return (
                <PanelSection>
                    <Row>
                        <Col className="ml-3 pl-3 mr-3 mb-1">
                            <Row>
                                <CgdTable
                                    id="regroupementDomainesTPTable"
                                    key="regroupementDomainesTPTable"
                                    data={rowsData}
                                    columns={cols}
                                    showPageSizeOptions={false}
                                    showPagination={false}
                                    pageSize={regroupementDomainesTP.length}
                                />
                            </Row>
                        </Col>
                    </Row>
                </PanelSection>
            );
        }
        return <div className="ml-2">{t('declarant.parametrageAMC.noRegroupementDomainesTP')}</div>;
    }

    renderPanelEdit() {
        const { regroupementDomainesTP, isEdit, isCopy, domainList } = this.props;
        const { name } = this.state;

        return (
            <div className="m-3">
                {isEdit ? (
                    <FieldArray
                        key={name}
                        name={name}
                        component={RegroupementDomainesTPFieldArrayComponent}
                        regroupementDomainesTP={regroupementDomainesTP}
                        domainList={domainList}
                    />
                ) : (
                    <FieldArray
                        key={name}
                        name={name}
                        component={CreateRegroupementDomainesTPFieldArrayComponent}
                        regroupementDomainesTP={regroupementDomainesTP}
                        domainList={domainList}
                        isCopy={isCopy}
                    />
                )}
            </div>
        );
    }

    render() {
        const { t, isCreate, isCopy, isEdit } = this.props;

        return (
            <Panel
                header={<PanelHeader title={t('declarant.regroupementDomainesTPTitle')} />}
                id="regroupementDomainesTP"
            >
                <PanelSection>
                    {isCreate || isEdit || isCopy ? this.renderPanelEdit() : this.renderPanelView()}
                </PanelSection>
            </Panel>
        );
    }
}

RegroupementDomainesTPComponent.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isCopy: PropTypes.bool,
    isEdit: PropTypes.bool,
    regroupementDomainesTP: PropTypes.arrayOf(PropTypes.shape()),
    domainList: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default RegroupementDomainesTPComponent;
