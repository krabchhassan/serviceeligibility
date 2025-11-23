/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { combineReducers } from 'redux';
import { routerReducer } from 'react-router-redux';
import { AlertReducer, BreadcrumbReducer } from '@beyond-framework/common-uitoolkit-beyond';
import common from './reducers/CommonReducer';
import configuration from '../services/configuration/configurationReducer';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const reducers = combineReducers({
    notifications: AlertReducer,
    router: routerReducer,
    breadcrumb: BreadcrumbReducer,
    common,
    configuration,
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
