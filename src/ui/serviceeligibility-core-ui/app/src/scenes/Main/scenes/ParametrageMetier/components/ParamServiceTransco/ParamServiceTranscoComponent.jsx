/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import ReactTable from 'react-table';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
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
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import { CommonCombobox, CommonInput } from '../../../../../../common/utils/Form/CommonFields';
import formConstants from './Constants';
import style from './style.module.scss';
import ServiceSubComponent from './components/ParamServiceSubComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
const createField = (name, input) => (
    <Field name={name} component={input} validate={required} inline labelPortion={0} />
);
const createMultiCombobox = (name, options, blur) => (
    <Field
        name={name}
        component={CommonCombobox}
        validate={required}
        inline
        labelPortion={0}
        options={options}
        multi
        onChange={() => blur()}
    />
);

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class ParamServiceTranscoComponent extends Component {
    constructor(props) {
        super(props);
        const { getAllServicesTransco } = this.props;

        getAllServicesTransco().then(() => {
            const newRowsData = this.setRowsData();
            this.setState({ rowsData: newRowsData });
        });
    }
    state = {
        isCreation: false,
        isUpdating: false,
        selectedService: null,
        indexToDelete: -1,
        editingIndex: -1,
        rowsData: null,
    };

    /**
     * Setting delete modal reference
     * @param {*} ref delete modal reference
     */
    @autobind
    getReferenceFromDeleteModal(ref) {
        this.deleteModalRef = ref;
    }

    /**
     * @description generating rows data for table
     */
    setRowsData() {
        const { serviceList, transcoParamList, t, invalid, pristine, change, blur } = this.props;
        const { isCreation, isUpdating, editingIndex } = this.state;

        let serviceArr = [];
        if (serviceList) {
            serviceArr = Object.values(serviceList);
        }
        const newRowsData = serviceArr.map((item, index) => {
            const arrTransco = [];
            item.listTransco.forEach((transco) => {
                Object.keys(transcoParamList).forEach((key) => {
                    const param = transcoParamList[key];
                    if (param.codeObjetTransco === transco) {
                        arrTransco.push(param.nomObjetTransco);
                    }
                });
            });

            const actions = (service, currentIndex) => (
                <Fragment>
                    <HabilitationFragment
                        allowedPermissions={permissionConstants.DELETE_TRANSCODING_PARAM_DATA_PERMISSION}
                    >
                        <Button
                            behavior="default"
                            outlineNoBorder
                            onClick={() => this.showDeleteModal(currentIndex)}
                            disabled={isUpdating || isCreation || service.controleContextuel}
                        >
                            <CgIcon name="trash" className="default" />
                        </Button>
                    </HabilitationFragment>
                </Fragment>
            );
            return {
                service: item.nom,
                transcoObjects: arrTransco.toString(),
                controleContextuel: item.controleContextuel,
                actions: () => actions(item, index),
            };
        });
        if (isCreation) {
            const datas = [];

            // Creating combobox data
            Object.keys(transcoParamList).forEach((key) => {
                const param = transcoParamList[key];
                const data = {
                    value: param.codeObjetTransco,
                    label: param.nomObjetTransco,
                };
                datas.push(data);
            });

            const creationActions = () => (
                <div className={style['service-line-actions']}>
                    <Button
                        behavior="primary"
                        onClick={() => this.createServiceTransco()}
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
                service: createField(formConstants.FIELDS.serviceCode, CommonInput),
                transcoObjects: createMultiCombobox(formConstants.FIELDS.serviceTransco, datas, blur),
                actions: () => creationActions(),
            };
            newRowsData.unshift(creationLine);
        }
        if (isUpdating && !isCreation) {
            const editedData = newRowsData[editingIndex];

            // Updating code on edit form
            change(formConstants.FIELDS.serviceCode, editedData.service);

            const datas = [];

            Object.keys(transcoParamList).forEach((key) => {
                const param = transcoParamList[key];
                const data = {
                    value: param.codeObjetTransco,
                    label: param.nomObjetTransco,
                };
                datas.push(data);
            });

            const actionButtons = () => (
                <div className={style['service-line-actions']}>
                    <Button behavior="primary" onClick={() => this.updateServiceTransco()}>
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
                service: createField(formConstants.FIELDS.serviceCode, CommonInput),
                transcoObjects: createMultiCombobox(formConstants.FIELDS.serviceTransco, datas, blur),
                actions: () => actionButtons(),
            };

            newRowsData[editingIndex] = updateLine;
        }

        return newRowsData;
    }

    /**
     * @description Toggle state to display insert line
     */
    toggleAddService() {
        const { change } = this.props;
        const { isCreation } = { ...this.state };

        // Empty form
        change(formConstants.FIELDS.serviceCode, null);
        change(formConstants.FIELDS.serviceTransco, null);

        this.setState({ isCreation: !isCreation });
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
     * @description Switch line in edition mode
     * @param {*} index
     *      line index for edition
     */
    switchToEditMode(index) {
        const { serviceList, change, transcoParamList } = this.props;

        const editedData = Object.values(serviceList)[index];

        // Updating code on edit form
        change(formConstants.FIELDS.serviceCode, editedData.nom);

        const objects = editedData.listTransco;
        const values = [];
        const datas = [];
        objects.forEach((object) => {
            // Searching in the list to find object code
            Object.keys(transcoParamList).forEach((key) => {
                const param = transcoParamList[key];
                const data = {
                    value: param.codeObjetTransco,
                    label: param.nomObjetTransco,
                };
                if (param.codeObjetTransco === object) {
                    values.push(data);
                }
                datas.push(data);
            });
        });

        // Updating combobox data
        change(formConstants.FIELDS.serviceTransco, values);

        this.setState({ isUpdating: true, editingIndex: index, firstEdit: true });
    }

    /**
     * @description Switching to read mode
     */
    switchToReadMode() {
        const { reset } = this.props;

        reset(formConstants.FORM_NAME);

        this.setState({ isUpdating: false, isCreation: false });
    }

    /**
     * @description updating transcoding service
     */
    updateServiceTransco() {
        const { updateServiceTransco, serviceCode, serviceTransco, serviceList } = this.props;

        // Get the id from the original list
        let updatedId;
        Object.keys(serviceList).forEach((key) => {
            const serviceItem = serviceList[key];
            if (serviceItem.nom === serviceCode) {
                updatedId = serviceItem.id;
            }
        });

        const service = {
            id: updatedId,
            nom: serviceCode,
        };

        const listTransco = [];
        serviceTransco.forEach((object) => {
            listTransco.push(object.value);
        });

        service.listTransco = listTransco;

        updateServiceTransco(service).then(() => {
            this.switchToReadMode();
        });
    }

    /**
     * @description Calling action to delete a service and changing state to read mode
     * @param {*} index service's index to delete
     */
    deleteService(index) {
        const { serviceList, deleteServiceTransco } = this.props;
        const serviceToDelete = serviceList[index];

        deleteServiceTransco(serviceToDelete.nom);
        this.switchToReadMode();
    }

    /**
     * @description open modal to delete service
     */
    @autobind
    openDeleteModal() {
        this.deleteModalRef.open();
    }

    /**
     * @description trancoding service creation
     */
    createServiceTransco() {
        const { createServiceTransco, serviceCode, serviceTransco, serviceList, reset, addAlert, t } = this.props;

        // Checking if service is already existing
        const existingService = Object.values(serviceList).filter((service) => service.nom === serviceCode);

        if (existingService.length === 0) {
            const service = {
                nom: serviceCode,
            };

            const listTransco = [];
            serviceTransco.forEach((object) => {
                listTransco.push(object.value);
            });

            service.listTransco = listTransco;

            createServiceTransco(service).then(() => {
                // Updating creation status
                this.toggleAddService();

                // Empty form
                reset(formConstants.FORM_NAME);
            });
        } else {
            // ALert
            addAlert({
                message: t('parameters.serviceExists'),
                behavior: 'danger',
            });
        }
    }

    /**
     * Rendering service's tableactionss
     */
    renderTableService() {
        const { t } = this.props;

        const rowsData = this.setRowsData();

        const columns = [
            {
                Header: t('parameters.transcoServiceCode'),
                accessor: 'service',
                minWidth: 30,
                headerStyle: { textAlign: 'left' },
            },
            {
                Header: t('parameters.transcoServiceObjects'),
                accessor: 'transcoObjects',
                headerStyle: { textAlign: 'left' },
            },
            {
                Header: '',
                accessor: 'actions',
                minWidth: 30,
                Cell: (props) => props.value(),
            },
        ];

        return (
            <ReactTable
                data={rowsData}
                defaultSorted={[
                    {
                        id: 'service',
                        desc: false,
                    },
                ]}
                columns={columns}
                minRows={10}
                className="-striped -highlight"
                showPageSizeOptions={false}
                showPagination={false}
                defaultPageSize={10}
                previousText={t('table.pagination.previous')}
                nextText={t('table.pagination.next')}
                loadingText={t('table.loading')}
                pageText={t('table.pagination.page')}
                ofText={t('table.pagination.of')}
                rowsText={t('table.pagination.rows')}
                noDataText={t('table.noData')}
                SubComponent={(data) => <ServiceSubComponent data={data.original} />}
            />
        );
    }

    /**
     * @description method called after every update
     */
    render() {
        const { t } = this.props;
        const { indexToDelete, isUpdating } = this.state;

        return (
            <Fragment>
                <Panel
                    header={
                        <PanelHeader
                            title={t('parameters.listPanelServiceTitle')}
                            actions={[
                                {
                                    id: 'addService',
                                    icon: 'add',
                                    buttonProps: { disabled: isUpdating },
                                    description: t('add'),
                                    action: () => this.toggleAddService(),
                                    allowedPermissions: permissionConstants.CREATE_TRANSCODING_PARAM_DATA_PERMISSION,
                                },
                            ]}
                        />
                    }
                >
                    <PanelSection>
                        <Row>
                            <Col xs="12">{this.renderTableService()}</Col>
                        </Row>
                    </PanelSection>
                </Panel>
                <ConfirmationModal
                    ref={this.getReferenceFromDeleteModal}
                    onConfirm={() => this.deleteService(indexToDelete)}
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
    invalid: PropTypes.bool,
    pristine: PropTypes.bool,
    change: PropTypes.func,
    reset: PropTypes.func,
    blur: PropTypes.func,
    addAlert: PropTypes.func,
    serviceList: PropTypes.shape(),
    transcoParamList: PropTypes.shape(),
    getAllServicesTransco: PropTypes.func.isRequired,
    createServiceTransco: PropTypes.func.isRequired,
    deleteServiceTransco: PropTypes.func.isRequired,
    updateServiceTransco: PropTypes.func.isRequired,
    serviceCode: PropTypes.string,
    serviceTransco: PropTypes.arrayOf(PropTypes.object),
};
// Default props
const defaultProps = {};

// Add prop types
ParamServiceTranscoComponent.propTypes = propTypes;
// Add default props
ParamServiceTranscoComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParamServiceTranscoComponent);
