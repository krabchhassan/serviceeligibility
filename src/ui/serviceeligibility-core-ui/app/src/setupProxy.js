const proxy = require('http-proxy-middleware');

module.exports = function (app) {
    const targetEnv = process.env.TARGET_ENV || 'otp-es15';
    const targetCluster = process.env.TARGET_CLUSTER || 'dev';
    const keyCloackRealm = process.env.keycloakRealm || 'htp';
    const keycloakEnabled = process.env.keycloakEnabled ? process.env.keycloakEnabled === 'true' : true;
    const authorizationEnabled = process.env.authorizationEnabled ? process.env.authorizationEnabled === 'true' : true;
    const clientType = process.env.CLIENT_TYPE || 'INSURER';
    const useBeyondApi = process.env.USE_BEYOND_API || 'true';
    const beyondApiUrl =
        process.env.BEYOND_API_URL || '/beyond/internal/api/bdds/v1/ui';
    const showItelisCode = process.env.SHOW_ITELIS_CODE ? process.env.SHOW_ITELIS_CODE === 'true' : false;
    const feature = process.env.FEATURE ? process.env.FEATURE : 'TP_ALMERYS';
    const editorInstance = process.env.EDITOR_INSTANCE;
    const notificationsEnabled = process.env.NOTIFICATIONS_ENABLED ? process.env.NOTIFICATIONS_ENABLED === 'true' : false;
    const target = `https://${targetEnv}.${targetCluster}.beyond.cegedim.cloud`;

    const proxies = {};
    proxies[target] = [
        '/referential/core/api', // for global referential
        '/claim/search/api', // for search claims
        '/claim/core/api', // for viewing claim details
        '/oc/core/api', // for viewing claim details
        '/claim/orchestrator/api', // for viewing bill details
        '/multioc/core/api',
        '/serviceprovider/core/api',
        '/referential/beneficiary/api',
        '/authorization/core/api',
        beyondApiUrl,
    ];

    proxies['http://localhost:8080'] = [
        // mettre le serviceeligibility ici
        '/serviceeligibility/core/api',
    ];

    Object.keys(proxies).forEach((key) => {
        proxies[key].forEach((calledUrl) => {
            app.use(calledUrl, proxy({ target: key, changeOrigin: true }));
        });
    });

    app.use('/configuration', (req, res) =>
        res.json({
            clientId: 'ui-es15',
            url: `https://idp.${targetCluster}.beyond.cegedim.cloud/auth`,
            realm: keyCloackRealm,
            enabled: keycloakEnabled,
            allowExport: true,
            allowImport: true,
            authorizationEnabled: authorizationEnabled,
            clientType,
            //useBeyondApi,
            //beyondApiUrl,
            showItelisCode,
            editorInstance: editorInstance,
            notificationsEnabled: notificationsEnabled,
            feature,
        }),
    );
};
