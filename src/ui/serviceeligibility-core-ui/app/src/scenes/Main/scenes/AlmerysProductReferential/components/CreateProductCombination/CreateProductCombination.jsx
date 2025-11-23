/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import globalActions from '../../../../../../common/actions';
import CreateProductCombinationComponent from './CreateProductCombinationComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    comboLabelsAllGT: state.common.common.comboLabelsAllGT,
    comboLabelsAllLot: state.common.common.comboLabelsAllLot,
});

const mapDispatchToProps = {
    addAlertError: globalActions.addAlertError,
    getAllGT: globalActions.getAllGT,
    getAllLot: globalActions.getAllLot,
};

const CreateProductCombination = connect(mapStateToProps, mapDispatchToProps)(CreateProductCombinationComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default CreateProductCombination;
