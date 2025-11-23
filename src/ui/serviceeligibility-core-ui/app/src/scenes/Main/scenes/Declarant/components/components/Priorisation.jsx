/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { PanelSection, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import get from 'lodash/get';
import isEqual from 'lodash/isEqual';
import GenericService from './generic/GenericService';
import ServiceParametrageLine from './generic/ServiceParametrageLine';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class Priorisation extends Component {
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
            </GenericService>
        );
    }
}

Priorisation.propTypes = {
    t: PropTypes.func,
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    declarant: PropTypes.shape(),
    pilotagePrefix: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Priorisation;
