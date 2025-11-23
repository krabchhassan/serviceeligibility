/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import FileTrackingComponent from './FileTrackingComponent';
import actions from './actions';
import globalActions from '../../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    // searching form data
    processus: state.main.fileTracking.processus,
    loadingProcess: state.main.fileTracking.loadingProcess,
    typesFile: state.main.fileTracking.typesFile,
    loadingTypesFile: state.main.fileTracking.loadingTypesFile,
    circuits: state.common.common.circuits,
    lightDeclarants: state.common.common.lightDeclarants,
    isLoadingLightDeclarants: state.common.common.isLoadingLightDeclarants,

    // received files
    fluxReceived: state.main.fileTracking.fluxReceived,
    fluxReceivedPaging: state.main.fileTracking.fluxReceivedPaging,
    totalFluxReceived: state.main.fileTracking.totalFluxReceived,
    loadingReceivedFlux: state.main.fileTracking.loadingReceivedFlux,

    // sent files
    fluxSent: state.main.fileTracking.fluxSent,
    fluxSentPaging: state.main.fileTracking.fluxSentPaging,
    totalFluxSent: state.main.fileTracking.totalFluxSent,
    loadingSentFlux: state.main.fileTracking.loadingSentFlux,
});

const mapDispatchToProps = {
    getAllProcessus: actions.getAllProcessus,
    getAllTypeFile: actions.getAllTypeFile,
    getLightDeclarants: globalActions.getLightDeclarants,
};

const FileTracking = connect(mapStateToProps, mapDispatchToProps)(FileTrackingComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default FileTracking;
