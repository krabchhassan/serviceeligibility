/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParamDomainISComponent from './ParamDomainISComponent';
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
    domainISList: state.main.parametrageMetier.domainISList,
    isLoading: state.main.parametrageMetier.isLoading,
    domainISCode: formSelector(state, Constants.FIELDS.domainISCode) || '',
    domainISName: formSelector(state, Constants.FIELDS.domainISName) || '',
    domainISTranscode: formSelector(state, Constants.FIELDS.domainISTranscode) || '',
});

const mapDispatchToProps = {
    getAllDomainsIS: actions.getAllDomainsIS,
    createDomainIS: actions.createDomainIS,
    updateDomainIS: actions.updateDomainIS,
    deleteDomainIS: actions.deleteDomainIS,
    addAlert: globalActions.addAlertError,
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParamDomainIS = connect(mapStateToProps, mapDispatchToProps)(ParamDomainISComponent);
export default ParamDomainIS;
