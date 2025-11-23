/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import VolumetryComponent from './VolumetryComponent';
import actions from './actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    volumetrics: state.main.volumetry.volumetrics,
    loadingVolumetrics: state.main.volumetry.loadingVolumetrics,
});

const mapDispatchToProps = {
    getAllVolumetryData: actions.getAllVolumetryData,
};

const Volumetry = connect(mapStateToProps, mapDispatchToProps)(VolumetryComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Volumetry;
