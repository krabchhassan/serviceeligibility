/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { Row, Col, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';

import { CommonInput } from '../../../../../../../common/utils/Form/CommonFields';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common', 'errors'])
class TextItem extends Component {
    render() {
        const { t, isConsultation, fieldName, value, additionalProps, propertyName } = this.props;
        const { disabled, validate, showRequired, columnSize, tooltip, placeholder, errorMessage, additionnalLabel } =
            additionalProps;

        const consultationValue =
            additionnalLabel && value && value !== '-' ? `${value} ${t(`declarant.${additionnalLabel}`)}` : value;
        return (
            <Col xs={columnSize || 3}>
                {isConsultation ? (
                    <LabelValuePresenter
                        id={fieldName}
                        label={t(`declarant.${propertyName}`)}
                        value={consultationValue}
                    />
                ) : (
                    <Fragment>
                        <Field
                            name={fieldName}
                            component={CommonInput}
                            label={t(`declarant.${propertyName}`)}
                            placeholder={placeholder}
                            disabled={disabled}
                            tooltip={tooltip}
                            validate={validate}
                            showRequired={showRequired}
                            {...additionalProps}
                            inline
                        />
                        {errorMessage && (
                            <Row>
                                <Col xs="8" className="ml-auto pl-1">
                                    <div className="mt-0 invalid-feedback">{t('fields.required')}</div>
                                </Col>
                            </Row>
                        )}
                    </Fragment>
                )}
            </Col>
        );
    }
}

TextItem.propTypes = {
    t: PropTypes.func,
    isConsultation: PropTypes.bool,
    additionalProps: PropTypes.shape(),
    errorMessage: PropTypes.string,
    fieldName: PropTypes.string,
    value: PropTypes.oneOf(PropTypes.string, PropTypes.shape()),
    propertyName: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TextItem;
