/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';

import TranscodingComponent from './TranscodingComponent';
import globalActions from '../../../../common/actions';
import actions from './actions';
import Constants from './Constants';

const formSelector = formValueSelector(Constants.FORM_NAME);

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const mapStateToProps = (state) => ({
    transcodageService: state.main.transcodage.transcodageService,
    transcoMapping: state.main.transcodage.transcoMapping,
    isLoading: state.main.transcodage.isLoading,
    selectedService: formSelector(state, Constants.FIELDS.service),
    selectedTransco: formSelector(state, Constants.FIELDS.transco),
    formCle: formSelector(state, Constants.FIELDS.cle) || '',
    formCodeTransco: formSelector(state, Constants.FIELDS.codeTransco) || '',
});

const mapDispatchToProps = {
    getAllTranscodageService: actions.getAllTranscodageService,
    getMapping: actions.getMapping,
    updateMapping: actions.updateMapping,
    addAlert: globalActions.addAlert,
};

const Transcoding = connect(mapStateToProps, mapDispatchToProps)(TranscodingComponent);

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Transcoding;
