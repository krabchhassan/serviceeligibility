/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import VolumetryResultSearchComponent from './VolumetryResultSearchComponent';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    searchCriteria: state.main.volumetry.searchCriteria,
    volumetrics: state.main.volumetry.volumetrics,
});

const VolumetryResultSearch = connect(mapStateToProps, null)(VolumetryResultSearchComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default VolumetryResultSearch;
