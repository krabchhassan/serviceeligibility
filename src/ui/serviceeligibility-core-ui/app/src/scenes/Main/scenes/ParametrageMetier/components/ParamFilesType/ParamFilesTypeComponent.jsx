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
class ParamFilesTypeComponent extends Component {
    componentDidMount() {
        const { getAllFilesType } = this.props;

        getAllFilesType();
    }

    generateRowsData() {
        const { filesTypeList } = this.props;

        let filesType = {};
        if (filesTypeList) {
            filesType = { ...filesTypeList };
        }

        return Object.values(filesType).map((item) => ({
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
                <Panel header={<PanelHeader title={t('parameters.listPanelFilesTypeTitle')} />}>
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
    getAllFilesType: PropTypes.func.isRequired,
    filesTypeList: PropTypes.shape(),
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

// Add prop types
ParamFilesTypeComponent.propTypes = propTypes;
// Add default props
ParamFilesTypeComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParamFilesTypeComponent;
