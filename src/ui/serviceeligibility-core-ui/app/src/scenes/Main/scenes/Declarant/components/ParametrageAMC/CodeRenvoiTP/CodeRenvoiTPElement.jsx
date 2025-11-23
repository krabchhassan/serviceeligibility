/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { formValueSelector } from 'redux-form';
import { connect } from 'react-redux';
import CodeRenvoiTPElementComponent from './CodeRenvoiTPElementComponent';
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
const CodeRenvoiTPElement = connect(mapStateToProps, null)(CodeRenvoiTPElementComponent);

export default CodeRenvoiTPElement;
