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
import ParametrageAMCUtils from '../ParametrageAMCUtils';
import DateUtils from '../../../../../../../common/utils/DateUtils';
import ConventionTPFieldArrayComponent from './ConventionTPFieldArrayComponent';
import CreateConventionTPFieldArrayComponent from './CreateConventionTPFieldArrayComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class ConventionTPComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: `${Constants.CONVENTION_TP_FIELD_ARRAY_NAME}`,
        };
    }

    getColumns() {
        const { t } = this.props;
        return [
            ParametrageAMCUtils.generateCommonColumn('reseauSoin', '20%', t),
            ParametrageAMCUtils.generateCommonColumn('domaineTP', '20%', t),
            ParametrageAMCUtils.generateCommonColumn('conventionCible', '20%', t),
            ParametrageAMCUtils.generateBooleanCommonColumn('concatenation', '10%', t),
            ParametrageAMCUtils.generateCommonColumn('dateDebut', '15%', t),
            ParametrageAMCUtils.generateCommonColumn('dateFin', '15%', t),
        ];
    }

    generateRowsData() {
        const { conventionTP, reseauxSoin, domainList, conventions } = this.props;

        return Object.values(conventionTP || []).map((item, index) => ({
            key: index,
            reseauSoin: ParametrageAMCUtils.getElementLabel(item.reseauSoin, reseauxSoin),
            domaineTP: ParametrageAMCUtils.getElementLabel(item.domaineTP, domainList),
            conventionCible: ParametrageAMCUtils.getConventionLabel(item.conventionCible, conventions),
            concatenation: item.concatenation,
            dateDebut: DateUtils.formatDisplayDate(item.dateDebut),
            dateFin: item.dateFin,
        }));
    }

    renderPanelView() {
        const { t, conventionTP } = this.props;

        const cols = this.getColumns();
        const rowsData = this.generateRowsData();
        if (conventionTP && conventionTP.length > 0) {
            return (
                <PanelSection>
                    <Row>
                        <Col className="ml-3 pl-3 mr-3 mb-1">
                            <Row>
                                <CgdTable
                                    id="conventionTPTable"
                                    key="conventionTPTable"
                                    data={rowsData}
                                    columns={cols}
                                    showPageSizeOptions={false}
                                    showPagination={false}
                                    pageSize={conventionTP.length}
                                />
                            </Row>
                        </Col>
                    </Row>
                </PanelSection>
            );
        }
        return <div className="ml-2">{t('declarant.parametrageAMC.noConventionTP')}</div>;
    }

    renderPanelEdit() {
        const { conventionTP, isEdit, isCopy, domainList, reseauxSoin, conventions } = this.props;
        const { name } = this.state;

        return (
            <div className="m-3">
                {isEdit ? (
                    <FieldArray
                        key={name}
                        name={name}
                        component={ConventionTPFieldArrayComponent}
                        conventionTP={conventionTP}
                        domainList={domainList}
                        reseauxSoin={reseauxSoin}
                        conventions={conventions}
                    />
                ) : (
                    <FieldArray
                        key={name}
                        name={name}
                        component={CreateConventionTPFieldArrayComponent}
                        conventionTP={conventionTP}
                        domainList={domainList}
                        reseauxSoin={reseauxSoin}
                        conventions={conventions}
                        isCopy={isCopy}
                    />
                )}
            </div>
        );
    }

    render() {
        const { t, isCreate, isCopy, isEdit } = this.props;

        return (
            <Panel header={<PanelHeader title={t('declarant.conventionTPTitle')} />} id="conventionTP">
                <PanelSection>
                    {isCreate || isEdit || isCopy ? this.renderPanelEdit() : this.renderPanelView()}
                </PanelSection>
            </Panel>
        );
    }
}

ConventionTPComponent.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isCopy: PropTypes.bool,
    isEdit: PropTypes.bool,
    conventionTP: PropTypes.arrayOf(PropTypes.shape()),
    domainList: PropTypes.arrayOf(PropTypes.shape()),
    reseauxSoin: PropTypes.arrayOf(PropTypes.shape()),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ConventionTPComponent;
