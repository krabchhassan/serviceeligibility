/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import Constants from './Constants';
import ConventionTPUtils from './ConventionTPUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class ConventionTPFieldArrayComponent extends Component {
    render() {
        const { t, fields, domainList, reseauxSoin, conventions, conventionTP, formValues } = this.props;

        return ConventionTPUtils.buildFieldArray(
            fields,
            t,
            domainList,
            reseauxSoin,
            conventions,
            conventionTP,
            false,
            formValues,
        );
    }
}

ConventionTPFieldArrayComponent.propTypes = {
    t: PropTypes.func,
    domainList: PropTypes.arrayOf(PropTypes.shape()),
    reseauxSoin: PropTypes.arrayOf(PropTypes.shape()),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    conventionTP: PropTypes.arrayOf(PropTypes.shape()),
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
})(ConventionTPFieldArrayComponent);
