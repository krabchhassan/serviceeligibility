/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
/* eslint-disable react/sort-comp */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import moment from 'moment';
import autobind from 'autobind-decorator';
import {
    BodyHeader,
    BreadcrumbPart,
    DesktopBreadcrumbPart,
    Button,
    ButtonGroup,
    CgdTable,
    Col,
    ConfirmationModal,
    Dropdown2,
    DropdownItem2,
    Filtering,
    HabilitationFragment,
    Modal,
    ModalBody,
    ModalHeader,
    PageLayout,
    Panel,
    PanelHeader,
    Row,
    ProgressBar,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import get from 'lodash/get';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import { CommonDatePicker, CommonInput, CommonMultiCombobox } from '../../../../common/utils/Form/CommonFields';
import DateUtils from '../../../../common/utils/DateUtils';
import Constants from './Constants';
import utils from '../../../../common/utils/businessUtils';
import StringUtils from '../../../../common/utils/StringUtils';
import ValidationFactories from '../../../../common/utils/Form/ValidationFactories';
import PermissionConstants from '../../PermissionConstants';
import TriggerDetail from './components/TriggerDetail';
import style from '../ParametrageDroits/style.module.scss';
import TriggerRecycleHistory from './components/TriggerRecycleHistory';

const statusBehaviorMapping = {
    Deleted: 'secondary',
    Processed: 'success',
    ProcessedWithErrors: 'danger',
    Processing: 'primary',
    ProcessedWithWarnings: 'warning',
    StandBy: 'warning',
    ToProcess: 'info',
    Abandonned: 'default',
    Abandoning: 'default',
};
/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const statusRawOptions = [
    'Deleted',
    'Processed',
    'ProcessedWithErrors',
    'ProcessedWithWarnings',
    'Processing',
    'StandBy',
    'ToProcess',
    'Abandonned',
    'Abandoning',
];

const maxLength13 = ValidationFactories.maxLengthWithoutSpace(13);

const endDateValidator = ValidationFactories.dateComparator(
    'isSameOrAfter',
    (allValues) => get(allValues, 'dateDebut'),
    'eligibility:triggers.sameOrAfterDebut',
);

const normalizeRo = (value) => {
    if (!value) {
        return value;
    }
    return utils.formatRO(value);
};

const dateFormat = 'YYYY-MM-DD';

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common', 'breadcrumb'], { wait: true })
class TriggersComponent extends Component {
    constructor(props) {
        super(props);
        const { t } = props;
        this.state = {
            isFirstRun: true,
            expanded: true,
            contratIndividuelSelected: true,
            amcOptions: [],
            modal: false,
            triggerDetail: null,
            modalHistory: false,
            recyclageHistory: null,
            showAlert: true,
        };

        this.statusOptions = statusRawOptions.map((item) => ({ label: t(`triggers.${[item]}`), value: item }));
    }

    componentDidMount() {
        const { t, getLightDeclarants, searchTriggers, change } = this.props;
        getLightDeclarants();

        change('status', [
            {
                value: 'ProcessedWithErrors',
                label: t('triggers.ProcessedWithErrors'),
            },
            {
                value: 'StandBy',
                label: t('triggers.StandBy'),
            },
        ]);

        const body = {
            status: 'ProcessedWithErrors,StandBy',
            page: 1,
            perPage: 10,
        };

        // eslint-disable-next-line react/no-did-mount-set-state
        this.setState({ body });

        searchTriggers(body);
    }

    componentWillReceiveProps(nextProps) {
        const { lightDeclarants } = this.props;
        const { amcOptions } = this.state;
        if (
            nextProps.lightDeclarants !== lightDeclarants ||
            (lightDeclarants && amcOptions && amcOptions.length === 0)
        ) {
            this.setState({
                amcOptions: nextProps.lightDeclarants.map((item) => ({
                    value: item.number,
                    label: `${item.label} - ${item.number}`,
                })),
            });
        }
    }

    @autobind
    setExpanded(a) {
        this.setState({ expanded: a });
    }

    @autobind
    getRef(ref) {
        this.ref = ref;
    }

    @autobind
    getReferenceFromAbandonModal(ref) {
        this.abandonModalRef = ref;
    }

    @autobind
    openAbandonModal() {
        this.abandonModalRef.open();
    }

    toggle(triggerDetail) {
        const { modal } = this.state;
        this.setState({ modal: !modal, triggerDetail });
    }

    toggleHistory(recyclageHistory) {
        const { modalHistory } = this.state;
        this.setState({ modalHistory: !modalHistory, recyclageHistory });
    }

    colour = (value) => {
        if (value !== 0) {
            return { color: 'red' };
        }
        return {};
    };

    renderModal(onClick) {
        const { t } = this.props;
        return (
            <Button id="historique-button" outlineNoBorder behavior="primary" onClick={onClick}>
                {`${t('details')} `}
                <CgIcon name="right-scroll" />
            </Button>
        );
    }

    getColumns() {
        const { t } = this.props;

        return [
            {
                Header: StringUtils.splitIntoDivs(t('triggers.statusTitle')),
                accessor: 'status',
                disableSortBy: true,
                id: 'status',
                Cell: (props) => (
                    <div>
                        {props.value !== 'Processing' ? (
                            <ProgressBar
                                value={100}
                                max={100}
                                behavior={statusBehaviorMapping[props.value]}
                                style={{ width: '140px', height: '20px' }}
                            >
                                <span style={{ fontSize: '1.3em' }}>
                                    {(this.statusOptions.find((item) => item.value === props.value) || {}).label}
                                </span>
                            </ProgressBar>
                        ) : (
                            <ProgressBar
                                value={props.row.original.nbBenef - props.row.original.nbBenefToProcess}
                                max={props.row.original.nbBenef}
                                behavior={statusBehaviorMapping[props.value]}
                                animated
                                style={{
                                    width: '150px',
                                    height: '20px',
                                    border: 'solid orange',
                                    'border-width': '1px',
                                }}
                            >
                                <span style={{ 'font-size': '1.3em', margin: '5px' }}>
                                    {(this.statusOptions.find((item) => item.value === props.value) || {}).label}
                                </span>
                            </ProgressBar>
                        )}
                    </div>
                ),
            },
            {
                Header: StringUtils.splitIntoDivs(t('triggers.debutDeTraitement')),
                accessor: 'dateDebutTraitement',
                id: 'dateDebutTraitement',
                Cell: (props) => DateUtils.formatDisplayDateTime(props.value),
                width: '12%',
            },
            {
                Header: StringUtils.splitIntoDivs(t('triggers.finDeTraitement')),
                accessor: 'dateFinTraitement',
                id: 'dateFinTraitement',
                Cell: (props) => DateUtils.formatDisplayDateTime(props.value),
                width: '12%',
            },
            {
                Header: StringUtils.splitIntoDivs(t('triggers.organism')),
                accessor: 'amc',
                disableSortBy: true,
                id: 'amc',
                Cell: (props) => <span>{this.getAmcLabel(props.value)}</span>,
            },
            {
                Header: StringUtils.splitIntoDivs(t('triggers.event')),
                accessor: 'origine',
                disableSortBy: true,
                id: 'origine',
                Cell: (props) => t(`triggers.${props.value}`),
                width: '15%',
            },
            {
                Header: StringUtils.splitIntoDivs(t('triggers.nbBenefKO')),
                accessor: 'nbBenefKO',
                disableSortBy: true,
                id: 'nbBenefKO',
                width: '7%',
                Cell: (props) => <span style={this.colour(props.value)}>{props.value !== 0 ? props.value : '-'}</span>,
            },
            {
                Header: StringUtils.splitIntoDivs(t('triggers.nbBenef')),
                accessor: 'nbBenef',
                disableSortBy: true,
                id: 'nbBenef',
                width: '7%',
            },
            {
                Header: StringUtils.splitIntoDivs(t('triggers.recycleHistory')),
                width: '7%',
                disableSortBy: true,
                id: 'recycleHistory',
                Cell: (props) => this.renderModal(() => this.toggleHistory(props.row.original)),
            },
            {
                id: 'modif',
                width: '14%',
                accessor: 'status',
                disableSortBy: true,
                Cell: (props) => (
                    <div className={style['align-table-elements']}>
                        {this.renderAction(props.value, props.row.original.id, props.row.original.status)}
                        {this.renderModal(() => this.toggle(props.row.original))}
                    </div>
                ),
            },
        ];
    }

    getAmcLabel(amcCode) {
        const { amcOptions } = this.state;
        return ((amcOptions || []).find((item) => item.value === amcCode) || {}).label;
    }

    @autobind
    cancelProcess(id) {
        const { callCancelProcess, searchTriggers } = this.props;
        const { body } = this.state;
        callCancelProcess(id).then(() => {
            searchTriggers(body);
        });
    }

    @autobind
    runRecycling(id) {
        const { callRecycle, searchTriggers, addAlertError } = this.props;
        const { body } = this.state;
        callRecycle(id)
            .then(() => {
                searchTriggers(body);
            })
            .catch((error) =>
                addAlertError({
                    message: error.response.data.errors[0].message,
                    behavior: 'danger',
                }),
            );
    }

    @autobind
    confirmAbandon() {
        const { callAbandon, searchTriggers } = this.props;
        const { body, idToAbandon } = this.state;
        callAbandon(idToAbandon).then(() => {
            searchTriggers(body);
            this.setState({ idToAbandon: null });
            this.setState({ showAlert: true });
        });
    }

    @autobind
    abandonTrigger(id) {
        this.setState({ idToAbandon: id });
        this.openAbandonModal();
    }

    @autobind
    triggerGeneration(id) {
        const { callTriggerGeneration, searchTriggers } = this.props;
        const { body } = this.state;
        callTriggerGeneration(id).then(() => {
            searchTriggers(body);
        });
    }

    createDropDownItem(icon, functionToCall, label, id, habilitation) {
        const { t } = this.props;
        return (
            <HabilitationFragment allowedPermissions={habilitation}>
                <DropdownItem2
                    type="item"
                    label={t(`triggers.${label}`)}
                    id={id}
                    action={() => functionToCall(id)}
                    icon={icon}
                />
            </HabilitationFragment>
        );
    }

    @autobind
    collapse() {
        const { expanded } = this.state;
        this.setExpanded(!expanded);
    }

    @autobind
    resetSearchFunction() {
        const { reset, change } = this.props;
        change('status', [{}]);
        const body = {
            status: '',
            page: 1,
            perPage: 10,
        };

        // eslint-disable-next-line react/no-did-mount-set-state
        this.setState({ body });
        reset();
    }

    @autobind
    handleSubmit(values) {
        const { searchTriggers } = this.props;
        const body = this.buildBody(values, 1, 10);
        this.setState({ body });
        searchTriggers(body);
    }

    setEmitters = (body) => {
        if (body.Renewal || body.Request || body.Event) {
            const emitters = [];
            if (body.Renewal) {
                emitters.push('Renewal');
            }
            if (body.Request) {
                emitters.push('Request');
            }
            if (body.Event) {
                emitters.push('Event');
            }
            body.emitters = emitters.join(',');
        }
    };

    @autobind
    fetchData(dat) {
        const { pageSize, pageIndex, sortBy } = dat;
        const { searchTriggers } = this.props;
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
            searchTriggers(body);
        }
    }

    buildBody(values, pageNumber, pageSize) {
        const { amcs, status, dateDebut, dateFin, contractNumber, nir, contratType } = values;
        const body = {
            ...values,
            isContratIndividuel: contratType !== Constants.VALUES.contratCollectif,
            page: pageNumber,
            perPage: pageSize,
        };
        delete body.contratType;
        if (amcs) {
            body.amcs = amcs.map((item) => item.value).join(',');
        }
        if (status) {
            body.status = status.map((item) => item.value).join(',');
        }
        if (dateDebut) {
            body.dateDebut = DateUtils.formatServerDate(dateDebut, dateFormat);
        } else {
            delete body.dateDebut;
        }
        if (dateFin) {
            // We add 1 day to dateFin to include that day in the search (otherwise, dateFin is excluded)
            const includingDateFin = moment(dateFin).add(1, 'days');
            body.dateFin = DateUtils.formatServerDate(includingDateFin, dateFormat);
        } else {
            delete body.dateFin;
        }
        if (nir) {
            body.nir = nir.replaceAll(/\s/g, '');
        }
        this.setEmitters(body);
        if (body.owner) {
            body.owner = 'true';
        }

        if (contractNumber) {
            body.numeroContrat = contractNumber;
        }
        delete body.Renewal;
        delete body.Request;
        delete body.Event;
        delete body.contractNumber;
        return body;
    }

    changeContrat() {
        const { change } = this.props;
        const { contratIndividuelSelected } = this.state;
        if (contratIndividuelSelected) {
            change(Constants.FIELDS.contratType, Constants.VALUES.contratCollectif);
        } else {
            change(Constants.FIELDS.contratType, Constants.VALUES.contratIndividuel);
        }
        this.setState({
            contratIndividuelSelected: !contratIndividuelSelected,
        });
    }

    renderFilters() {
        const { t, invalid } = this.props;
        const { contratIndividuelSelected, amcOptions } = this.state;

        return (
            <Fragment>
                <Row>
                    <Col xs="4">
                        <h4 className="text-colored-mix-0">{t('triggers.dateTitle')}</h4>
                        <Row>
                            <Col xs="6">
                                <Field
                                    name={Constants.FIELDS.dateDebut}
                                    component={CommonDatePicker}
                                    label={t('triggers.dateDebut')}
                                />
                            </Col>
                            <Col>
                                <Field
                                    name={Constants.FIELDS.dateFin}
                                    component={CommonDatePicker}
                                    label={t('triggers.dateFin')}
                                    validate={endDateValidator}
                                />
                            </Col>
                        </Row>
                    </Col>
                    <Col xs="8">
                        <h4 className="text-colored-mix-0">{t('triggers.amcTitle')}</h4>
                        <Field
                            id="amcs"
                            name={Constants.FIELDS.amcs}
                            component={CommonMultiCombobox}
                            label={t('triggers.amcs')}
                            options={amcOptions}
                            placeholder={t('all')}
                            clearable
                            searchable
                            multi
                        />
                    </Col>
                </Row>
                <Row>
                    <Col xs="4">
                        <ButtonGroup className="pb-2">
                            <Button
                                onClick={() => this.changeContrat()}
                                outline
                                type="button"
                                active={contratIndividuelSelected}
                            >
                                {t('triggers.contratIndividuel')}
                            </Button>
                            <Button
                                onClick={() => this.changeContrat()}
                                outline
                                type="button"
                                active={!contratIndividuelSelected}
                            >
                                {t('triggers.contratCollectif')}
                            </Button>
                        </ButtonGroup>
                        <div className="pt-4">
                            <Field
                                id="parametrage-contrat-number"
                                name="contractNumber"
                                component={CommonInput}
                                placeholder={t('triggers.contratPlaceholder')}
                            />
                        </div>
                    </Col>
                    <Col xs="4">
                        <h4 className="text-colored-mix-0">{t('triggers.nirTitle')}</h4>

                        <Field
                            id="file-tracking-search-emitter"
                            name={Constants.FIELDS.nir}
                            component={CommonInput}
                            label={t('triggers.nir')}
                            placeholder={t('triggers.nirPlaceholder')}
                            normalize={normalizeRo}
                            validate={maxLength13}
                        />
                    </Col>
                    <Col xs="4">
                        <h4 className="text-colored-mix-0">{t('triggers.statusTitle')}</h4>
                        <Field
                            id="file-tracking-search-emitter"
                            name={Constants.FIELDS.status}
                            component={CommonMultiCombobox}
                            options={this.statusOptions}
                            placeholder={t('all')}
                            label={t('triggers.statusHint')}
                            clearable
                            searchable
                            multi
                        />
                    </Col>
                </Row>
                <Row>
                    <Col xs="7">
                        <h4 className="text-colored-mix-0">{t('triggers.ticksTitle')}</h4>
                        <Row>
                            <Col xs="3" className="">
                                <Field
                                    id="Renewal"
                                    name="Renewal"
                                    component={CommonInput}
                                    label={t('triggers.Renewal')}
                                    type="checkbox"
                                    formGroupClassName="pl-0"
                                />
                            </Col>

                            <Col xs="3">
                                <Field
                                    id="Request"
                                    name="Request"
                                    component={CommonInput}
                                    label={t('triggers.Request')}
                                    type="checkbox"
                                />
                            </Col>
                            <Col xs="3">
                                <Field
                                    id="Event"
                                    name="Event"
                                    component={CommonInput}
                                    label={t('triggers.Event')}
                                    type="checkbox"
                                />
                            </Col>
                            <Col xs="1" className="d-flex justify-content-center">
                                <div className="dividerV-nomargin" />
                            </Col>
                            <Col xs="2">
                                <Field
                                    id="tick4"
                                    name="owner"
                                    component={CommonInput}
                                    label={t('triggers.tick4')}
                                    type="checkbox"
                                />
                            </Col>
                        </Row>
                    </Col>
                    <Col className="d-flex form-group pt-5 justify-content-end" xs="5">
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
                        <Button
                            id="submit-declarant-form"
                            type="submit"
                            behavior="primary"
                            disabled={invalid}
                            onClick={() => this.setState({ showAlert: true })}
                        >
                            {t('parametersDroits.apply')}
                        </Button>
                    </Col>
                </Row>
            </Fragment>
        );
    }

    renderAction(value, id, status) {
        const { t } = this.props;
        if (status === 'Abandoning') {
            return <div className={style.separator} />;
        }
        if (value === 'StandBy') {
            return (
                <Dropdown2 id="id-2" label={t('parametersDroits.action')} isIcon right size="sm">
                    {this.createDropDownItem(
                        'file-export',
                        this.triggerGeneration,
                        'triggerGeneration',
                        id,
                        PermissionConstants.TRIGGER_GENERATE,
                    )}
                    {this.createDropDownItem(
                        'file-xmark',
                        this.cancelProcess,
                        'cancelProcess',
                        id,
                        PermissionConstants.TRIGGER_CANCEL,
                    )}
                </Dropdown2>
            );
        }
        if (value === 'ProcessedWithErrors') {
            return (
                <Dropdown2 id="id-2" label={t('parametersDroits.action')} isIcon right size="sm">
                    {this.createDropDownItem(
                        'recycle',
                        this.runRecycling,
                        'runRecycling',
                        id,
                        PermissionConstants.TRIGGER_RECYCLE,
                    )}
                    {this.createDropDownItem(
                        'cancel-trigger',
                        this.abandonTrigger,
                        'showAbandon',
                        id,
                        PermissionConstants.TRIGGER_ABANDON,
                    )}
                </Dropdown2>
            );
        }
        return <div className={style.separator} />;
    }

    renderTable() {
        const { t, triggers, totalPages, loading, addAlert } = this.props;
        const { pageSizeState, showAlert } = this.state;
        const body = triggers.map((item) => ({
            ...item,
            debutTraitement: DateUtils.formatDisplayDate(item.debutTraitement),
            finTraitement: DateUtils.formatDisplayDate(item.finTraitement),
        }));
        const hasAbandoningStatus = triggers.some((item) => item.status === 'Abandoning');

        if (hasAbandoningStatus && showAlert) {
            addAlert({
                message: t('triggers.triggerAbandoning'),
            });
            this.setState({ showAlert: false });
        }

        return (
            <CgdTable
                id="paramTable"
                initialPageSize={pageSizeState}
                manual
                pageCount={totalPages}
                showPagination={totalPages > 1}
                data={body}
                initialPageIndex={0}
                columns={this.getColumns()}
                initialSortBy={[{ id: 'dateDebutTraitement', desc: true }]}
                useDebounce={500}
                fetchData={this.fetchData}
                loading={loading}
                withFilters
            />
        );
    }

    render() {
        const { t, handleSubmit, totalElements } = this.props;
        const { expanded, modal, triggerDetail, modalHistory, recyclageHistory } = this.state;

        return (
            <form onSubmit={handleSubmit(this.handleSubmit)}>
                <PageLayout header={<BodyHeader title={t('triggers.pageTitle')} />}>
                    <BreadcrumbPart
                        label={t('breadcrumb.triggerTracking')}
                        parentPart={<DesktopBreadcrumbPart label={t('breadcrumb.tracking')} path="/tracking" />}
                    />
                    <Filtering
                        onCollapseClick={this.collapse}
                        expanded={expanded}
                        ref={this.getRef}
                        filterLabel={t('filters')}
                    >
                        {this.renderFilters()}
                    </Filtering>

                    <Panel
                        header={<PanelHeader title={t('triggers.resultTitle')} counter={totalElements} />}
                        border={false}
                        panelTheme="secondary"
                    >
                        {this.renderTable()}
                    </Panel>

                    <Modal size="full-width" isOpen={modal} toggle={() => this.toggle()} backdrop="static">
                        <ModalHeader toggle={() => this.toggle()}>
                            {t('triggers.modalTitle', {
                                amc: this.getAmcLabel((triggerDetail || {}).amc),
                                date: DateUtils.formatDisplayDateTime((triggerDetail || {}).dateCreation),
                            })}
                        </ModalHeader>
                        <ModalBody>
                            <TriggerDetail trigger={triggerDetail} />
                        </ModalBody>
                    </Modal>

                    <Modal
                        size="full-width"
                        isOpen={modalHistory}
                        toggle={() => this.toggleHistory()}
                        backdrop="static"
                    >
                        <ModalHeader toggle={() => this.toggleHistory()}>{t('recycleHistory.header')}</ModalHeader>
                        <ModalBody>
                            <TriggerRecycleHistory trigger={recyclageHistory} />
                        </ModalBody>
                    </Modal>

                    <ConfirmationModal
                        ref={this.getReferenceFromAbandonModal}
                        onConfirm={() => this.confirmAbandon()}
                        onCancel={() => {
                            this.setState({ idToAbandon: null });
                        }}
                        message={t('triggers.abandonModalText')}
                        title={t('triggers.abandonModalTitle')}
                        confirmButton={t('yes')}
                        cancelButton={t('no')}
                    />
                </PageLayout>
            </form>
        );
    }
}

TriggersComponent.propTypes = {
    t: PropTypes.func,
    lightDeclarants: PropTypes.arrayOf(PropTypes.shape()),
    triggers: PropTypes.arrayOf(PropTypes.shape()),
    handleSubmit: PropTypes.func,
    change: PropTypes.func,
    reset: PropTypes.func,
    invalid: PropTypes.bool,
    loading: PropTypes.bool,
    addAlertError: PropTypes.func,
    addAlert: PropTypes.func,
    getLightDeclarants: PropTypes.func,
    searchTriggers: PropTypes.func,
    callTriggerGeneration: PropTypes.func,
    callCancelProcess: PropTypes.func,
    callRecycle: PropTypes.func,
    callAbandon: PropTypes.func,

    totalElements: PropTypes.number,
    totalPages: PropTypes.number,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    enableReinitialize: true,
    form: Constants.FORM_NAME,
})(TriggersComponent);
