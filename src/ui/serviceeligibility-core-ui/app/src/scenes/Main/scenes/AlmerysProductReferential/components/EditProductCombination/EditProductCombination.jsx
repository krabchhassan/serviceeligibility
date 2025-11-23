/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import globalActions from '../../../../../../common/actions';
import EditProductCombinationComponent from './EditProductCombinationComponent';
import actions from '../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    comboLabelsAllGT: state.common.common.comboLabelsAllGT,
    comboLabelsAllLot: state.common.common.comboLabelsAllLot,
});

const mapDispatchToProps = {
    addAlert: globalActions.addAlert,
    addAlertError: globalActions.addAlertError,
    getAllGT: globalActions.getAllGT,
    getAllLot: globalActions.getAllLot,
    updateAlmerysProduct: actions.updateAlmerysProduct,
};

const EditProductCombination = connect(mapStateToProps, mapDispatchToProps)(EditProductCombinationComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default EditProductCombination;
