/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import RegroupementDomainesTPElementComponent from './RegroupementDomainesTPElementComponent';
import Constants from './Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const formSelector = formValueSelector(Constants.FORM_NAME);
/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state, ownProps) => ({
    data: formSelector(state, ownProps.member),
});

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const RegroupementDomainesTPElement = connect(mapStateToProps)(RegroupementDomainesTPElementComponent);

export default RegroupementDomainesTPElement;
