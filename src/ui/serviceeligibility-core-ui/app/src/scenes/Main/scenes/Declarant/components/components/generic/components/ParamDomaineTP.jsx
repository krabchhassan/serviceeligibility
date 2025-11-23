/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import ParamDomaineTPComponent from './ParamDomaineTPComponent';
import Constants from './Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const getFormSelector = (ownProps) => {
    return ownProps.isCreate ? formValueSelector(Constants.CREATE_FORM_NAME) : formValueSelector(Constants.FORM_NAME);
};
/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state, ownProps) => ({
    data: getFormSelector(ownProps)(state, ownProps.member),
});

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamDomaineTP = connect(mapStateToProps, null)(ParamDomaineTPComponent);

export default ParamDomaineTP;
