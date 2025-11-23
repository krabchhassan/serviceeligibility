import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';

import TriggersComponent from './TriggersComponent';
import actions from './actions';
import globalActions from '../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const formSelector = formValueSelector('paramSearch');

const mapStateToProps = (state) => ({
    amcs: formSelector(state, 'amcs'),
    services: state.common.common.services,
    lightDeclarants: state.common.common.comboLabelsLightDeclarants,
    isLoadingLightDeclarants: state.common.common.isLoadingLightDeclarants,
    loading: state.main.triggers.loading,
    triggers: state.main.triggers.triggers,
    totalElements: state.main.triggers.totalElements,
    totalPages: state.main.triggers.totalPages,
});

const mapDispatchToProps = {
    getLightDeclarants: globalActions.getLightDeclarants,
    searchTriggers: actions.searchTriggers,
    callRecycle: actions.callRecycle,
    callCancelProcess: actions.callCancelProcess,
    callTriggerGeneration: actions.callTriggerGeneration,
    callAbandon: actions.callAbandon,
    addAlertError: globalActions.addAlertError,
    addAlert: globalActions.addAlert,
};

const Triggers = connect(mapStateToProps, mapDispatchToProps)(TriggersComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Triggers;
