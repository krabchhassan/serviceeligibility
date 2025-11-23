/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import ParametrageMetierComponent from './ParametrageMetierComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = () => ({});

const mapDispatchToProps = {};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParametrageMetier = connect(mapStateToProps, mapDispatchToProps)(ParametrageMetierComponent);

export default ParametrageMetier;
