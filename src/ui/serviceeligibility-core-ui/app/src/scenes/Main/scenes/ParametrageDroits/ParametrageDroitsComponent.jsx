/* eslint-disable react/sort-comp */
/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import _ from 'lodash';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import autobind from 'autobind-decorator';
import withRouter from 'react-router/withRouter';
import moment from 'moment';
import {
    BodyHeader,
    BreadcrumbPart,
    Button,
    CgdTable,
    Col,
    Dropdown2,
    DropdownItem2,
    Feedback,
    Filtering,
    HabilitationFragment,
    Modal,
    ModalBody,
    ModalHeader,
    PageLayout,
    Panel,
    PanelHeader,
    Row,
    Status,
    Tooltip,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CommonInput, CommonMultiCombobox } from '../../../../common/utils/Form/CommonFields';
import DateUtils from '../../../../common/utils/DateUtils';
import StringUtils from '../../../../common/utils/StringUtils';
import history from '../../../../history';
import PermissionConstants from '../../PermissionConstants';
import ParametrageDetail from './components/ParametrageDetail';
import ParametrageBreadcrumb from '../Parametrage/components/ParametrageBreadcrumb';
import style from './style.module.scss';
import businessUtils from '../../../../common/utils/businessUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const extractFilterAmcs = (body, values) => {
    if (values) {
        if (values.amcs) {
            modifyBodyAmcs(values, body);
        }
        if (values.triggerMode) {
            modifyBodyTriggerMode(values, body);
        }
        if (values.status === true) {
            body.status = values.status;
        } else {
            body.status = false;
        }
        extractFilterLots(body, values.lots);
        extractFilterGTs(body, values.gts);
    }
};

function modifyBodyAmcs(values, body) {
    const amcs = (values.amcs || []).map((item) => item.number).filter((item) => item !== 'all');
    if (amcs && amcs.length > 0) {
        body.amcs = amcs.join(',');
    }
}

function modifyBodyTriggerMode(values, body) {
    const modes = (values.triggerMode || []).map((item) => item.value).filter((item) => item !== 'all');
    if (modes && modes.length > 0) {
        body.triggerMode = modes.join(',');
    }
}

const extractFilterLots = (body, lots) => {
    const idlots = (lots || []).map((item) => item.id);
    if (lots && lots.length > 0) {
        body.lots = idlots.join(',');
    }
};

const extractFilterGTs = (body, gts) => {
    const idGts = (gts || []).map((item) => `${item.number}#${item.value}`);
    if (idGts && idGts.length > 0) {
        body.gts = idGts.join(',');
    }
};

const goToCreatePage = () => history.push('/parameters/create_param_droit');

const goToDuplicatePage = (idParam) => history.push(`/parameters/duplicate_param_droit/${idParam}`);

