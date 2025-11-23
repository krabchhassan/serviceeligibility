/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import get from 'lodash/get';
import { TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class RegroupementItem extends Component {
    render() {
        const { t, basicProps, additionalProps } = this.props;
        const { isCreate, isEdit } = basicProps;
        const {
            rootComponent: RootComponent,
            trueLabel,
            falseLabel,
            fieldName,
            forcedValue,
            errorMessage,
            regroupement,
            prefix,
            onChange,
        } = additionalProps;
        const completeFieldName = prefix ? `${prefix}.${fieldName}` : fieldName;

        const transTooltipKey = `declarant.${fieldName}Tooltip`;
        const transTooltip = t(transTooltipKey);
        const tooltipTranslation = transTooltip === transTooltipKey ? null : transTooltip;

        const transPlaceholderKey = `declarant.${fieldName}Placeholder`;
        const transPlaceholder = t(transPlaceholderKey);
        const placeholderTranslation = transPlaceholder === transPlaceholderKey ? null : transPlaceholder;

        return (
            <RootComponent
                isCreate={isCreate}
                isEdit={isEdit}
                regroupement={regroupement}
                additionalProps={{
                    ...additionalProps,
                    behavior: errorMessage ? 'danger' : 'success',
                    onChange,
                    errorMessage,
                    tooltip: tooltipTranslation,
                    placeholder: placeholderTranslation,
                }}
                trueLabel={trueLabel}
                falseLabel={falseLabel}
                isConsultation={!isCreate && !isEdit}
                fieldName={completeFieldName}
                propertyName={fieldName}
                value={forcedValue || get(regroupement, fieldName)}
            />
        );
    }
}

RegroupementItem.propTypes = {
    t: PropTypes.func,
    basicProps: PropTypes.shape(),
    additionalProps: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default RegroupementItem;
