/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import ImportLotPanelComponent from './ImportLotPanelComponent';
import commonActions from '../../../../../../../../common/actions';
import actions from '../../../actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const Home = connect(mapStateToProps, mapDispatchToProps)(ImportLotPanelComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */

/* ************************************* */
function mapStateToProps(state) {
    return {
        isImportLotPending: state.main.HomeStore.isImportLotPending,
    };
}

function mapDispatchToProps(dispatch) {
    return {
        importLotMappingFile: (file) => dispatch(actions.importLotMappingFile(file)),
        displayAlert: (message, behavior) => dispatch(commonActions.displayAlert(message, behavior)),
    };
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Home;
