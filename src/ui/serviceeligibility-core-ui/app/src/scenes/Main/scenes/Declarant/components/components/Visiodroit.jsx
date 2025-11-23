/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import get from 'lodash/get';
import isEqual from 'lodash/isEqual';
import PropTypes from 'prop-types';
import { Row, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import TextItem from './generic/TextItem';
import DateItem from './generic/DateItem';
import BooleanItem from './generic/BooleanItem';
import GenericService from './generic/GenericService';
import GenericItem from './generic/GenericItem';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const required = ValidationFactories.required();
const isNumber = ValidationFactories.isNumber();
const maxLength5 = ValidationFactories.maxLength(5);

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class Visiodroit extends Component {
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
                                disabled: !isServiceOpen,
                                ...openValidation,
                            }}
                        />
                        <GenericItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'generateFichier',
                                prefix: `${pilotagePrefix}.regroupements[0]`,
                                rootComponent: BooleanItem,
                                disabled: !isServiceOpen,
                            }}
                        />
                        <GenericItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'codeComptable',
                                prefix: `${pilotagePrefix}.regroupements[0]`,
                                rootComponent: TextItem,
                                disabled:
                                    !isServiceOpen || get(pilotage, 'regroupements[0].generateFichier') !== 'true',
                                validate: [isNumber, maxLength5],
                            }}
                        />
                    </Row>
                </PanelSection>
            </GenericService>
        );
    }
}

Visiodroit.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    declarant: PropTypes.shape(),
    pilotagePrefix: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Visiodroit;
