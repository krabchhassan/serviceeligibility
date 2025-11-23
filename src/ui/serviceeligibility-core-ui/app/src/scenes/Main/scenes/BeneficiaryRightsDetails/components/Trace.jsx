/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import PropTypes from 'prop-types';
import {
    PanelHeader,
    Panel,
    PanelSection,
    Row,
    Col,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import autobind from 'autobind-decorator';
import LabelValuePresenter from '../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import CommonSpinner from '../../../../../common/components/CommonSpinner/CommonSpinner';
import TraceServiceExtraction from './components/TraceServiceExtraction';
import TraceServiceSansBatch from './components/TraceServiceSansBatch';
import TraceServiceTraitement from './components/TraceServiceTraitement';
import TraceServiceConsolidation from './components/TraceServiceConsolidation';
import Constants from '../Constants';
import DateUtils from '../../../../../common/utils/DateUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const getTraceComponentByService = (service, index) => {
    const { typeService } = service;
    switch (typeService) {
        case Constants.typeService.extraction:
            return <TraceServiceExtraction key={index} service={service} isAlsoConso={false} />;
        case Constants.typeService.sansBatch:
            return <TraceServiceSansBatch key={index} service={service} />;
        case Constants.typeService.traitement:
            return <TraceServiceTraitement key={index} service={service} />;
        case Constants.typeService.consolidation:
            return <TraceServiceConsolidation key={index} service={service} />;
        case Constants.typeService.consolidationExtraction:
            return <TraceServiceExtraction key={index} service={service} isAlsoConso />;
        default:
            return null;
    }
};
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class Trace extends Component {
    constructor(props) {
        super(props);

        this.state = {
            isOpen: !!props.open,
        };
    }

    @autobind
    toggleCollapse() {
        const { isOpen } = this.state;
        this.setState({ isOpen: !isOpen });
    }

    buildTraceHeader() {
        const { t, trace } = this.props;
        const { motifDeclaration, typeMouvement } = trace;

        const reason = `${t('beneficiaryRightsDetails.trace.motif')} ${motifDeclaration}`;
        const movment = `${t('beneficiaryRightsDetails.trace.movement')} ${
            typeMouvement ? t(`beneficiaryRightsDetails.trace.typeMvt${typeMouvement}`) : ''
        }`;

        const context = `${reason} / ${movment}`;
        return (
            <PanelHeader
                title={t('beneficiaryRightsDetails.trace.title')}
                context={motifDeclaration ? context : ''}
                titleClassname="col-md-6"
                contextClassname="col-md-6"
            />
        );
    }

    render() {
        const { isOpen } = this.state;
        const { t, trace, circuits } = this.props;
        const canRender = trace && circuits;
        if (!canRender) {
            return <CommonSpinner />;
        }

        const {
            dateTraitement,
            codeEtat,
            codeCircuit,
            backOfficeEmetteur,
            codePartenaire,
            operateurPrincipal,
            versionTDB,
            nomFichier,
            tracesServices,
            motifDeclaration,
        } = trace;

        const pageTitle = t('beneficiaryRightsDetails.trace.title');
        const selectedCircuit = circuits.find((circuit) => circuit.codeCircuit === codeCircuit) || {};

        return (
            <Panel
                id="trace-panel"
                header={this.buildTraceHeader()}
                label={pageTitle}
                expanded={isOpen}
                onCollapseClick={this.toggleCollapse}
            >
                {motifDeclaration && (
                    <Fragment>
                        <PanelSection title={t('beneficiaryRightsDetails.trace.declarationReception')}>
                            <Row>
                                <Col>
                                    <LabelValuePresenter
                                        id="traceability-date"
                                        label={t(`beneficiaryRightsDetails.trace.tracingDate`)}
                                        value={DateUtils.formatDisplayDateTimeWithTimeZone(dateTraitement)}
                                    />
                                    <LabelValuePresenter
                                        id="traceability-declaration-partner"
                                        label={t(`beneficiaryRightsDetails.trace.partner`)}
                                        value={codePartenaire}
                                    />
                                    <LabelValuePresenter
                                        id="traceability-declaration-physical-file"
                                        label={t(`beneficiaryRightsDetails.trace.fileName`)}
                                        value={
                                            nomFichier && nomFichier.length > 35
                                                ? `${nomFichier.substring(0, 35)}...`
                                                : nomFichier
                                        }
                                        tooltip={nomFichier}
                                    />
                                </Col>
                                <Col>
                                    <LabelValuePresenter
                                        id="traceability-declaration-status"
                                        label={t(`beneficiaryRightsDetails.trace.declarationState`)}
                                        value={t(`beneficiaryRightsDetails.trace.etatDeclaration${codeEtat}`)}
                                    />
                                    <LabelValuePresenter
                                        id="traceability-declaration-operator"
                                        label={t(`beneficiaryRightsDetails.trace.mainOperator`)}
                                        value={operateurPrincipal}
                                    />
                                </Col>
                                <Col>
                                    <LabelValuePresenter
                                        id="traceability-declaration-rights-emitter"
                                        label={t(`beneficiaryRightsDetails.trace.rightsEmitter`)}
                                        value={selectedCircuit.emetteur}
                                    />
                                    <LabelValuePresenter
                                        id="traceability-declaration-operator"
                                        label={t(`beneficiaryRightsDetails.trace.circuitDroits`)}
                                        value={selectedCircuit.libelleCircuit}
                                    />
                                </Col>
                                <Col>
                                    <LabelValuePresenter
                                        id="traceability-declaration-back-office-rights-emitter"
                                        label={t(`beneficiaryRightsDetails.trace.rightsEmitterBO`)}
                                        value={backOfficeEmetteur}
                                    />
                                    <LabelValuePresenter
                                        id="traceability-declaration-operator"
                                        label={t(`beneficiaryRightsDetails.trace.versionTDB`)}
                                        value={versionTDB}
                                    />
                                </Col>
                            </Row>
                        </PanelSection>
                        {tracesServices && tracesServices.length > 0 && (
                            <PanelSection title={t('beneficiaryRightsDetails.trace.utilisationServices')}>
                                {tracesServices.map((service, index) => getTraceComponentByService(service, index))}
                            </PanelSection>
                        )}
                    </Fragment>
                )}
            </Panel>
        );
    }
}

Trace.propTypes = {
    t: PropTypes.func,
    open: PropTypes.bool,
    trace: PropTypes.shape(),
    circuits: PropTypes.arrayOf(PropTypes.shape),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Trace;
