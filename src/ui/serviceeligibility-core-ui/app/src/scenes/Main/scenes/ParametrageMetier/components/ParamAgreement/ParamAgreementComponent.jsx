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
const createField = (name, input, disabled) => {
    if (disabled) {
        return <Field name={name} component={input} validate={required} inline labelPortion={0} disabled />;
    }

    return <Field name={name} component={input} validate={required} inline labelPortion={0} />;
};

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class ParamAgreementComponent extends Component {
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
        const { getAllAgreement } = this.props;

        getAllAgreement();
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
     * @description Calling action to delete an agreement and changing state to read mode
     * @param {*} index agreement's index to delete
     */
    deleteAgreement(index) {
        const { agreementList, deleteAgreement } = this.props;
        const agreementToDelete = agreementList[index];

        deleteAgreement(agreementToDelete.code);
        delete agreementList[index];
        this.switchToReadMode(false);
    }

    /**
     * @description Switching to read mode
     */
    switchToReadMode(retrieveAgreements = true) {
        const { reset, getAllAgreement } = this.props;

        reset(formConstants.FORM_NAME);

        if (retrieveAgreements) {
            getAllAgreement();
        }
        this.setState({ isUpdating: false, isCreation: false });
    }

    /**
     * @description Switch line in edition mode
     * @param {*} index
     *      line index for edition
     */
    switchToEditMode(index) {
        const { change, agreementList } = this.props;

        const editedData = Object.values(agreementList)[index];

        // Updating code and libelle on edit form
        change(formConstants.FIELDS.agreementCode, editedData.code);
        change(formConstants.FIELDS.agreementName, editedData.libelle);

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
     * @description open modal to delete agreement
     */
    @autobind
    openDeleteModal() {
        this.deleteModalRef.open();
    }

    toggleAddAgreement() {
        const { change, getAllAgreement } = this.props;
        const { isCreation } = { ...this.state };

        // Empty form
        change(formConstants.FIELDS.agreementCode, null);
        change(formConstants.FIELDS.agreementName, null);

        getAllAgreement();
        this.setState({ isCreation: !isCreation });
    }

    /**
     * @description Agreement creation
     */
    createAgreement() {
        const { createAgreement, agreementCode, agreementName, agreementList, reset, addAlert, t } = this.props;

        // Checking if agreement is already existing
        const existingAgreement = Object.values(agreementList).filter((agreement) => agreement.code === agreementCode);

        if (existingAgreement.length === 0) {
            const agreement = {
                code: agreementCode,
                libelle: agreementName,
            };

            createAgreement(agreement)
                .then(() => {
                    // Updating creation status
                    this.toggleAddAgreement();

                    // Empty form
                    reset(formConstants.FORM_NAME);
                })
                .catch((error) => {
                    console.error(error);
                });
        } else {
            // ALert
            addAlert({
                message: t('parameters.agreementExists'),
                behavior: 'danger',
            });
        }
    }

    updateAgreement() {
        const { agreementCode, agreementName, updateAgreement, addAlert, t } = this.props;

        const agreementToUpdate = {
            code: agreementCode,
            libelle: agreementName,
        };

        updateAgreement(agreementToUpdate)
            .then(() => {
                this.switchToReadMode();
                addAlert({
                    message: t('parameters.updated'),
                    behavior: 'success',
                    timeout: 5000,
                });
            })
            .catch((error) => {
                console.error(error);
            });
    }

    generateRowsData() {
        const { agreementList, invalid, pristine, t } = this.props;
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

        let agreements = {};
        if (agreementList) {
            agreements = { ...agreementList };
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
        const rowsData = Object.values(agreements).map((item, index) => ({
            code: item.code,
            libelle: item.libelle,
            actions: () => actions(index),
        }));
        if (isCreation) {
            const creationActions = () => (
                <div className={style['agreement-line-actions']}>
                    <Button behavior="primary" onClick={() => this.createAgreement()} disabled={invalid || pristine}>
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
                code: createField(formConstants.FIELDS.agreementCode, CommonInput),
                libelle: createField(formConstants.FIELDS.agreementName, CommonInput),
                actions: () => creationActions(),
            };

            rowsData.unshift(creationLine);
        }
        if (isUpdating && !isCreation) {
            const actionButtons = () => (
                <div className={style['agreement-line-actions']}>
                    <Button
                        behavior="primary"
                        onMouseDown={() => this.updateAgreement()}
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
                code: createField(formConstants.FIELDS.agreementCode, CommonInput, true),
                libelle: createField(formConstants.FIELDS.agreementName, CommonInput),
                actions: () => actionButtons(),
            };

            rowsData[editingIndex] = updateLine;
        }

        return rowsData;
    }

    renderTable() {
        const { t } = this.props;

        const rowsData = this.generateRowsData();

        const columns = ParamUtils.codeLabelActionsWithCellColumns(t);

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
                            title={t('parameters.listPanelAgreementTitle')}
                            actions={[
                                {
                                    id: 'edit',
                                    icon: 'add',
                                    description: t('add'),
                                    action: () => this.toggleAddAgreement(),
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
                    onConfirm={() => this.deleteAgreement(indexToDelete)}
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
    getAllAgreement: PropTypes.func.isRequired,
    createAgreement: PropTypes.func.isRequired,
    updateAgreement: PropTypes.func.isRequired,
    deleteAgreement: PropTypes.func.isRequired,
    agreementList: PropTypes.shape(),
    agreementCode: PropTypes.string,
    agreementName: PropTypes.string,
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
ParamAgreementComponent.propTypes = propTypes;
// Add default props
ParamAgreementComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParamAgreementComponent);
