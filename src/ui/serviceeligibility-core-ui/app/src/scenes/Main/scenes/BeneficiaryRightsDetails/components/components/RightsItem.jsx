/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import RightsItemComponent from './RightsItemComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    domainList: state.main.parametrageMetier.domainList,
    clientType: state.common.configuration.values.clientType,
});

const mapDispatchToProps = {};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const RightsItem = connect(mapStateToProps, mapDispatchToProps)(RightsItemComponent);
export default RightsItem;
