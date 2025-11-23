import { connect } from 'react-redux';

import actions from '../actions';
import TriggerDetailComponent from './TriggerDetailComponent';
import globalActions from '../../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapStateToProps = (state) => ({
    triggeredKOBeneficiaries: state.main.triggers.triggeredKOBeneficiaries,
    koTotalElements: state.main.triggers.koTotalElements,
    koTotalPages: state.main.triggers.koTotalPages,
    koLoading: state.main.triggers.koLoading,
});

const mapDispatchToProps = {
    getKOTriggeredBeneficiaries: actions.getKOTriggeredBeneficiaries,
    addAlert: globalActions.addAlert,
};

const TriggerDetail = connect(mapStateToProps, mapDispatchToProps)(TriggerDetailComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TriggerDetail;
