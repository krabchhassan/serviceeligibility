/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import { FieldArray } from 'redux-form';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import { PanelSection, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import isEqual from 'lodash/isEqual';
import BooleanItem from './generic/BooleanItem';
import Regroupements from './Regroupements/Regroupements';
import GenericService from './generic/GenericService';
import GenericItem from './generic/GenericItem';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const required = ValidationFactories.required();

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class Almv3 extends Component {
    shouldComponentUpdate(nextProps) {
        const isEditionMode =
            !isEqual(nextProps.isCreate, this.props.isCreate) || !isEqual(nextProps.isEdit, this.props.isEdit);
        if (
            isEditionMode ||
            !isEqual(nextProps.conventions, this.props.conventions) ||
            !isEqual(nextProps.change, this.props.change) ||
            !isEqual(nextProps.formName, this.props.formName)
        ) {
            return true;
        }
        return !isEqual(
            get(nextProps.declarant, nextProps.pilotagePrefix),
            get(this.props.declarant, this.props.pilotagePrefix),
        );
    }

    render() {
        const { t, isCreate, isEdit, declarant, pilotagePrefix, conventions, change, formName, calculatedErrors } =
            this.props;
        const pilotage = get(declarant, pilotagePrefix) || {};

        const basicProps = {
            isCreate,
            isEdit,
            declarant,
        };

        const isServiceOpen = pilotage.serviceOuvert === 'true';

        const title = get(declarant, `${pilotagePrefix}.nom`);
        const statusValue = get(declarant, `${pilotagePrefix}.serviceOuvert`) === 'true';

        return (
            <GenericService title={title} statusValue={statusValue} isCreate={isCreate}>
                <PanelSection title={t('declarant.serviceParametrage')}>
                    <Row>
                        <GenericItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'serviceOuvert',
                                prefix: pilotagePrefix,
                                rootComponent: BooleanItem,
                                validate: required,
                            }}
                        />
                    </Row>
                </PanelSection>

                <PanelSection title={t('declarant.listDeGroupement')}>
                    <FieldArray
                        name={`${pilotagePrefix}.regroupements`}
                        component={Regroupements}
                        isServiceOpen={isServiceOpen}
                        isCreate={isCreate}
                        isEdit={isEdit}
                        conventions={conventions}
                        change={change}
                        formName={formName}
                        calculatedErrors={calculatedErrors}
                    />
                </PanelSection>
            </GenericService>
        );
    }
}

Almv3.propTypes = {
    t: PropTypes.func,
    change: PropTypes.func,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    formName: PropTypes.string,
    declarant: PropTypes.shape(),
    calculatedErrors: PropTypes.arrayOf(PropTypes.bool),
    pilotagePrefix: PropTypes.string,
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Almv3;
