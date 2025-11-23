/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import DeclarationRightsItemComponent from './DeclarationRightsItemComponent';

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
const DeclarationRightsItem = connect(mapStateToProps, mapDispatchToProps)(DeclarationRightsItemComponent);
export default DeclarationRightsItem;
