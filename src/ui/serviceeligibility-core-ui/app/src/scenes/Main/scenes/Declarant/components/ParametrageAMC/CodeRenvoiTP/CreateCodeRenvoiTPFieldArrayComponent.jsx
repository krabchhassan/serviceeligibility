/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { Component } from 'react';
import { reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import Constants from './Constants';
import CodeRenvoiTPUtils from './CodeRenvoiTPUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class CreateCodeRenvoiTPFieldArrayComponent extends Component {
    render() {
        const { t, fields, domainList, reseauxSoin, codeRenvoiTP, formValues, isCopy, returnCodesList } = this.props;

        return CodeRenvoiTPUtils.buildFieldArray(
            fields,
            t,
            domainList,
            reseauxSoin,
            returnCodesList,
            codeRenvoiTP,
            isCopy,
            formValues,
        );
    }
}

CreateCodeRenvoiTPFieldArrayComponent.propTypes = {
    t: PropTypes.func,
    isCopy: PropTypes.func,
    reseauxSoin: PropTypes.arrayOf(PropTypes.shape()),
    domainList: PropTypes.arrayOf(PropTypes.shape()),
    returnCodesList: PropTypes.shape(),
    codeRenvoiTP: PropTypes.arrayOf(PropTypes.shape()),
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
})(CreateCodeRenvoiTPFieldArrayComponent);
