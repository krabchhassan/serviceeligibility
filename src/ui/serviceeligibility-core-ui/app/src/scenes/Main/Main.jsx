/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import MainComponent from './MainComponent';
import actions from '../../common/actions';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

const mapStateToProps = (state) => ({
    alerts: state.common.notifications,
    breadcrumb: state.common.breadcrumb,
    ignorePermissions:
        !state.common.configuration.values.enabled || !state.common.configuration.values.authorizationEnabled,
    notificationsEnabled:
        state.common.configuration.values.notificationsEnabled === 'true' ||
        state.common.configuration.values.notificationsEnabled === true,
});

const mapDispatchToProps = (dispatch) =>
    bindActionCreators(
        {
            removeAlert: actions.removeAlert,
        },
        dispatch,
    );

const Main = connect(mapStateToProps, mapDispatchToProps)(MainComponent);

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Main;
