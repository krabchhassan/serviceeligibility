/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import { Field, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import {
    Panel,
    PanelHeader,
    PanelSection,
    Button,
    HabilitationFragment,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import 'react-table/react-table.css';
import ReactTable from 'react-table';
import ParamUtils from '../../../../../../common/utils/ParamUtils';
import formConstants from './Constants';
import permissionConstants from '../../../../PermissionConstants';
import style from '../ParamAgreement/style.module.scss';
import { CommonInput } from '../../../../../../common/utils/Form/CommonFields';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';

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
class ParamCodesRenvoiComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isCreation: false,
            isUpdating: false,
            editingIndex: -1,
            firstEdit: false,
        };
    }

    componentDidMount() {
        const { getAllReturnCodes } = this.props;

        getAllReturnCodes();
    }

    switchToReadMode(retrieveAgreements = true) {
        const { reset, getAllReturnCodes } = this.props;

        reset(formConstants.FORM_NAME);

        if (retrieveAgreements) {
            getAllReturnCodes();
        }
        this.setState({ isUpdating: false, isCreation: false });
    }

    switchToEditMode(index) {
        const { change, returnCodesList } = this.props;

        const editedData = Object.values(returnCodesList)[index];

        // Updating code and libelle on edit form
        change(formConstants.FIELDS.codesRenvoi, editedData.code);
        change(formConstants.FIELDS.libelleCodesRenvoi, editedData.libelle);

        this.setState({ isUpdating: true, editingIndex: index, firstEdit: true });
    }

    toggleAddCodesRenvoi() {
        const { change, getAllReturnCodes } = this.props;
        const { isCreation } = { ...this.state };

        // Empty form
        change(formConstants.FIELDS.codesRenvoi, null);
        change(formConstants.FIELDS.libelleCodesRenvoi, null);

        getAllReturnCodes();
        this.setState({ isCreation: !isCreation });
    }

    createCodesRenvoi() {
        const { createCodesRenvoi, codesRenvoi, libelleCodesRenvoi, returnCodesList, reset, addAlert, t } = this.props;
        const codesRenvoiUpper = codesRenvoi.toUpperCase();

        // Checking if codesRenvoi is already existing
        const existingCodesRenvoi = Object.values(returnCodesList).filter(
            (returnCodes) => returnCodes.code === codesRenvoiUpper,
        );

        if (existingCodesRenvoi.length === 0) {
            const returnCodes = {
                code: codesRenvoiUpper,
                libelle: libelleCodesRenvoi,
            };

            createCodesRenvoi(returnCodes)
                .then(() => {
                    // Updating creation status
                    this.toggleAddCodesRenvoi();

                    // Empty form
                    reset(formConstants.FORM_NAME);
                    this.switchToReadMode();
                })
                .catch((error) => {
                    console.error(error);
                });
        } else {
            // ALert
            addAlert({
                message: t('parameters.codeRenvoiExists'),
                behavior: 'danger',
            });
        }
    }

    updateCodesRenvoi() {
        const { codesRenvoi, libelleCodesRenvoi, updateCodesRenvoi, addAlert, t } = this.props;
        const codesRenvoiUpper = codesRenvoi.toUpperCase();

        const codesRenvoiToUpdate = {
            code: codesRenvoiUpper,
            libelle: libelleCodesRenvoi,
        };

        updateCodesRenvoi(codesRenvoiToUpdate)
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
        const { returnCodesList, invalid, pristine, t } = this.props;
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

        let returnCodes = {};
        if (returnCodesList) {
            returnCodes = { ...returnCodesList };
        }

        const actions = (index) => (
            <Fragment>
                <HabilitationFragment allowedPermissions={permissionConstants.CREATE_PARAM_DATA_PERMISSION}>
                    {generateEditButton(index)}
                </HabilitationFragment>
            </Fragment>
        );
        const rowsData = Object.values(returnCodes).map((item, index) => ({
            code: item.code,
            libelle: item.libelle,
            actions: () => actions(index),
        }));
        if (isCreation) {
            const creationActions = () => (
                <div className={style['returnCodes-line-actions']}>
                    <Button behavior="primary" onClick={() => this.createCodesRenvoi()} disabled={invalid || pristine}>
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
                code: createField(formConstants.FIELDS.codesRenvoi, CommonInput),
                libelle: createField(formConstants.FIELDS.libelleCodesRenvoi, CommonInput),
                actions: () => creationActions(),
            };

            rowsData.unshift(creationLine);
        }
        if (isUpdating && !isCreation) {
            const actionButtons = () => (
                <div className={style['returnCodes-line-actions']}>
                    <Button
                        behavior="primary"
                        onMouseDown={() => this.updateCodesRenvoi()}
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
                code: createField(formConstants.FIELDS.codesRenvoi, CommonInput, true),
                libelle: createField(formConstants.FIELDS.libelleCodesRenvoi, CommonInput),
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

        return (
            <ReactTable
                data={rowsData}
                defaultSorted={[
                    {
                        id: 'codesRenvoi',
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
            />
        );
    }

    render() {
        const { t } = this.props;
        return (
            <Fragment>
                <Panel
                    header={
                        <PanelHeader
                            title={t('parameters.listPanelCodesRenvoi')}
                            actions={[
                                {
                                    id: 'edit',
                                    icon: 'add',
                                    description: t('add'),
                                    action: () => this.toggleAddCodesRenvoi(),
                                    allowedPermissions: permissionConstants.CREATE_PARAM_DATA_PERMISSION,
                                },
                            ]}
                        />
                    }
                >
                    <PanelSection>{this.renderTable()}</PanelSection>
                </Panel>
            </Fragment>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    getAllReturnCodes: PropTypes.func.isRequired,
    returnCodesList: PropTypes.shape(),
    change: PropTypes.func,
    codesRenvoi: PropTypes.string,
    libelleCodesRenvoi: PropTypes.string,
    createCodesRenvoi: PropTypes.func.isRequired,
    updateCodesRenvoi: PropTypes.func.isRequired,
    addAlert: PropTypes.func,
    reset: PropTypes.func,
};
// Default props
const defaultProps = {};

// Add prop types
ParamCodesRenvoiComponent.propTypes = propTypes;
// Add default props
ParamCodesRenvoiComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: formConstants.FORM_NAME,
})(ParamCodesRenvoiComponent);
