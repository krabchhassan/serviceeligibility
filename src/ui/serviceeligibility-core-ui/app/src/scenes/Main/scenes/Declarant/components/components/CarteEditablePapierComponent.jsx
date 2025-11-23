/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { Col, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CommonRadio } from '../../../../../../common/utils/Form/CommonFields';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class CarteEditablePapierComponent extends Component {
    render() {
        const { t, pilotagePrefix } = this.props;

        return (
            <form>
                <Row className="mr-4">
                    <Col xs="4">
                        <Field
                            type="radio"
                            name={`${pilotagePrefix}.isCarteEditable`}
                            component={CommonRadio}
                            cgdComment={false}
                            label={t('yes')}
                            value="true"
                            labelPortion={0}
                            inline
                        />
                    </Col>
                    <Col xs="4">
                        <Field
                            type="radio"
                            name={`${pilotagePrefix}.isCarteEditable`}
                            component={CommonRadio}
                            cgdComment={false}
                            label={t('no')}
                            value="false"
                            labelPortion={0}
                            inline
                        />
                    </Col>
                </Row>
            </form>
        );
    }
}

CarteEditablePapierComponent.propTypes = {
    t: PropTypes.func,
    pilotagePrefix: PropTypes.string,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default CarteEditablePapierComponent;
