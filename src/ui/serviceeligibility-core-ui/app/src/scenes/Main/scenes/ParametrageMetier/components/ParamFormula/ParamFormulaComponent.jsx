/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import ReactTable from 'react-table';
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

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
const createField = (name, input, disabled) => (
    <Field name={name} component={input} validate={required} inline labelPortion={0} disabled={disabled} />
);

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
class ParamFormulaComponent extends Component {
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
        const { getAllFormulas } = this.props;

        getAllFormulas();
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
     * @description Calling action to delete a formula and changing state to read mode
     * @param {*} index formula's index to delete
     */
    deleteFormula(index) {
        const { formulaList, deleteFormula } = this.props;
        const formulaToDelete = formulaList[index];

        deleteFormula(formulaToDelete.code);
        delete formulaList[index];
        this.switchToReadMode(false);
    }

    /**
     * @description Switching to read mode
     */
    switchToReadMode(retrieveFormulas = true) {
        const { reset, getAllFormulas } = this.props;

        reset(formConstants.FORM_NAME);

        if (retrieveFormulas) {
            getAllFormulas();
        }
        this.setState({ isUpdating: false, isCreation: false });
    }

    /**
     * @description Switch line in edition mode
     * @param {*} index
     *      line index for edition
     */
    switchToEditMode(index) {
        const { change, formulaList } = this.props;

        const editedData = Object.values(formulaList)[index];

        // Updating code and libelle on edit form
        change(formConstants.FIELDS.formulaCode, editedData.code);
        change(formConstants.FIELDS.formulaParam1, editedData.param1);
        change(formConstants.FIELDS.formulaParam2, editedData.param2);
        change(formConstants.FIELDS.formulaParam3, editedData.param3);
        change(formConstants.FIELDS.formulaParam4, editedData.param4);
        change(formConstants.FIELDS.formulaParam5, editedData.param5);
        change(formConstants.FIELDS.formulaParam6, editedData.param6);
        change(formConstants.FIELDS.formulaParam7, editedData.param7);
        change(formConstants.FIELDS.formulaParam8, editedData.param8);
        change(formConstants.FIELDS.formulaParam9, editedData.param9);
        change(formConstants.FIELDS.formulaParam10, editedData.param10);

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
     * @description open modal to delete service
     */
    @autobind
    openDeleteModal() {
        this.deleteModalRef.open();
    }

    toggleAddFormula() {
        const { change, getAllFormulas } = this.props;
        const { isCreation } = { ...this.state };

        // Empty form
        change(formConstants.FIELDS.formulaCode, null);
        change(formConstants.FIELDS.formulaParam1, false);
        change(formConstants.FIELDS.formulaParam2, false);
        change(formConstants.FIELDS.formulaParam3, false);
        change(formConstants.FIELDS.formulaParam4, false);
        change(formConstants.FIELDS.formulaParam5, false);
        change(formConstants.FIELDS.formulaParam6, false);
        change(formConstants.FIELDS.formulaParam7, false);
        change(formConstants.FIELDS.formulaParam8, false);
        change(formConstants.FIELDS.formulaParam9, false);
        change(formConstants.FIELDS.formulaParam10, false);

        getAllFormulas();
        this.setState({ isCreation: !isCreation });
    }

    /**
     * @description formula creation
     */
    createFormula() {
        const {
            createFormula,
            formulaCode,
            formulaParam1,
            formulaParam2,
            formulaParam3,
            formulaParam4,
            formulaParam5,
            formulaParam6,
            formulaParam7,
            formulaParam8,
            formulaParam9,
            formulaParam10,
            formulaList,
            reset,
            addAlert,
            t,
        } = this.props;

        // Checking if formula is already existing
        const existingFormula = Object.values(formulaList).filter((domain) => domain.code === formulaCode);

        if (existingFormula.length === 0) {
            const formula = {
                code: formulaCode,
                param1: formulaParam1,
                param2: formulaParam2,
                param3: formulaParam3,
                param4: formulaParam4,
                param5: formulaParam5,
                param6: formulaParam6,
                param7: formulaParam7,
                param8: formulaParam8,
                param9: formulaParam9,
                param10: formulaParam10,
            };

            createFormula(formula).then(() => {
                // Updating creation status
                this.toggleAddFormula();

                // Empty form
                reset(formConstants.FORM_NAME);
            });
        } else {
            // ALert
            addAlert({
                message: t('parameters.formulaExists'),
                behavior: 'danger',
            });
        }
    }

    updateFormula() {
        const {
            formulaCode,
            formulaParam1,
            formulaParam2,
            formulaParam3,
            formulaParam4,
            formulaParam5,
            formulaParam6,
            formulaParam7,
            formulaParam8,
            formulaParam9,
            formulaParam10,
            updateFormula,
            addAlert,
            t,
        } = this.props;

        const formulaToUpdate = {
            code: formulaCode,
            param1: formulaParam1,
            param2: formulaParam2,
            param3: formulaParam3,
            param4: formulaParam4,
            param5: formulaParam5,
            param6: formulaParam6,
            param7: formulaParam7,
            param8: formulaParam8,
            param9: formulaParam9,
            param10: formulaParam10,
        };

        updateFormula(formulaToUpdate).then(() => {
            this.switchToReadMode();
            addAlert({
                message: t('parameters.updated'),
                behavior: 'success',
                timeout: 5000,
            });
        });
    }

    generateRowsData() {
        const { formulaList, invalid, pristine, t } = this.props;
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

        let formulas = {};
        if (formulaList) {
            formulas = { ...formulaList };
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
        const rowsData = Object.values(formulas).map((item, index) => ({
            key: index,
            code: item.code,
            param1: item.param1,
            param2: item.param2,
            param3: item.param3,
            param4: item.param4,
            param5: item.param5,
            param6: item.param6,
            param7: item.param7,
            param8: item.param8,
            param9: item.param9,
            param10: item.param10,
            actions: () => actions(index),
        }));
        if (isCreation) {
            const creationActions = () => (
                <div className={style['formula-line-actions']}>
                    <Button behavior="primary" onClick={() => this.createFormula()} disabled={invalid || pristine}>
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
                code: createField(formConstants.FIELDS.formulaCode, CommonInput),
                param1: false,
                param2: false,
                param3: false,
                param4: false,
                param5: false,
                param6: false,
                param7: false,
                param8: false,
                param9: false,
                param10: false,
                actions: () => creationActions(),
            };

            rowsData.unshift(creationLine);
        }
        if (isUpdating && !isCreation) {
            const updateActions = () => (
                <div className={style['formula-line-actions']}>
                    <Button behavior="primary" onClick={() => this.updateFormula()}>
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
                code: createField(formConstants.FIELDS.formulaCode, CommonInput, true),
                param1: false,
                param2: false,
                param3: false,
                param4: false,
                param5: false,
                param6: false,
                param7: false,
                param8: false,
                param9: false,
                param10: false,
                actions: () => updateActions(),
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
                Header: t('parameters.code'),
                accessor: 'code',
                minWidth: 30,
                headerStyle: { textAlign: 'left' },
            },
            {
                Header: t('parameters.param1'),
                accessor: 'param1',
                minWidth: 20,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.formulaParam1,
                            CommonInput,
                            false,
                            original.param1,
                        );
                    }
                    return (
                        <div id={`checkbox-param-formula-param1-${original.key}`}>
                            <input type="checkbox" className="checkbox" checked={original.param1 === true} disabled />
                        </div>
                    );
                },
            },
            {
                Header: t('parameters.param2'),
                accessor: 'param2',
                minWidth: 20,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.formulaParam2,
                            CommonInput,
                            false,
                            original.param2,
                        );
                    }
                    return (
                        <div id={`checkbox-param-formula-param2-${original.key}`}>
                            <input type="checkbox" className="checkbox" checked={original.param2 === true} disabled />
                        </div>
                    );
                },
            },
            {
                Header: t('parameters.param3'),
                accessor: 'param3',
                minWidth: 20,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.formulaParam3,
                            CommonInput,
                            false,
                            original.param3,
                        );
                    }
                    return (
                        <div id={`checkbox-param-formula-param3-${original.key}`}>
                            <input type="checkbox" className="checkbox" checked={original.param3 === true} disabled />
                        </div>
                    );
                },
            },
            {
                Header: t('parameters.param4'),
                accessor: 'param4',
                minWidth: 20,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.formulaParam4,
                            CommonInput,
                            false,
                            original.param4,
                        );
                    }
                    return (
                        <div id={`checkbox-param-formula-param4-${original.key}`}>
                            <input type="checkbox" className="checkbox" checked={original.param4 === true} disabled />
                        </div>
                    );
                },
            },
            {
                Header: t('parameters.param5'),
                accessor: 'param5',
                minWidth: 20,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.formulaParam5,
                            CommonInput,
                            false,
                            original.param5,
                        );
                    }
                    return (
                        <div id={`checkbox-param-formula-param5-${original.key}`}>
                            <input type="checkbox" className="checkbox" checked={original.param5 === true} disabled />
                        </div>
                    );
                },
            },
            {
                Header: t('parameters.param6'),
                accessor: 'param6',
                minWidth: 20,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.formulaParam6,
                            CommonInput,
                            false,
                            original.param6,
                        );
                    }
                    return (
                        <div id={`checkbox-param-formula-param6-${original.key}`}>
                            <input type="checkbox" className="checkbox" checked={original.param6 === true} disabled />
                        </div>
                    );
                },
            },
            {
                Header: t('parameters.param7'),
                accessor: 'param7',
                minWidth: 20,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.formulaParam7,
                            CommonInput,
                            false,
                            original.param7,
                        );
                    }
                    return (
                        <div id={`checkbox-param-formula-param7-${original.key}`}>
                            <input type="checkbox" className="checkbox" checked={original.param7 === true} disabled />
                        </div>
                    );
                },
            },
            {
                Header: t('parameters.param8'),
                accessor: 'param8',
                minWidth: 20,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.formulaParam8,
                            CommonInput,
                            false,
                            original.param8,
                        );
                    }
                    return (
                        <div id={`checkbox-param-formula-param8-${original.key}`}>
                            <input type="checkbox" className="checkbox" checked={original.param8 === true} disabled />
                        </div>
                    );
                },
            },
            {
                Header: t('parameters.param9'),
                accessor: 'param9',
                minWidth: 20,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.formulaParam9,
                            CommonInput,
                            false,
                            original.param9,
                        );
                    }
                    return (
                        <div id={`checkbox-param-formula-param9-${original.key}`}>
                            <input type="checkbox" className="checkbox" checked={original.param9 === true} disabled />
                        </div>
                    );
                },
            },
            {
                Header: t('parameters.param10'),
                accessor: 'param10',
                minWidth: 20,
                Cell: ({ original }) => {
                    if (original.key === 'new' || original.key === 'update') {
                        return createCheckboxField(
                            formConstants.FIELDS.formulaParam10,
                            CommonInput,
                            false,
                            original.param10,
                        );
                    }
                    return (
                        <div id={`checkbox-param-formula-param10-${original.key}`}>
                            <input type="checkbox" className="checkbox" checked={original.param10 === true} disabled />
                        </div>
                    );
                },
            },
            {
                Header: '',
                accessor: 'actions',
                Cell: (props) => props.value(),
                minWidth: 30,
            },
        ];

        const customProps = { id: 'formula-table' };
        return (
            <ReactTable
                data={rowsData}
                columns={columns}
                minRows={10}
                className="-striped -highlight"
                showPageSizeOptions={false}
                defaultPageSize={10}
                previousText={t('table.pagination.previous')}
                nextText={t('table.pagination.next')}
                loadingText={t('table.loading')}
                pageText={t('table.pagination.page')}
                ofText={t('table.pagination.of')}
                rowsText={t('table.pagination.rows')}
                noDataText={t('table.noData')}
                getProps={() => customProps}
            />
        );
    }

    render() {
        const { t } = this.props;
        const { indexToDelete } = this.state;
        return (
            <Fragment>
                <Panel
                    header={
                        <PanelHeader
                            title={t('parameters.listPanelFormulaTitle')}
                            actions={[
                                {
                                    id: 'edit',
                                    icon: 'add',
                                    description: t('add'),
                                    action: () => this.toggleAddFormula(),
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
                    onConfirm={() => this.deleteFormula(indexToDelete)}
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
    getAllFormulas: PropTypes.func.isRequired,
    createFormula: PropTypes.func.isRequired,
    updateFormula: PropTypes.func.isRequired,
    deleteFormula: PropTypes.func.isRequired,
    formulaList: PropTypes.shape(),
    formulaCode: PropTypes.string,
    formulaParam1: PropTypes.string,
    formulaParam2: PropTypes.string,
    formulaParam3: PropTypes.string,
    formulaParam4: PropTypes.string,
    formulaParam5: PropTypes.string,
    formulaParam6: PropTypes.string,
    formulaParam7: PropTypes.string,
    formulaParam8: PropTypes.string,
    formulaParam9: PropTypes.string,
    formulaParam10: PropTypes.string,
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
ParamFormulaComponent.propTypes = propTypes;
// Add default props
ParamFormulaComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParamFormulaComponent);
