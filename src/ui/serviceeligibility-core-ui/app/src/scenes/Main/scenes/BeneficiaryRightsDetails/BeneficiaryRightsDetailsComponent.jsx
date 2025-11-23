/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import autobind from 'autobind-decorator';
import { ConfirmationModal, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import Contract from './components/components/Contract';
import HistoriqueConsoContract from './components/HistoriqueConsoContract';
import Rights from './components/Rights';
import OtherBeneficiaries from './components/components/OtherBeneficiaries';
import PermissionConstants from '../../PermissionConstants';
import HistoriqueDeclarations from './components/components/HistoriqueDeclarations';
import AttestationsContract from './components/components/AttestationsContract';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class BeneficiaryRightsDetailsComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            isFirstLoad: true,
            selectedTab: props.selectedMenuTab,
            idDeclarant: null,
            numeroContrat: null,
            numeroAdherent: null,
            isHTP: props.clientType === 'INSURER' || props.environment === 'external',
        };
    }

    componentDidMount() {
        const { contractToOpen, declarationDetails, attestations } = this.props;
        const { isFirstLoad } = this.state;

        // Si c'est le premier chargement et qu'il n'y a pas de contrat consolidé mais des attestations ou déclarations
        if (
            isFirstLoad &&
            contractToOpen === null &&
            (declarationDetails?.infosAssureDtos?.length !== 0 || attestations?.certifications?.length !== 0)
        ) {
            this.setTab();
        }
    }

    componentDidUpdate(prevProps) {
        const { selectedMenuTab } = this.props;

        // Si selectedMenuTab a changé, synchroniser avec le state
        if (selectedMenuTab !== prevProps.selectedMenuTab) {
            this.changeSelectedMenuTab();
        }
    }

    @autobind
    onCancel() {
        this.confirmRef.close();
    }

    @autobind onConfirm() {
        const { t, postConsolidate, addAlert } = this.props;
        const { idDeclarant, numeroContrat, numeroAdherent } = this.state;
        this.confirmRef.close();
        postConsolidate(idDeclarant, numeroContrat, numeroAdherent).then(() => {
            addAlert({
                message: t('consolidatedContract.consolidatedContract'),
            });
        });
    }

    setTab() {
        const { attestations } = this.props;
        this.setState({
            selectedTab: attestations && attestations.certifications?.length !== 0 ? 2 : 3,
            isFirstLoad: false,
        });
    }

    getToolbarConsolider(idDeclarant, numeroContrat, numeroAdherent) {
        const { t, clientType, environment } = this.props;
        if (
            this.state.idDeclarant !== idDeclarant ||
            this.state.numeroContrat !== numeroContrat ||
            this.state.numeroAdherent !== numeroAdherent
        ) {
            this.setState({
                idDeclarant,
                numeroContrat,
                numeroAdherent,
            });
        }
        let displayConsoButton = false;
        if (clientType !== 'OTP' || environment === 'internal') {
            displayConsoButton = true;
        }
        return (
            (displayConsoButton && {
                label: t('consolidatedContract.consolide'),
                action: () => this.confirmRef.open(),
                id: 'consolide',
                behavior: 'primary',
                icon: 'arrows-retweet',
                allowedPermissions: PermissionConstants.SUPPORT_BDDS,
            }) ||
            {}
        );
    }

    @autobind
    getReferenceFromConfirmModal(ref) {
        this.confirmRef = ref;
    }

    @autobind
    openModal() {
        this.confirmRef.open();
    }

    changeSelectedMenuTab() {
        const { selectedMenuTab } = this.props;
        this.setState({ selectedTab: selectedMenuTab });
    }

    render() {
        const {
            t,
            personNumber,
            contractToOpen,
            conventions,
            amcName,
            numerosAMCEchanges,
            circuits,
            declarationDetails,
            serviceTab,
            historiqueConsolidations,
            attestations,
            otherBenefs,
            status,
            benefToDisplay,
            environment,
        } = this.props;
        const { selectedTab, isHTP } = this.state;
        let toolbarConsolider;

        let { idDeclarant, numeroContrat } = contractToOpen || {};
        if (contractToOpen) {
            const { numeroAdherent } = contractToOpen;
            toolbarConsolider = this.getToolbarConsolider(idDeclarant, numeroContrat, numeroAdherent);
        } else {
            const { infosAssureDtos } = declarationDetails;
            const assure = infosAssureDtos.find(
                (insured) => insured.identification?.assure?.numeroPersonne === personNumber,
            );
            idDeclarant = assure?.identification?.assureur?.numeroAMC
                ? assure.identification.assureur.numeroAMC
                : benefToDisplay.amc.idDeclarant;
            numeroContrat = assure?.contrat?.numero;
            const numeroAdherent = assure?.identification?.assure
                ? assure.identification.assure.numeroAdherent
                : benefToDisplay.contrats[0]?.numeroAdherent;
            toolbarConsolider = assure ? this.getToolbarConsolider(idDeclarant, numeroContrat, numeroAdherent) : {};
        }

        return (
            <div>
                {selectedTab === 0 && (
                    <Contract
                        contract={contractToOpen}
                        benefToDisplay={benefToDisplay}
                        conventions={conventions}
                        amcName={amcName}
                        numerosAMCEchanges={numerosAMCEchanges}
                        status={status}
                        isHTP={isHTP}
                    />
                )}
                {selectedTab === 1 && <Rights benefToDisplay={benefToDisplay} conventions={conventions} />}
                {selectedTab === 2 && (
                    <AttestationsContract
                        attestations={attestations?.certifications}
                        searchNextCertifications={attestations?.searchNextCertifications}
                        conventions={conventions}
                        idDeclarant={idDeclarant}
                        numeroContrat={numeroContrat}
                        personNumber={personNumber}
                        environment={environment}
                    />
                )}
                {selectedTab === 3 && (
                    <HistoriqueDeclarations
                        declarations={declarationDetails.infosAssureDtos}
                        searchNextDeclarations={declarationDetails.searchNextDeclarations}
                        conventions={conventions}
                        circuits={circuits}
                        toolbar={toolbarConsolider}
                        amcName={amcName}
                        amcNumber={idDeclarant}
                        numerosAMCEchanges={numerosAMCEchanges}
                        personNumber={personNumber}
                        numeroContrat={numeroContrat}
                        environment={environment}
                        isHTP={isHTP}
                    />
                )}
                {selectedTab === 4 && (
                    <HistoriqueConsoContract
                        numeroPersonne={personNumber}
                        historiqueConsolidations={historiqueConsolidations?.consolidatedContracts}
                        searchNextConsoContractTp={historiqueConsolidations?.searchNextConsoContractTp}
                        conventions={conventions}
                        toolbar={toolbarConsolider}
                        environment={environment}
                    />
                )}
                {selectedTab === 5 && (
                    <OtherBeneficiaries
                        beneficiaries={otherBenefs}
                        amc={idDeclarant}
                        serviceTab={serviceTab}
                        environment={environment}
                    />
                )}
                <ConfirmationModal
                    title={t('consolidatedContract.confirmationModalTitle')}
                    ref={this.getReferenceFromConfirmModal}
                    onConfirm={this.onConfirm}
                    onCancel={this.onCancel}
                    message={
                        <>
                            {t('consolidatedContract.confirmationModal')}
                            <br />
                            {t('consolidatedContract.informationModal')}
                        </>
                    }
                    confirmButton={t('consolidatedContract.confirmationYes')}
                    cancelButton={t('consolidatedContract.confirmationNo')}
                />
            </div>
        );
    }
}

BeneficiaryRightsDetailsComponent.propTypes = {
    t: PropTypes.func,
    contractToOpen: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    addAlert: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default BeneficiaryRightsDetailsComponent;
