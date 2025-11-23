/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import { Field, reduxForm } from 'redux-form';
import autobind from 'autobind-decorator';
import PropTypes from 'prop-types';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import {
    BodyHeader,
    DesktopBreadcrumbPart,
    Button,
    CgdTable,
    Col,
    ConfirmationModal,
    HabilitationFragment,
    PageLayout,
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CommonCombobox, CommonInput } from '../../../../common/utils/Form/CommonFields';
import formConstants from './Constants';
import ValidationFactories from '../../../../common/utils/Form/ValidationFactories';
import permissionConstants from '../../PermissionConstants';
import CommonSpinner from '../../../../common/components/CommonSpinner/CommonSpinner';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const required = ValidationFactories.required();
const createField = (name) => <Field name={name} component={CommonInput} validate={required} inline labelPortion={0} />;

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class TranscodingComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isCreation: false,
            indexToDelete: -1,
        };
    }

    componentDidMount() {
        const { getAllTranscodageService } = this.props;
        getAllTranscodageService();
    }

    componentDidUpdate(prevProps) {
        const { selectedService, selectedTransco, change, getMapping } = this.props;
        if (selectedService !== prevProps.selectedService) {
            if (((selectedService || {}).transco || []).length === 1) {
                const onlyPossibility = selectedService.transco[0];
                change(formConstants.FIELDS.transco, onlyPossibility);
                getMapping(selectedService.codeService, onlyPossibility.codeObjetTransco);
            } else {
                change(formConstants.FIELDS.transco, null);
            }
        } else if (selectedService && selectedTransco && selectedTransco !== prevProps.selectedTransco) {
            getMapping(selectedService.codeService, selectedTransco.codeObjetTransco);
        }
    }

    @autobind
    getReferenceFromDeleteModal(ref) {
        this.deleteModalRef = ref;
    }

    getColumns(cols) {
        const { t } = this.props;
        const { isCreation } = this.state;
        const width = 450 / (cols.length + 1);
        const colMap = cols.map((item, index) => this.generateCommonColumn(item, index, width));

        return [
            ...colMap,
            {
                Header: t('transcodage.codeTransco'),
                accessor: 'codeTransco',
                id: 'codeTransco',
                width,
                disableSortBy: true,
                key: 'codeTransco',
            },
            {
                Header: '',
                accessor: (rowValues, rowIndex) => {
                    return isCreation && rowIndex === 0 ? this.createAction() : this.deleteAction(rowIndex);
                },
                id: 'actions',
                width: 30,
                disableSortBy: true,
                key: 'actions',
            },
        ];
    }

    generateCommonColumn(item, index, width) {
        const { i18n, t } = this.props;
        return {
            Header: i18n.exists(`eligibility:transcodage.colNames.${item}`) ? t(`transcodage.colNames.${item}`) : item,
            accessor: `${index}`,
            id: index,
            width,
            disableSortBy: true,
            key: index,
        };
    }

    @autobind
    openDeleteModal() {
        this.deleteModalRef.open();
    }

    @autobind
    deleteTransco(index) {
        const { transcoMapping } = this.props;
        const newTransco = { ...transcoMapping, transcoList: [...transcoMapping.transcoList] };
        newTransco.transcoList.splice(index, 1);
        this.updateTransco(newTransco);
    }

    @autobind
    createTransco() {
        const { t, transcoMapping, formCle, formCodeTransco, addAlert, change } = this.props;
        let newTransco = {};
        if (transcoMapping) {
            newTransco = { ...transcoMapping, transcoList: [...transcoMapping.transcoList] };
        } else {
            newTransco = { ...this.initializeTranscoding() };
        }
        const newTranscoObject = {
            cle: Object.values(formCle),
            codeTransco: formCodeTransco,
        };

        const doesTranscoExist = newTransco.transcoList.some((item) => {
            let keysExist = true;
            item.cle.forEach((cle, index) => {
                if (cle !== newTranscoObject.cle[index]) {
                    keysExist = false;
                }
            });
            return keysExist;
        });
        if (!doesTranscoExist) {
            newTransco.transcoList.push(newTranscoObject);
            this.updateTransco(newTransco).then(() => {
                this.setState({
                    isCreation: false,
                });
                change(formConstants.FIELDS.cle, null);
                change(formConstants.FIELDS.codeTransco, null);
            });
        } else {
            addAlert({
                message: t('transcodage.mappingExists'),
                behavior: 'danger',
            });
        }
    }

    @autobind
    showDeleteModal(index) {
        this.setState({
            indexToDelete: index,
        });

        this.openDeleteModal();
    }

    @autobind
    toggleAddTransco() {
        const { isCreation } = this.state;
        this.setState({
            isCreation: !isCreation,
        });
    }

    updateTransco(newTransco) {
        const { updateMapping, selectedService, selectedTransco, getMapping } = this.props;
        return updateMapping(selectedService.codeService, selectedTransco.codeObjetTransco, newTransco).then(() =>
            getMapping(selectedService.codeService, selectedTransco.codeObjetTransco),
        );
    }

    initializeTranscoding() {
        const { selectedService, selectedTransco } = this.props;

        return {
            codeObjetTransco: selectedTransco.codeObjetTransco,
            codeService: selectedService.codeService,
            transcoList: [],
        };
    }

    createAction() {
        const { t, invalid, pristine, formCle } = this.props;
        return (
            <Button
                behavior="primary"
                onClick={this.createTransco}
                disabled={invalid || pristine || Object.values(formCle || {}).length === 0}
            >
                {t('create')}
            </Button>
        );
    }

    deleteAction(index) {
        return (
            <HabilitationFragment allowedPermissions={permissionConstants.DELETE_TRANSCODAGE_DATA_PERMISSION}>
                <Button behavior="default" outlineNoBorder onClick={() => this.showDeleteModal(index)}>
                    <CgIcon name="trash" className="default" />
                </Button>
            </HabilitationFragment>
        );
    }

    renderSearchForm() {
        const { t, transcodageService, selectedService } = this.props;
        const filteredTransco = selectedService
            ? transcodageService.find((item) => item.codeService === selectedService.codeService)
            : [];
        const transco = (filteredTransco || {}).transco || [];
        return (
            <Row>
                <Col xs="6">
                    <Field
                        name={formConstants.FIELDS.service}
                        component={CommonCombobox}
                        label={t('transcodage.service')}
                        placeholder={t('transcodage.servicePlaceholder')}
                        tooltip={t('transcodage.serviceTooltip')}
                        options={transcodageService}
                        labelKey="codeService"
                        valueKey="codeService"
                        idKey="id"
                        showRequired="(*)"
                        searchable
                    />
                </Col>
                <Col xs="6">
                    <Field
                        name={formConstants.FIELDS.transco}
                        component={CommonCombobox}
                        label={t('transcodage.transcodage')}
                        placeholder={t('transcodage.transcodagePlaceholder')}
                        tooltip={t('transcodage.transcodageTooltip')}
                        options={transco}
                        labelKey="nomObjetTransco"
                        valueKey="codeObjetTransco"
                        idKey="nomObjetTransco"
                        showRequired="(*)"
                        searchable
                    />
                </Col>
            </Row>
        );
    }

    renderTable() {
        const { transcoMapping, selectedTransco } = this.props;
        const { isCreation } = this.state;
        if (!selectedTransco) {
            return null;
        }

        const columns = this.getColumns(selectedTransco.colNames);

        const transcoList = ((transcoMapping || {}).transcoList || []).sort((a, b) => {
            return a.cle > b.cle;
        });
        const rowsData = transcoList.map((item) => {
            return {
                ...(item || {}).cle,
                codeTransco: item.codeTransco,
            };
        });

        if (isCreation) {
            const creationLine = {
                ...selectedTransco.colNames.map((item) => createField(`cle.${item}`)),
                codeTransco: createField(formConstants.FIELDS.codeTransco),
            };
            rowsData.unshift(creationLine);
        }

        return <CgdTable id="transoTable" data={rowsData} columns={columns} />;
    }

    render() {
        const { t, isLoading, transcodageService, selectedTransco, selectedService } = this.props;
        const { indexToDelete } = this.state;
        const canRender = !isLoading && (transcodageService || []).length > 0;
        const canShowList = selectedTransco && selectedService;
        if (isLoading) {
            return <CommonSpinner />;
        }
        return !canRender ? null : (
            <PageLayout header={<BodyHeader title={t('transcodage.pageTitle')} />}>
                <DesktopBreadcrumbPart label={t('breadcrumb.transcodage')} />
                <Row>
                    <Col xs="12" lg="6">
                        {this.renderSearchForm()}
                    </Col>
                </Row>
                {canShowList && (
                    <Panel
                        header={
                            <PanelHeader
                                title={t('transcodage.listPanelTitle')}
                                actions={[
                                    {
                                        id: 'edit',
                                        icon: 'add',
                                        description: t('add'),
                                        action: this.toggleAddTransco,
                                        allowedPermissions: permissionConstants.CREATE_TRANSCODAGE_DATA_PERMISSION,
                                    },
                                ]}
                            />
                        }
                    >
                        <PanelSection>
                            <div className="d-flex">{this.renderTable()}</div>
                        </PanelSection>
                    </Panel>
                )}
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
            </PageLayout>
        );
    }
}

TranscodingComponent.propTypes = {
    t: PropTypes.func,
    i18n: PropTypes.shape(),
    addAlert: PropTypes.func,
    // Redux props
    change: PropTypes.func,
    pristine: PropTypes.bool.isRequired,
    invalid: PropTypes.bool.isRequired,
    updateMapping: PropTypes.func,
    getAllTranscodageService: PropTypes.func,
    getMapping: PropTypes.func,
    selectedService: PropTypes.shape(),
    selectedTransco: PropTypes.shape(),
    isLoading: PropTypes.bool,
    transcodageService: PropTypes.arrayOf(PropTypes.shape()),
    transcoMapping: PropTypes.shape(),
    formCle: PropTypes.shape(),
    formCodeTransco: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(TranscodingComponent);