const CustomGarantiesCell = (props) => (
    <span>
        <span id={`toolgaranties-${props.row.index}`}>{props.value}</span>
        <Tooltip target={`toolgaranties-${props.row.index}`}>{props.value}</Tooltip>
    </span>
);

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common', 'breadcrumb'], { wait: true })
class ParametrageDroitsComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            pageSizeState: 10,
            isFirstRun: true,
            chips: [],
            expanded: false,
            body: {
                page: 1,
                perPage: 10,
            },
            TPrightsGenerated: StringUtils.getUrlParams(this.props.location.search).TPrightsGenerated,
            counter: undefined,
            modal: false,
            parametrageDetail: null,
        };
        this.hasCreated = props.hasCreated;
        this.autocompleteGTs = _.debounce(this.autocompleteGTsCall, 1000);
    }

    componentDidMount() {
        const { t, getLightDeclarants, searchParametrage, addAlert, getLots, getAllCodeRenvoi, getCodesItelis } =
            this.props;
        const { pageSizeState, TPrightsGenerated } = this.state;
        getLightDeclarants();
        getLots();
        getAllCodeRenvoi();
        getCodesItelis();
        const body = {
            page: 1,
            perPage: pageSizeState,
        };
        searchParametrage(body);

        if (TPrightsGenerated && TPrightsGenerated !== 'undefined') {
            addAlert({
                message: t('parametersDroits.createdParams', {
                    date: DateUtils.formatDisplayDate(moment.now()),
                    amc: TPrightsGenerated,
                }),
            });
        }
    }

    componentWillReceiveProps(nextProps) {
        const { amcs, triggerMode, status } = nextProps;
        const chips = new Set();

        if (amcs) {
            amcs.forEach((item) => {
                chips.add({ key: item.value, value: item.label });
            });
        }
        if (triggerMode) {
            triggerMode.forEach((item) => {
                chips.add({ key: item.value, value: item.label });
            });
        }
        if (status === true) {
            chips.add({ key: 'Status', value: 'Affichage des paramétrages désactivés' });
        }

        // Convertir l'ensemble des chips en tableau.
        const newChips = [...chips];

        this.setState({
            chips: newChips,
        });
    }

    @autobind
    onCloseChip(chip) {
        const { amcs, triggerMode, status, change } = this.props;
        const { chips } = this.state;

        const newChips = chips.filter((elt) => elt.key !== chip.key);
        let newAmcs = Array.isArray(amcs) ? [...amcs] : [];
        let newTriggerMode = Array.isArray(triggerMode) ? [...triggerMode] : [];
        const newStatus = chip.key === 'Status' ? false : status;

        newAmcs = newAmcs.filter((item) => item.value !== chip.key);
        newTriggerMode = newTriggerMode.filter((item) => item.value !== chip.key);

        const updatedFilters = {
            amcs: newAmcs,
            triggerMode: newTriggerMode,
            status: newStatus,
        };

        change('amcs', newAmcs);
        change('triggerMode', newTriggerMode);
        change('status', newStatus);

        this.setState({ chips: newChips });
        this.handleSubmit(updatedFilters);
    }

    @autobind
    setExpanded(a) {
        this.setState({ expanded: a });
    }

    @autobind
    resetSearchFunction() {
        const { reset } = this.props;
        reset();
    }

    @autobind
    getRef(ref) {
        this.ref = ref;
    }

    toggle(parametrageDetail) {
        const { modal } = this.state;
        this.setState({ modal: !modal, parametrageDetail });
    }

    renderModal(parametrageDetail) {
        const { t } = this.props;
        return (
            <Button id="open-button" outlineNoBorder behavior="primary" onClick={() => this.toggle(parametrageDetail)}>
                {`${t('open')} `}
                <CgIcon name="right-scroll" />
            </Button>
        );
    }

    renderAction(value, props) {
        const { t } = this.props;
        return (
            <HabilitationFragment allowedPermissions={PermissionConstants.PARAM_CHANGE_STATUS}>
                <DropdownItem2
                    type="item"
                    label={t(`parametersDroits.${value === 'Actif' ? 'deactivate' : 'activate'}`)}
                    id="buttonId"
                    action={() => this.modifyItem(props)}
                    icon={value === 'Actif' ? 'file-xmark' : 'file-activate'}
                />
                <DropdownItem2
                    type="item"
                    label={t('parametersDroits.duplicate')}
                    id="duplicateButtonId"
                    action={() => goToDuplicatePage(props.row.original.id)}
                    icon="duplicate"
                />
            </HabilitationFragment>
        );
    }

    getColumns() {
        const { t } = this.props;
        return [
            {
                Header: t('parametersDroits.status'),
                accessor: 'statut',
                id: 'statut',
                width: '6%',
                disableSortBy: true,
                Cell: (props) => (
                    <Status
                        id="status"
                        label={props.value}
                        behavior={props.value === 'Actif' ? 'success' : 'default'}
                    />
                ),
            },
            {
                Header: t('parametersDroits.priorite'),
                accessor: 'priorite',
                id: 'priorite',
                width: '6%',
            },
            {
                Header: StringUtils.splitIntoDivs(t('parametersDroits.dateDebutValidite')),
                accessor: 'dateDebutValidite',
                id: 'dateDebutValidite',
                width: '8%',
                Cell: (props) => <div>{DateUtils.transformDateForDisplay(props.value)}</div>,
            },
            {
                Header: t('parametersDroits.amc'),
                accessor: 'amc',
                id: 'amcNom',
                width: '12%',
                Cell: (props) => (
                    <span>
                        <span id={`toolamc-${props.row.index}`}>{props.value}</span>
                        <Tooltip target={`toolamc-${props.row.index}`}>{props.value}</Tooltip>
                    </span>
                ),
            },
            {
                Header: StringUtils.splitIntoDivs(t('parametersDroits.identifiantCollectivite')),
                accessor: 'identifiantCollectivite',
                id: 'identifiantCollectivite',
                width: '10%',
            },
            {
                Header: StringUtils.splitIntoDivs(t('parametersDroits.groupePopulation')),
                accessor: 'groupePopulation',
                id: 'groupePopulation',
                width: '10%',
            },
            {
                Header: StringUtils.splitIntoDivs(t('parametersDroits.critereSecondaireDetaille')),
                accessor: 'critereSecondaireDetaille',
                id: 'critereSecondaireDetaille',
                width: '10%',
            },
            {
                Header: t('parametersDroits.garanties'),
                accessor: 'garanties',
                id: 'garanties',
                disableSortBy: true,
                Cell: CustomGarantiesCell,
            },
            {
                id: 'modif',
                width: '15%',
                accessor: 'statut',
                disableSortBy: true,
                Cell: (props) => (
                    <div className={style['align-table-elements']}>
                        <Dropdown2 id="id-2" label={t('parametersDroits.action')} isIcon right size="sm">
                            {this.renderAction(props.value, props)}
                        </Dropdown2>
                        {this.renderModal(props.row.original)}
                    </div>
                ),
            },
        ];
    }

    modifyItem(props) {
        const { t, changeStatus, searchParametrage, displayAlert } = this.props;
        const { body } = this.state;
        const { id } = props.row.original;
        const newStatus = props.value === 'Actif' ? 'Inactif' : 'Actif';
        const mode = props.row.original.parametrageRenouvellement.modeDeclenchement;
        const lastExec = props.row.original.parametrageRenouvellement.derniereExecution;
        if (newStatus === 'Actif' && mode === 'Manuel' && lastExec) {
            displayAlert(t('error:paramLastExec'), 'danger');
        } else {
            changeStatus(id, newStatus).then(() => {
                searchParametrage(body);
            });
        }
    }

    @autobind
    collapse() {
        const { expanded } = this.state;
        this.setExpanded(!expanded);
    }

    @autobind
    handleSubmit(values) {
        const { searchParametrage } = this.props;

        const body = {
            page: 1,
            perPage: 10,
        };
        extractFilterAmcs(body, values);
        this.setState({ body, expanded: false });
        searchParametrage(body);
    }

    @autobind
    fetchData(dat) {
        const { pageSize, pageIndex, sortBy } = dat;
        const { searchParametrage } = this.props;
        const { isFirstRun, body } = this.state;

        body.page = pageIndex + 1;
        body.perPage = pageSize;

        if (sortBy && sortBy.length > 0) {
            body.sortBy = sortBy[0].id;
            body.direction = sortBy[0].desc ? 'desc' : 'asc';
        }
        if (isFirstRun) {
            this.setState({ isFirstRun: false });
        } else {
            this.setState({ body });
            searchParametrage(body);
        }
    }

    renderTable() {
        const { paramDroitsList, totalPages, loading } = this.props;
        const { pageSizeState } = this.state;
        const body = paramDroitsList.map((item, index) => ({
            ...item,
            amc: `${item.amc} - ${item.amcNom}`,
            dateCreation: DateUtils.formatDisplayDate(item.dateCreation),
            key: index,
            identifiantCollectivite: item.identifiantCollectivite || '-',
            critereSecondaireDetaille: item.critereSecondaireDetaille || '-',
            groupePopulation: item.groupePopulation || '-',
            garanties: this.collectLotGT(item) || '-',
        }));

        return (
            <CgdTable
                id="paramTable"
                initialPageSize={pageSizeState}
                manual
                pageCount={totalPages}
                data={body}
                initialPageIndex={0}
                columns={this.getColumns()}
                initialSortBy={[{ id: 'dateCreation', desc: true }]}
                useDebounce={500}
                fetchData={this.fetchData}
                loading={loading}
                showPagination={totalPages > 1}
                withFilters
                height="60%"
            />
        );
    }

    render() {
        const {
            t,
            handleSubmit,
            invalid,
            totalElements,
            comboLabelsLightDeclarants,
            comboLabelsAllGT,
            comboLabelsAllLot,
            lotsParametrage,
            codesRenvoiList,
            codesItelis,
            showItelisCode,
        } = this.props;
        const { expanded, chips, counter, modal, parametrageDetail } = this.state;

        const toolbar = {
            label: t('headers:home.actions'),
            items: [
                {
                    label: t('parametersDroits.createParams'),
                    action: goToCreatePage,
                    allowedPermissions: PermissionConstants.SE_P_CREATE_GENERATION_TP_PARAMETERS_UI,
                },
            ],
        };

        const comboLabelsModeDeclenchement = [
            { label: 'Automatique', value: 'Automatique' },
            { label: 'Manuel', value: 'Manuel' },
            { label: 'Pilotage par le Back-Office', value: 'PilotageBO' },
        ];

        return (
            <form onSubmit={handleSubmit(this.handleSubmit)}>
                <BreadcrumbPart label={t('breadcrumb.parametrageDroitsTP')} parentPart={<ParametrageBreadcrumb />} />
                <PageLayout header={<BodyHeader title={t('parametersDroits.pageTitle')} toolbar={toolbar} />}>
                    <Filtering
                        chips={chips}
                        onCollapseClick={this.collapse}
                        expanded={expanded}
                        ref={this.getRef}
                        onCloseChip={this.onCloseChip}
                        filterLabel={t('filter')}
                    >
                        <Row className="align-items-end">
                            <Col xs="3" sm="3">
                                <Field
                                    id="file-tracking-search-emitter"
                                    name="amcs"
                                    component={CommonMultiCombobox}
                                    label={t('parametersDroits.amc')}
                                    options={comboLabelsLightDeclarants}
                                    placeholder={t('allFemale')}
                                    clearable
                                    searchable
                                    multi
                                />
                            </Col>
                            <Col xs="12" sm="6">
                                <Field
                                    id="search-filter-gts"
                                    name="gts"
                                    component={CommonMultiCombobox}
                                    label={t('parametersDroits.search_gt')}
                                    options={comboLabelsAllGT}
                                    placeholder={t('allFemale')}
                                    filterOptions={businessUtils.filterAndLimit50}
                                    onInputChange={(input) => this.autocompleteGTs(input)}
                                    clearable
                                    searchable
                                    multi
                                />
                            </Col>
                            <Col xs="12" sm="6">
                                <Field
                                    id="search-filter-lots"
                                    name="lots"
                                    component={CommonMultiCombobox}
                                    label={t('parametersDroits.search_lots')}
                                    options={comboLabelsAllLot}
                                    placeholder={t('allFemale')}
                                    clearable
                                    searchable
                                    multi
                                />
                            </Col>
                            <Col xs="3" sm="3">
                                <Field
                                    id="file-tracking-search"
                                    name="triggerMode"
                                    component={CommonMultiCombobox}
                                    label={t('parametersDroits.triggerMode')}
                                    options={comboLabelsModeDeclenchement}
                                    placeholder={t('allFemale')}
                                    clearable
                                    searchable
                                    multi
                                />
                            </Col>
                            <Col xs="3" sm="3">
                                <Field
                                    id="status"
                                    name="status"
                                    component={CommonInput}
                                    label={t('parametersDroits.disabledParams')}
                                    type="checkbox"
                                    formGroupClassName="mb-4"
                                />
                            </Col>
                            <Col xs="auto auto form-group" className="col">
                                <span className="pr-2">
                                    <Button
                                        type="button"
                                        outline
                                        onClick={this.resetSearchFunction}
                                        id="clear-search-beneficiary"
                                    >
                                        <CgIcon name="undo" size="1x" />
                                    </Button>
                                </span>
                                <Button id="submit-declarant-form" type="submit" behavior="primary" disabled={invalid}>
                                    {t('parametersDroits.apply')}
                                </Button>
                            </Col>
                        </Row>
                    </Filtering>
                    <Fragment>
                        <Panel
                            header={
                                <Feedback counter={counter} direction="right" behavior="success">
                                    <PanelHeader title={t('parametersDroits.result')} counter={totalElements} />
                                </Feedback>
                            }
                            border={false}
                            panelTheme="secondary"
                        />
                        {this.renderTable()}
                    </Fragment>
                    {parametrageDetail && (
                        <Modal size="full-width" isOpen={modal} toggle={() => this.toggle()} backdrop="static">
                            <ModalHeader toggle={() => this.toggle()}>
                                {t('parametersDroits.modalTitle', {
                                    amc: (parametrageDetail || {}).amc,
                                    date: (parametrageDetail || {}).dateCreation,
                                    user: (parametrageDetail || {}).userCreation,
                                })}
                            </ModalHeader>
                            <ModalBody>
                                <ParametrageDetail
                                    parametrage={parametrageDetail}
                                    allLot={lotsParametrage}
                                    codesRenvoiList={codesRenvoiList}
                                    codesItelis={codesItelis}
                                    showItelisCode={showItelisCode}
                                />
                            </ModalBody>
                        </Modal>
                    )}
                </PageLayout>
            </form>
        );
    }

    autocompleteGTsCall(input) {
        const { getAllGT } = this.props;
        if (input && input.length > 2) {
            const searchCriteria = {};
            searchCriteria.search = input;
            getAllGT(searchCriteria);
        }
    }

    collectLotGT(item) {
        const { t, lotsParametrage } = this.props;
        const separator = ', ';
        const stringLots = businessUtils.stringifyListLots(item.idLots, lotsParametrage);
        const stringGTs = businessUtils.stringifyListGTs(item.garantieTechniques);

        const res = [];
        if (stringLots) {
            res.push(`${t('parametersDroits.label_lots')} : ${stringLots}`);
        }
        if (stringGTs) {
            res.push(`${t('parametersDroits.label_gts')} : ${stringGTs}`);
        }
        return res.join(separator);
    }
}

