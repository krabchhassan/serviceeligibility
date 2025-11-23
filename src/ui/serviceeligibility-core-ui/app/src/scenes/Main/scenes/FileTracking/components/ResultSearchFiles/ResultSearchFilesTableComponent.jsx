/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import get from 'lodash/get';
import isEqual from 'lodash/isEqual';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import 'react-table/react-table.css';
import {
    Panel,
    PanelHeader,
    PanelSection,
    CgdTable,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import ResultSearchFilesSubComponent from './components/ResultSearchFilesSubComponent';
import fluxUtils from '../../fluxUtils';
import DateUtils from '../../../../../../common/utils/DateUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const getNumberOfPages = (totalFlux) => Math.ceil(totalFlux / 5);
const getCurrentNumberOfPages = (currentRowNumber) => Math.ceil((currentRowNumber || 0) / 5);

const getProcessusLibelle = (processusList, codeProcessus) => {
    const processus = (processusList || []).find((processusItem) => processusItem.code === codeProcessus);
    return processus ? processus.libelle : null;
};
const getTableData = (fluxInfo, isReceivedFiles, processusList) =>
    (fluxInfo || []).map((fluxItem) => {
        const { dateExecution, codePartenaire, idDeclarant, processus, typeFichier, classStatut, ...other } = fluxItem;
        const nomAMC = fluxItem.nomAMC || '';
        const rejetPourcentage = Number.isNaN(fluxItem.infoFichier.rejetPourcentage)
            ? ''
            : `${fluxItem.infoFichier.rejetPourcentage.toFixed(2)} %`;
        return {
            treatmentDate: DateUtils.formatDisplayDateTimeWithTimeZone(dateExecution),
            codePartenaire,
            amc: `${idDeclarant} - ${nomAMC}`,
            processus: getProcessusLibelle(processusList, processus),
            fileType: typeFichier,
            fileName: fluxItem.infoFichier.nomFichier,
            fileNumber: fluxItem.infoFichier.numeroFichier,
            modalData: other,
            selected: false,
            ...(isReceivedFiles
                ? {
                      status: classStatut,
                      receivedMovment: fluxItem.infoFichier.mouvementRecus,
                      rejectedMovement: fluxItem.infoFichier.mouvementRejetes,
                      okMovement: fluxItem.infoFichier.mouvementOk,
                  }
                : {
                      sentMovements: fluxItem.infoFichier.mouvementEmis,
                      notSentDataPercentage: rejetPourcentage,
                  }),
        };
    });

const getDerivedStateTableData = (nextProps, prevState, nextState) => {
    const { fluxInfo, isReceivedFiles, processus } = nextProps;
    const nextTableData = getTableData(fluxInfo, isReceivedFiles, processus);
    const { tableData } = prevState;
    if (
        tableData &&
        nextTableData &&
        (nextTableData.length !== tableData.length || !isEqual(nextTableData, tableData))
    ) {
        return {
            ...nextState,
            tableData: nextTableData,
        };
    }
    return nextState;
};

const getDerivedStatePaging = (nextProps, prevState, nextState) => {
    const nextPage = getCurrentNumberOfPages(nextProps.paging);
    if (nextPage !== prevState.page) {
        return {
            ...nextState,
            page: nextPage,
        };
    }
    return nextState;
};

// eslint-disable-next-line react/prop-types
const getCellContentWithTooltip = ({ value }, mapper = (val) => val) => {
    if (value !== undefined && value !== null) {
        return (
            <span className="cgd-text-overflow" title={mapper(value)}>
                {mapper(value)}
            </span>
        );
    }
    return <span />;
};

const buildColumnObjectWithTooltip = (Header, accessor, id, headerClassName, width) => ({
    Header,
    headerClassName,
    id,
    accessor,
    width,
    disableSortBy: true,
    Cell: (value) => getCellContentWithTooltip(value, (name) => name),
});

const buildColumnObject = (Header, accessor, id, headerClassName, width) => ({
    Header,
    headerClassName,
    id,
    accessor,
    width,
    disableSortBy: true,
});

const textRight = 'text-right';
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class ResultSearchFilesTableComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            page: getCurrentNumberOfPages(props.paging),
            tableData: [],
        };
    }

    static getDerivedStateFromProps(nextProps, prevState) {
        if (nextProps && prevState) {
            let nextState = { ...prevState };
            if (nextProps.fluxInfo) {
                nextState = getDerivedStateTableData(nextProps, prevState, nextState);
            }
            if (nextProps.paging || nextProps.paging === 0) {
                nextState = getDerivedStatePaging(nextProps, prevState, nextState);
            }

            return !isEqual(nextState, prevState) ? { ...nextState } : null;
        }
        return null;
    }

    getColumns() {
        const { t } = this.props;
        const specificsColumns = this.buildSpecificColumns();

        const textLeft = 'text-left';
        return [
            buildColumnObject(
                this.createColumnHeader(t('fileTrackingResult.treatmentDate')),
                'treatmentDate',
                'treatmentDate',
                textLeft,
                135,
            ),
            buildColumnObject(t('fileTrackingResult.partner'), 'codePartenaire', 'codePartenaire', textLeft),
            buildColumnObjectWithTooltip(t('fileTrackingResult.amc'), 'amc', 'amc', textLeft),
            buildColumnObject(t('fileTrackingResult.process'), 'processus', 'processus', textLeft),
            buildColumnObject(t('fileTrackingResult.fileType'), 'fileType', 'fileType', textLeft),
            buildColumnObjectWithTooltip(t('fileTrackingResult.fileName'), 'fileName', 'fileName', textLeft),
            buildColumnObject(t('fileTrackingResult.fileNumber'), 'fileNumber', 'fileNumber', textRight),
            ...specificsColumns,
        ];
    }

    createColumnHeader = (text) => {
        let words = text.split(' ');
        if (words.length > 2) {
            words = text.match(/((\S+ ){1}\S+)|(\S+( \S+)*)(?= *\n|$)|\S+/g);
        }
        return (
            <div>
                <div>{words[0]}</div>
                <div>{words[1]}</div>
            </div>
        );
    };

    buildSpecificColumns() {
        const { t, isReceivedFiles } = this.props;
        return isReceivedFiles
            ? [
                  buildColumnObject(
                      t('fileTrackingResult.status'),
                      (row) => <CgIcon name={row.status ? 'validate' : 'attention'} />,
                      'status',
                      'text-left',
                  ),
                  buildColumnObject(
                      this.createColumnHeader(t('fileTrackingResult.receivedMovment')),
                      'receivedMovment',
                      'receivedMovment',
                      textRight,
                  ),
                  buildColumnObject(
                      this.createColumnHeader(t('fileTrackingResult.rejectedMovement')),
                      'rejectedMovement',
                      'rejectedMovement',
                      textRight,
                  ),
                  buildColumnObject(
                      this.createColumnHeader(t('fileTrackingResult.okMovement')),
                      'okMovement',
                      'okMovement',
                      textRight,
                  ),
              ]
            : [
                  buildColumnObject(
                      this.createColumnHeader(t('fileTrackingResult.sentMovements')),
                      'sentMovements',
                      'sentMovements',
                      textRight,
                  ),
                  buildColumnObject(
                      this.createColumnHeader(t('fileTrackingResult.notSentDataPercentage')),
                      'notSentDataPercentage',
                      'notSentDataPercentage',
                      textRight,
                  ),
              ];
    }

    @autobind
    handleToggleModal() {
        const { isModalOpen } = this.state;
        this.setState({
            isModalOpen: !isModalOpen,
        });
    }

    @autobind
    fetchPaginatedData({ pageIndex }) {
        const { dispatch, isReceivedFiles } = this.props;

        if (isReceivedFiles) {
            fluxUtils.getDataFluxEntrantPagination(pageIndex * 5, dispatch);
        } else {
            fluxUtils.getDataFluxSortantPagination(pageIndex * 5, dispatch);
        }
    }

    render() {
        const { pageSize, t, circuits, isReceivedFiles, id } = this.props;
        const { tableData } = this.state;
        const columns = this.getColumns();
        return (
            <Panel
                header={
                    <PanelHeader
                        title={t(`fileTrackingResult.${isReceivedFiles ? 'receiverTitle' : 'emitterTitle'}`)}
                        counter={pageSize}
                    />
                }
                className="bg-background-variant-1 pl-0 pr-0"
                wrapperClassName="flex-auto w-100"
                border={false}
            >
                <PanelSection>
                    <CgdTable
                        height="300px"
                        id={id}
                        subComponent={(data) => (
                            <ResultSearchFilesSubComponent
                                data={get(data, 'row.original.modalData')}
                                circuits={circuits}
                                isReceivedFiles={isReceivedFiles}
                            />
                        )}
                        data={tableData}
                        columns={columns}
                        initialPageSize={5}
                        pageSize={5}
                        showPagination={getNumberOfPages(pageSize) > 1}
                        pageCount={getNumberOfPages(pageSize)}
                        fetchData={this.fetchPaginatedData}
                        manual
                    />
                </PanelSection>
            </Panel>
        );
    }
}

ResultSearchFilesTableComponent.propTypes = {
    t: PropTypes.func,
    dispatch: PropTypes.func,
    id: PropTypes.string,
    paging: PropTypes.number,
    pageSize: PropTypes.number,
    circuits: PropTypes.arrayOf(PropTypes.shape()),
    isReceivedFiles: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ResultSearchFilesTableComponent;
