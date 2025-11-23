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
import FondCarteFieldArrayComponent from './FondCarteFieldArrayComponent';
import CreateFondCarteFieldArrayComponent from './CreateFondCarteFieldArrayComponent';
import ParametrageAMCUtils from '../ParametrageAMCUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class FondCarteComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: `${Constants.FOND_CARTE_FIELD_ARRAY_NAME}`,
        };
    }

    getColumns() {
        const { t } = this.props;
        return [
            ParametrageAMCUtils.generateCommonColumn('reseauSoin', '35%', t),
            ParametrageAMCUtils.generateCommonColumn('fondCarte', '35%', t),
            ParametrageAMCUtils.generateCommonColumn('dateDebut', '15%', t),
            ParametrageAMCUtils.generateCommonColumn('dateFin', '15%', t),
        ];
    }

    generateRowsData() {
        const { fondCarte, reseauxSoin } = this.props;

        const rowsData = Object.values(fondCarte || []).map((item, index) => ({
            key: index,
            reseauSoin: ParametrageAMCUtils.getElementLabel(item.reseauSoin, reseauxSoin),
            fondCarte: item.fondCarte || '-',
            dateDebut: DateUtils.formatDisplayDate(item.dateDebut),
            dateFin: item.dateFin,
        }));

        return rowsData;
    }

    renderPanelView() {
        const { t, fondCarte } = this.props;

        const cols = this.getColumns();
        const rowsData = this.generateRowsData();
        if (fondCarte && fondCarte.length > 0) {
            return (
                <PanelSection>
                    <Row>
                        <Col className="ml-3 pl-3 mr-3 mb-1">
                            <Row>
                                <CgdTable
                                    id="fondCarteTable"
                                    key="fondCarteTable"
                                    data={rowsData}
                                    columns={cols}
                                    showPageSizeOptions={false}
                                    showPagination={false}
                                    pageSize={fondCarte.length}
                                />
                            </Row>
                        </Col>
                    </Row>
                </PanelSection>
            );
        }
        return <div className="ml-2">{t('declarant.parametrageAMC.noFondCarte')}</div>;
    }

    renderPanelEdit() {
        const { fondCarte, isEdit, isCopy, reseauxSoin } = this.props;
        const { name } = this.state;

        return (
            <div className="m-3">
                {isEdit ? (
                    <FieldArray
                        key={name}
                        name={name}
                        component={FondCarteFieldArrayComponent}
                        fondCarte={fondCarte}
                        reseauxSoin={reseauxSoin}
                    />
                ) : (
                    <FieldArray
                        key={name}
                        name={name}
                        component={CreateFondCarteFieldArrayComponent}
                        fondCarte={fondCarte}
                        reseauxSoin={reseauxSoin}
                        isCopy={isCopy}
                    />
                )}
            </div>
        );
    }

    render() {
        const { t, isCreate, isCopy, isEdit } = this.props;

        return (
            <Panel header={<PanelHeader title={t('declarant.fondCarteTitle')} />} id="fondCarte">
                <PanelSection>
                    {isCreate || isEdit || isCopy ? this.renderPanelEdit() : this.renderPanelView()}
                </PanelSection>
            </Panel>
        );
    }
}

FondCarteComponent.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isCopy: PropTypes.bool,
    isEdit: PropTypes.bool,
    fondCarte: PropTypes.arrayOf(PropTypes.shape()),
    reseauxSoin: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default FondCarteComponent;
