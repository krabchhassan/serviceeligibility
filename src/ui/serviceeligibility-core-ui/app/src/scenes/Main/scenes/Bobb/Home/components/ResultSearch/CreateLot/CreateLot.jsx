/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import CreateLotComponent from './CreateLotComponent';
import globalActions from '../../../../../../../../common/actions';
import actions from '../../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    comboLabelsAllGT: state.common.common.comboLabelsAllGT,
    isLoadingAllGT: state.common.common.isLoadingAllGT,
    allLot: state.common.common.allLot,
});

const mapDispatchToProps = {
    newLot: actions.newLot,
    addAlert: globalActions.addAlert,
    addAlertError: globalActions.addAlertError,
    getAllGT: globalActions.getAllGT,
    getAllLot: globalActions.getAllLot,
};

const CreateLot = connect(mapStateToProps, mapDispatchToProps)(CreateLotComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default CreateLot;
