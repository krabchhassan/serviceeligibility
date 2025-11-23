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
    FeatureToggling,
    Panel,
    PanelSection,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import ParametrageBreadcrumb from './components/ParametrageBreadcrumb';
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
class ParametrageComponent extends Component {
    goToBusinessParameters = () => history.push(`/parameters/business`);
    goToGenerateTPRigths = () => history.push(`/parameters/generateTPRigths`);
    goToContractTPRights = () => history.push(`/parameters/contractTPRights`);
    goToAlmerysProductReferential = () => history.push(`/parameters/almerysProductReferential`);
    render() {
        const { t, feature } = this.props;
        const isAlmerysActivated = FeatureToggling.isActivated(feature);

        return (
            <Fragment>
                <ParametrageBreadcrumb />
                <PageLayout header={<BodyHeader title={t('homeParameters.pageTitle')} />}>
                    <Panel border={false}>
                        <PanelSection>
                            <Row className="justify-content-center">
                                <HabilitationFragment
                                    allowedPermissions={PermissionConstants.BUSINESS_PARAM_PERMISSION}
                                >
                                    <MenuButton
                                        id="menu-button-benef"
                                        icon="business-settings"
                                        onClick={this.goToBusinessParameters}
                                        title={t('homeParameters.menuButton.businessParameters.title')}
                                        xs={12}
                                        sm={6}
                                        md={6}
                                        lg={4}
                                        xl={2}
                                    />
                                </HabilitationFragment>
                                <HabilitationFragment
                                    allowedPermissions={PermissionConstants.PARAM_GENERATE_TP_RIGHTS_PERMISSION}
                                >
                                    <MenuButton
                                        id="menu-button-contract"
                                        icon="generation-droits-contrat"
                                        onClick={this.goToGenerateTPRigths}
                                        title={t('homeParameters.menuButton.generateTPParamaters.title')}
                                        xs={12}
                                        sm={6}
                                        md={6}
                                        lg={4}
                                        xl={2}
                                    />
                                </HabilitationFragment>
                                <HabilitationFragment
                                    allowedPermissions={PermissionConstants.PARAM_CONTRACT_TP_RIGHTS_PERMISSION}
                                >
                                    <MenuButton
                                        id="menu-button-params"
                                        icon="generation-droits"
                                        onClick={this.goToContractTPRights}
                                        title={t('homeParameters.menuButton.generateTPContract.title')}
                                        xs={12}
                                        sm={6}
                                        md={6}
                                        lg={4}
                                        xl={2}
                                    />
                                </HabilitationFragment>
                                {isAlmerysActivated && (
                                    <HabilitationFragment allowedPermissions={PermissionConstants.READ_LOT_PERMISSION}>
                                        <MenuButton
                                            id="menu-button-almerys"
                                            icon="almerys"
                                            onClick={this.goToAlmerysProductReferential}
                                            title={t('homeParameters.menuButton.almerysProductReferential.title')}
                                            iconSize="3x"
                                            xs={12}
                                            sm={6}
                                            md={6}
                                            lg={4}
                                            xl={2}
                                        />
                                    </HabilitationFragment>
                                )}
                            </Row>
                        </PanelSection>
                    </Panel>
                </PageLayout>
            </Fragment>
        );
    }
}

ParametrageComponent.propTypes = {
    t: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParametrageComponent;
