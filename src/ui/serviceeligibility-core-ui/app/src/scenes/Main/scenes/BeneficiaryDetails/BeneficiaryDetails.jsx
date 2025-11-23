/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import BeneficiaryDetailsComponent from './BeneficiaryDetailsComponent';
import globalActions from '../../../../common/actions';
import { actions as benefActions } from './actions';
import actions from '../ParametrageMetier/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapStateToProps = (state, ownProps) => ({
    id: ownProps.match.params.id,
    env: ownProps.match.params.env,
    tab: ownProps.match.params.tab,
    declarationId: state.main.beneficiaryDetails.declarationId,
    loadingDecl: state.main.beneficiaryDetails.loading,
    benefDetails: state.main.beneficiaryDetails.benefDetails,
    sasContractList: state.main.beneficiaryDetails.sasContractList,
    loadingPrestation: state.main.beneficiaryDetails.loadingPrestation,
    servicePrestations: state.main.beneficiaryDetails.servicePrestations,
    servicePrestIJs: state.main.beneficiaryDetails.servicePrestIJs,
    loadingServicePrestIJ: state.main.beneficiaryDetails.loadingServicePrestIJ,
    serviceMetierList: state.main.parametrageMetier.serviceMetierList,
    amcName: state.main.beneficiaryDetails.amcName,
    numerosAMCEchanges: state.main.beneficiaryDetails.numerosAMCEchanges,
    loadingDeclarant: state.main.beneficiaryDetails.loadingDeclarant,
    loadingBenef: state.main.beneficiaryDetails.loadingBenef,
    consoFinished: state.main.beneficiaryDetails.consoFinished,
    clientType: state.common.configuration.values.clientType,
});

const mapDispatchToProps = {
    addAlertError: globalActions.addAlertError,
    getPrestationService: benefActions.getPrestationService,
    getServicePrestIJs: benefActions.getServicePrestIJs,
    saveBenefHistorique: benefActions.saveBenefHistorique,
    getOneDeclarant: benefActions.getOneDeclarant,
    getAllServiceMetier: actions.getAllServiceMetier,
    getBeneficiaryTpDetails: benefActions.getBeneficiaryTpDetails,
    getSasContrats: benefActions.getSasContrats,
    getOneBenef: benefActions.getOneBenef,
    clearBeneficiaryDetails: benefActions.clearBeneficiaryDetails,
};

const BeneficiaryDetails = connect(mapStateToProps, mapDispatchToProps)(BeneficiaryDetailsComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default BeneficiaryDetails;
