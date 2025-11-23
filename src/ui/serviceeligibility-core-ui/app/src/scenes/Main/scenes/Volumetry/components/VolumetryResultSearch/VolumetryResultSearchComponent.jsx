/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import ReactTable from 'react-table';
import { get } from 'lodash/object';
import autobind from 'autobind-decorator';
import 'react-table/react-table.css';
import {
    Panel,
    PanelHeader,
    PanelSection,
    FileDownloaderPopup,
    AuthProvider,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import Volumetry from '../../../../../../common/resources/Volumetry';
import PermissionConstants from '../../../../PermissionConstants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const getFilteredTableData = (tableData, searchCriteria) => {
    if (!searchCriteria) {
        return tableData;
    }

    return tableData.filter((dataRow) => {
        const amcCriterias = get(searchCriteria, 'amc.value');
        if (amcCriterias) {
            return dataRow.amc.includes(amcCriterias);
        } else if (searchCriteria.codePartenaire) {
            return dataRow.codePartenaire === searchCriteria.codePartenaire;
        }
        return true;
    });
};

const pathParamBuilder = (searchCriteria) => {
    if (searchCriteria) {
        if (searchCriteria.amc && searchCriteria.codePartenaire) {
            return `?amc=${searchCriteria.amc}&codePartenaire=${searchCriteria.codePartenaire}`;
        } else if (searchCriteria.amc) {
            return `?amc=${searchCriteria.amc}`;
        } else if (searchCriteria.codePartenaire) {
            return `?codePartenaire=${searchCriteria.codePartenaire}`;
        }
    }
    return '';
};

const buildColumnObject = (Header, Footer, accessor, id, headerClassName, className) => ({
    Header,
    headerClassName,
    id,
    accessor,
    Footer,
    className: className || headerClassName,
});

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class VolumetryResultSearchComponent extends Component {
    static populateHeader() {
        let downloadRequestHeaders = '';
        if (AuthProvider.get()) {
            downloadRequestHeaders = {
                Authorization: `Bearer ${AuthProvider.get().token}`,
                'X-CGD-TENANT': AuthProvider.get().realm,
            };
        }
        return downloadRequestHeaders;
    }

    constructor(props) {
        super(props);
        this.state = {
            data: [],
            startDownload: false,
        };
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (nextProps && prevState) {
            let nextState = { ...prevState };
            if (nextProps.volumetrics) {
                const nextTableData = getFilteredTableData(nextProps.volumetrics, nextProps.searchCriteria);
                if (nextTableData !== prevState.data) {
                    nextState = {
                        ...nextState,
                        data: nextTableData,
                    };
                }
            }

            return nextState;
        }
        return null;
    }

    getColumns() {
        const { t } = this.props;
        const textRight = 'text-right';
        return [
            buildColumnObject(t('volumetry.partner'), '', 'codePartenaire', 'partner', 'text-left'),
            buildColumnObject(t('volumetry.amc'), `${t('volumetry.totals')}${t('colon')}`, 'amc', 'amc', 'text-left'),
            buildColumnObject(
                t('volumetry.declarationNumber'),
                this.totalValuePresenter('declarations'),
                'declarations',
                'declarationNumber',
                textRight,
            ),
            buildColumnObject(
                t('volumetry.personsNumber'),
                this.totalValuePresenter('personnes'),
                'personnes',
                'personsNumber',
                textRight,
            ),
            buildColumnObject(
                t('volumetry.openedRightsPersonsNumber'),
                this.totalValuePresenter('personnesDroitsOuverts'),
                'personnesDroitsOuverts',
                'openedRightsPersonsNumber',
                textRight,
            ),
            buildColumnObject(
                t('volumetry.closedRightsPersonsNumber'),
                this.totalValuePresenter('personnesDroitsFermes'),
                'personnesDroitsFermes',
                'closedRightsPersonsNumber',
                textRight,
            ),
        ];
    }

    @autobind
    toggleFile() {
        const { startDownload } = this.state;
        this.setState({ startDownload: !startDownload });
    }

    totalValuePresenter(columnName) {
        const { data } = this.state;
        if (!data || data.length === 0 || !columnName) {
            return 0;
        }
        const columnValues = data.map((item) => item[columnName]);
        return columnValues.reduce((accumulator, currentValue) => accumulator + currentValue).toLocaleString();
    }

    formatData() {
        const { data } = this.state;

        const duplicate = data.map((value) => ({ ...value }));

        duplicate.forEach((line) => {
            line.declarations = line.declarations.toLocaleString();
            line.personnes = line.personnes.toLocaleString();
            line.personnesDroitsFermes = line.personnesDroitsFermes.toLocaleString();
            line.personnesDroitsOuverts = line.personnesDroitsOuverts.toLocaleString();
        });

        return duplicate;
    }

    render() {
        const { t, searchCriteria } = this.props;
        const { startDownload } = this.state;
        const columns = this.getColumns();
        const formattedData = this.formatData();
        const downloadUrl = `${Volumetry.excel().url()}${pathParamBuilder(searchCriteria)}`;
        const toolbar = {
            label: t('common:download'),
            action: this.toggleFile,
            id: 'download',
            behavior: 'secondary',
            icon: 'download',
            allowedPermissions: PermissionConstants.READ_TRANSCODAGE_DATA_PERMISSION,
        };

        return (
            <Fragment>
                <FileDownloaderPopup
                    fileUrl={downloadUrl}
                    toggleStartDownload={this.toggleFile}
                    isDownloading={startDownload}
                    requestHeaders={VolumetryResultSearchComponent.populateHeader()}
                    showConfirm={false}
                    useClient
                />
                <Panel
                    header={
                        <PanelHeader title={t('volumetry.resultTitle')} actions={[toolbar]} className="pl-0 pr-0" />
                    }
                    id="history-panel"
                    className="bg-background-variant-1"
                    border={false}
                >
                    <PanelSection className="pl-0 pr-0">
                        <ReactTable
                            data={formattedData}
                            columns={columns}
                            pageSize={formattedData.length}
                            minRows={10}
                            className="-striped -highlight bg-background-default"
                            showPageSizeOptions={false}
                            showPagination={false}
                        />
                    </PanelSection>
                </Panel>
            </Fragment>
        );
    }
}

VolumetryResultSearchComponent.propTypes = {
    t: PropTypes.func,
    searchCriteria: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default VolumetryResultSearchComponent;
