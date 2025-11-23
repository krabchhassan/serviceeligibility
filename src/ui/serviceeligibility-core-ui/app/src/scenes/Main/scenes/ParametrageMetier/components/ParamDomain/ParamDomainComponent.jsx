/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import { Field, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';

import {
    Button,
    Col,
    ConfirmationModal,
    HabilitationFragment,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Tooltip,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import autobind from 'autobind-decorator';
import 'react-table/react-table.css';
import permissionConstants from '../../../../PermissionConstants';
import formConstants from './Constants';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import { CommonInput } from '../../../../../../common/utils/Form/CommonFields';
import style from './style.module.scss';
import ParamsUnsortedTable from '../ParamsUnsortedTable';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
const isNumber = ValidationFactories.isNumber();
const priorityValidation = ValidationFactories.inBetweenValue(0, 9);
const createField = (name, input, disabled, isPriority) => {
    if (isPriority) {
        return (
            <Field
                name={name}
                component={input}
                validate={[required, isNumber, priorityValidation]}
                inline
                labelPortion={0}
            />
        );
    }

    if (disabled) {
        return <Field name={name} component={input} validate={required} inline labelPortion={0} disabled />;
    }

    return <Field name={name} component={input} validate={required} inline labelPortion={0} />;
};

const createCheckboxField = (name, input, disabled, value) => {
    if (value) {
        return (
            <Field
                name={name}
                component={input}
                validate={required}
                disabled={disabled}
                type="checkbox"
                value={value}
            />
        );
    }

    return (
        <Field
            name={name}
            component={input}
            validate={required}
            inline
            labelPortion={0}
            disabled={disabled}
            type="checkbox"
        />
    );
};

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class ParamDomainComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isCreation: false,
            isUpdating: false,
            indexToDelete: -1,
            editingIndex: -1,
            firstEdit: false,
            initializingEdit: false,
        };
    }

    componentDidMount() {
        const { getAllDomains } = this.props;

        getAllDomains();
    }

    componentDidUpdate(prevProps) {
        const { initializingEdit } = this.state;

        if (!initializingEdit) {
            this.handleDomainIsAddingWarrantiesChange(prevProps);
            this.handleDomainIsAddingWarrantiesCappedChange(prevProps);
        }

        if (initializingEdit) {
            this.resetInitializingEdit();
        }
    }

    /**
     * Setting delete modal reference
     * @param {*} ref delete modal reference
     */
    @autobind
    getReferenceFromDeleteModal(ref) {
        this.deleteModalRef = ref;
    }

    handleDomainIsAddingWarrantiesChange(prevProps) {
        const { domainIsAddingWarranties, domainIsAddingWarrantiesCapped, change } = this.props;

        if (domainIsAddingWarranties !== prevProps.domainIsAddingWarranties) {
            if (domainIsAddingWarranties && !domainIsAddingWarrantiesCapped) {
                change(formConstants.FIELDS.domainIsAddingWarrantiesCapped, true);
            } else if (!domainIsAddingWarranties) {
                change(formConstants.FIELDS.domainIsAddingWarrantiesCapped, false);
            }
        }
    }

    handleDomainIsAddingWarrantiesCappedChange(prevProps) {
        const { domainIsAddingWarranties, domainIsAddingWarrantiesCapped, change } = this.props;

        if (domainIsAddingWarrantiesCapped !== prevProps.domainIsAddingWarrantiesCapped) {
            if (domainIsAddingWarrantiesCapped && !domainIsAddingWarranties) {
                change(formConstants.FIELDS.domainIsAddingWarranties, true);
            }
        }
    }

    resetInitializingEdit() {
        this.setState({ initializingEdit: false });
    }

    /**
     * @description Calling action to delete a domain and changing state to read mode
     * @param {*} index domain's index to delete
     */
    deleteDomain(index) {
        const { domainList, deleteDomain } = this.props;
        const domainToDelete = domainList[index];

        deleteDomain(domainToDelete.code);
        delete domainList[index];
        this.switchToReadMode(false);
    }

    /**
     * @description Switching to read mode
     */
    switchToReadMode(retrieveDomains = true) {
        const { reset, getAllDomains } = this.props;

        reset(formConstants.FORM_NAME);

        if (retrieveDomains) {
            getAllDomains();
        }
        this.setState({ isUpdating: false, isCreation: false });
    }

    /**
     * @description Switch line in edition mode
     * @param {*} index
     *      line index for edition
     */
    switchToEditMode(index) {
        const { change, domainList } = this.props;

        const editedData = Object.values(domainList)[index];

        // Updating code and libelle on edit form
        change(formConstants.FIELDS.domainCode, editedData.code);
        change(formConstants.FIELDS.domainName, editedData.libelle);
        change(formConstants.FIELDS.domainTranscode, editedData.transcodification);
        change(formConstants.FIELDS.domainCategory, editedData.categorie);
        change(formConstants.FIELDS.domainPriority, editedData.priorite);
        change(formConstants.FIELDS.domainIsAddingWarranties, editedData.isCumulGaranties);
        change(formConstants.FIELDS.domainIsAddingWarrantiesCapped, editedData.isCumulPlafonne);

        this.setState({ isUpdating: true, editingIndex: index, firstEdit: true, initializingEdit: true });
    }

    /**
     * @description Display deletion modal
     * @param {*} index
     *      line index for deletion
     */
    showDeleteModal(index) {
        this.setState({
            indexToDelete: index,
        });

        this.openDeleteModal();
    }

    /**
     * @description open modal to delete domain
     */
    @autobind
    openDeleteModal() {
        this.deleteModalRef.open();
    }

    toggleAddDomain() {
        const { change, getAllDomains } = this.props;
        const { isCreation } = { ...this.state };

        // Empty form
        change(formConstants.FIELDS.domainCode, null);
        change(formConstants.FIELDS.domainName, null);
        change(formConstants.FIELDS.domainTranscode, null);
        change(formConstants.FIELDS.domainCategory, null);
        change(formConstants.FIELDS.domainPriority, null);
        change(formConstants.FIELDS.domainIsAddingWarranties, null);
        change(formConstants.FIELDS.domainIsAddingWarrantiesCapped, null);

        getAllDomains();
        this.setState({ isCreation: !isCreation });
    }

    /**
     * @description domain creation
     */
    createDomain() {
        const {
            createDomain,
            domainCode,
            domainName,
            domainTranscode,
            domainCategory,
            domainPriority,
            domainIsAddingWarranties,
            domainIsAddingWarrantiesCapped,
            domainList,
            reset,
            addAlert,
            t,
        } = this.props;

        // Checking if domain is already existing
        const existingDomain = Object.values(domainList).filter((domain) => domain.code === domainCode);

        if (existingDomain.length === 0) {
            const domain = {
                code: domainCode,
                libelle: domainName,
                transcodification: domainTranscode,
                categorie: domainCategory,
                priorite: domainPriority === '' ? '0' : domainPriority,
                isCumulGaranties: domainIsAddingWarranties === '' ? false : domainIsAddingWarranties,
                isCumulPlafonne: domainIsAddingWarrantiesCapped === '' ? false : domainIsAddingWarrantiesCapped,
            };

            createDomain(domain).then(() => {
                // Updating creation status
                this.toggleAddDomain();

                // Empty form
                reset(formConstants.FORM_NAME);
            });
        } else {
            // ALert
            addAlert({
                message: t('parameters.domainExists'),
                behavior: 'danger',
            });
        }
    }

    updateDomain() {
        const {
            domainCode,
            domainName,
            domainTranscode,
            domainCategory,
            domainPriority,
            domainIsAddingWarranties,
            domainIsAddingWarrantiesCapped,
            updateDomain,
            addAlert,
            t,
        } = this.props;

        const domainToUpdate = {
            code: domainCode,
            libelle: domainName,
            transcodification: domainTranscode,
            categorie: domainCategory,
            priorite: domainPriority === '' ? '0' : domainPriority,
            isCumulGaranties: domainIsAddingWarranties === '' ? false : domainIsAddingWarranties,
            isCumulPlafonne: domainIsAddingWarrantiesCapped === '' ? false : domainIsAddingWarrantiesCapped,
        };

        updateDomain(domainToUpdate).then(() => {
            this.switchToReadMode();
            addAlert({
                message: t('parameters.updated'),
                behavior: 'success',
                timeout: 5000,
            });
        });
    }

    generateRowsData() {
        const { domainList, invalid, pristine, t } = this.props;
        const { isCreation, isUpdating, editingIndex } = this.state;

        const generateEditButton = (index) => {
            const additionalProp = {};
            if (isUpdating || isCreation) {
                additionalProp.disabled = true;
            }
            return (
                <Button
                    {...additionalProp}
                    behavior="default"
                    outlineNoBorder
                    onClick={() => this.switchToEditMode(index)}
                >
                    <CgIcon name="pencil" className="default" />
                </Button>
            );
        };

        let domains = {};
        if (domainList) {
            domains = { ...domainList };
        }

        const actions = (index) => (
            <Fragment>
                <HabilitationFragment allowedPermissions={permissionConstants.CREATE_PARAM_DATA_PERMISSION}>
                    {generateEditButton(index)}
                </HabilitationFragment>

                <HabilitationFragment allowedPermissions={permissionConstants.DELETE_PARAM_DATA_PERMISSION}>
                    <Button
                        behavior="default"
                        outlineNoBorder
                        onClick={() => this.showDeleteModal(index)}
                        disabled={isUpdating || isCreation}
                    >
                        <CgIcon name="trash" className="default" />
                    </Button>
                </HabilitationFragment>
            </Fragment>
        );
        const rowsData = Object.values(domains).map((item, index) => ({
            key: index,
            code: item.code,
            libelle: item.libelle,
            transcodification: item.transcodification,
            categorie: item.categorie,
            priorite: item.priorite,
            isCumulGaranties: !item.isCumulGaranties || item.isCumulGaranties === '' ? false : item.isCumulGaranties,
            isCumulPlafonne: !item.isCumulPlafonne || item.isCumulPlafonne === '' ? false : item.isCumulPlafonne,
            actions: () => actions(index),
        }));
        if (isCreation) {
            const creationActions = () => (
                <div className={style['domain-line-actions']}>
                    <Button behavior="primary" onClick={() => this.createDomain()} disabled={invalid || pristine}>
                        {t('create')}
                    </Button>
                    <Button
                        outline
                        behavior="secondary"
                        className={style['cancel-button']}
                        onClick={() => this.switchToReadMode()}
                    >
                        {t('cancel')}
                    </Button>
                </div>
            );
            const creationLine = {
                key: 'new',
                code: createField(formConstants.FIELDS.domainCode, CommonInput, false, false),
                libelle: createField(formConstants.FIELDS.domainName, CommonInput, false, false),
                transcodification: createField(formConstants.FIELDS.domainTranscode, CommonInput, false, false),
                categorie: createField(formConstants.FIELDS.domainCategory, CommonInput, false, false),
                isCumulGaranties: false,
                isCumulPlafonne: false,
                priorite: createField(formConstants.FIELDS.domainPriority, CommonInput, false, true),
                actions: () => creationActions(),
            };

            rowsData.unshift(creationLine);
        }
        if (isUpdating && !isCreation) {
            const actionButtons = () => (
                <div className={style['domain-line-actions']}>
                    <Button behavior="primary" onMouseDown={() => this.updateDomain()} disabled={invalid || pristine}>
                        {t('edit')}
                    </Button>
                    <Button
                        outline
                        behavior="secondary"
                        className={style['cancel-button']}
                        onClick={() => this.switchToReadMode()}
                    >
                        {t('cancel')}
                    </Button>
                </div>
            );

            const updateLine = {
                key: 'update',
                code: createField(formConstants.FIELDS.domainCode, CommonInput, true, false),
                libelle: createField(formConstants.FIELDS.domainName, CommonInput, false, false),
                transcodification: createField(formConstants.FIELDS.domainTranscode, CommonInput, false, false),
                categorie: createField(formConstants.FIELDS.domainCategory, CommonInput, false, false),
                priorite: createField(formConstants.FIELDS.domainPriority, CommonInput, false, true),
                isCumulGaranties: false,
                isCumulPlafonne: false,
                actions: () => actionButtons(),
            };

            rowsData[editingIndex] = updateLine;
        }

        return rowsData;
    }

    renderTable() {
        const { t } = this.props;

        const rowsData = this.generateRowsData();

        const columns = [
            {
                id: 'code',
                Header: t('parameters.code'),
                accessor: 'code',
                minWidth: 30,
                headerStyle: { textAlign: 'left' },
            },
            {
                id: 'label',
                Header: t('parameters.label'),
                accessor: 'libelle',
                headerStyle: { textAlign: 'left' },
            },
            {
                Header: t('parameters.isAddingWarranties'),
                accessor: 'isCumulGaranties',
                minWidth: 30,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.domainIsAddingWarranties,
                            CommonInput,
                            false,
                            original.isCumulGaranties,
                        );
                    }
                    return (
                        <div id={`checkbox-param-isAddingWarranties-${original.key}`}>
                            <input
                                type="checkbox"
                                className="checkbox"
                                checked={original.isCumulGaranties === true}
                                disabled
                            />
                        </div>
                    );
                },
            },
            {
                Header: t('parameters.isAddingWarrantiesCapped'),
                accessor: 'isCumulPlafonne',
                minWidth: 30,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.domainIsAddingWarrantiesCapped,
                            CommonInput,
                            false,
                            original.isCumulPlafonne,
                        );
                    }
                    return (
                        <div id={`checkbox-param-isAddingWarranties-${original.key}`}>
                            <input
                                type="checkbox"
                                className="checkbox"
                                checked={original.isCumulPlafonne === true}
                                disabled
                            />
                        </div>
                    );
                },
            },
            {
                id: 'priorite',
                Header: (
                    <span>
                        {t('parameters.priority')}{' '}
                        <CgIcon fixedWidth name="information-tooltip" id="information-tooltip" />{' '}
                        <Tooltip placement="top" target="information-tooltip">
                            {t('parameters.tooltipPriority')}
                        </Tooltip>
                    </span>
                ),
                accessor: 'priorite',
                minWidth: 30,
                headerStyle: { textAlign: 'left' },
            },
            {
                id: 'categorie',
                Header: t('parameters.category'),
                accessor: 'categorie',
                minWidth: 20,
                headerStyle: { textAlign: 'left' },
            },
            {
                id: 'transcode',
                Header: t('parameters.transcode'),
                accessor: 'transcodification',
                minWidth: 30,
                headerStyle: { textAlign: 'left' },
            },
            {
                id: 'actions',
                Header: '',
                accessor: 'actions',
                Cell: (props) => props.value(),
                minWidth: 30,
            },
        ];

        return <ParamsUnsortedTable rowsData={rowsData} columns={columns} />;
    }

    render() {
        const { t } = this.props;
        const { indexToDelete } = this.state;
        return (
            <Fragment>
                <Panel
                    header={
                        <PanelHeader
                            title={t('parameters.listPanelDomainTitle')}
                            actions={[
                                {
                                    id: 'edit',
                                    icon: 'add',
                                    description: t('add'),
                                    action: () => this.toggleAddDomain(),
                                    allowedPermissions: permissionConstants.CREATE_PARAM_DATA_PERMISSION,
                                },
                            ]}
                        />
                    }
                >
                    <PanelSection>
                        <Row>
                            <Col xs="12">{this.renderTable()}</Col>
                        </Row>
                    </PanelSection>
                </Panel>
                <ConfirmationModal
                    ref={this.getReferenceFromDeleteModal}
                    onConfirm={() => this.deleteDomain(indexToDelete)}
                    onCancel={() => {
                        // This is intentional
                    }}
                    message={t('transcodage.deleteModalQuestion')}
                    confirmButton={t('transcodage.deleteModalConfirm')}
                    cancelButton={t('transcodage.deleteModalCancel')}
                />
            </Fragment>
        );
    }
}

// Prop types
const propTypes = {
    getAllDomains: PropTypes.func.isRequired,
    createDomain: PropTypes.func.isRequired,
    updateDomain: PropTypes.func.isRequired,
    deleteDomain: PropTypes.func.isRequired,
    domainList: PropTypes.shape(),
    domainTranscode: PropTypes.string,
    domainCategory: PropTypes.string,
    domainPriority: PropTypes.string,
    domainIsAddingWarranties: PropTypes.string,
    domainIsAddingWarrantiesCapped: PropTypes.string,
    domainCode: PropTypes.string,
    domainName: PropTypes.string,
    reset: PropTypes.func,
    change: PropTypes.func,
    addAlert: PropTypes.func,
    invalid: PropTypes.bool,
    pristine: PropTypes.bool,
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

// Add prop types
ParamDomainComponent.propTypes = propTypes;
// Add default props
ParamDomainComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParamDomainComponent);
