/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParamServiceTranscoComponent from './ParamServiceTranscoComponent';
import actions from '../../actions';
import globalActions from '../../../../../../common/actions';
import Constants from './Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const formSelector = formValueSelector(Constants.FORM_NAME);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    serviceList: state.main.parametrageMetier.serviceList,
    transcoParamList: state.main.parametrageMetier.transcoParamList,
    serviceCode: formSelector(state, Constants.FIELDS.serviceCode) || '',
    serviceTransco: formSelector(state, Constants.FIELDS.serviceTransco) || [],
});

const mapDispatchToProps = {
    getAllServicesTransco: actions.getAllServicesTransco,
    createServiceTransco: actions.createServiceTransco,
    deleteServiceTransco: actions.deleteServiceTransco,
    updateServiceTransco: actions.updateServiceTransco,
    addAlert: globalActions.addAlertError,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamServiceTransco = connect(mapStateToProps, mapDispatchToProps)(ParamServiceTranscoComponent);
export default ParamServiceTransco;
