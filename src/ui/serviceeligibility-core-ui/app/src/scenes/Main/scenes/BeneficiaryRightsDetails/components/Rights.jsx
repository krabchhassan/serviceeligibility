/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import RightsComponent from './RightsComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    clientType: state.common.configuration.values.clientType,
});

const mapDispatchToProps = {};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const Rights = connect(mapStateToProps, mapDispatchToProps)(RightsComponent);
export default Rights;
