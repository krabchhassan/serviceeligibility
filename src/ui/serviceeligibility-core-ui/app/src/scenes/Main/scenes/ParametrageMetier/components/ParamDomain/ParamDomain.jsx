/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParamDomainComponent from './ParamDomainComponent';
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
    domainList: state.main.parametrageMetier.domainList,
    isLoading: state.main.parametrageMetier.isLoading,
    domainCode: formSelector(state, Constants.FIELDS.domainCode) || '',
    domainName: formSelector(state, Constants.FIELDS.domainName) || '',
    domainTranscode: formSelector(state, Constants.FIELDS.domainTranscode) || '',
    domainCategory: formSelector(state, Constants.FIELDS.domainCategory) || '',
    domainPriority: formSelector(state, Constants.FIELDS.domainPriority) || '',
    domainIsAddingWarranties: formSelector(state, Constants.FIELDS.domainIsAddingWarranties) || '',
    domainIsAddingWarrantiesCapped: formSelector(state, Constants.FIELDS.domainIsAddingWarrantiesCapped) || '',
});

const mapDispatchToProps = {
    getAllDomains: actions.getAllDomains,
    createDomain: actions.createDomain,
    updateDomain: actions.updateDomain,
    deleteDomain: actions.deleteDomain,
    addAlert: globalActions.addAlertError,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamDomain = connect(mapStateToProps, mapDispatchToProps)(ParamDomainComponent);
export default ParamDomain;
