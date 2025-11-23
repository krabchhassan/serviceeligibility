/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import isEqual from 'lodash/isEqual';
import { Row, PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import TextItem from './generic/TextItem';
import BooleanItem from './generic/BooleanItem';
import GenericService from './generic/GenericService';
import GenericItem from './generic/GenericItem';
import ValidationFactories from '../../../../../../common/utils/Form/ValidationFactories';
import ServiceParametrageLine from './generic/ServiceParametrageLine';
import FluxLine from './generic/FluxLine';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const required = ValidationFactories.required();

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class Tpgis extends Component {
    shouldComponentUpdate(nextProps) {
        if (
            !isEqual(nextProps.isCreate, this.props.isCreate) ||
            !isEqual(nextProps.isEdit, this.props.isEdit) ||
            !isEqual(nextProps.conventions, this.props.conventions)
        ) {
            return true;
        }
        return !isEqual(
            get(nextProps.declarant, nextProps.pilotagePrefix),
            get(this.props.declarant, this.props.pilotagePrefix),
        );
    }

    renderItem(field, prefix, Type, ...additionalProps) {
        const { isCreate, isEdit, declarant } = this.props;
        return (
            <Type
                isCreate={isCreate}
                isEdit={isEdit}
                declarant={declarant}
                fieldName={field}
                prefix={prefix}
                additionalProps={additionalProps}
            />
        );
    }

    render() {
        const { t, isCreate, isEdit, declarant, pilotagePrefix, conventions } = this.props;

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
                validate: [required],
            };
        }

        const title = get(declarant, `${pilotagePrefix}.nom`);
        const statusValue = get(declarant, `${pilotagePrefix}.serviceOuvert`) === 'true';

        return (
            <GenericService title={title} statusValue={statusValue} isCreate={isCreate}>
                <PanelSection title={t('declarant.serviceParametrage')}>
                    <ServiceParametrageLine
                        isCreate={isCreate}
                        isEdit={isEdit}
                        declarant={declarant}
                        pilotagePrefix={pilotagePrefix}
                    />
                </PanelSection>
                <PanelSection title={t('declarant.flux')}>
                    <FluxLine
                        isCreate={isCreate}
                        isEdit={isEdit}
                        declarant={declarant}
                        pilotagePrefix={pilotagePrefix}
                        conventions={conventions}
                    />
                    <Row className="align-items-baseline">
                        <GenericItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'codeClient',
                                prefix: `${pilotagePrefix}.regroupements[0]`,
                                rootComponent: TextItem,
                                disabled: !isServiceOpen,
                                ...openValidation,
                            }}
                        />
                        <GenericItem
                            basicProps={basicProps}
                            additionalProps={{
                                fieldName: 'filtreDomaine',
                                prefix: `${pilotagePrefix}.regroupements[0]`,
                                rootComponent: BooleanItem,
                                disabled: !isServiceOpen,
                                ...openValidation,
                            }}
                        />
                    </Row>
                </PanelSection>
            </GenericService>
        );
    }
}

Tpgis.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    declarant: PropTypes.shape(),
    pilotagePrefix: PropTypes.string,
    conventions: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Tpgis;
