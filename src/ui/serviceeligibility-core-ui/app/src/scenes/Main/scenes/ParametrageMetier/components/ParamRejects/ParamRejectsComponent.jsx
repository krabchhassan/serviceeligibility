/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import ReactTable from 'react-table';
import PropTypes from 'prop-types';

import { Panel, PanelHeader, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import 'react-table/react-table.css';
import ParamRejectsSubComponent from './ParamRejectsSubComponent';
import ParamUtils from '../../../../../../common/utils/ParamUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class ParamRejectsComponent extends Component {
    componentDidMount() {
        const { getAllRejects } = this.props;

        getAllRejects();
    }

    generateRowsData() {
        const { rejectsList } = this.props;

        let rejects = {};
        if (rejectsList) {
            rejects = { ...rejectsList };
        }

        return Object.values(rejects).map((item) => ({
            code: item.code,
            libelle: item.libelle,
            motif: item.motif,
            typeErreur: item.typeErreur,
            niveauErreur: item.niveauErreur,
            actions: '',
        }));
    }

    renderTable() {
        const { t } = this.props;

        const rowsData = this.generateRowsData();

        const columns = ParamUtils.codeLabelActionsColumns(t);

        return (
            <ReactTable
                data={rowsData}
                defaultSorted={[
                    {
                        id: 'code',
                        desc: false,
                    },
                ]}
                columns={columns}
                minRows={10}
                className="-striped -highlight"
                showPageSizeOptions={false}
                defaultPageSize={10}
                previousText={t('table.pagination.previous')}
                nextText={t('table.pagination.next')}
                loadingText={t('table.loading')}
                pageText={t('table.pagination.page')}
                ofText={t('table.pagination.of')}
                rowsText={t('table.pagination.rows')}
                noDataText={t('table.noData')}
                SubComponent={(data) => <ParamRejectsSubComponent data={data.original} />}
            />
        );
    }

    render() {
        const { t } = this.props;
        return (
            <Fragment>
                <Panel header={<PanelHeader title={t('parameters.listPanelRejectsTitle')} />}>
                    <PanelSection>{this.renderTable()}</PanelSection>
                </Panel>
            </Fragment>
        );
    }
}

// Prop types
const propTypes = {
    getAllRejects: PropTypes.func.isRequired,
    rejectsList: PropTypes.shape(),
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

// Add prop types
ParamRejectsComponent.propTypes = propTypes;
// Add default props
ParamRejectsComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParamRejectsComponent;
