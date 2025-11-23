import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Field, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import {
    Button,
    Modal,
    ModalBody,
    ModalHeader,
    Row,
    ModalFooter,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { CommonInput } from '../../../../../common/utils/Form/CommonFields';
import SecondStepComponent from './SecondStepComponent';
import Constants from '../Constants';

@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class ModalCartes extends Component {
    @autobind
    handleSubmit(values) {
        const { commonBody, validateFunc } = this.props;
        commonBody.forcePapier = values.forcePapier;
        commonBody.forceDemat = values.forceDemat;
        validateFunc(commonBody);
    }

    render() {
        const { t, modal, toggle, commonBody, handleSubmit } = this.props;

        return (
            <Modal size="sm" isOpen={modal} toggle={() => toggle()} backdrop="static">
                <form onSubmit={handleSubmit(this.handleSubmit)}>
                    <ModalHeader toggle={() => toggle()}>{t('modalCartes.title')}</ModalHeader>
                    <ModalBody>
                        <Row>
                            <Field
                                id="parametrage-forcePapier"
                                name="forcePapier"
                                component={CommonInput}
                                label={t('modalCartes.papierLabel')}
                                placeholder={t('modalCartes.papierSelect')}
                                disabled={!commonBody.forcePapier}
                                type="checkbox"
                            />
                        </Row>
                        <Row>
                            <Field
                                id="parametrage-forceDemat"
                                name="forceDemat"
                                component={CommonInput}
                                label={t('modalCartes.dematLabel')}
                                placeholder={t('modalCartes.dematSelect')}
                                disabled={!commonBody.forceDemat}
                                type="checkbox"
                            />
                        </Row>
                    </ModalBody>
                    <ModalFooter>
                        <Button id="modal-valid" type="submit" behavior="primary">
                            {t('modalCartes.apply')}
                        </Button>
                    </ModalFooter>
                </form>
            </Modal>
        );
    }
}

// Add prop types
SecondStepComponent.propTypes = {
    t: PropTypes.func,
    toggle: PropTypes.func,
    modal: PropTypes.bool,
    commonBody: PropTypes.shape(),
    validateFunc: PropTypes.func,
    handleSubmit: PropTypes.func,
};

export default reduxForm({
    form: Constants.FORM_MODAL_CARTES,
})(connect()(ModalCartes));
