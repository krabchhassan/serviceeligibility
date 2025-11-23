/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParamDomainSPComponent from './ParamDomainSPComponent';
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
    domainSPList: state.main.parametrageMetier.domainSPList,
    isLoading: state.main.parametrageMetier.isLoading,
    domainSPCode: formSelector(state, Constants.FIELDS.domainSPCode) || '',
    domainSPName: formSelector(state, Constants.FIELDS.domainSPName) || '',
    domainSPTranscode: formSelector(state, Constants.FIELDS.domainSPTranscode) || '',
});

const mapDispatchToProps = {
    getAllDomainsSP: actions.getAllDomainsSP,
    createDomainSP: actions.createDomainSP,
    updateDomainSP: actions.updateDomainSP,
    deleteDomainSP: actions.deleteDomainSP,
    addAlert: globalActions.addAlertError,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamDomainSP = connect(mapStateToProps, mapDispatchToProps)(ParamDomainSPComponent);
export default ParamDomainSP;
