/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import CommonDataProviderComponent from './CommonDataProviderComponent';
import globalActions from '../../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const mapDispatchToProps = {
    getAllServices: globalActions.getAllServices,
    getAllCircuits: globalActions.getAllCircuits,
    getAllConventions: globalActions.getAllConventions,
    getAllServiceMetier: globalActions.getAllServiceMetier,
};

const CommonDataProvider = connect(null, mapDispatchToProps)(CommonDataProviderComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default CommonDataProvider;
