/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { Col, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';

import { CommonCombobox } from '../../../../../../../common/utils/Form/CommonFields';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import formConstants from '../../../Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class SelectItem extends Component {
    render() {
        const { t, isConsultation, fieldName, additionalProps, value, propertyName } = this.props;
        const {
            disabled,
            validate,
            showRequired,
            valueKey,
            labelKey,
            options,
            columnSize,
            tooltip,
            placeholder,
            clearable,
        } = additionalProps;

        const conditionalProps = {};
        if (valueKey) {
            conditionalProps.valueKey = valueKey;
        }
        if (labelKey) {
            conditionalProps.labelKey = labelKey;
        }

        return (
            <Col xs={columnSize || 3}>
                {isConsultation ? (
                    <LabelValuePresenter id={fieldName} label={t(`declarant.${propertyName}`)} value={value} />
                ) : (
                    <Field
                        name={fieldName}
                        component={CommonCombobox}
                        label={t(`declarant.${propertyName}`)}
                        placeholder={placeholder}
                        disabled={disabled}
                        tooltip={tooltip}
                        validate={validate}
                        showRequired={showRequired}
                        options={options}
                        labelPortion={formConstants.LABEL_PORTION}
                        clearable={clearable || false}
                        {...conditionalProps}
                        inline
                    />
                )}
            </Col>
        );
    }
}

SelectItem.propTypes = {
    t: PropTypes.func,
    isConsultation: PropTypes.bool,
    fieldName: PropTypes.string,
    value: PropTypes.oneOf(PropTypes.string, PropTypes.shape()),
    additionalProps: PropTypes.shape(),
    propertyName: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default SelectItem;
