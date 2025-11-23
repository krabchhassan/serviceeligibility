/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { Route } from 'react-router-dom';
import React, { Component, Fragment } from 'react';
import { BreadcrumbRoute, withHabilitation, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import asyncComponentLoading from '../../common/asyncComponentLoading';
import asyncComponentLoadingWithHabilitation from '../../common/asyncLoadingWithHabilitation';
import permissionConstants from './PermissionConstants';
import Constants from '../../common/utils/Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const Home = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/Home/Home'),
    permissionConstants.HOME_PERMISSION,
);

const BobbHome = asyncComponentLoading(() => import('./scenes/Bobb/Home/Home'));

const Declarant = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/Declarant/Declarant'),
    permissionConstants.DECLARANT_PERMISSION,
);

const Tracking = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/Tracking/Tracking'),
    permissionConstants.TRACKING_PERMISSION,
);

const FileTracking = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/FileTracking/FileTracking'),
    permissionConstants.FILE_TRACKING_PERMISSION,
);

const Triggers = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/Triggers/Triggers'),
    permissionConstants.TRIGGER_TRACKING_PERMISSION,
);

const Transcoding = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/Transcoding/Transcoding'),
    permissionConstants.TRANSCODING_PERMISSION,
);

const ParamDroits = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/ParametrageDroits/ParametrageDroits'),
    permissionConstants.BUSINESS_PARAM_PERMISSION,
);

const ParamContrat = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/ParametrageContrat/ParametrageContrat'),
    permissionConstants.BUSINESS_PARAM_PERMISSION,
);

const ParametrageMetier = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/ParametrageMetier/ParametrageMetier'),
    permissionConstants.BUSINESS_PARAM_PERMISSION,
);

const ParametrageTPContrat = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/ParametrageDroits/components/Create/ParametrageTPContrat'),
    permissionConstants.PARAM_CONTRACT_TP_RIGHTS_PERMISSION,
);

const Parametrage = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/Parametrage/Parametrage'),
    permissionConstants.PARAM_PERMISSION,
);

const AlmerysProductReferential = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/AlmerysProductReferential/AlmerysProductReferential'),
    permissionConstants.PARAM_PERMISSION,
);

const Volumetry = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/Volumetry/Volumetry'),
    permissionConstants.VOLUMETRY_PERMISSION,
);

const Beneficiaries = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/Beneficiary/BeneficiariesComponent'),
    permissionConstants.BENEFICIARY_RIGHTS_PERMISSION,
);

const BeneficiaryDetails = asyncComponentLoadingWithHabilitation(
    () => import('./scenes/BeneficiaryDetails/BeneficiaryDetails'),
    permissionConstants.BENEFICIARY_RIGHTS_PERMISSION,
);

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
class Routes extends Component {
    render() {
        return (
            <Fragment>
                <BreadcrumbRoute path="/" component={Home} exact />
                <BreadcrumbRoute path="/create_insurer" component={Declarant} exact />
                <BreadcrumbRoute path="/insurer/:id" component={Declarant} exact />
                <BreadcrumbRoute path="/insurer/:id/:action" component={Declarant} exact />
                <BreadcrumbRoute path="/tracking" component={Tracking} exact />
                <BreadcrumbRoute path="/tracking/file_tracking" component={FileTracking} exact />
                <BreadcrumbRoute path="/tracking/triggers" component={Triggers} exact />
                <BreadcrumbRoute path="/transcoding" component={Transcoding} exact />
                <BreadcrumbRoute path="/volumetry" component={Volumetry} exact />
                <BreadcrumbRoute path="/beneficiary_tracking" component={Beneficiaries} exact />
                <BreadcrumbRoute path="/parameters" component={Parametrage} exact />
                <BreadcrumbRoute path="/parameters/generateTPRigths/:hasCreated" component={ParamDroits} exact />
                <BreadcrumbRoute path="/parameters/generateTPRigths" component={ParamDroits} exact />
                <BreadcrumbRoute path="/parameters/create_param_droit" component={ParametrageTPContrat} exact />
                <BreadcrumbRoute path="/parameters/duplicate_param_droit/:id" component={ParametrageTPContrat} exact />
                <BreadcrumbRoute path="/parameters/contractTPRights" component={ParamContrat} exact />
                <BreadcrumbRoute path="/parameters/business" component={ParametrageMetier} exact />
                <BreadcrumbRoute
                    path="/parameters/almerysProductReferential"
                    component={AlmerysProductReferential}
                    exact
                />
                <BreadcrumbRoute
                    path="/beneficiary_tracking/:env(internal|external)/details/:id/:tab"
                    component={BeneficiaryDetails}
                    exact
                />
                <BreadcrumbRoute
                    path="/beneficiary_tracking/:env(internal|external)/details/:id"
                    component={BeneficiaryDetails}
                    exact
                />
                <BreadcrumbRoute path="/beneficiary/:id" component={BeneficiaryDetails} exact />
                <Route path="/bobb/" component={BobbHome} exact name="BobbHome" />
            </Fragment>
        );
    }
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default TranslationsSubscriber('errors')(
    withHabilitation(Routes, permissionConstants.MICROSERVICE_PERMISSION, Constants.ACCES_DENIED_TRANSLATION_CODE),
);
