/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    CgdTable,
    Col,
    Panel,
    PanelSection,
    Row,
    Tooltip,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { FieldArray } from 'redux-form';
import Constants from './Constants';
import StringUtils from '../../../../../../common/utils/StringUtils';
import TranscodageFieldArrayComponent from './TranscodageFieldArrayComponent';
import CreateTranscodageFieldArrayComponent from './CreateTranscodageFieldArrayComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class TranscodageComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: `${Constants.TRANSCO_FIELD_ARRAY_NAME}`,
        };
    }

    getTranscodageDomainesTP() {
        const { declarant } = this.props;

        if (declarant) {
            const { transcodageDomainesTP } = declarant;
            return transcodageDomainesTP;
        }

        return [];
    }

    getColumns() {
        return [this.generateCommonColumn('domaineSource', 100), this.generateCommonColumn('domainesCible', 200)];
    }

    generateCommonColumn(key, width) {
        const { t } = this.props;
        return {
            Header: StringUtils.splitIntoDivs(t(`transcodageDomaineTP.${key}`), false),
            accessor: key,
            id: key,
            width,
            disableSortBy: true,
            key,
            Cell: (props) => (
                <span>
                    <span id={`${key}-${props.row.index}`}>{props.value}</span>
                    <Tooltip placement="right" target={`${key}-${props.row.index}`}>
                        {props.value}{' '}
                    </Tooltip>
                </span>
            ),
        };
    }

    renderPanelView() {
        const { t, domainList } = this.props;

        const objectsList = Object.values(domainList || []).map((domain) => domain.value);
        const transcodageDomainesTP = this.getTranscodageDomainesTP();
        const cols = this.getColumns();

        const rowsData = (transcodageDomainesTP || []).map((item) => {
            const domaineSourceFound = objectsList.find((domain) => domain.code === item.domaineSource);
            const domainesCible = [];
            item.domainesCible.forEach((domaineCible) => {
                const domaineCibleFound = objectsList.find((domain) => domain.code === domaineCible);
                domainesCible.push(`${domaineCible} (${domaineCibleFound && domaineCibleFound.label})`);
            });

            return {
                domaineSource: `${item.domaineSource} (${domaineSourceFound && domaineSourceFound.label})`,
                domainesCible: domainesCible.join(', '),
            };
        });

        if (transcodageDomainesTP && transcodageDomainesTP.length > 0) {
            return (
                <PanelSection>
                    <Row>
                        <Col className="ml-3 pl-3 mr-3 mb-1">
                            <Row>
                                <CgdTable
                                    id="transcodageTable"
                                    key="transcodageTable"
                                    data={rowsData}
                                    columns={cols}
                                    showPageSizeOptions={false}
                                    showPagination={false}
                                    pageSize={transcodageDomainesTP.length}
                                />
                            </Row>
                        </Col>
                    </Row>
                </PanelSection>
            );
        }
        return <div className="ml-2">{t('transcodageDomaineTP.noTransco')}</div>;
    }

    renderPanelEdit() {
        const { declarant, isEdit, isCopy, domainList } = this.props;
        const { name } = this.state;

        const { transcodageDomainesTP } = declarant || {};
        return (
            <div className="m-3">
                {isCopy || isEdit ? (
                    <FieldArray
                        key={name}
                        name={name}
                        component={TranscodageFieldArrayComponent}
                        transcodageDomainesTP={transcodageDomainesTP}
                        domainList={domainList}
                    />
                ) : (
                    <FieldArray
                        key={name}
                        name={name}
                        component={CreateTranscodageFieldArrayComponent}
                        transcodageDomainesTP={transcodageDomainesTP}
                        domainList={domainList}
                    />
                )}
            </div>
        );
    }

    render() {
        const { isCreate, isCopy, isEdit } = this.props;

        return (
            <Panel>
                <PanelSection>
                    {isCreate || isEdit || isCopy ? this.renderPanelEdit() : this.renderPanelView()}
                </PanelSection>
            </Panel>
        );
    }
}

TranscodageComponent.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isCopy: PropTypes.bool,
    isEdit: PropTypes.bool,
    declarant: PropTypes.shape(),
    domainList: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TranscodageComponent;
