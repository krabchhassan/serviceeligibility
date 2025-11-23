/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import Constants from './Constants';
import RegroupementDomainesTPUtils from './RegroupementDomainesTPUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class RegroupementDomainesTPFieldArrayComponent extends Component {
    render() {
        const { t, fields, domainList, regroupementDomainesTP, formValues } = this.props;

        return RegroupementDomainesTPUtils.buildFieldArray(
            fields,
            t,
            domainList,
            regroupementDomainesTP,
            false,
            formValues,
        );
    }
}

RegroupementDomainesTPFieldArrayComponent.propTypes = {
    t: PropTypes.func,
    domainList: PropTypes.arrayOf(PropTypes.shape()),
    regroupementDomainesTP: PropTypes.arrayOf(PropTypes.shape()),
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
})(RegroupementDomainesTPFieldArrayComponent);
