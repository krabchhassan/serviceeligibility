/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
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
import autobind from 'autobind-decorator';
import 'react-table/react-table.css';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import { CommonInput } from '../../../../../../common/utils/Form/CommonFields';
import permissionConstants from '../../../../PermissionConstants';
import formConstants from './Constants';
import style from './style.module.scss';
import ParamsSortedTable from '../ParamsSortedTable';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
const createField = (name) => <Field name={name} component={CommonInput} validate={required} inline labelPortion={0} />;

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class ParamObjetTranscoComponent extends Component {
    state = {
        isCreation: false,
        indexToDelete: -1,
        modifiedIndex: -1,
    };
    componentWillMount() {
        const { getAllTranscodageParam } = this.props;

        getAllTranscodageParam();
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
     * Creation a transcoding object
     */
    createObjetTransco() {
        const {
            codeObjetTransco,
            nomObjetTransco,
            colonnesTransco,
            createTranscodageParam,
            change,
            transcoParamList,
            addAlert,
            t,
        } = this.props;

        // Verifying transcoding parameter does not exists
        let existingTransco = false;
        Object.keys(transcoParamList || {}).forEach((key) => {
            const currentTransco = transcoParamList[key];
            if (currentTransco.codeObjetTransco === codeObjetTransco) {
                existingTransco = true;
            }
        });

        if (!existingTransco) {
            const paramTransco = {
                codeObjetTransco,
                nomObjetTransco,
            };

            // Build columns table
            paramTransco.colNames = colonnesTransco.split(',');

            createTranscodageParam(paramTransco).then(() => {
                // Updating creation state
                this.toggleAddTransco();

                // Emptying form
                change(formConstants.FIELDS.codeObjetTransco, null);
                change(formConstants.FIELDS.nomObjetTransco, null);
                change(formConstants.FIELDS.colNames, null);
            });
        } else {
            // ALert
            addAlert({
                message: t('parameters.transcoParamExists'),
                behavior: 'danger',
            });
        }
    }

    modifyTransco() {
        const { modifiedItem, updateTrancodageParam, addAlert, t } = this.props;
        const colArray = Array.isArray(modifiedItem.colNames)
            ? modifiedItem.colNames
            : modifiedItem.colNames.split(',');
        const dto = {
            ...modifiedItem,
            colNames: colArray,
        };
        return updateTrancodageParam(dto).then(() => {
            this.cancelEdit();
            addAlert({
                message: t('parameters.updated'),
                behavior: 'success',
                timeout: 5000,
            });
        });
    }

    /**
     * Setting index to delete and calling method to open modal
     * @param {*} index Index to delete
     */
    showDeleteModal(index) {
        const { serviceList, transcoParamList, addAlert, t } = this.props;
        this.setState({
            indexToDelete: index,
        });

        // Verifying that we don't have a service using the object we want to delete
        let objectUsedOnService = false;
        if (serviceList) {
            Object.keys(serviceList).forEach((key) => {
                const service = serviceList[key];
                if (service.listTransco) {
                    service.listTransco.forEach((transco) => {
                        if (transco === transcoParamList[index].codeObjetTransco) {
                            objectUsedOnService = true;
                        }
                    });
                }
            });
        }

        if (objectUsedOnService) {
            addAlert({
                message: t('parameters.trancoObjectUsedOnService'),
                behavior: 'danger',
            });
        } else {
            this.openDeleteModal();
        }
    }

    /**
     * @description Opening delete modal
     */
    @autobind
    openDeleteModal() {
        this.deleteModalRef.open();
    }

    /**
     * @description Toggling creation state
     */
    toggleAddTransco() {
        const { isCreation } = this.state;
        this.setState({
            isCreation: !isCreation,
        });
    }
    cancelEdit() {
        this.setState({
            modifiedIndex: -1,
        });
    }

    modifyItem(item, index) {
        const { change } = this.props;

        change('modified', { ...item });

        this.setState({
            modifiedIndex: index,
        });
    }

    /**
     * Deleting transcoding object
     * @param {*} index Index to delete
     */
    deleteTransco(index) {
        const { transcoParamList, deleteTrancodageParam } = this.props;
        const transcoToDelete = transcoParamList[index];
        deleteTrancodageParam(transcoToDelete.codeObjetTransco);
    }

    /**
     * Rendering objects table
     */
    renderTable() {
        const { transcoParamList, t, invalid, pristine, isLoading } = this.props;
        const { isCreation, modifiedIndex } = this.state;

        const actions = (item, index) => (
            <HabilitationFragment allowedPermissions={permissionConstants.DELETE_TRANSCODING_PARAM_DATA_PERMISSION}>
                <Button
                    behavior="default"
                    outlineNoBorder
                    onClick={() => this.showDeleteModal(index)}
                    disabled={isCreation}
                >
                    <CgIcon name="trash" className="default" />
                </Button>
                <Button behavior="default" outlineNoBorder onClick={() => this.modifyItem(item, index)}>
                    <CgIcon name="pencil" className="default" />
                </Button>
            </HabilitationFragment>
        );
        const modifyActions = () => (
            <div className={style['transco-object-line-actions']}>
                <Button
                    behavior="primary"
                    type="submit"
                    onClick={() => this.modifyTransco()}
                    disabled={invalid || isLoading}
                >
                    {t('update')}
                </Button>
                <Button
                    outline
                    behavior="secondary"
                    className={style['cancel-button']}
                    onClick={() => this.cancelEdit()}
                >
                    {t('cancel')}
                </Button>
            </div>
        );

        const rowsData = Object.values(transcoParamList || {}).map((item, index) => {
            if (index === modifiedIndex) {
                return {
                    code: item.codeObjetTransco,
                    name: createField(formConstants.FIELDS.modifiedNomObjetTransco),
                    colNames: createField(formConstants.FIELDS.modifiedColNames),
                    actions: () => modifyActions(),
                };
            }
            return {
                code: item.codeObjetTransco,
                name: item.nomObjetTransco,
                colNames: (item.colNames || []).toString(),
                actions: () => actions(item, index),
            };
        });
        if (isCreation) {
            const creationActions = () => (
                <div className={style['transco-object-line-actions']}>
                    <Button behavior="primary" onClick={() => this.createObjetTransco()} disabled={invalid || pristine}>
                        {t('create')}
                    </Button>
                    <Button
                        outline
                        behavior="secondary"
                        className={style['cancel-button']}
                        onClick={() => this.toggleAddTransco()}
                    >
                        {t('cancel')}
                    </Button>
                </div>
            );
            const creationLine = {
                code: createField(formConstants.FIELDS.codeObjetTransco),
                name: createField(formConstants.FIELDS.nomObjetTransco),
                colNames: createField(formConstants.FIELDS.colNames),
                actions: () => creationActions(),
            };
            rowsData.unshift(creationLine);
        }

        const columns = [
            {
                Header: t('parameters.transcoObjectCode'),
                accessor: 'code',
                minWidth: 30,
                headerStyle: { textAlign: 'left' },
            },
            {
                Header: t('parameters.transcoObjectName'),
                accessor: 'name',
                minWidth: 50,
                headerStyle: { textAlign: 'left' },
            },
            {
                Header: t('parameters.transcoColsName'),
                accessor: 'colNames',
                headerStyle: { textAlign: 'left' },
            },
            {
                Header: '',
                accessor: 'actions',
                minWidth: 30,
                Cell: (props) => props.value(),
            },
        ];

        return <ParamsSortedTable rowsData={rowsData} columns={columns} />;
    }

    /**
     * @description Method called after every update
     */
    render() {
        const { t } = this.props;
        const { indexToDelete } = this.state;

        return (
            <Fragment>
                <Panel
                    header={
                        <PanelHeader
                            title={t('parameters.listPanelTranscoTitle')}
                            actions={[
                                {
                                    id: 'edit',
                                    icon: 'add',
                                    description: t('add'),
                                    action: () => this.toggleAddTransco(),
                                    allowedPermissions: permissionConstants.CREATE_TRANSCODING_PARAM_DATA_PERMISSION,
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
                    onConfirm={() => this.deleteTransco(indexToDelete)}
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
    t: PropTypes.func,
    addAlert: PropTypes.func,
    invalid: PropTypes.bool,
    pristine: PropTypes.bool,
    isLoading: PropTypes.bool,
    change: PropTypes.func,
    transcoParamList: PropTypes.shape(),
    serviceList: PropTypes.shape(),
    modifiedItem: PropTypes.shape(),
    createTranscodageParam: PropTypes.func.isRequired,
    deleteTrancodageParam: PropTypes.func.isRequired,
    updateTrancodageParam: PropTypes.func.isRequired,
    getAllTranscodageParam: PropTypes.func.isRequired,
    codeObjetTransco: PropTypes.string,
    nomObjetTransco: PropTypes.string,
    colonnesTransco: PropTypes.string,
};
// Default props
const defaultProps = {};

// Add prop types
ParamObjetTranscoComponent.propTypes = propTypes;
// Add default props
ParamObjetTranscoComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParamObjetTranscoComponent);
