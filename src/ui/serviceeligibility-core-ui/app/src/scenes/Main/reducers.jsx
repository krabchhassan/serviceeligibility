/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { combineReducers } from 'redux';
import home from './scenes/Home/reducers';
import declarant from './scenes/Declarant/reducer';
import beneficiaryDetails from './scenes/BeneficiaryDetails/reducer';
import beneficiaryRightsDetails from './scenes/BeneficiaryRightsDetails/components/reducer';
import beneficiary from './scenes/Beneficiary/reducer';
import fileTracking from './scenes/FileTracking/reducer';
import transcodage from './scenes/Transcoding/reducer';
import parametrageMetier from './scenes/ParametrageMetier/reducer';
import volumetry from './scenes/Volumetry/reducer';
import paramDroits from './scenes/ParametrageDroits/reducer';
import paramDroitsTP from './scenes/ParametrageDroits/components/Create/reducer';
import paramContrat from './scenes/ParametrageContrat/reducer';
import triggers from './scenes/Triggers/reducer';
import HomeStore from './scenes/Bobb/Home/reducer';
import almerysProduct from './scenes/AlmerysProductReferential/reducer';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const reducers = combineReducers({
    home,
    triggers,
    declarant,
    beneficiaryDetails,
    beneficiaryRightsDetails,
    beneficiary,
    fileTracking,
    transcodage,
    parametrageMetier,
    volumetry,
    paramDroits,
    paramDroitsTP,
    paramContrat,
    HomeStore,
    almerysProduct,
});

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reducers;
