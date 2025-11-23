/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import { Field } from 'redux-form';
import PropTypes from 'prop-types';
import { Button, Col, Row, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import { CommonInput } from '../../../../../../../../common/utils/Form/CommonFields';
import ValidationFactories from '../../../../../../../../common/utils/Form/ValidationFactories';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
const strictLength = ValidationFactories.strictLength(10);
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class NumAMCEchangesFieldArray extends Component {
    deleteAMC(event, index) {
        const { fields } = this.props;
        event.preventDefault();

        fields.remove(index);
    }

    handleAMCChange(index, member) {
        const { change } = this.props;
        index.preventDefault();

        change(member, index.target.value);
    }

    render() {
        const { t, fields } = this.props;
        return (
            <Fragment>
                {fields.length === 0 ? (
                    <div className="mb-2" style={{ paddingTop: '-25px' }}>
                        {t('declarant.aucunAMCEchange')}
                    </div>
                ) : (
                    (fields || []).map((member, index) => (
                        <Row>
                            <Col xs={9}>
                                <Field
                                    name={member}
                                    component={CommonInput}
                                    placeholder={t('declarant.numerosAMCEchanges')}
                                    type="text"
                                    onChange={(event) => this.handleAMCChange(event, member)}
                                    formGroupClassName="mb-2"
                                    validate={[required, strictLength]}
                                />
                            </Col>
                            <Col xs={1}>
                                <div>
                                    <Button
                                        behavior="default"
                                        outlineNoBorder
                                        onClick={(event) => this.deleteAMC(event, index)}
                                    >
                                        <CgIcon behavior="secondary" name="trash-o" />
                                    </Button>
                                </div>
                            </Col>
                        </Row>
                    ))
                )}

                <Row className="ml-0">
                    <CgIcon
                        behavior="secondary"
                        name="plus-square"
                        size="1x"
                        onClick={(event) => {
                            event.preventDefault();
                            fields.push('');
                        }}
                    />
                    <span className="ml-2">{t('declarant.ajouterAMCEchange')}</span>
                </Row>
            </Fragment>
        );
    }
}

NumAMCEchangesFieldArray.propTypes = {
    t: PropTypes.func,
    change: PropTypes.func,
    fields: PropTypes.shape({
        push: PropTypes.func,
        name: PropTypes.string,
        remove: PropTypes.func,
        length: PropTypes.number,
    }).isRequired,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default NumAMCEchangesFieldArray;
