/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import Constants from './Constants';
import TranscodageUtils from './TranscodageUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class TranscodageFieldArrayComponent extends Component {
    render() {
        const { t, fields, change, domainList, formValues } = this.props;

        return TranscodageUtils.buildFieldArray(fields, t, change, domainList, formValues);
    }
}

TranscodageFieldArrayComponent.propTypes = {
    t: PropTypes.func,
    change: PropTypes.func,
    domainList: PropTypes.arrayOf(PropTypes.shape()),
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
    form: Constants.FORM_NAME,
})(TranscodageFieldArrayComponent);
