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
import { CommonInput, CommonMultiCombobox } from '../../../../../../common/utils/Form/CommonFields';
import style from './style.module.scss';
import ParamsUnsortedTable from '../ParamsUnsortedTable';
import ParamUtils from '../../../../../../common/utils/ParamUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
const createField = (name, input, disabled, options) => {
    if (disabled) {
        return <Field name={name} component={input} validate={required} inline labelPortion={0} disabled />;
    }
    if (options) {
        return <Field name={name} component={input} inline labelPortion={0} options={options} />;
    }

    return <Field name={name} component={input} validate={required} inline labelPortion={0} />;
};

const getDomainCodeListForCombobox = (domainList) =>
    (Object.entries(domainList) || []).map((domain) => ({
        codeDomaine: domain[1].code,
        label: domain[1].code,
        value: domain[1].code,
    }));

const getDomainCodeListForEditMode = (domainList) =>
    (Object.entries(domainList) || []).map((domain) => ({
        codeDomaine: domain[1].codeDomaine,
        label: domain[1].codeDomaine,
        value: domain[1].codeDomaine,
    }));

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class ParamClaimComponent extends Component {
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
        const { getAllClaims, getAllDomains } = this.props;

        getAllClaims();
        getAllDomains();
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
     * @description Calling action to delete a claim and changing state to read mode
     * @param {*} index claim's index to delete
     */
    deleteClaim(index) {
        const { claimList, deleteClaim } = this.props;
        const claimToDelete = claimList[index];

        delete claimList[index];
        this.switchToReadMode(false);
        deleteClaim(claimToDelete.code);
    }

    /**
     * @description Switching to read mode
     */
    switchToReadMode(retrieveClaims = true) {
        const { reset, getAllClaims } = this.props;

        reset(formConstants.FORM_NAME);

        if (retrieveClaims) {
            getAllClaims();
        }
        this.setState({ isUpdating: false, isCreation: false });
    }

    /**
     * @description Switch line in edition mode
     * @param {*} index
     *      line index for edition
     */
    switchToEditMode(index) {
        const { change, claimList } = this.props;

        const editedData = Object.values(claimList)[index];
        // Updating code and libelle on edit form
        change(formConstants.FIELDS.claimCode, editedData.code);
        change(formConstants.FIELDS.claimName, editedData.libelle);
        change(formConstants.FIELDS.claimDomains, getDomainCodeListForEditMode(editedData.domaines));

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
     * @description open modal to delete claim
     */
    @autobind
    openDeleteModal() {
        this.deleteModalRef.open();
    }

    toggleAddClaim() {
        const { change, getAllClaims } = this.props;
        const { isCreation } = { ...this.state };

        // Empty form
        change(formConstants.FIELDS.claimCode, null);
        change(formConstants.FIELDS.claimName, null);
        change(formConstants.FIELDS.claimDomains, null);

        getAllClaims();
        this.setState({ isCreation: !isCreation });
    }

    /**
     * @description claim creation
     */
    createClaim() {
        const { createClaim, claimCode, claimName, claimDomains, claimList, reset, addAlert, t } = this.props;

        // Checking if claim is already existing
        const existingClaim = Object.values(claimList).filter((claim) => claim.code === claimCode);

        if (existingClaim.length === 0) {
            if (/\s/.test(claimCode)) {
                addAlert({
                    message: t('parameters.claimContainsSpaces'),
                    behavior: 'danger',
                });
            } else {
                const claim = {
                    code: claimCode,
                    libelle: claimName,
                    domaines: (claimDomains || []).map((domaine) => ({ codeDomaine: domaine.codeDomaine })),
                };

                createClaim(claim).then(() => {
                    // Updating creation status
                    this.toggleAddClaim();

                    // Empty form
                    reset(formConstants.FORM_NAME);
                });
            }
        } else {
            // ALert
            addAlert({
                message: t('parameters.claimExists'),
                behavior: 'danger',
            });
        }
    }

    checkIfDomainExist(claimDomain) {
        const { domainList } = this.props;
        let domainExist = false;

        (Object.values(domainList) || []).forEach(checkCode);

        function checkCode(domain) {
            if (domain.code === claimDomain.codeDomaine) {
                domainExist = true;
            }
        }
        return domainExist;
    }

    updateClaim() {
        const { claimCode, claimName, claimDomains, updateClaim, addAlert, t } = this.props;

        let missingDomain = false;
        let missingDomainCode = '';

        const checkClaimDomains = (claimDomain) => {
            if (!this.checkIfDomainExist(claimDomain[1])) {
                missingDomain = true;
                missingDomainCode = claimDomain[1].codeDomaine;
            }
        };
        (Object.entries(claimDomains) || []).forEach(checkClaimDomains);

        if (!missingDomain) {
            const claimToUpdate = {
                code: claimCode,
                libelle: claimName,
                domaines: (claimDomains || []).map((domaine) => ({ codeDomaine: domaine.codeDomaine })),
            };
            updateClaim(claimToUpdate).then(() => {
                this.switchToReadMode();
                addAlert({
                    message: t('parameters.updated'),
                    behavior: 'success',
                    timeout: 5000,
                });
            });
        } else {
            addAlert({
                message: t('parameters.missingDomain', { name: missingDomainCode }),
                behavior: 'danger',
            });
        }
    }

    generateRowsData() {
        const { claimList, domainList, invalid, pristine, t } = this.props;
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

        let claims = {};
        if (claimList) {
            claims = { ...claimList };
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
        const rowsData = Object.values(claims).map((item, index) => ({
            code: item.code,
            libelle: this.renderLibelle(item),
            domaines: (item.domaines || []).map((domaine) => domaine.codeDomaine).join(' '),
            actions: () => actions(index),
        }));
        if (isCreation) {
            const creationActions = () => (
                <div className={style['claim-line-actions']}>
                    <Button behavior="primary" onClick={() => this.createClaim()} disabled={invalid || pristine}>
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
                code: createField(formConstants.FIELDS.claimCode, CommonInput),
                libelle: createField(formConstants.FIELDS.claimName, CommonInput),
                domaines: createField(
                    formConstants.FIELDS.claimDomains,
                    CommonMultiCombobox,
                    false,
                    getDomainCodeListForCombobox(domainList),
                ),
                actions: () => creationActions(),
            };

            rowsData.unshift(creationLine);
        }
        if (isUpdating && !isCreation) {
            const actionButtons = () => (
                <div className={style['claim-line-actions']}>
                    <Button behavior="primary" onMouseDown={() => this.updateClaim()} disabled={invalid || pristine}>
                        {t('edit')}
                    </Button>
                    <Button
                        outline
                        behavior="secondary"
                        className={style['cancel-button']}
                        onMouseDown={() => this.switchToReadMode()}
                    >
                        {t('cancel')}
                    </Button>
                </div>
            );

            const updateLine = {
                code: createField(formConstants.FIELDS.claimCode, CommonInput, true),
                libelle: createField(formConstants.FIELDS.claimName, CommonInput),
                domaines: createField(
                    formConstants.FIELDS.claimDomains,
                    CommonMultiCombobox,
                    false,
                    getDomainCodeListForCombobox(domainList),
                ),
                actions: () => actionButtons(),
            };

            rowsData[editingIndex] = updateLine;
        }

        return rowsData;
    }

    renderLibelle = (item) => {
        const lines = (item.libelle || '').match(/.{1,46}(\s|$)/g);
        return (
            <div>
                {(lines || []).map((line) => (
                    <div className="cgd-text-overflow">{line}</div>
                ))}
            </div>
        );
    };

    renderTable() {
        const { t } = this.props;
        const rowsData = this.generateRowsData();

        const columns = ParamUtils.codeLabelActionsForClaimWithCellColumns(t);

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
                            title={t('parameters.listPanelClaimTitle')}
                            actions={[
                                {
                                    id: 'edit',
                                    icon: 'add',
                                    description: t('add'),
                                    action: () => this.toggleAddClaim(),
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
                    onConfirm={() => this.deleteClaim(indexToDelete)}
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
    getAllClaims: PropTypes.func.isRequired,
    createClaim: PropTypes.func.isRequired,
    updateClaim: PropTypes.func.isRequired,
    deleteClaim: PropTypes.func.isRequired,
    claimList: PropTypes.shape(),
    getAllDomains: PropTypes.func.isRequired,
    claimCode: PropTypes.string,
    claimName: PropTypes.string,
    claimDomains: PropTypes.shape(),
    domainList: PropTypes.shape(),
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
ParamClaimComponent.propTypes = propTypes;
// Add default props
ParamClaimComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParamClaimComponent);
