/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import { Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import TextItem from './TextItem';
import SelectItem from './SelectItem';
import GenericItem from './GenericItem';
import ValidationFactories from '../../../../../../../common/utils/Form/ValidationFactories';
import CommonSpinner from '../../../../../../../common/components/CommonSpinner/CommonSpinner';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const required = ValidationFactories.required();
const isNumber = ValidationFactories.isNumber();
const maxLength10 = ValidationFactories.maxLength(10);

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class FluxLine extends Component {
    render() {
        const { isCreate, isEdit, declarant, pilotagePrefix, conventions, dclben } = this.props;

        const pilotage = get(declarant, pilotagePrefix) || {};
        const basicProps = {
            isCreate,
            isEdit,
            declarant,
        };

        const canRender = declarant;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const isServiceOpen = pilotage.serviceOuvert === 'true';
        let openValidation = {};
        let fichierValidator = {};
        if (isServiceOpen) {
            openValidation = {
                showRequired: true,
                validate: [required],
            };

            fichierValidator = {
                showRequired: true,
                validate: [required, isNumber, maxLength10],
            };
        }
        let forcedConventionValue = null;
        let activateTypeConvention = true;
        if (!isCreate && !isEdit) {
            const selectedConventionCode = get(pilotage, 'regroupements[0].typeConventionnement');
            if (selectedConventionCode) {
                const selectedConvention =
                    conventions.find((convention) => convention.code === selectedConventionCode) || {};
                forcedConventionValue = `${selectedConvention.label}`;
            }
        } else {
            activateTypeConvention = !dclben;
        }

        return (
            <Row>
                <GenericItem
                    basicProps={basicProps}
                    additionalProps={{
                        fieldName: 'typeConventionnement',
                        prefix: `${pilotagePrefix}.regroupements[0]`,
                        rootComponent: SelectItem,
                        disabled: !isServiceOpen || !activateTypeConvention,
                        options: conventions,
                        forcedValue: forcedConventionValue,
                        valueKey: 'code',
                        ...openValidation,
                    }}
                />
                <GenericItem
                    basicProps={basicProps}
                    additionalProps={{
                        fieldName: 'numDebutFichier',
                        prefix: `${pilotagePrefix}.regroupements[0]`,
                        rootComponent: TextItem,
                        disabled: !isServiceOpen,
                        ...fichierValidator,
                    }}
                />
                <GenericItem
                    basicProps={basicProps}
                    additionalProps={{
                        fieldName: 'numEmetteur',
                        prefix: `${pilotagePrefix}.regroupements[0]`,
                        rootComponent: TextItem,
                        disabled: !isServiceOpen,
                        ...openValidation,
                    }}
                />
                <GenericItem
                    basicProps={basicProps}
                    additionalProps={{
                        fieldName: 'versionNorme',
                        prefix: `${pilotagePrefix}.regroupements[0]`,
                        rootComponent: TextItem,
                        disabled: !isServiceOpen,
                        ...openValidation,
                    }}
                />
            </Row>
        );
    }
}

FluxLine.propTypes = {
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    declarant: PropTypes.shape(),
    pilotagePrefix: PropTypes.string,
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default FluxLine;
