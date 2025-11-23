/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import { Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import TextItem from './TextItem';
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
class ServiceParametrageLine extends Component {
    render() {
        const { isCreate, isEdit, declarant, pilotagePrefix } = this.props;
        const pilotage = get(declarant, pilotagePrefix) || {};

        const basicProps = {
            isCreate,
            isEdit,
            declarant,
        };

        const isServiceOpen = pilotage.serviceOuvert === 'true';
        let openValidation = {};
        if (isServiceOpen) {
            openValidation = {
                showRequired: true,
                validate: required,
            };
        }

        return (
            <Fragment>
                <Row className="align-items-baseline">
                    <GenericItem
                        basicProps={basicProps}
                        additionalProps={{
                            fieldName: 'serviceOuvert',
                            prefix: pilotagePrefix,
                            rootComponent: BooleanItem,
                            validate: required,
                            columnSize: 2,
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
                            columnSize: 4,
                        }}
                    />
                    <GenericItem
                        basicProps={basicProps}
                        additionalProps={{
                            fieldName: 'dateSynchronisation',
                            prefix: `${pilotagePrefix}.regroupements[0]`,
                            rootComponent: DateItem,
                            disabled: !isServiceOpen,
                            columnSize: 3,
                        }}
                    />
                    <GenericItem
                        basicProps={basicProps}
                        additionalProps={{
                            fieldName: 'couloirClient',
                            prefix: `${pilotagePrefix}.regroupements[0]`,
                            rootComponent: TextItem,
                            disabled: !isServiceOpen,
                            ...openValidation,
                            columnSize: 3,
                        }}
                    />
                </Row>
            </Fragment>
        );
    }
}

ServiceParametrageLine.propTypes = {
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    declarant: PropTypes.shape(),
    pilotagePrefix: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ServiceParametrageLine;
