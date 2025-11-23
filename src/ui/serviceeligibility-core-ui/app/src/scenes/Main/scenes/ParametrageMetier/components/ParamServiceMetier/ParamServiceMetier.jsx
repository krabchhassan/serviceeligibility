/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParamServiceMetierComponent from './ParamServiceMetierComponent';
import globalActions from '../../../../../../common/actions';
import actions from '../../actions';
import Constants from './Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const formSelector = formValueSelector(Constants.FORM_NAME);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    serviceMetierList: state.main.parametrageMetier.serviceMetierList,
    isLoading: state.main.parametrageMetier.isLoading,
    serviceMetierCode: formSelector(state, Constants.FIELDS.serviceMetierCode) || '',
    serviceMetierName: formSelector(state, Constants.FIELDS.serviceMetierName) || '',
    serviceMetierOrder: formSelector(state, Constants.FIELDS.serviceMetierOrder) || '',
    serviceMetierIcon: formSelector(state, Constants.FIELDS.serviceMetierIcon) || '',
});

const mapDispatchToProps = {
    getAllServiceMetier: actions.getAllServiceMetier,
    createServiceMetier: actions.createServiceMetier,
    updateServiceMetier: actions.updateServiceMetier,
    deleteServiceMetier: actions.deleteServiceMetier,
    addAlert: globalActions.addAlertError,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamServiceMetier = connect(mapStateToProps, mapDispatchToProps)(ParamServiceMetierComponent);
export default ParamServiceMetier;
