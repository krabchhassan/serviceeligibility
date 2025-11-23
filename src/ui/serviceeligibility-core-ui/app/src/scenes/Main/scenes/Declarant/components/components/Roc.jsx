/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import get from 'lodash/get';
import isEqual from 'lodash/isEqual';
import PropTypes from 'prop-types';
import { Row, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import DateItem from './generic/DateItem';
import TextItem from './generic/TextItem';
import SelectItem from './generic/SelectItem';
import BooleanItem from './generic/BooleanItem';
import GenericService from './generic/GenericService';
import GenericItem from './generic/GenericItem';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const required = ValidationFactories.required();
const isNumber = ValidationFactories.isNumber();
const minValue = ValidationFactories.minValue(-1);

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
const getPeriodeValues = (props) => {
    const { declarant, pilotagePrefix } = props;
    const pilotage = get(declarant, pilotagePrefix) || {};
    const duree = get(pilotage, 'regroupements[0].dureeValidite');
    const periode = get(pilotage, 'regroupements[0].periodeValidite');
    let hasDuree = false;
    if (duree !== undefined) {
        hasDuree = duree.toString().length > 0;
    }
    let hasPeriode = false;
    if (periode !== undefined) {
        hasPeriode = periode != null && periode !== '';
    }
    return {
        hasDuree,
        hasPeriode,
    };
};

@TranslationsSubscriber(['eligibility', 'common'])
class Roc extends Component {
    shouldComponentUpdate(nextProps) {
        if (!isEqual(nextProps.isCreate, this.props.isCreate) || !isEqual(nextProps.isEdit, this.props.isEdit)) {
            return true;
        }
        return !isEqual(
            get(nextProps.declarant, nextProps.pilotagePrefix),
            get(this.props.declarant, this.props.pilotagePrefix),
        );
    }

    render() {
        const { t, isCreate, isEdit, declarant, pilotagePrefix } = this.props;

        const periodesValidite = [
            { code: 'debutMois', label: 'Début de mois' },
            { code: 'milieuMois', label: 'Milieu de mois' },
            { code: 'finMois', label: 'Fin de mois' },
        ];
        const pilotage = get(declarant, pilotagePrefix) || {};
        const basicProps = {
            isCreate,
            isEdit,
            declarant,
        };
        const disableProps = {
            isCreate: false,
            isEdit: false,
            declarant,
        };

        const isServiceOpen = pilotage.serviceOuvert === 'true';
        const { hasDuree, hasPeriode } = getPeriodeValues(this.props);

        let openValidation = {};
        const openValidationDuree = { showRequired: false, validate: [isNumber, minValue] };
        if (isServiceOpen) {
            openValidation = {
                showRequired: true,
                validate: [required],
            };
        }
        let forcedPeriodeValue = null;
        if (!isCreate && !isEdit) {
            const selectedPeriodeCode = get(pilotage, 'regroupements[0].periodeValidite');
            if (selectedPeriodeCode) {
                const selectedPeriode = periodesValidite.find((periode) => periode.code === selectedPeriodeCode) || {};
                forcedPeriodeValue = `${selectedPeriode.label}`;
            }
        }

        const title = get(declarant, `${pilotagePrefix}.nom`);
        const statusValue = get(declarant, `${pilotagePrefix}.serviceOuvert`) === 'true';

        return (
            <GenericService title={title} statusValue={statusValue} isCreate={isCreate}>
                <PanelSection title={t('declarant.serviceParametrage')}>
                    <Row className="align-items-baseline">
                        <GenericItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'serviceOuvert',
                                prefix: pilotagePrefix,
                                rootComponent: BooleanItem,
                            }}
                        />
                        <GenericItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'dateOuverture',
                                prefix: `${pilotagePrefix}.regroupements[0]`,
                                rootComponent: DateItem,
                                disabled: pilotage.serviceOuvert === 'false',
                                ...openValidation,
                            }}
                        />
                    </Row>
                </PanelSection>
                <PanelSection title={t('declarant.validite')}>
                    <Row>
                        <GenericItem
                            basicProps={hasPeriode ? disableProps : basicProps}
                            additionalProps={{
                                fieldName: 'dureeValidite',
                                prefix: `${pilotagePrefix}.regroupements[0]`,
                                rootComponent: TextItem,
                                disabled: !isServiceOpen || hasPeriode,
                                ...openValidationDuree,
                            }}
                        />
                        <GenericItem
                            basicProps={hasDuree ? disableProps : basicProps}
                            additionalProps={{
                                fieldName: 'periodeValidite',
                                prefix: `${pilotagePrefix}.regroupements[0]`,
                                rootComponent: SelectItem,
                                columnSize: 4,
                                options: periodesValidite,
                                forcedValue: forcedPeriodeValue,
                                valueKey: 'code',
                                disabled: !isServiceOpen || hasDuree,
                                clearable: true,
                            }}
                        />
                    </Row>
                </PanelSection>
            </GenericService>
        );
    }
}

Roc.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    declarant: PropTypes.shape(),
    pilotagePrefix: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Roc;
