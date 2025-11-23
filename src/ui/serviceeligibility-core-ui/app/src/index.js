/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { AuthProvider, TranslationsProvider, renderWithUserLanguage } from '@beyond-framework/common-uitoolkit-beyond';
import React from 'react';
import ReactDOM from 'react-dom';
import { ConnectedRouter } from 'react-router-redux';
import 'moment/locale/fr';
// eslint-disable-next-line import/extensions,import/no-extraneous-dependencies
import 'bootstrap/dist/js/bootstrap.min.js';
import createHistory from 'history/createHashHistory';
import { Provider } from 'react-redux';
import { Route } from 'react-router-dom';

import store from './store';
import Routes from './Routes';
import configurationService from './services/configuration/configurationService';
import './index.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const render = () => {
    ReactDOM.render(
        <TranslationsProvider>
            <Provider store={store}>
                {/* ConnectedRouter will use the store from Provider automatically */}
                <ConnectedRouter history={createHistory()}>
                    <Route component={Routes} />
                </ConnectedRouter>
            </Provider>
        </TranslationsProvider>,
        document.getElementById('app'),
    );
};

AuthProvider.init((status) => {
    if (!status.error || status.error === 'Keycloak is not enabled') {
        renderWithUserLanguage(render);
    }
});

configurationService.init(store);
