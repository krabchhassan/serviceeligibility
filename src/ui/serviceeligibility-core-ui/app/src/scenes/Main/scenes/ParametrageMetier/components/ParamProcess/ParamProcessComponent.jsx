/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';

import {
    Col,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import 'react-table/react-table.css';
import ParamsSortedTable from '../ParamsSortedTable';
import ParamUtils from '../../../../../../common/utils/ParamUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class ParamProcessComponent extends Component {
    componentDidMount() {
        const { getAllProcess } = this.props;

        getAllProcess();
    }

    generateRowsData() {
        const { processList } = this.props;

        let processes = {};
        if (processList) {
            processes = { ...processList };
        }

        return Object.values(processes).map((item) => ({
            code: item.code,
            libelle: item.libelle,
            actions: '',
        }));
    }

    renderTable() {
        const { t } = this.props;

        const rowsData = this.generateRowsData();

        const columns = ParamUtils.codeLabelActionsColumns(t);

        return <ParamsSortedTable rowsData={rowsData} columns={columns} />;
    }

    render() {
        const { t } = this.props;
        return (
            <Fragment>
                <Panel header={<PanelHeader title={t('parameters.listPanelProcessTitle')} />}>
                    <PanelSection>
                        <Row>
                            <Col xs="12">{this.renderTable()}</Col>
                        </Row>
                    </PanelSection>
                </Panel>
            </Fragment>
        );
    }
}

// Prop types
const propTypes = {
    getAllProcess: PropTypes.func.isRequired,
    processList: PropTypes.shape(),
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

// Add prop types
ParamProcessComponent.propTypes = propTypes;
// Add default props
ParamProcessComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParamProcessComponent;
