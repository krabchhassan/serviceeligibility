/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParamFormulaComponent from './ParamFormulaComponent';
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
    formulaList: state.main.parametrageMetier.formulaList,
    isLoading: state.main.parametrageMetier.isLoading,
    formulaCode: formSelector(state, Constants.FIELDS.formulaCode) || '',
    formulaParam1: formSelector(state, Constants.FIELDS.formulaParam1) || '',
    formulaParam2: formSelector(state, Constants.FIELDS.formulaParam2) || '',
    formulaParam3: formSelector(state, Constants.FIELDS.formulaParam3) || '',
    formulaParam4: formSelector(state, Constants.FIELDS.formulaParam4) || '',
    formulaParam5: formSelector(state, Constants.FIELDS.formulaParam5) || '',
    formulaParam6: formSelector(state, Constants.FIELDS.formulaParam6) || '',
    formulaParam7: formSelector(state, Constants.FIELDS.formulaParam7) || '',
    formulaParam8: formSelector(state, Constants.FIELDS.formulaParam8) || '',
    formulaParam9: formSelector(state, Constants.FIELDS.formulaParam9) || '',
    formulaParam10: formSelector(state, Constants.FIELDS.formulaParam10) || '',
});

const mapDispatchToProps = {
    getAllFormulas: actions.getAllFormulas,
    createFormula: actions.createFormula,
    updateFormula: actions.updateFormula,
    deleteFormula: actions.deleteFormula,
    addAlert: globalActions.addAlertError,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamFormula = connect(mapStateToProps, mapDispatchToProps)(ParamFormulaComponent);
export default ParamFormula;
