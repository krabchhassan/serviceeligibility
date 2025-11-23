/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import FileTrackingSearchFormComponent from './FileTrackingSearchFormComponent';
import actions from '../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapDispatchToProps = {
    fetchingSentFlux: actions.fetchingSentFlux,
    fetchingReceivedFlux: actions.fetchingReceivedFlux,
};

const FileTrackingSearchForm = connect(null, mapDispatchToProps)(FileTrackingSearchFormComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default FileTrackingSearchForm;
