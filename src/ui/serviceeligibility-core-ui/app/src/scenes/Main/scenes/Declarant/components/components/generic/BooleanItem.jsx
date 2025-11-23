/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { Col, Row, Tooltip, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';

import { CommonRadio } from '../../../../../../../common/utils/Form/CommonFields';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import formConstants from '../../../Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class BooleanItem extends Component {
    render() {
        const { t, isConsultation, additionalProps, trueLabel, falseLabel, fieldName, value, propertyName } =
            this.props;
        const { showRequired, columnSize, validate, tooltip } = additionalProps;
        const labelValue = value === 'true' ? t(trueLabel) : t(falseLabel);
        const formTitle = `${t(`declarant.${propertyName}`)}${showRequired ? ' *' : ''}`;

        let spanClass = 'cgd-comment';
        if (!isConsultation) {
            spanClass += ' text-colored-mix-0';
        }

        return (
            <Col xs={columnSize || 3}>
                {isConsultation ? (
                    <LabelValuePresenter id={fieldName} label={t(`declarant.${propertyName}`)} value={labelValue} />
                ) : (
                    <Row noGutters>
                        <Col xs={formConstants.LABEL_PORTION} className="align-right pr-2">
                            <span className={spanClass}>
                                {formTitle}
                                {t('colon')}
                            </span>
                        </Col>
                        <Col xs={12 - formConstants.LABEL_PORTION}>
                            <div className="d-flex flex-fill align-items-baseline">
                                <Field
                                    type="radio"
                                    name={fieldName}
                                    component={CommonRadio}
                                    label={t(trueLabel)}
                                    value="true"
                                    validate={validate}
                                />
                                <Field
                                    type="radio"
                                    name={fieldName}
                                    component={CommonRadio}
                                    label={t(falseLabel)}
                                    value="false"
                                    validate={validate}
                                />
                                {tooltip && (
                                    <Fragment>
                                        <CgIcon id={fieldName} name="information-tooltip" />
                                        <Tooltip key="tooltip" placement="bottom" target={fieldName}>
                                            {tooltip}
                                        </Tooltip>
                                    </Fragment>
                                )}
                            </div>
                        </Col>
                    </Row>
                )}
            </Col>
        );
    }
}

BooleanItem.propTypes = {
    t: PropTypes.func,
    isConsultation: PropTypes.bool,
    additionalProps: PropTypes.shape(),
    trueLabel: PropTypes.string,
    falseLabel: PropTypes.string,
    fieldName: PropTypes.string,
    propertyName: PropTypes.string,
    value: PropTypes.oneOf(PropTypes.string, PropTypes.shape()),
};

BooleanItem.defaultProps = {
    trueLabel: 'yes',
    falseLabel: 'no',
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default BooleanItem;
