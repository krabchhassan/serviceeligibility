/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import RegroupementsComponent from './RegroupementsComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const formSelector = (formName) => formValueSelector(formName);

const mapStateToProps = (state, ownProps) => ({
    existingRegroupements: formSelector(ownProps.formName)(state, 'pilotages[6].regroupements'),
});

const Regroupements = connect(mapStateToProps, null)(RegroupementsComponent);
export default Regroupements;
