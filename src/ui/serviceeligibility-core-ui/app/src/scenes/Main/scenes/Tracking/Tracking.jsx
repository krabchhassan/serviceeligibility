/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
    HabilitationFragment,
    PageLayout,
    Row,
    BodyHeader,
    Panel,
    PanelSection,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import TrackingBreadcrumb from './components/TrackingBreadcrumb';
import MenuButton from '../../../../common/components/MenuButton/MenuButton';
import PermissionConstants from '../../PermissionConstants';
import history from '../../../../history';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility'], { wait: true })
class Tracking extends Component {
    goToFileTracking = () => history.push(`/tracking/file_tracking`);
    goToTriggerTracking = () => history.push(`/tracking/triggers`);
    render() {
        const { t } = this.props;
        return (
            <Fragment>
                <TrackingBreadcrumb />
                <PageLayout header={<BodyHeader title={t('homeTracking.pageTitle')} />}>
                    <Panel border={false}>
                        <PanelSection>
                            <Row className="justify-content-center">
                                <HabilitationFragment allowedPermissions={PermissionConstants.FILE_TRACKING_PERMISSION}>
                                    <MenuButton
                                        id="menu-button-file"
                                        icon="supervision-flux3"
                                        onClick={this.goToFileTracking}
                                        title={t('homeTracking.menuButton.file_tracking.title')}
                                        subtitle={t('homeTracking.menuButton.file_tracking.subtitle')}
                                        xs={12}
                                        sm={6}
                                        md={6}
                                        lg={4}
                                        xl={2}
                                    />
                                </HabilitationFragment>
                                <HabilitationFragment
                                    allowedPermissions={PermissionConstants.TRIGGER_TRACKING_PERMISSION}
                                >
                                    <MenuButton
                                        id="menu-button-trigger"
                                        icon="supervision-declencheurs3"
                                        onClick={this.goToTriggerTracking}
                                        title={t('homeTracking.menuButton.trigger_tracking.title')}
                                        subtitle={t('homeTracking.menuButton.trigger_tracking.subtitle')}
                                        xs={12}
                                        sm={6}
                                        md={6}
                                        lg={4}
                                        xl={2}
                                    />
                                </HabilitationFragment>
                            </Row>
                        </PanelSection>
                    </Panel>
                </PageLayout>
            </Fragment>
        );
    }
}

Tracking.propTypes = {
    t: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Tracking;
