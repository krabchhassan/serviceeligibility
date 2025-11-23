/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import UpdateLotComponent from './UpdateLotComponent';
import globalActions from '../../../../../../../../common/actions';
import actions from '../../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    comboLabelsAllGT: state.common.common.comboLabelsAllGT,
});
const mapDispatchToProps = {
    addAlertError: globalActions.addAlertError,
    updateLot: actions.updateLot,
    addAlert: globalActions.addAlert,
    getAllGT: globalActions.getAllGT,
};

const UpdateLot = connect(mapStateToProps, mapDispatchToProps)(UpdateLotComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default UpdateLot;
