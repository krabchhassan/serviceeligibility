/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import Constants from './Constants';
import FondCarteUtils from './FondCarteUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class CreateFondCarteFieldArrayComponent extends Component {
    render() {
        const { t, fields, reseauxSoin, fondCarte, formValues, isCopy } = this.props;

        return FondCarteUtils.buildFieldArray(fields, t, reseauxSoin, fondCarte, isCopy, formValues);
    }
}

CreateFondCarteFieldArrayComponent.propTypes = {
    t: PropTypes.func,
    isCopy: PropTypes.func,
    reseauxSoin: PropTypes.arrayOf(PropTypes.shape()),
    fondCarte: PropTypes.arrayOf(PropTypes.shape()),
    fields: PropTypes.shape({
        push: PropTypes.func,
        name: PropTypes.string,
        remove: PropTypes.func,
        length: PropTypes.number,
    }).isRequired,
    formValues: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: Constants.CREATE_FORM_NAME,
})(CreateFondCarteFieldArrayComponent);
