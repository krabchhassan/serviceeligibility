/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import DateItem from './DateItem';
import BooleanItem from './BooleanItem';
import GenericItem from './GenericItem';
import ValidationFactories from '../../../../../../../common/utils/Form/ValidationFactories';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const required = ValidationFactories.required();

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class ServiceParamLineForCarteTP extends Component {
    render() {
        const { isCreate, isEdit, declarant, pilotagePrefix, isServiceOpen } = this.props;

        const basicProps = {
            isCreate,
            isEdit,
            declarant,
        };

        let openValidation = {};
        if (isServiceOpen) {
            openValidation = {
                showRequired: true,
                validate: required,
            };
        }

        return (
            <Fragment>
                <GenericItem
                    basicProps={basicProps}
                    additionalProps={{
                        fieldName: 'serviceOuvert',
                        prefix: pilotagePrefix,
                        rootComponent: BooleanItem,
                        validate: required,
                        columnSize: 5,
                    }}
                />
                <GenericItem
                    basicProps={basicProps}
                    additionalProps={{
                        fieldName: 'dateOuverture',
                        prefix: `${pilotagePrefix}.regroupements[0]`,
                        rootComponent: DateItem,
                        disabled: !isServiceOpen,
                        ...openValidation,
                        columnSize: 7,
                    }}
                />
            </Fragment>
        );
    }
}

ServiceParamLineForCarteTP.propTypes = {
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    declarant: PropTypes.shape(),
    pilotagePrefix: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ServiceParamLineForCarteTP;
