/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParamObjetTranscoComponent from './ParamObjetTranscoComponent';
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
    transcoParamList: state.main.parametrageMetier.transcoParamList,
    serviceList: state.main.parametrageMetier.serviceList,
    isLoading: state.main.parametrageMetier.isLoading,
    existingTransco: state.main.parametrageMetier.existingTransco,
    codeObjetTransco: formSelector(state, Constants.FIELDS.codeObjetTransco) || '',
    nomObjetTransco: formSelector(state, Constants.FIELDS.nomObjetTransco) || '',
    colonnesTransco: formSelector(state, Constants.FIELDS.colNames) || '',
    modifiedItem: formSelector(state, 'modified'),
});

const mapDispatchToProps = {
    getAllTranscodageParam: actions.getAllTranscodageParam,
    createTranscodageParam: actions.createTranscodageParam,
    getTranscodageParamByCode: actions.getTranscodageParamByCode,
    deleteTrancodageParam: actions.deleteTrancodageParam,
    updateTrancodageParam: actions.updateTrancodageParam,
    addAlert: globalActions.addAlertError,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamObjetTransco = connect(mapStateToProps, mapDispatchToProps)(ParamObjetTranscoComponent);
export default ParamObjetTransco;
