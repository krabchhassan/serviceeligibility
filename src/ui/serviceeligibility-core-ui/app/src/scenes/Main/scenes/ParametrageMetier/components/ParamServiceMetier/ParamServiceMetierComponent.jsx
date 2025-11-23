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
import ParamUtils from '../../../../../../common/utils/ParamUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
const isNumber = ValidationFactories.isNumber();
const createField = (name, input, disabled, validateField) => {
    const validationFunctions = validateField ? [required, validateField] : required;
    if (disabled) {
        return <Field name={name} component={input} validate={validationFunctions} inline labelPortion={0} disabled />;
    }

    return <Field name={name} component={input} validate={validationFunctions} inline labelPortion={0} />;
};

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class ParamServiceMetierComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isCreation: false,
            isUpdating: false,
            indexToDelete: -1,
            editingIndex: -1,
            firstEdit: false,
        };
    }

    componentDidMount() {
        const { getAllServiceMetier } = this.props;

        getAllServiceMetier();
    }

    /**
     * Setting delete modal reference
     * @param {*} ref delete modal reference
     */
    @autobind
    getReferenceFromDeleteModal(ref) {
        this.deleteModalRef = ref;
    }

    /**
     * @description Calling action to delete an serviceMetier and changing state to read mode
     * @param {*} index serviceMetier's index to delete
     */
    deleteServiceMetier(index) {
        const { serviceMetierList, deleteServiceMetier } = this.props;
        const serviceMetierToDelete = serviceMetierList[index];

        deleteServiceMetier(serviceMetierToDelete.code);
        delete serviceMetierList[index];
        this.switchToReadMode(false);
    }

    /**
     * @description Switching to read mode
     */
    switchToReadMode(retrieveServices = true) {
        const { reset, getAllServiceMetier } = this.props;

        reset(formConstants.FORM_NAME);

        if (retrieveServices) {
            getAllServiceMetier();
        }
        this.setState({ isUpdating: false, isCreation: false });
    }

    /**
     * @description Switch line in edition mode
     * @param {*} index
     *      line index for edition
     */
    switchToEditMode(index) {
        const { change, serviceMetierList } = this.props;

        const editedData = Object.values(serviceMetierList)[index];

        // Updating code and libelle on edit form
        change(formConstants.FIELDS.serviceMetierCode, editedData.code);
        change(formConstants.FIELDS.serviceMetierName, editedData.libelle);
        change(formConstants.FIELDS.serviceMetierOrder, editedData.ordre);
        change(formConstants.FIELDS.serviceMetierIcon, editedData.icone);

        this.setState({ isUpdating: true, editingIndex: index, firstEdit: true });
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
     * @description open modal to delete serviceMetier
     */
    @autobind
    openDeleteModal() {
        this.deleteModalRef.open();
    }

    toggleAddServiceMetier() {
        const { change, getAllServiceMetier } = this.props;
        const { isCreation } = { ...this.state };

        // Empty form
        change(formConstants.FIELDS.serviceMetierCode, null);
        change(formConstants.FIELDS.serviceMetierName, null);
        change(formConstants.FIELDS.serviceMetierOrder, null);
        change(formConstants.FIELDS.serviceMetierIcon, null);

        getAllServiceMetier();
        this.setState({ isCreation: !isCreation });
    }

    /**
     * @description ServiceMetier creation
     */
    createServiceMetier() {
        const {
            createServiceMetier,
            serviceMetierCode,
            serviceMetierName,
            serviceMetierOrder,
            serviceMetierIcon,
            serviceMetierList,
            reset,
            addAlert,
            t,
        } = this.props;

        // Checking if serviceMetier is already existing
        const existingServiceMetier = Object.values(serviceMetierList).filter(
            (serviceMetier) => serviceMetier.code === serviceMetierCode,
        );

        const existingServiceMetierOrdre = Object.values(serviceMetierList).filter(
            (serviceMetier) => serviceMetier.ordre === serviceMetierOrder,
        );

        if (existingServiceMetier.length === 0 && existingServiceMetierOrdre.length === 0) {
            const serviceMetier = {
                code: serviceMetierCode,
                libelle: serviceMetierName,
                ordre: serviceMetierOrder,
                icone: serviceMetierIcon,
            };

            createServiceMetier(serviceMetier).then(() => {
                // Updating creation status
                this.toggleAddServiceMetier();

                // Empty form
                reset(formConstants.FORM_NAME);
            });
        } else if (existingServiceMetier.length !== 0) {
            // ALert
            addAlert({
                message: t('parameters.serviceMetierExists'),
                behavior: 'danger',
            });
        } else if (existingServiceMetierOrdre.length !== 0) {
            addAlert({
                message: t('parameters.serviceMetierOrderExists'),
                behavior: 'danger',
            });
        }
    }

    updateServiceMetier() {
        const {
            serviceMetierCode,
            serviceMetierName,
            serviceMetierOrder,
            serviceMetierIcon,
            updateServiceMetier,
            serviceMetierList,
            addAlert,
            t,
        } = this.props;

        const existingServiceMetierOrdre = Object.values(serviceMetierList).filter(
            (serviceMetier) => serviceMetier.code !== serviceMetierCode && serviceMetier.ordre === serviceMetierOrder,
        );

        if (existingServiceMetierOrdre.length === 0) {
            const serviceMetierToUpdate = {
                code: serviceMetierCode,
                libelle: serviceMetierName,
                ordre: serviceMetierOrder,
                icone: serviceMetierIcon,
            };

            updateServiceMetier(serviceMetierToUpdate).then(() => {
                this.switchToReadMode();
                addAlert({
                    message: t('parameters.updated'),
                    behavior: 'success',
                    timeout: 5000,
                });
            });
        } else {
            addAlert({
                message: t('parameters.serviceMetierOrderExists'),
                behavior: 'danger',
            });
        }
    }

    generateRowsData() {
        const { serviceMetierList, invalid, pristine, t } = this.props;
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

        let serviceMetiers = {};
        if (serviceMetierList) {
            serviceMetiers = { ...serviceMetierList };
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
        const rowsData = Object.values(serviceMetiers).map((item, index) => ({
            code: item.code,
            libelle: item.libelle,
            ordre: item.ordre,
            icone: item.icone,
            actions: () => actions(index),
        }));
        if (isCreation) {
            const creationActions = () => (
                <div className={style['serviceMetier-line-actions']}>
                    <Button
                        behavior="primary"
                        onClick={() => this.createServiceMetier()}
                        disabled={invalid || pristine}
                    >
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
                code: createField(formConstants.FIELDS.serviceMetierCode, CommonInput),
                libelle: createField(formConstants.FIELDS.serviceMetierName, CommonInput),
                ordre: createField(formConstants.FIELDS.serviceMetierOrder, CommonInput, false, isNumber),
                icone: createField(formConstants.FIELDS.serviceMetierIcon, CommonInput),
                actions: () => creationActions(),
            };

            rowsData.unshift(creationLine);
        }
        if (isUpdating && !isCreation) {
            const actionButtons = () => (
                <div className={style['serviceMetier-line-actions']}>
                    <Button
                        behavior="primary"
                        onMouseDown={() => this.updateServiceMetier()}
                        disabled={invalid || pristine}
                    >
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
                code: createField(formConstants.FIELDS.serviceMetierCode, CommonInput, true),
                libelle: createField(formConstants.FIELDS.serviceMetierName, CommonInput),
                ordre: createField(formConstants.FIELDS.serviceMetierOrder, CommonInput, false, isNumber),
                icone: createField(formConstants.FIELDS.serviceMetierIcon, CommonInput),
                actions: () => actionButtons(),
            };

            rowsData[editingIndex] = updateLine;
        }

        return rowsData;
    }

    renderTable() {
        const { t } = this.props;

        const rowsData = this.generateRowsData();

        const columns = ParamUtils.codeLabelActionsWithOrderAndCellColumns(t);

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
                            title={t('parameters.listPanelServiceMetierTitle')}
                            actions={[
                                {
                                    id: 'edit',
                                    icon: 'add',
                                    description: t('add'),
                                    action: () => this.toggleAddServiceMetier(),
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
                    onConfirm={() => this.deleteServiceMetier(indexToDelete)}
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
    getAllServiceMetier: PropTypes.func.isRequired,
    createServiceMetier: PropTypes.func.isRequired,
    updateServiceMetier: PropTypes.func.isRequired,
    deleteServiceMetier: PropTypes.func.isRequired,
    serviceMetierList: PropTypes.shape(),
    reset: PropTypes.func,
    change: PropTypes.func,
    addAlert: PropTypes.func,
    invalid: PropTypes.bool,
    pristine: PropTypes.bool,
    serviceMetierCode: PropTypes.string,
    serviceMetierName: PropTypes.string,
    serviceMetierOrder: PropTypes.string,
    serviceMetierIcon: PropTypes.string,
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

// Add prop types
ParamServiceMetierComponent.propTypes = propTypes;
// Add default props
ParamServiceMetierComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParamServiceMetierComponent);
