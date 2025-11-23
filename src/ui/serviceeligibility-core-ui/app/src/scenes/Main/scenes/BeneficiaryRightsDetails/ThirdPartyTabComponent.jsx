/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import get from 'lodash.get';
import { Col, Row } from '@beyond-framework/common-uitoolkit-beyond';
import ThirdPartyMenu from './ThirdPartyMenuComponent';
import BeneficiaryRightsDetails from './BeneficiaryRightsDetails';
import businessUtils from '../../../../common/utils/businessUtils';
import style from './style.module.scss';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const orderDeclarations = (declarationsList) => {
    const bDeclarations = (declarationsList || []).filter((declaration) => declaration.qualification === 'B');
    const orderedBDeclarations = (bDeclarations || []).sort((a, b) => a.numero.localeCompare(b.numero));
    const otherDeclarations = (declarationsList || []).filter((declaration) => declaration.qualification !== 'B');
    const orderedOtherDeclarations = (otherDeclarations || []).sort((a, b) => a.numero.localeCompare(b.numero));

    return [...orderedBDeclarations, ...orderedOtherDeclarations];
};

function getContractNumbers(benefDetails, declarationsList) {
    const contratsValides = new Set(declarationsList.map((declaration) => declaration?.numero));

    return (benefDetails?.contrats || [])
        .filter((contrat) => contratsValides.has(contrat.numeroContrat))
        .sort((a, b) => {
            // Tri par numeroAdherent décroissant
            const adherentComparison = b.numeroAdherent.localeCompare(a.numeroAdherent);
            // Si les numeroAdherent sont identiques, tri par numeroContrat décroissant
            return adherentComparison !== 0 ? adherentComparison : b.numeroContrat.localeCompare(a.numeroContrat);
        })
        .map((contrat) => contrat.numeroContrat);
}
/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
class ThirdPartyTabComponent extends Component {
    constructor(props) {
        super(props);

        const rightsList = get(this.props, 'declarationsList', []);
        const consolidatedContractList = get(this.props, 'consolidatedContractList', []);
        const declarationsList = [];

        rightsList.forEach((item) => {
            const declarations = get(item, 'contrats', []);
            Array.prototype.push.apply(declarationsList, declarations);
        });

        const orderedDeclarationList = orderDeclarations(declarationsList);
        const contract = get(this.props, 'contractToOpen', null);
        this.state = {
            declarationsList: orderedDeclarationList,
            declarationDetails: get(this.props, 'declarationDetails', []),
            consolidatedContractList,
            contractToOpen: contract,
            historiqueConsolidations: get(this.props, 'historiqueConsolidations', []),
            attestations: get(this.props, 'attestations', []),
            otherBenefs: get(this.props, 'otherBenefs', []),
            discardedContract: contract === null ? this.props.benefDetails?.contrats[0]?.numeroContrat : null,
            clientType: get(this.props, 'clientType', []),
            numContratUsedForLightCall: null,
        };

        this.updateBenefTpDetails = this.updateBenefTpDetails.bind(this);
    }

    componentDidMount() {
        const { getAllDomains, getLightDeclarants } = this.props;

        getAllDomains();
        getLightDeclarants();
    }

    componentDidUpdate(prevProps) {
        if (prevProps.declarationsList !== this.props.declarationsList) {
            const declarationsList = [];
            this.props.declarationsList.forEach((item) => {
                const declarations = get(item, 'contrats', []);
                Array.prototype.push.apply(declarationsList, declarations);
            });

            const orderedDeclarationList = orderDeclarations(declarationsList);
            if (orderedDeclarationList && orderedDeclarationList !== declarationsList) {
                this.setDeclarationsList(orderedDeclarationList);
            }
        }
    }

    setDeclarationsList(orderedDeclarationList) {
        this.setState({ declarationsList: orderedDeclarationList });
    }

    updateBenefTpDetails(newContractToOpen, newDeclarationDetails, discardedContract, numContrat) {
        this.setState({
            contractToOpen: newContractToOpen !== undefined ? newContractToOpen : null,
            declarationDetails: newDeclarationDetails !== undefined ? newDeclarationDetails : [],
            discardedContract,
            numContratUsedForLightCall: numContrat,
        });
    }

