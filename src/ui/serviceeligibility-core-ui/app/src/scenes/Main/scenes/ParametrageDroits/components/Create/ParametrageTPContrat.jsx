/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import ParametrageTPContratComponent from './ParametrageTPContratComponent';
import globalActions from '../../../../../../common/actions';
import actions from './actions';
import parametrageMetierActions from '../../../ParametrageMetier/actions';
import Constants from './Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const initValues = () => ({
    parametrageDroitsCarteTP: {
        detailsDroit: [],
        isCarteEditablePapier: 'false',
        isCarteDematerialisee: 'false',
    },
});
const formSelector = formValueSelector(Constants.FORM_NAME);
/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */
const mapStateToProps = (state, ownProps) => {
    const pathname = ownProps.route.path;
    const isCreate = pathname === '/parameters/create_param_droit';

    return {
        id: ownProps.match.params.id,
        isCopy: !isCreate,
        selectedAmc: formSelector(state, Constants.FIELDS.contratAMC),
        selectedCollectivityID: formSelector(state, Constants.FIELDS.contratCollectivityID),
        selectedPopulationGroup: formSelector(state, Constants.FIELDS.contratPopulationGroup),
        selectedCriterias: formSelector(state, Constants.FIELDS.contratCriteria),
        selectedDroits: formSelector(state, Constants.FIELDS.detailsDroit),
        selectedDateRenouvellement: formSelector(state, Constants.FIELDS.dateRenouvellementCarteTP),
        selectedDebutEcheance: formSelector(state, Constants.FIELDS.debutEcheance),
        selectedDelaiDeclenchementCarteTP: formSelector(state, Constants.FIELDS.delaiDeclenchementCarteTP),
        selectedDateDebutDroitTP: formSelector(state, Constants.FIELDS.dateDebutDroitTP),
        selectedDateExecutionBatch: formSelector(state, Constants.FIELDS.dateExecutionBatch),
        comboLightDeclarants: state.common.common.comboLightDeclarants,
        comboLabelsLightDeclarants: state.common.common.comboLabelsLightDeclarants,
        paramDroitsRefsList: state.main.paramDroitsTP.paramDroitsRefsList,
        initialValues: initValues(),
        comboLabelsAllGT: state.common.common.comboLabelsAllGT,
        comboLabelsAllLot: state.common.common.comboLabelsAllLot,
        isLoadingLots: state.common.common.isLoadingLots,
        selectedLots: formSelector(state, Constants.FIELDS.selectedLots),
        selectedGTs: formSelector(state, Constants.FIELDS.selectedGTs),
        paramPriorites: state.main.paramDroitsTP.paramPriorites,
        paramToDuplicate: state.main.paramDroitsTP.paramToDuplicate,
        loadingParamToDuplicate: state.main.paramDroitsTP.loadingParamToDuplicate,
        returnCodesList: state.main.parametrageMetier.returnCodesList,
        showItelisCode: state.common.configuration.values.showItelisCode,
        domainList: state.main.paramDroitsTP.domainesTP,
        conventions: state.common.common.conventions,
    };
};

const mapDispatchToProps = {
    getLightDeclarants: globalActions.getLightDeclarants,
    getParamForAmc: actions.getParamForAmc,
    createParam: actions.createParam,
    getDomainesTP: actions.getDomainesTP,
    reinitOffers: actions.reinitOffers,
    getLots: globalActions.getLots,
    getAllGT: globalActions.getAllGT,
    getCodesItelis: actions.getCodesItelis,
    getPrioriteByAmc: actions.getPrioriteByAmc,
    getParamById: actions.getOneParam,
    getAllReturnCodes: parametrageMetierActions.getAllReturnCodes,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParametrageTPContrat = connect(mapStateToProps, mapDispatchToProps)(ParametrageTPContratComponent);

export default ParametrageTPContrat;
