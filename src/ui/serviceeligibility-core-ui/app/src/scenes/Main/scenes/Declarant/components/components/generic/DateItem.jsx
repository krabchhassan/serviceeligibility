/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { Col, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';

import { CommonDatePicker } from '../../../../../../../common/utils/Form/CommonFields';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import DateUtils from '../../../../../../../common/utils/DateUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common', 'errors'])
class DateItem extends Component {
    render() {
        const { t, isConsultation, fieldName, additionalProps, value, propertyName } = this.props;
        const { disabled, columnSize, errorMessage } = additionalProps;

        return (
            <Col xs={columnSize || 3}>
                {isConsultation || disabled ? (
                    <LabelValuePresenter
                        id={fieldName}
                        label={t(`declarant.${propertyName}`)}
                        value={DateUtils.formatDisplayDate(value)}
                    />
                ) : (
                    <Fragment>
                        <Field
                            name={fieldName}
                            component={CommonDatePicker}
                            label={t(`declarant.${propertyName}`)}
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

DateItem.propTypes = {
    t: PropTypes.func,
    isConsultation: PropTypes.bool,
    additionalProps: PropTypes.shape(),
    fieldName: PropTypes.string,
    errorMessage: PropTypes.string,
    value: PropTypes.oneOf(PropTypes.string, PropTypes.shape()),
    propertyName: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default DateItem;
