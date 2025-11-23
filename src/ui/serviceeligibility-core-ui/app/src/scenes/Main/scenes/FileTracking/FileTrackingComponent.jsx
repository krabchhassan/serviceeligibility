/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { reduxForm } from 'redux-form';
import {
    PageLayout,
    BodyHeader,
    BreadcrumbPart,
    DesktopBreadcrumbPart,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import CommonSpinner from '../../../../common/components/CommonSpinner/CommonSpinner';
import FileTrackingSearchForm from './components/FileTrackingSearchForm/FileTrackingSearchForm';
import Constants from './Constants';
import ResultSearchFilesTable from './components/ResultSearchFiles/ResultSearchFilesTable';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class FileTrackingComponent extends Component {
    constructor(props) {
        super(props);
        props.getAllProcessus();
        props.getAllTypeFile();
        props.getLightDeclarants();
    }

    render() {
        const {
            t,
            processus,
            loadingProcess,
            loadingTypesFile,
            circuits,
            typesFile,
            lightDeclarants,
            isLoadingLightDeclarants,
            fluxReceived,
            loadingReceivedFlux,
            fluxReceivedPaging,
            totalFluxReceived,
            fluxSent,
            loadingSentFlux,
            fluxSentPaging,
            totalFluxSent,
        } = this.props;
        const canRender =
            !loadingProcess &&
            !loadingTypesFile &&
            !isLoadingLightDeclarants &&
            processus &&
            typesFile &&
            circuits &&
            lightDeclarants;

        if (!canRender) {
            return <CommonSpinner />;
        }

        const pageTitle = t('fileTracking.pageTitle');

        return (
            <PageLayout header={<BodyHeader title={pageTitle} />}>
                <BreadcrumbPart
                    label={t('breadcrumb.fileTracking')}
                    parentPart={<DesktopBreadcrumbPart label={t('breadcrumb.tracking')} path="/tracking" />}
                />
                <FileTrackingSearchForm
                    typesFile={typesFile}
                    circuits={circuits}
                    processus={processus}
                    declarants={lightDeclarants}
                />
                {fluxReceived && (
                    <ResultSearchFilesTable
                        id="fluxReceivedTable"
                        fluxInfo={fluxReceived}
                        paging={fluxReceivedPaging}
                        pageSize={totalFluxReceived}
                        loading={loadingReceivedFlux}
                        processus={processus}
                        isReceivedFiles
                    />
                )}
                {fluxSent && (
                    <ResultSearchFilesTable
                        id="fluxSentTable"
                        fluxInfo={fluxSent}
                        paging={fluxSentPaging}
                        pageSize={totalFluxSent}
                        loading={loadingSentFlux}
                        processus={processus}
                        isReceivedFiles={false}
                    />
                )}
            </PageLayout>
        );
    }
}

FileTrackingComponent.propTypes = {
    t: PropTypes.func,
    getAllProcessus: PropTypes.func,
    getAllTypeFile: PropTypes.func,
    getLightDeclarants: PropTypes.func,
    processus: PropTypes.arrayOf(PropTypes.shape()),
    circuits: PropTypes.arrayOf(PropTypes.shape()),
    typesFile: PropTypes.arrayOf(PropTypes.shape()),
    lightDeclarants: PropTypes.arrayOf(PropTypes.shape()),
    fluxReceived: PropTypes.arrayOf(PropTypes.shape()),
    fluxSent: PropTypes.arrayOf(PropTypes.shape()),
    loadingProcess: PropTypes.bool,
    loadingTypesFile: PropTypes.bool,
    isLoadingLightDeclarants: PropTypes.bool,
    loadingReceivedFlux: PropTypes.bool,
    loadingSentFlux: PropTypes.bool,
    fluxReceivedPaging: PropTypes.number,
    totalFluxReceived: PropTypes.number,
    fluxSentPaging: PropTypes.number,
    totalFluxSent: PropTypes.number,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: Constants.FORM_NAME,
})(FileTrackingComponent);
