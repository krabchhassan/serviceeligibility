/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { combineReducers } from 'redux';
import { reducer as formReducer } from 'redux-form';
import { beyondAppLayoutReducer, ControlledPanelReducer } from '@beyond-framework/common-uitoolkit-beyond';

import main from './scenes/Main/reducers';
import common from './common/reducers';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const reducers = combineReducers({
    form: formReducer,
    common,
    main,
    panels: ControlledPanelReducer,
    desktop: beyondAppLayoutReducer,
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
