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
class ParamDomainISComponent extends Component {
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
        const { getAllDomainsIS } = this.props;

        getAllDomainsIS();
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
     * @description Calling action to delete a domain and changing state to read mode
     * @param {*} index domain's index to delete
     */
    deleteDomainIS(index) {
        const { domainISList, deleteDomainIS } = this.props;
        const domainISToDelete = domainISList[index];

        deleteDomainIS(domainISToDelete.code);
        delete domainISList[index];
        this.switchToReadMode(false);
    }

    /**
     * @description Switching to read mode
     */
    switchToReadMode(retrieveDomains = true) {
        const { reset, getAllDomainsIS } = this.props;

        reset(formConstants.FORM_NAME);

        if (retrieveDomains) {
            getAllDomainsIS();
        }
        this.setState({ isUpdating: false, isCreation: false });
    }

    /**
     * @description Switch line in edition mode
     * @param {*} index
     *      line index for edition
     */
    switchToEditMode(index) {
        const { change, domainISList } = this.props;

        const editedData = Object.values(domainISList)[index];

        // Updating code and libelle on edit form
        change(formConstants.FIELDS.domainISCode, editedData.code);
        change(formConstants.FIELDS.domainISName, editedData.libelle);
        change(formConstants.FIELDS.domainISTranscode, editedData.transcodification);

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
     * @description open modal to delete domain IS
     */
    @autobind
    openDeleteModal() {
        this.deleteModalRef.open();
    }

    toggleAddDomainIS() {
        const { change, getAllDomainsIS } = this.props;
        const { isCreation } = { ...this.state };

        // Empty form
        change(formConstants.FIELDS.domainISCode, null);
        change(formConstants.FIELDS.domainISName, null);
        change(formConstants.FIELDS.domainISTranscode, null);

        getAllDomainsIS();
        this.setState({ isCreation: !isCreation });
    }

    /**
     * @description domain IS creation
     */
    createDomainIS() {
        const { createDomainIS, domainISCode, domainISName, domainISTranscode, domainISList, reset, addAlert, t } =
            this.props;

        // Checking if domain IS is already existing
        const existingDomainIS = Object.values(domainISList).filter((domain) => domain.code === domainISCode);

        if (existingDomainIS.length === 0) {
            const domainIS = {
                code: domainISCode,
                libelle: domainISName,
                transcodification: domainISTranscode,
            };

            createDomainIS(domainIS).then(() => {
                // Updating creation status
                this.toggleAddDomainIS();

                // Empty form
                reset(formConstants.FORM_NAME);
            });
        } else {
            // ALert
            addAlert({
                message: t('parameters.domainISExists'),
                behavior: 'danger',
            });
        }
    }

    updateDomainIS() {
        const { domainISCode, domainISName, domainISTranscode, updateDomainIS, addAlert, t } = this.props;

        const domainISToUpdate = {
            code: domainISCode,
            libelle: domainISName,
            transcodification: domainISTranscode,
        };

        updateDomainIS(domainISToUpdate).then(() => {
            this.switchToReadMode();
            addAlert({
                message: t('parameters.updated'),
                behavior: 'success',
                timeout: 5000,
            });
        });
    }

    generateRowsData() {
        const { domainISList, invalid, pristine, t } = this.props;
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

        let domainsIS = {};
        if (domainISList) {
            domainsIS = { ...domainISList };
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
        const rowsData = Object.values(domainsIS).map((item, index) => ({
            code: item.code,
            libelle: item.libelle,
            transcodification: item.transcodification,
            actions: () => actions(index),
        }));
        if (isCreation) {
            const creationActions = () => (
                <div className={style['domainIS-line-actions']}>
                    <Button behavior="primary" onClick={() => this.createDomainIS()} disabled={invalid || pristine}>
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
                code: createField(formConstants.FIELDS.domainISCode, CommonInput),
                libelle: createField(formConstants.FIELDS.domainISName, CommonInput),
                transcodification: createField(formConstants.FIELDS.domainISTranscode, CommonInput),
                actions: () => creationActions(),
            };

            rowsData.unshift(creationLine);
        }
        if (isUpdating && !isCreation) {
            const actionButtons = () => (
                <div className={style['domainIS-line-actions']}>
                    <Button behavior="primary" onMouseDown={() => this.updateDomainIS()} disabled={invalid || pristine}>
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
                code: createField(formConstants.FIELDS.domainISCode, CommonInput, true),
                libelle: createField(formConstants.FIELDS.domainISName, CommonInput),
                transcodification: createField(formConstants.FIELDS.domainISTranscode, CommonInput),
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
                            title={t('parameters.listPanelDomainISTitle')}
                            actions={[
                                {
                                    id: 'edit',
                                    icon: 'add',
                                    description: t('add'),
                                    action: () => this.toggleAddDomainIS(),
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
                    onConfirm={() => this.deleteDomainIS(indexToDelete)}
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
    getAllDomainsIS: PropTypes.func.isRequired,
    createDomainIS: PropTypes.func.isRequired,
    updateDomainIS: PropTypes.func.isRequired,
    deleteDomainIS: PropTypes.func.isRequired,
    domainISList: PropTypes.shape(),
    domainISCode: PropTypes.string,
    domainISName: PropTypes.string,
    domainISTranscode: PropTypes.string,
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
ParamDomainISComponent.propTypes = propTypes;
// Add default props
ParamDomainISComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParamDomainISComponent);
