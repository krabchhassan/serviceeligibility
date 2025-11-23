/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import { FieldArray } from 'redux-form';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Row,
    Panel,
    PanelSection,
    PanelHeader,
    Col,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import history from '../../../../../../history';
import TextItem from './generic/TextItem';
import SelectItem from './generic/SelectItem';
import GenericItem from './generic/GenericItem';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import PermissionConstants from '../../../../PermissionConstants';
import CommonSpinner from '../../../../../../common/components/CommonSpinner/CommonSpinner';
import NumAMCEchangesFieldArray from './generic/components/NumAMCEchangesFieldArray';
import CreateNumAMCEchangesFieldArray from './generic/components/CreateNumAMCEchangesFieldArray';
import Constants from '../../../Beneficiary/Constants';
import businessUtils from '../../../../../../common/utils/businessUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const required = ValidationFactories.required();
const strictLength = ValidationFactories.strictLength(10);
const isNumber = ValidationFactories.isNumber();
const minValue = ValidationFactories.minIncludedValue(0);
const maxValue = ValidationFactories.maxValue(365);
const isInteger = ValidationFactories.isInteger();

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class Identification extends Component {
    @autobind
    goToEdit() {
        const { declarant } = this.props;
        history.push(`/insurer/${declarant.numero}/edit`);
    }

    @autobind
    goToDuplicate() {
        const { declarant } = this.props;
        history.push(`/insurer/${declarant.numero}/duplicate`);
    }

    renderNumAMCEchangesFieldArray() {
        const { isEdit, isCopy, change } = this.props;
        return isCopy || isEdit ? (
            <FieldArray name="numerosAMCEchanges" component={NumAMCEchangesFieldArray} change={change} />
        ) : (
            <FieldArray name="numerosAMCEchanges" component={CreateNumAMCEchangesFieldArray} change={change} />
        );
    }

    render() {
        const { t, isCreate, isEdit, declarant, circuits, conventions, clientType } = this.props;

        const canRender = declarant && circuits && conventions;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const selectedCircuit = circuits.find((circuit) => circuit.codeCircuit === declarant.codeCircuit) || {};
        const selectedConvention =
            conventions.find((convention) => convention.code === declarant.operateurPrincipal) || {};

        const newDeclarant = {
            ...declarant,
            codeCircuit: businessUtils.getLibelleConverter(selectedCircuit.codeCircuit, selectedCircuit.libelleCircuit),
            operateurPrincipal: `${selectedConvention.label}`,
            delaiRetention: declarant.delaiRetention ? declarant.delaiRetention : '-',
        };

        const basicProps = {
            isCreate,
            isEdit,
            declarant: newDeclarant,
        };

        const actions = [];
        if (!isCreate && !isEdit) {
            actions.push({
                label: t('duplicate'),
                action: this.goToDuplicate,
                key: 'duplicate',
                id: 'duplicate',
                behavior: 'secondary',
                icon: 'duplicate',
                buttonProps: { type: 'button' },
                allowedPermissions: PermissionConstants.CREATE_AMC_DATA_PERMISSION,
            });
            actions.push({
                label: t('edit'),
                action: this.goToEdit,
                key: 'edit',
                id: 'edit',
                behavior: 'secondary',
                icon: 'pencil',
                buttonProps: { type: 'button' },
                allowedPermissions: PermissionConstants.UPDATE_AMC_DATA_PERMISSION,
            });
        }

        return (
            <Panel header={<PanelHeader title={t('declarant.informationTitle')} actions={actions} />}>
                <PanelSection>
                    <Row>
                        <Col xs={4}>
                            <GenericItem
                                basicProps={basicProps}
                                additionalProps={{
                                    fieldName: 'numero',
                                    rootComponent: TextItem,
                                    columnSize: 12,
                                    showRequired: true,
                                    disabled: isEdit,
                                    validate: [required, strictLength],
                                }}
                            />
                            <GenericItem
                                basicProps={basicProps}
                                additionalProps={{
                                    fieldName: 'codePartenaire',
                                    rootComponent: TextItem,
                                    columnSize: 12,
                                    showRequired: true,
                                    required,
                                }}
                            />
                            <GenericItem
                                basicProps={basicProps}
                                additionalProps={{
                                    fieldName: 'operateurPrincipal',
                                    rootComponent: SelectItem,
                                    columnSize: 12,
                                    options: conventions,
                                    valueKey: 'code',
                                    showRequired: true,
                                    required,
                                }}
                            />
                            {!isEdit && !isCreate ? (
                                <GenericItem
                                    basicProps={basicProps}
                                    additionalProps={{
                                        fieldName: 'numerosAMCEchanges',
                                        rootComponent: TextItem,
                                        columnSize: 12,
                                        options: conventions,
                                        showRequired: true,
                                    }}
                                />
                            ) : (
                                <Col xs={12}>
                                    <div className="mb-2 form-group">
                                        <Row className="align-items-baseline no-gutters">
                                            <Col xs={4} className="text-right cgd-comment mb-4">
                                                {/* eslint-disable-next-line jsx-a11y/label-has-for */}
                                                <label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                                                    {t('declarant.numerosAMCEchanges')} :
                                                </label>
                                            </Col>
                                            <Col xs={8}>{this.renderNumAMCEchangesFieldArray()}</Col>
                                        </Row>
                                    </div>
                                </Col>
                            )}
                        </Col>
                        <Col xs={4}>
                            <GenericItem
                                basicProps={basicProps}
                                additionalProps={{
                                    fieldName: 'nom',
                                    rootComponent: TextItem,
                                    columnSize: 12,
                                    showRequired: true,
                                    required,
                                }}
                            />
                            <GenericItem
                                basicProps={basicProps}
                                additionalProps={{
                                    fieldName: 'codeCircuit',
                                    rootComponent: SelectItem,
                                    columnSize: 12,
                                    options: circuits,
                                    valueKey: 'codeCircuit',
                                    showRequired: true,
                                    required,
                                }}
                            />
                            <GenericItem
                                basicProps={basicProps}
                                additionalProps={{
                                    fieldName: 'siret',
                                    rootComponent: TextItem,
                                    columnSize: 12,
                                }}
                            />
                            {clientType === Constants.TOPOLOGY_INSURER && (
                                <GenericItem
                                    basicProps={basicProps}
                                    additionalProps={{
                                        fieldName: 'delaiRetention',
                                        additionnalLabel: 'jours',
                                        rootComponent: TextItem,
                                        columnSize: 12,
                                        validate: [required, isNumber, isInteger, minValue, maxValue],
                                        showRequired: true,
                                    }}
                                />
                            )}
                        </Col>
                        <Col xs={4}>
                            <GenericItem
                                basicProps={basicProps}
                                additionalProps={{
                                    fieldName: 'libelle',
                                    rootComponent: TextItem,
                                    columnSize: 12,
                                }}
                            />
                            <GenericItem
                                basicProps={basicProps}
                                additionalProps={{
                                    fieldName: 'emetteurDroits',
                                    rootComponent: TextItem,
                                    columnSize: 12,
                                    disabled: true,
                                }}
                            />
                            <GenericItem
                                basicProps={basicProps}
                                additionalProps={{
                                    fieldName: 'idClientBO',
                                    rootComponent: TextItem,
                                    columnSize: 12,
                                }}
                            />
                        </Col>
                    </Row>
                </PanelSection>
            </Panel>
        );
    }
}

Identification.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    isCopy: PropTypes.bool,
    declarant: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    circuits: PropTypes.arrayOf(PropTypes.shape()),
    change: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Identification;
