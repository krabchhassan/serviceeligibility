/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { reduxForm } from 'redux-form';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import autobind from 'autobind-decorator';
import {
    PanelHeader,
    CgdTable,
    Panel,
    Modal,
    ModalBody,
    ModalHeader,
    PageLayout,
    Button,
    Row,
    Col,
    HabilitationFragment,
    BodyHeader,
    FileDownloaderPopup,
    LoadingSpinner2,
    AuthProvider,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import TechnicalGuaranteeDetail from './TechnicalGuaranteeDetail';
import StringUtils from '../../../../../../../common/utils/StringUtils';
import style from './style.module.scss';
import CreateLot from './CreateLot/CreateLot';
import UpdateLot from './UpdateLot/UpdateLot';
import PermissionConstants from '../../../../../PermissionConstants';
import ImportLotPanel from './ImportLotPanel/ImportLotPanel';
import Lot from '../../../../../../../common/resources/Lot';
import formConstants from '../Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['bobb', 'common'])
class ResultSearchComponent extends Component {
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
            garanties: null,
            modal: false,
            modalLot: false,
            modalEditLot: false,
            pageSizeState: 10,
            selectedLotToModify: {},
            isFirstRun: true,
            isDownloading: false,
            isDownloadingOne: false,
            modalImport: false,
            selectedLotToExport: {},
            fileNameToImport: '',
        };
        this.toggleModalLot = this.toggleModalLot.bind(this);
        this.toggleModalEditLot = this.toggleModalEditLot.bind(this);
    }

    getColumns() {
        const { t } = this.props;

        return [
            {
                Header: StringUtils.splitIntoDivs(t('resultSearch.code')),
                accessor: 'code',
                id: 'code',
                Cell: (props) => props.value,
            },
            {
                Header: StringUtils.splitIntoDivs(t('resultSearch.libelle')),
                width: '40%',
                accessor: 'libelle',
                id: 'libelle',
                Cell: (props) => props.value,
            },
            {
                Header: StringUtils.splitIntoDivs(t('resultSearch.garantieTechniques')),
                accessor: 'garantieTechniques',
                disableSortBy: true,
                id: 'garantieTechniques',
                Cell: (props) => {
                    const filteredData = props.value.filter(
                        (item) => !item.dateSuppressionLogique || item.dateSuppressionLogique.length === 0,
                    );
                    return props.row.original.size ? props.row.original.size : filteredData.length;
                },
            },
            {
                id: 'details',
                width: '25%',
                accessor: 'details',
                disableSortBy: true,
                Cell: (props) => (
                    <div className={style['align-table-elements']}>
                        {this.renderExport(props.row.original)}
                        {this.renderAction(props.row.original)}
                        {this.renderModal(props.row.original)}
                    </div>
                ),
            },
        ];
    }

    getToolbar() {
        const { t } = this.props;
        return {
            label: t('resultSearch.importExport'),
            items: [
                {
                    id: 'import-lot',
                    label: 'Import',
                    action: () => this.toggleImport(),
                    allowedPermissions: PermissionConstants.CREATE_LOT_PERMISSION,
                },
                {
                    id: 'export-lot',
                    label: 'Export',
                    action: () => this.setState({ isDownloading: true }),
                    allowedPermissions: PermissionConstants.READ_LOT_PERMISSION,
                },
            ],
        };
    }

    getLotsToDisplay = (arrayLots, displayDeletedGT, bodyCriterias) => {
        if (arrayLots.length > 0) {
            if (displayDeletedGT) {
                return arrayLots;
            }
            return this.hideDeletedGt(arrayLots, bodyCriterias);
        }
        return arrayLots;
    };

    hideDeletedGt = (arrayLots, bodyCriterias) => {
        const lotsToDisplay = [];
        arrayLots.forEach((lot) => {
            const alteredLot = {
                id: lot.id,
                code: lot.code,
                libelle: lot.libelle,
                garantieTechniques: lot.garantieTechniques,
                size: lot.garantieTechniques.length,
            };
            const { garantieTechniques } = alteredLot;
            const hasCodeGarantie = bodyCriterias.codeGarantie !== undefined;
            const hasCodeAssureur = bodyCriterias.codeAssureur !== undefined;
            if (!hasCodeGarantie && !hasCodeAssureur) {
                lotsToDisplay.push(alteredLot);
                return;
            }
            const matches = (gt) => {
                return (
                    (hasCodeGarantie && gt.codeGarantie === bodyCriterias.codeGarantie) ||
                    (hasCodeAssureur && gt.codeAssureur === bodyCriterias.codeAssureur)
                );
            };
            const isSearchedLot = garantieTechniques.some(matches);

            const hasDeletedMatchingGt = garantieTechniques.some(
                (gt) => matches(gt) && gt.dateSuppressionLogique != null && gt.dateSuppressionLogique !== '',
            );

            if (!isSearchedLot || !hasDeletedMatchingGt) {
                lotsToDisplay.push(alteredLot);
            }
        });
        return lotsToDisplay;
    };

    toggle(garantieTechniques) {
        const { modal } = this.state;
        this.setState({ modal: !modal, garanties: garantieTechniques });
    }

    toggleModalLot() {
        this.setState((prevState) => ({
            modalLot: !prevState.modalLot,
        }));
    }

    toggleModalEditLot(lot) {
        this.setState((prevState) => ({
            modalEditLot: !prevState.modalEditLot,
            selectedLotToModify: lot,
        }));
    }

    toggleExport(lot) {
        this.setState((prevState) => ({
            isDownloadingOne: !prevState.isDownloadingOne,
            selectedLotToExport: lot,
        }));
    }

    toggleImport() {
        const { modalImport } = this.state;
        this.setState({ modalImport: !modalImport });
    }

    @autobind
    fetchData(dat) {
        const { pageSize, pageIndex, sortBy } = dat;
        const { searchLots, bodyCriterias } = this.props;
        const { isFirstRun } = this.state;

        bodyCriterias.page = pageIndex + 1;
        bodyCriterias.perPage = pageSize;

        if (isFirstRun) {
            this.setState({ isFirstRun: false });
        } else {
            if (sortBy && sortBy.length > 0) {
                bodyCriterias.sortBy = sortBy[0].id;
                bodyCriterias.direction = sortBy[0].desc ? 'desc' : 'asc';
            }
            searchLots(bodyCriterias);
        }
    }

    @autobind
    handleLotCreated(body) {
        const { searchLots } = this.props;

        this.toggleModalLot();
        if (body.code) {
            searchLots(body);
        }
    }

    @autobind
    handleLotModified() {
        const { searchLots, bodyCriterias } = this.props;

        this.toggleModalEditLot();
        searchLots(bodyCriterias);
    }

    renderModal(garantieTechniques) {
        const { t } = this.props;
        return (
            <Button
                id="details-button"
                outlineNoBorder
                behavior="primary"
                onClick={() => this.toggle(garantieTechniques)}
            >
                {`${t('details')} `}
                <CgIcon name="right-scroll" />
            </Button>
        );
    }

    renderExport(lot) {
        const { t } = this.props;
        return (
            <HabilitationFragment allowedPermissions={PermissionConstants.READ_LOT_PERMISSION}>
                <Button behavior="default" outlineNoBorder onClick={() => this.toggleExport(lot)}>
                    {t('action.export')} <CgIcon name="download" className="default" />
                </Button>
            </HabilitationFragment>
        );
    }

    renderAction(lot) {
        const { t } = this.props;
        return (
            <HabilitationFragment allowedPermissions={PermissionConstants.CREATE_LOT_PERMISSION}>
                <Button behavior="default" outlineNoBorder onClick={() => this.toggleModalEditLot(lot)}>
                    {t('action.edit')}
                    <CgIcon name="pencil" className="default" />
                </Button>
            </HabilitationFragment>
        );
    }

    renderTable(arrayLots) {
        const { loading, pagination } = this.props;
        const { pageSizeState } = this.state;
        const { totalPages } = pagination || {};

        return (
            <CgdTable
                id="paramTable"
                initialPageSize={pageSizeState}
                manual
                pageCount={totalPages}
                showPagination={totalPages > 1}
                data={arrayLots}
                initialPageIndex={0}
                initialSortBy={[{ id: 'code', desc: false }]}
                columns={this.getColumns()}
                useDebounce={500}
                fetchData={this.fetchData}
                loading={loading}
                withFilters
            />
        );
    }

    render() {
        const { t, lots, isImportLotPending, bodyCriterias } = this.props;
        const {
            modal,
            garanties,
            modalLot,
            modalEditLot,
            selectedLotToModify,
            modalImport,
            isDownloading,
            isDownloadingOne,
            selectedLotToExport,
            fileNameToImport,
        } = this.state;
        const arrayLots = lots ? Object.values(lots).map((lot) => ({ ...lot, key: lot.id })) : [];

        const displayDeletedGT =
            bodyCriterias.codeGarantie === undefined && bodyCriterias.codeAssureur === undefined
                ? false
                : bodyCriterias.gTSupprimees;

        const lotsToDisplay = this.getLotsToDisplay(arrayLots, displayDeletedGT, bodyCriterias);

        const setFileName = (filename) => {
            this.setState({ fileNameToImport: filename });
        };

        return (
            <Fragment>
                <Row>
                    <Col xs={9} className="pr-0">
                        <div className="lots-header">
                            <PanelHeader title={t('resultSearch.title')} counter={lotsToDisplay.length} />
                        </div>
                    </Col>
                    <Col xs={3} className="pr-0 pl-0">
                        <Row>
                            <Col xs={5} className="mt-1 pr-0 pl-5">
                                <HabilitationFragment allowedPermissions={PermissionConstants.CREATE_LOT_PERMISSION}>
                                    <Button behavior="default" type="submit" onClick={this.toggleModalLot}>
                                        {t('resultSearch.ajoutLot')}
                                    </Button>
                                </HabilitationFragment>
                            </Col>
                            <Col xs={4} className="ml-0 pl-0 pr-0">
                                <BodyHeader className="pt-0 pb-0 pl-0" toolbar={this.getToolbar()} />
                            </Col>
                        </Row>
                    </Col>
                </Row>
                <Row>
                    <Col xs={12}>
                        <PageLayout>
                            {lotsToDisplay.length > 0 && (
                                <div className="lots-container">
                                    <Panel border={false} panelTheme="secondary">
                                        {this.renderTable(lotsToDisplay)}
                                    </Panel>
                                </div>
                            )}
                        </PageLayout>
                    </Col>
                </Row>
                <Row className="mt-2">
                    {(isDownloading || isDownloadingOne || isImportLotPending) && (
                        <div
                            style={{
                                width: '100%',
                                height: '100px',
                                position: 'relative',
                                zIndex: -1,
                            }}
                        >
                            <LoadingSpinner2
                                behavior="primary"
                                type="container"
                                customText={t('bobb:import.spinnerMessage', {
                                    fileNameToImport,
                                })}
                            />
                        </div>
                    )}
                </Row>
                <Modal size="md" isOpen={modal} toggle={() => this.toggle()} backdrop="static">
                    <ModalHeader toggle={() => this.toggle()}>
                        {t('resultSearch.modalTitle', { lot: garanties?.code })}
                    </ModalHeader>
                    <ModalBody>
                        <TechnicalGuaranteeDetail
                            technicalGuarantee={arrayLots.find((lot) => lot.code === garanties?.code)}
                            displayDeletedGT={displayDeletedGT}
                        />
                    </ModalBody>
                </Modal>
                <Modal size="md" isOpen={modalImport} toggle={() => this.toggleImport()} backdrop="static">
                    <ModalHeader toggle={() => this.toggleImport()}>{t('resultSearch.importModalTitle')}</ModalHeader>
                    <ModalBody>
                        <ImportLotPanel
                            closeImportPanel={() => this.toggleImport()}
                            isImportLotPending={isImportLotPending}
                            setFileName={setFileName}
                        />
                    </ModalBody>
                </Modal>
                <Modal size="md" isOpen={modalLot} toggle={this.toggleModalLot} backdrop="static">
                    <ModalHeader toggle={this.toggleModalLot}>{t('resultSearch.ajoutLot')}</ModalHeader>
                    <ModalBody>
                        <CreateLot toggle={this.toggleModalLot} onLotCreated={this.handleLotCreated} />
                    </ModalBody>
                </Modal>
                <Modal size="md" isOpen={modalEditLot} toggle={this.toggleModalEditLot} backdrop="static">
                    <ModalHeader toggle={this.toggleModalEditLot}>{t('resultSearch.modificationLot')}</ModalHeader>
                    <ModalBody>
                        <UpdateLot lotToModify={selectedLotToModify} onLotModified={this.handleLotModified} />
                    </ModalBody>
                </Modal>
                <FileDownloaderPopup
                    fileUrl={Lot.export().url()}
                    toggleStartDownload={() => this.setState({ isDownloading: false })} // In reality, it is fired when download is ended
                    isDownloading={isDownloading}
                    useClient
                    showConfirm={false}
                    requestHeaders={ResultSearchComponent.populateHeader()}
                    alertHeader={t('errors:download.alertTitle')}
                    alertBody={t('errors:download.alertBody')}
                />
                <FileDownloaderPopup
                    fileUrl={Lot.exportOne(selectedLotToExport.code).url()}
                    toggleStartDownload={() => this.setState({ isDownloadingOne: false })} // In reality, it is fired when download is ended
                    isDownloading={isDownloadingOne}
                    useClient
                    showConfirm={false}
                    requestHeaders={ResultSearchComponent.populateHeader()}
                    alertHeader={t('errors:download.alertTitle')}
                    alertBody={t('errors:download.alertBody')}
                />
            </Fragment>
        );
    }
}

ResultSearchComponent.propTypes = {
    t: PropTypes.func,
    lots: PropTypes.arrayOf(PropTypes.shape()),
    pagination: PropTypes.shape(),
    searchLots: PropTypes.func.isRequired,
    lotToModify: PropTypes.shape({
        id: PropTypes.string,
        code: PropTypes.string,
        libelle: PropTypes.string,
        garantieTechniques: PropTypes.arrayOf(
            PropTypes.shape({
                codeAssureur: PropTypes.string,
                codeGarantie: PropTypes.string,
                dateAjout: PropTypes.string,
                dateSuppressionLogique: PropTypes.string,
            }),
        ),
    }),
    bodyCriterias: PropTypes.shape().isRequired,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ResultSearchComponent);
