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
class CreateRegroupementDomainesTPFieldArrayComponent extends Component {
    render() {
        const { t, fields, domainList, regroupementDomainesTP, formValues, isCopy } = this.props;

        return RegroupementDomainesTPUtils.buildFieldArray(
            fields,
            t,
            domainList,
            regroupementDomainesTP,
            isCopy,
            formValues,
        );
    }
}

CreateRegroupementDomainesTPFieldArrayComponent.propTypes = {
    t: PropTypes.func,
    isCopy: PropTypes.bool,
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
    form: Constants.CREATE_FORM_NAME,
})(CreateRegroupementDomainesTPFieldArrayComponent);
