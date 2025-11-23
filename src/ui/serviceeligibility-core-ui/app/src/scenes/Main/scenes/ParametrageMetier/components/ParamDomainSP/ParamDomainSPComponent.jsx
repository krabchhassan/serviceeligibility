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
class ParamDomainSPComponent extends Component {
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
        const { getAllDomainsSP } = this.props;

        getAllDomainsSP();
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
     * @description Calling action to delete a SPSANTE domain and changing state to read mode
     * @param {*} index domain's index to delete
     */
    deleteDomainSP(index) {
        const { domainSPList, deleteDomainSP } = this.props;
        const domainSPToDelete = domainSPList[index];

        deleteDomainSP(domainSPToDelete.code);
        delete domainSPList[index];
        this.switchToReadMode(false);
    }

    /**
     * @description Switching to read mode
     */
    switchToReadMode(retrieveDomains = true) {
        const { reset, getAllDomainsSP } = this.props;

        reset(formConstants.FORM_NAME);

        if (retrieveDomains) {
            getAllDomainsSP();
        }
        this.setState({ isUpdating: false, isCreation: false });
    }

    /**
     * @description Switch line in edition mode
     * @param {*} index
     *      line index for edition
     */
    switchToEditMode(index) {
        const { change, domainSPList } = this.props;

        const editedData = Object.values(domainSPList)[index];

        // Updating code and libelle on edit form
        change(formConstants.FIELDS.domainSPCode, editedData.code);
        change(formConstants.FIELDS.domainSPName, editedData.libelle);
        change(formConstants.FIELDS.domainSPTranscode, editedData.transcodification);

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
     * @description open modal to delete SPSANTE domain
     */
    @autobind
    openDeleteModal() {
        this.deleteModalRef.open();
    }

    toggleAddDomainSP() {
        const { change, getAllDomainsSP } = this.props;
        const { isCreation } = { ...this.state };

        // Empty form
        change(formConstants.FIELDS.domainSPCode, null);
        change(formConstants.FIELDS.domainSPName, null);
        change(formConstants.FIELDS.domainSPTranscode, null);

        getAllDomainsSP();
        this.setState({ isCreation: !isCreation });
    }

    /**
     * @description SPSANTE domain creation
     */
    createDomainSP() {
        const { createDomainSP, domainSPCode, domainSPName, domainSPTranscode, domainSPList, reset, addAlert, t } =
            this.props;

        // Checking if domain SP is already existing
        const existingDomainSP = Object.values(domainSPList).filter((domain) => domain.code === domainSPCode);

        if (existingDomainSP.length === 0) {
            const domainSP = {
                code: domainSPCode,
                libelle: domainSPName,
                transcodification: domainSPTranscode,
            };

            createDomainSP(domainSP).then(() => {
                // Updating creation status
                this.toggleAddDomainSP();

                // Empty form
                reset(formConstants.FORM_NAME);
            });
        } else {
            // ALert
            addAlert({
                message: t('parameters.domainSPExists'),
                behavior: 'danger',
            });
        }
    }

    updateDomainSP() {
        const { domainSPCode, domainSPName, domainSPTranscode, updateDomainSP, addAlert, t } = this.props;

        const domainSPToUpdate = {
            code: domainSPCode,
            libelle: domainSPName,
            transcodification: domainSPTranscode,
        };

        updateDomainSP(domainSPToUpdate).then(() => {
            this.switchToReadMode();
            addAlert({
                message: t('parameters.updated'),
                behavior: 'success',
                timeout: 5000,
            });
        });
    }

    generateRowsData() {
        const { domainSPList, invalid, pristine, t } = this.props;
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

        let domainsSP = {};
        if (domainSPList) {
            domainsSP = { ...domainSPList };
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
        const rowsData = Object.values(domainsSP).map((item, index) => ({
            code: item.code,
            libelle: item.libelle,
            transcodification: item.transcodification,
            actions: () => actions(index),
        }));
        if (isCreation) {
            const creationActions = () => (
                <div className={style['domainSP-line-actions']}>
                    <Button behavior="primary" onClick={() => this.createDomainSP()} disabled={invalid || pristine}>
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
                code: createField(formConstants.FIELDS.domainSPCode, CommonInput),
                libelle: createField(formConstants.FIELDS.domainSPName, CommonInput),
                transcodification: createField(formConstants.FIELDS.domainSPTranscode, CommonInput),
                actions: () => creationActions(),
            };

            rowsData.unshift(creationLine);
        }
        if (isUpdating && !isCreation) {
            const actionButtons = () => (
                <div className={style['domainSP-line-actions']}>
                    <Button behavior="primary" onMouseDown={() => this.updateDomainSP()} disabled={invalid || pristine}>
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
                code: createField(formConstants.FIELDS.domainSPCode, CommonInput, true),
                libelle: createField(formConstants.FIELDS.domainSPName, CommonInput),
                transcodification: createField(formConstants.FIELDS.domainSPTranscode, CommonInput),
                actions: () => actionButtons(),
            };

            rowsData[editingIndex] = updateLine;
        }

        return rowsData;
    }

    renderTable() {
        const { t } = this.props;

        const rowsData = this.generateRowsData();

        const columns = ParamUtils.codeLabelTranscodeActionsColumns(t);

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
                            title={t('parameters.listPanelDomainSPTitle')}
                            actions={[
                                {
                                    id: 'edit',
                                    icon: 'add',
                                    description: t('add'),
                                    action: () => this.toggleAddDomainSP(),
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
                    onConfirm={() => this.deleteDomainSP(indexToDelete)}
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
    getAllDomainsSP: PropTypes.func.isRequired,
    createDomainSP: PropTypes.func.isRequired,
    updateDomainSP: PropTypes.func.isRequired,
    deleteDomainSP: PropTypes.func.isRequired,
    domainSPList: PropTypes.shape(),
    domainSPCode: PropTypes.string,
    domainSPName: PropTypes.string,
    domainSPTranscode: PropTypes.string,
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
ParamDomainSPComponent.propTypes = propTypes;
// Add default props
ParamDomainSPComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParamDomainSPComponent);
