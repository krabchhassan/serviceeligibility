import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';

import ParametrageDroitsComponent from './ParametrageDroitsComponent';
import actions from './actions';
import parametrageDroitsActions from './components/Create/actions';
import globalActions from '../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const formSelector = formValueSelector('paramSearch');

const mapStateToProps = (state, ownProps) => ({
    amcs: formSelector(state, 'amcs'),
    triggerMode: formSelector(state, 'triggerMode'),
    status: formSelector(state, 'status'),
    services: state.common.common.services,
    comboLabelsLightDeclarants: state.common.common.comboLabelsLightDeclarants,
    isLoadingLightDeclarants: state.common.common.isLoadingLightDeclarants,
    paramDroitsList: state.main.paramDroits.paramDroitsList,
    loading: state.main.paramDroits.loading,
    totalElements: state.main.paramDroits.totalElements,
    totalPages: state.main.paramDroits.totalPages,
    hasCreated: ownProps.match.params.hasCreated,
    comboLabelsAllGT: state.common.common.comboLabelsAllGT,
    isLoadingAllGT: state.common.common.isLoadingAllGT,
    comboLabelsAllLot: state.common.common.comboLabelsAllLot,
    isLoadingAllLot: state.common.common.isLoadingAllLot,
    lotsParametrage: state.common.common.lots,
    lots: formSelector(state, 'lots'),
    gts: formSelector(state, 'gts'),
    codesRenvoiList: state.common.common.codesRenvoiList,
    codesItelis: state.main.paramDroitsTP.codesItelis,
    showItelisCode: state.common.configuration.values.showItelisCode,
});

const mapDispatchToProps = {
    getLightDeclarants: globalActions.getLightDeclarants,
    searchParametrage: actions.searchParametrage,
    changeStatus: actions.changeStatus,
    addAlert: globalActions.addAlert,
    displayAlert: globalActions.displayAlert,
    getAllGT: globalActions.getAllGT,
    getLotsById: globalActions.getLotsById,
    getLots: globalActions.getLots,
    getAllCodeRenvoi: globalActions.getAllCodeRenvoi,
    getCodesItelis: parametrageDroitsActions.getCodesItelis,
};

const ParametrageDroits = connect(mapStateToProps, mapDispatchToProps)(ParametrageDroitsComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParametrageDroits;
