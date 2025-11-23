/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    PageLayout,
    BreadcrumbPart,
    ControlledPanel,
    ControlledPanelBodyHeader,
    PanelHeader,
    PanelSection,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import ParamObjetTransco from './components/ParamObjetTransco/ParamObjetTransco';
import ParamServiceTransco from './components/ParamServiceTransco/ParamServiceTransco';
import ParamAgreement from './components/ParamAgreement/ParamAgreement';
import ParamServiceMetier from './components/ParamServiceMetier/ParamServiceMetier';
import ParamProcess from './components/ParamProcess/ParamProcess';
import ParamFilesType from './components/ParamFilesType/ParamFilesType';
import ParamRejects from './components/ParamRejects/ParamRejects';
import ParamClaim from './components/ParamClaim/ParamClaim';
import ParamDomain from './components/ParamDomain/ParamDomain';
import ParamDomainIS from './components/ParamDomainIS/ParamDomainIS';
import ParamDomainSP from './components/ParamDomainSP/ParamDomainSP';
import ParamFormula from './components/ParamFormula/ParamFormula';
import ParamCodesRenvoi from './components/ParamCodesRenvoi/ParamCodesRenvoi';
import ParametrageBreadcrumb from '../Parametrage/components/ParametrageBreadcrumb';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
@TranslationsSubscriber(['breadcrumb', 'eligibility', 'common'], { wait: true })
class ParametrageMetierComponent extends Component {
    render() {
        const { t } = this.props;

        const renderObjetTransco = () => <ParamObjetTransco />;
        const renderService = () => <ParamServiceTransco />;
        const renderAgreement = () => <ParamAgreement />;
        const renderServiceMetier = () => <ParamServiceMetier />;
        const renderProcess = () => <ParamProcess />;
        const renderFilesType = () => <ParamFilesType />;
        const renderRejects = () => <ParamRejects />;
        const renderClaim = () => <ParamClaim />;
        const renderDomain = () => <ParamDomain />;
        const renderDomainIS = () => <ParamDomainIS />;
        const renderDomainSP = () => <ParamDomainSP />;
        const renderFormula = () => <ParamFormula />;
        const renderCodesRenvoi = () => <ParamCodesRenvoi />;
        const panelHeaderTranscodingObject = <PanelHeader title={t('parameters.panelTranscodingObject')} />;
        const panelHeaderTranscodingService = <PanelHeader title={t('parameters.panelTranscodingService')} />;
        const panelHeaderAgreement = <PanelHeader title={t('parameters.panelAgreement')} />;
        const panelHeaderServiceMetier = <PanelHeader title={t('parameters.panelServiceMetier')} />;
        const panelHeaderProcess = <PanelHeader title={t('parameters.panelProcess')} />;
        const panelHeaderFilesType = <PanelHeader title={t('parameters.panelFilesType')} />;
        const panelHeaderRejects = <PanelHeader title={t('parameters.panelRejects')} />;
        const panelHeaderClaim = <PanelHeader title={t('parameters.panelClaim')} />;
        const panelHeaderDomain = <PanelHeader title={t('parameters.panelDomain')} />;
        const panelHeaderDomainIS = <PanelHeader title={t('parameters.panelDomainIS')} />;
        const panelHeaderDomainSP = <PanelHeader title={t('parameters.panelDomainSP')} />;
        const panelHeaderFormula = <PanelHeader title={t('parameters.panelFormula')} />;
        const panelHeaderCodesRenvoi = <PanelHeader title={t('parameters.panelCodesRenvoi')} />;

        return (
            <PageLayout header={<ControlledPanelBodyHeader title={t('parameters.pageTitle')} />}>
                <BreadcrumbPart label={t('breadcrumb.businessParameters')} parentPart={<ParametrageBreadcrumb />} />

                <ControlledPanel
                    id="panel-parameters-agreement"
                    label={t('parameters.panelAgreement')}
                    header={panelHeaderAgreement}
                    initialExpanded
                >
                    <PanelSection>{renderAgreement()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-parameters-domain"
                    label={t('parameters.panelDomain')}
                    header={panelHeaderDomain}
                >
                    <PanelSection>{renderDomain()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-parameters-domainIS"
                    label={t('parameters.panelDomainIS')}
                    header={panelHeaderDomainIS}
                >
                    <PanelSection>{renderDomainIS()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-parameters-domainSP"
                    label={t('parameters.panelDomainSP')}
                    header={panelHeaderDomainSP}
                >
                    <PanelSection>{renderDomainSP()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-parameters-formula"
                    label={t('parameters.panelFormula')}
                    header={panelHeaderFormula}
                >
                    <PanelSection>{renderFormula()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-transcoding-objects"
                    label={t('parameters.panelTranscodingObject')}
                    header={panelHeaderTranscodingObject}
                >
                    <PanelSection>{renderObjetTransco()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-parameters-claim"
                    label={t('parameters.panelClaim')}
                    header={panelHeaderClaim}
                >
                    <PanelSection>{renderClaim()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-parameters-process"
                    label={t('parameters.panelProcess')}
                    header={panelHeaderProcess}
                >
                    <PanelSection>{renderProcess()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-parameters-rejects"
                    label={t('parameters.panelRejects')}
                    header={panelHeaderRejects}
                >
                    <PanelSection>{renderRejects()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-transcoding-services"
                    label={t('parameters.panelTranscodingService')}
                    header={panelHeaderTranscodingService}
                >
                    <PanelSection>{renderService()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-parameters-filestype"
                    label={t('parameters.panelFilesType')}
                    header={panelHeaderFilesType}
                >
                    <PanelSection>{renderFilesType()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-parameters-serviceMetier"
                    label={t('parameters.panelServiceMetier')}
                    header={panelHeaderServiceMetier}
                >
                    <PanelSection>{renderServiceMetier()}</PanelSection>
                </ControlledPanel>
                <ControlledPanel
                    id="panel-parameters-codesRenvoi"
                    label={t('parameters.panelCodesRenvoi')}
                    header={panelHeaderCodesRenvoi}
                >
                    <PanelSection>{renderCodesRenvoi()}</PanelSection>
                </ControlledPanel>
            </PageLayout>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

// Add prop types
ParametrageMetierComponent.propTypes = propTypes;
// Add default props
ParametrageMetierComponent.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParametrageMetierComponent;
