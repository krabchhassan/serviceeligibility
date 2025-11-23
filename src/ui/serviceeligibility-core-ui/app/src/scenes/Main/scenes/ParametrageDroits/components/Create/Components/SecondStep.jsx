/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import SecondStepComponent from './SecondStepComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    codesItelis: state.main.paramDroitsTP.codesItelis,
});

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const SecondStep = connect(mapStateToProps, null)(SecondStepComponent);

export default SecondStep;
