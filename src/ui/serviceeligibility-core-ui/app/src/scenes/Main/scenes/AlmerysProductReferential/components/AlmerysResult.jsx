/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import AlmerysResultComponent from './AlmerysResultComponent';
import actions from './../actions';
import globalActions from '../../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    updatedPdt: state.main.almerysProduct.product,
});
const mapDispatchToProps = {
    addAlert: globalActions.addAlert,
    updateAlmerysProduct: actions.updateAlmerysProduct,
};

const AlmerysResult = connect(mapStateToProps, mapDispatchToProps)(AlmerysResultComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default AlmerysResult;
