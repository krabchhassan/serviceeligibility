/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import ConventionTPElementComponent from './ConventionTPElementComponent';
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
const ConventionTPElement = connect(mapStateToProps)(ConventionTPElementComponent);

export default ConventionTPElement;
