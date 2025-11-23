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
import CodeRenvoiTPFieldArrayComponent from './CodeRenvoiTPFieldArrayComponent';
import CreateCodeRenvoiTPFieldArrayComponent from './CreateCodeRenvoiTPFieldArrayComponent';
import ParametrageAMCUtils from '../ParametrageAMCUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class CodeRenvoiTPComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: `${Constants.CODE_RENVOI_TP_FIELD_ARRAY_NAME}`,
        };
    }

    getColumns() {
        const { t } = this.props;
        return [
            ParametrageAMCUtils.generateCommonColumn('domaineTP', '20%', t),
            ParametrageAMCUtils.generateCommonColumn('reseauSoin', '18%', t),
            ParametrageAMCUtils.generateCommonColumn('codeRenvoi', '32%', t),
            ParametrageAMCUtils.generateCommonColumn('dateDebut', '15%', t),
            ParametrageAMCUtils.generateCommonColumn('dateFin', '15%', t),
        ];
    }

    generateRowsData() {
        const { codeRenvoiTP, reseauxSoin, domainList, returnCodesList } = this.props;

        return Object.values(codeRenvoiTP || []).map((item, index) => ({
            key: index,
            domaineTP: ParametrageAMCUtils.getElementLabel(item.domaineTP, domainList),
            reseauSoin: ParametrageAMCUtils.getElementLabel(item.reseauSoin, reseauxSoin),
            codeRenvoi: ParametrageAMCUtils.getCodeRenvoiLabel(item.codeRenvoi, returnCodesList),
            dateDebut: DateUtils.formatDisplayDate(item.dateDebut),
            dateFin: item.dateFin,
        }));
    }

    renderPanelView() {
        const { t, codeRenvoiTP } = this.props;

        const cols = this.getColumns();
        const rowsData = this.generateRowsData();
        if (codeRenvoiTP && codeRenvoiTP.length > 0) {
            return (
                <PanelSection>
                    <Row>
                        <Col className="ml-3 pl-3 mr-3 mb-1">
                            <Row>
                                <CgdTable
                                    id="codeRenvoiTPTable"
                                    key="codeRenvoiTPTable"
                                    data={rowsData}
                                    columns={cols}
                                    showPageSizeOptions={false}
                                    showPagination={false}
                                    pageSize={codeRenvoiTP.length}
                                />
                            </Row>
                        </Col>
                    </Row>
                </PanelSection>
            );
        }
        return <div className="ml-2">{t('declarant.parametrageAMC.noCodeRenvoiTP')}</div>;
    }

    renderPanelEdit() {
        const { codeRenvoiTP, isEdit, isCopy, domainList, reseauxSoin, returnCodesList } = this.props;
        const { name } = this.state;

        return (
            <div className="m-3">
                {isEdit ? (
                    <FieldArray
                        key={name}
                        name={name}
                        component={CodeRenvoiTPFieldArrayComponent}
                        codeRenvoiTP={codeRenvoiTP}
                        domainList={domainList}
                        reseauxSoin={reseauxSoin}
                        returnCodesList={returnCodesList}
                    />
                ) : (
                    <FieldArray
                        key={name}
                        name={name}
                        component={CreateCodeRenvoiTPFieldArrayComponent}
                        codeRenvoiTP={codeRenvoiTP}
                        domainList={domainList}
                        reseauxSoin={reseauxSoin}
                        isCopy={isCopy}
                        returnCodesList={returnCodesList}
                    />
                )}
            </div>
        );
    }

    render() {
        const { t, isCreate, isCopy, isEdit } = this.props;

        return (
            <Panel header={<PanelHeader title={t('declarant.codeRenvoiTPTitle')} />} id="codeRenvoiTP">
                <PanelSection>
                    {isCreate || isEdit || isCopy ? this.renderPanelEdit() : this.renderPanelView()}
                </PanelSection>
            </Panel>
        );
    }
}

CodeRenvoiTPComponent.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isCopy: PropTypes.bool,
    isEdit: PropTypes.bool,
    codeRenvoiTP: PropTypes.arrayOf(PropTypes.shape()),
    domainList: PropTypes.arrayOf(PropTypes.shape()),
    reseauxSoin: PropTypes.arrayOf(PropTypes.shape()),
    returnCodesList: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default CodeRenvoiTPComponent;
