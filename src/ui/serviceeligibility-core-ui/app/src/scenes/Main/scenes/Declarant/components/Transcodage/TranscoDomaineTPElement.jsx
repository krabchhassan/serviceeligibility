/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import TranscoDomaineTPElementComponent from './TranscoDomaineTPElementComponent';
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
const TranscoDomaineTPElement = connect(mapStateToProps)(TranscoDomaineTPElementComponent);

export default TranscoDomaineTPElement;
