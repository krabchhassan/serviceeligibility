/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    AlertList,
    BeyondAppLayout,
    HabilitationProvider,
    AuthProvider,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import { Route } from 'react-router-dom';
import Routes from './Routes';
import './style.module.scss';
import CommonDataProvider from './CommonDataProvider/CommonDataProvider';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const propTypes = {
    t: PropTypes.func,
    alerts: PropTypes.arrayOf(PropTypes.object),
    removeAlert: PropTypes.func,
    breadcrumb: PropTypes.arrayOf(PropTypes.shape({})),
    i18n: PropTypes.arrayOf(PropTypes.shape({})),
    ignorePermissions: PropTypes.bool,
    notificationsEnabled: PropTypes.bool,
};

const defaultProps = {
    alerts: [],
    notificationsEnabled: false,
};

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['errors'], { wait: true })
class MainComponent extends Component {
    onAlertDismissed(alert) {
        const { removeAlert } = this.props;
        removeAlert(alert);
    }

    getSelectedMenuItemId() {
        const { history } = this.props;
        const { pathname } = history.location;

        const defaultItemId = 'next-serviceeligibility-core';

        if (pathname.includes('/beneficiary_tracking')) {
            return 'next-serviceeligibility-core-benefTracking';
        }
        if (pathname.includes('/volumetry')) {
            return 'next-serviceeligibility-volumetry';
        }
        if (pathname.includes('/parameters')) {
            return 'next-serviceeligibility-parameters';
        }
        if (pathname.includes('/transcoding')) {
            return 'next-serviceeligibility-transcodage';
        }
        if (pathname.includes('/tracking')) {
            return 'next-serviceeligibility-file-tracking';
        }
        if (pathname.includes('/bobb')) {
            return 'next-bobb-core';
        }
        return defaultItemId;
    }

    render() {
        const { alerts, t, breadcrumb, i18n, ignorePermissions, notificationsEnabled } = this.props;
        alerts.map((n) => {
            const notif = n;
            notif.headline = t(notif.headline);
            notif.message = t(notif.message);
            return notif;
        });
        return (
            <HabilitationProvider permissions={AuthProvider.getPermissions()} ignorePermissions={ignorePermissions}>
                <BeyondAppLayout
                    breadcrumb={breadcrumb}
                    selectedMenuItemId={this.getSelectedMenuItemId()}
                    language={i18n.language || i18n.languages[0]}
                    notificationsEnabled={notificationsEnabled}
                >
                    <Route component={Routes} />
                </BeyondAppLayout>
                <CommonDataProvider />
                <AlertList
                    position="top-right"
                    header
                    icon
                    alerts={alerts}
                    onDismiss={(item) => this.onAlertDismissed(item)}
                />
            </HabilitationProvider>
        );
    }
}

MainComponent.propTypes = propTypes;
MainComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default MainComponent;
