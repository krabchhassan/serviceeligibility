import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
    Panel,
    PanelHeader,
    PanelSection,
    Row,
    Col,
    CardDeck,
    Modal,
    ModalHeader,
    ModalBody,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import DateUtils from '../../../../../common/utils/DateUtils';
import HistoriqueDestinatairesPaiement from './HistoriqueDestinatairesPaiement';
import HistoriqueDestinatairesPrestation from './HistoriqueDestinatairesPrestation';
import DestinatairePaiement from './DestinatairePaiement';
import DestinatairePrestation from './DestinatairePrestation';

@TranslationsSubscriber(['eligibility', 'common'], { wait: true })
class PrestationServiceDestinataires extends Component {
    constructor(props) {
        super(props);

        this.state = {
            expanded: false,
            modalPaiements: false,
            modalPrestations: false,
            historiquePaiements: null,
            historiquePrestations: null,
        };
    }

    onCollapseClick() {
        const { expanded } = this.state;
        this.setState({ expanded: !expanded });
    }

    togglePaiements(historiquePaiements) {
        const { modalPaiements } = this.state;
        this.setState({ modalPaiements: !modalPaiements, historiquePaiements });
    }

    togglePrestations(historiquePrestations) {
        const { modalPrestations } = this.state;
        this.setState({ modalPrestations: !modalPrestations, historiquePrestations });
    }

    renderDestinatairesPaiements(firstDestinatairesPaiements) {
        const { t, destinatairesPaiements } = this.props;
        const destinatairesPaiementsPeriode = (firstDestinatairesPaiements || {}).periode;

        return (
            <Panel
                header={
                    <PanelHeader
                        title={t('beneficiaryDetails.servicePrestation.destPaiement')}
                        status={
                            <span>
                                <span className="cgd-comment">
                                    {' '}
                                    {t('beneficiaryDetails.servicePrestation.periodeGarantieDebut')}{' '}
                                    {DateUtils.transformDateForDisplay(destinatairesPaiementsPeriode.debut)}{' '}
                                </span>
                                <span className="cgd-comment">
                                    {t('beneficiaryDetails.servicePrestation.periodeGarantieFin')}{' '}
                                    {DateUtils.transformDateForDisplay(destinatairesPaiementsPeriode.fin)}
                                </span>
                            </span>
                        }
                        actions={[
                            {
                                id: 'showHistoryPaiements',
                                icon: 'open-modal',
                                description: t('beneficiaryDetails.servicePrestation.destPaiementHistory'),
                                action: () => this.togglePaiements(destinatairesPaiements),
                            },
                        ]}
                    />
                }
            >
                <DestinatairePaiement firstDestinatairesPaiements={firstDestinatairesPaiements} />
            </Panel>
        );
    }

    renderDestinatairesRelevePrestations(firstDestinatairesRelevePrestations) {
        const { t, destinatairesRelevePrestations } = this.props;
        const destinatairesRelevePrestationsPeriode = (firstDestinatairesRelevePrestations || {}).periode;

        return (
            <Panel
                header={
                    <PanelHeader
                        title={t('beneficiaryDetails.servicePrestation.destRelevePrestation')}
                        status={
                            <span>
                                <span className="cgd-comment">
                                    {' '}
                                    {t('beneficiaryDetails.servicePrestation.periodeGarantieDebut')}{' '}
                                    {DateUtils.transformDateForDisplay(destinatairesRelevePrestationsPeriode.debut)}{' '}
                                </span>
                                <span className="cgd-comment">
                                    {t('beneficiaryDetails.servicePrestation.periodeGarantieFin')}{' '}
                                    {DateUtils.transformDateForDisplay(destinatairesRelevePrestationsPeriode.fin)}
                                </span>
                            </span>
                        }
                        actions={[
                            {
                                id: 'showHistoryPrestation',
                                icon: 'open-modal',
                                description: t('beneficiaryDetails.servicePrestation.destPrestationHistory'),
                                action: () => this.togglePrestations(destinatairesRelevePrestations),
                            },
                        ]}
                    />
                }
            >
                <DestinatairePrestation firstDestinatairesRelevePrestations={firstDestinatairesRelevePrestations} />
            </Panel>
        );
    }

    render() {
        const { t, destinatairesPaiements, destinatairesRelevePrestations } = this.props;
        const firstDestinatairesPaiements =
            destinatairesPaiements && destinatairesPaiements[0] ? destinatairesPaiements[0] : null;
        const firstDestinatairesRelevePrestations =
            destinatairesRelevePrestations && destinatairesRelevePrestations[0]
                ? destinatairesRelevePrestations[0]
                : null;
        const { expanded, modalPaiements, modalPrestations, historiquePaiements, historiquePrestations } = this.state;
        return (
            <Panel
                onCollapseClick={() => this.onCollapseClick()}
                expanded={expanded}
                header={<PanelHeader title={t('beneficiaryDetails.servicePrestation.destinatairesTitle')} />}
            >
                <PanelSection>
                    <Row>
                        <Col xs="12" md="6">
                            {firstDestinatairesPaiements &&
                                this.renderDestinatairesPaiements(firstDestinatairesPaiements)}
                        </Col>
                        <Col xs="12" md="6">
                            {firstDestinatairesRelevePrestations &&
                                this.renderDestinatairesRelevePrestations(firstDestinatairesRelevePrestations)}
                        </Col>
                    </Row>
                    {historiquePaiements && (
                        <Modal
                            size="full-width"
                            isOpen={modalPaiements}
                            toggle={() => this.togglePaiements()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.togglePaiements()}>
                                {t('historiquePaiements.header')}
                            </ModalHeader>
                            <ModalBody>
                                <CardDeck>
                                    <HistoriqueDestinatairesPaiement historique={historiquePaiements} />
                                </CardDeck>
                            </ModalBody>
                        </Modal>
                    )}
                    {historiquePrestations && (
                        <Modal
                            size="full-width"
                            isOpen={modalPrestations}
                            toggle={() => this.togglePrestations()}
                            backdrop="static"
                        >
                            <ModalHeader toggle={() => this.togglePrestations()}>
                                {t('historiquePrestations.header')}
                            </ModalHeader>
                            <ModalBody>
                                <CardDeck>
                                    <HistoriqueDestinatairesPrestation historique={historiquePrestations} />
                                </CardDeck>
                            </ModalBody>
                        </Modal>
                    )}
                </PanelSection>
            </Panel>
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
};
// Default props
const defaultProps = {};

PrestationServiceDestinataires.propTypes = propTypes;
// Add default props
PrestationServiceDestinataires.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default PrestationServiceDestinataires;