ParametrageDroitsComponent.propTypes = {
    t: PropTypes.func,
    comboLabelsLightDeclarants: PropTypes.arrayOf(PropTypes.shape()),
    paramDroitsList: PropTypes.arrayOf(PropTypes.shape()),
    amcs: PropTypes.arrayOf(PropTypes.shape()),
    triggerMode: PropTypes.arrayOf(PropTypes.shape()),
    status: PropTypes.bool,
    handleSubmit: PropTypes.func,
    changeStatus: PropTypes.func,
    reset: PropTypes.func,
    change: PropTypes.func,
    invalid: PropTypes.bool,
    loading: PropTypes.bool,
    hasCreated: PropTypes.bool,
    location: PropTypes.shape(),
    getLightDeclarants: PropTypes.func,
    addAlert: PropTypes.func,
    searchParametrage: PropTypes.func,
    totalElements: PropTypes.number,
    totalPages: PropTypes.number,
    displayAlert: PropTypes.func,
    getAllGT: PropTypes.func,
    comboLabelsAllGT: PropTypes.arrayOf(PropTypes.shape()),
    getLots: PropTypes.func,
    comboLabelsAllLot: PropTypes.arrayOf(PropTypes.shape()),
    lotsParametrage: PropTypes.arrayOf(PropTypes.shape()),
    getAllCodeRenvoi: PropTypes.func,
    codesRenvoiList: PropTypes.arrayOf(PropTypes.shape()),
    getCodesItelis: PropTypes.func,
    codesItelis: PropTypes.arrayOf(PropTypes.shape()),
    showItelisCode: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    enableReinitialize: true,
    form: 'paramSearch',
})(withRouter(ParametrageDroitsComponent));
