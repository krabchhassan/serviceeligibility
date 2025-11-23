/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import isEqual from 'lodash/isEqual';
import { PanelSection, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import TextItem from './generic/TextItem';
import DateItem from './generic/DateItem';
import SelectItem from './generic/SelectItem';
import BooleanItem from './generic/BooleanItem';
import RegroupementItem from './generic/RegroupementItem';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const isNumber = ValidationFactories.isNumber();

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class RegroupementForm extends Component {
    shouldComponentUpdate(nextProps, nextState) {
        return !isEqual(nextProps, this.props) || !isEqual(nextState, this.state);
    }

    setForcedConventionValue = (isCreate, isEdit, selectedConventionCode, conventions) => {
        let forcedConventionValue = null;
        if (!isCreate && !isEdit && selectedConventionCode) {
            const selectedConvention =
                conventions.find((convention) => convention.code === selectedConventionCode) || {};
            forcedConventionValue = `${selectedConvention.label}`;
        }
        return forcedConventionValue;
    };

    @autobind
    toggleCollapse() {
        const { expanded } = this.state;
        this.setState({ expanded: !expanded });
    }

    checkIfFieldIsMissing(field) {
        const { prefix, calculatedErrors } = this.props;
        if ((calculatedErrors || []).length === 1 && calculatedErrors[0] === false) {
            return false;
        }
        const regroupementErrors = (calculatedErrors || []).filter(
            (error) => error.regroupementNumber === parseInt(prefix.match(/\d+/g)[1], 10),
        );
        const { missingFields } = (regroupementErrors || [])[0] || {};
        return (missingFields || []).includes(field);
    }

    render() {
        const { t, isCreate, isEdit, regroupement, isServiceOpen, conventions, selectedConventionCode, prefix } =
            this.props;

        const forcedConventionValue = this.setForcedConventionValue(
            isCreate,
            isEdit,
            selectedConventionCode,
            conventions,
        );

        const basicProps = {
            isCreate,
            isEdit,
        };

        let fileNumberProps = {};
        if (isServiceOpen) {
            fileNumberProps = {
                validate: [isNumber],
            };
        }

        return (
            <React.Fragment>
                <PanelSection title={t('declarant.criteriaDefinition')}>
                    <Row>
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'critereRegroupementDetaille',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                regroupement,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'critereRegroupement',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                regroupement,
                                prefix,
                            }}
                        />
                    </Row>
                </PanelSection>
                <PanelSection title={t('declarant.serviceParametrage')}>
                    <Row>
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'dateOuverture',
                                rootComponent: DateItem,
                                disabled: !isServiceOpen,
                                errorMessage: this.checkIfFieldIsMissing('dateOuverture'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'dateSynchronisation',
                                rootComponent: DateItem,
                                disabled: !isServiceOpen,
                                regroupement,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'couloirClient',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                errorMessage: this.checkIfFieldIsMissing('couloirClient'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'typeConventionnement',
                                rootComponent: SelectItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                forcedValue: forcedConventionValue,
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                    </Row>
                </PanelSection>
                <PanelSection title={t('declarant.flux')}>
                    <Row>
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'numDebutFichier',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('numDebutFichier'),
                                ...fileNumberProps,
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'numEmetteur',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('numEmetteur'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'numClient',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('numClient'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'nomClient',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('nomClient'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                    </Row>
                    <Row>
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'versionNorme',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('versionNorme'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'typeFichier',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('typeFichier'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'codePerimetre',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('codePerimetre'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'nomPerimetre',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('nomPerimetre'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                    </Row>
                    <Row>
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'typeGestionnaireBO',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('typeGestionnaireBO'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'libelleGestionnaireBO',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('libelleGestionnaireBO'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'codeClient',
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                errorMessage: this.checkIfFieldIsMissing('codeClient'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                    </Row>
                    <Row className="align-items-baseline">
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'numExterneContratIndividuel',
                                rootComponent: BooleanItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                trueLabel: t('intern'),
                                falseLabel: t('extern'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                        <RegroupementItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'numExterneContratCollectif',
                                rootComponent: BooleanItem,
                                disabled: !isServiceOpen,
                                options: conventions,
                                valueKey: 'code',
                                trueLabel: t('intern'),
                                falseLabel: t('extern'),
                                regroupement,
                                showRequired: true,
                                prefix,
                            }}
                        />
                    </Row>
                </PanelSection>
            </React.Fragment>
        );
    }
}

RegroupementForm.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    isServiceOpen: PropTypes.bool,
    prefix: PropTypes.string,
    regroupement: PropTypes.shape(),
    calculatedErrors: PropTypes.arrayOf(PropTypes.shape()),
    selectedConventionCode: PropTypes.string,
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default RegroupementForm;
