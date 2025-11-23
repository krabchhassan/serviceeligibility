/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import 'whatwg-fetch';
import { AuthProvider, i18n } from '@beyond-framework/common-uitoolkit-beyond';

const withAuthentication = (Api) => {
    // Send JWT to API for each calls
    Api.addRequestInterceptor((config) => {
        config.headers['Content-Type'] = 'application/json';
        config.headers.Accept = 'application/json';
        // If not undefined, so, auth is enabled
        if (AuthProvider.get()) {
            config.headers.Authorization = `Bearer ${AuthProvider.get().token}`;
            config.headers['X-CGD-TENANT'] = AuthProvider.get().realm;
        }

        return config;
    });

    return Api;
};

const getLanguage = () => i18n.language || window.localStorage.i18nextLng;

const withLanguageHeader = (Api) => {
    Api.addRequestInterceptor((config) => {
        config.headers['Accept-Language'] = getLanguage();
        return config;
    });
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ApiUtils = {
    withLanguageHeader,
    withAuthentication,
};

export default ApiUtils;