    handleData(data, isHistoConsoOrCertification = false) {
        const { contractToOpen, numContratUsedForLightCall } = this.state;
        if (contractToOpen?.numeroContrat != null || numContratUsedForLightCall != null) {
            return contractToOpen?.numeroContrat
                ? data[contractToOpen.numeroContrat]
                : data[numContratUsedForLightCall];
        }
        return isHistoConsoOrCertification ? Object.values(data).flat()[0] : Object.values(data).flat();
    }

    render() {
        const {
            selectedMenuTab,
            benefDetails,
            conventions,
            amcName,
            numerosAMCEchanges,
            tabSelected,
            circuits,
            serviceTab,
            postConsolidate,
            environment,
        } = this.props;
        const {
            declarationsList,
            declarationDetails,
            consolidatedContractList,
            contractToOpen,
            historiqueConsolidations,
            attestations,
            otherBenefs,
            discardedContract,
            clientType,
        } = this.state;
        const historiqueConsolidationsOfContractToOpen = this.handleData(historiqueConsolidations, true);
        const attestationsOfContractToOpen = this.handleData(attestations, true);
        const otherBenefsOfContractToOpen = this.handleData(otherBenefs);

        const { beneficiaires } = contractToOpen ?? {};
        let benefToDisplay;
        if (beneficiaires && beneficiaires.length > 0) {
            const extractedBenefFromContractToOpen = Object.values(beneficiaires || []).filter(
                (benef) => benef.numeroPersonne === benefDetails?.identite?.numeroPersonne,
            );
            benefToDisplay = extractedBenefFromContractToOpen[0];
        } else {
            benefToDisplay = benefDetails;
        }
        const contractIdList = benefDetails?.numerosContratTP || getContractNumbers(benefDetails, declarationsList);

        const personNumber = benefDetails?.identite?.numeroPersonne;

        const periodeList = contractToOpen && businessUtils.getPeriodeList(benefToDisplay);

        const status =
            contractToOpen &&
            businessUtils.calculateStatus(
                contractToOpen.dateResiliation,
                benefToDisplay.dateRadiation,
                periodeList,
                contractToOpen.suspension,
            );

        return (
            <Row>
                <Col xs={3} className={style.menu}>
                    <ThirdPartyMenu
                        benefKey={benefDetails.key}
                        selectedMenuTab={selectedMenuTab}
                        declarationsList={declarationsList}
                        consolidatedContractList={consolidatedContractList}
                        contractToOpen={contractToOpen}
                        historiqueConsolidations={historiqueConsolidations}
                        attestations={attestations}
                        otherBenefs={otherBenefs}
                        updateBenefTpDetails={this.updateBenefTpDetails}
                        discardedContract={discardedContract}
                        updateContractAndHistoConso={this.updateContractAndHistoConso}
                        benefToDisplay={benefToDisplay}
                        benefDetails={benefDetails}
                        contractIdList={contractIdList}
                        environment={environment}
                    />
                </Col>
                <Col xs={10} className={style.content}>
                    <BeneficiaryRightsDetails
                        selectedMenuTab={tabSelected}
                        amcName={amcName}
                        numerosAMCEchanges={numerosAMCEchanges}
                        personNumber={personNumber}
                        conventions={conventions}
                        contractToOpen={contractToOpen}
                        historiqueConsolidations={historiqueConsolidationsOfContractToOpen}
                        attestations={attestationsOfContractToOpen}
                        otherBenefs={otherBenefsOfContractToOpen}
                        circuits={circuits}
                        declarationDetails={declarationDetails}
                        serviceTab={serviceTab}
                        status={status}
                        benefToDisplay={benefToDisplay}
                        clientType={clientType}
                        postConsolidate={postConsolidate}
                        environment={environment}
                    />
                </Col>
            </Row>
        );
    }
}

ThirdPartyTabComponent.propTypes = {
    getLightDeclarants: PropTypes.func,
    postConsolidate: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ThirdPartyTabComponent;
