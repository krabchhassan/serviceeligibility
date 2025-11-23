/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import BeneficiaryDetailsMenu from '../BeneficiaryDetails/BeneficiaryDetailsMenu';
import businessUtils from '../../../../common/utils/businessUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
class ThirdPartyMenuComponent extends Component {
    constructor(props) {
        super(props);

        this.state = {
            contractList: this.getContractList(),
        };
    }

    getStatus(item, consolidatedContract) {
        const { benefToDisplay } = this.props;
        if (benefToDisplay == null || !consolidatedContract) {
            return {
                behavior: 'default',
                status: 'pasDeConsolidation',
            };
        }
        const benefInContract = item?.beneficiaires?.find(
            (benef) => benef.numeroPersonne === benefToDisplay.numeroPersonne,
        );
        const periodeList = businessUtils.getPeriodeList(benefInContract);
        return businessUtils.calculateStatus(
            item.dateResiliation,
            benefInContract?.dateRadiation,
            periodeList,
            item.suspension,
        );
    }

    getContractList() {
        const { contractIdList, consolidatedContractList, benefDetails } = this.props;
        const contractList = [];

        contractIdList.forEach((numero) => {
            let contrat;
            if (consolidatedContractList && consolidatedContractList.length > 0) {
                contrat = consolidatedContractList.find((contract) => contract.numeroContrat === numero);
                if (contrat) {
                    contractList.push({ ...contrat, status: this.getStatus(contrat, true) });
                }
            }
            if (!contrat && benefDetails && benefDetails.contrats && benefDetails.contrats.length > 0) {
                contrat = benefDetails.contrats.find((contract) => contract.numeroContrat === numero);
                if (contrat) {
                    contractList.push({ ...contrat, status: this.getStatus(contrat, false) });
                }
            }
        });
        return contractList;
    }

    render() {
        const {
            selectedMenuTab,
            declarationsList,
            contractToOpen,
            benefKey,
            updateBenefTpDetails,
            historiqueConsolidations,
            attestations,
            otherBenefs,
            discardedContract,
            environment,
        } = this.props;
        const { contractList } = this.state;

        return contractList.map((item) => {
            const numeroContrat = item.numeroContrat || null;
            const numeroAdherent = item.numeroAdherent || null;
            let isExpanded;
            if (contractToOpen) {
                isExpanded = numeroContrat === contractToOpen.numeroContrat;
            } else if (discardedContract !== null) {
                isExpanded = numeroContrat === discardedContract;
            } else if (contractToOpen === null && (declarationsList?.length !== 0 || attestations?.length !== 0)) {
                // Cas du bénef n'ayant pas de contrat consolidé
                isExpanded = true;
            }

            return (
                <BeneficiaryDetailsMenu
                    key={`beneficiaryMenu_${numeroContrat}`}
                    benefKey={benefKey}
                    numeroContrat={numeroContrat}
                    numeroAdherent={numeroAdherent}
                    declarationsList={declarationsList}
                    contractToOpen={contractToOpen}
                    isExpanded={isExpanded}
                    selectedMenuTab={selectedMenuTab}
                    historiqueConsolidations={historiqueConsolidations}
                    attestations={attestations}
                    otherBenefs={otherBenefs}
                    updateBenefTpDetails={updateBenefTpDetails}
                    discardedContract={discardedContract}
                    status={item.status}
                    environment={environment}
                />
            );
        });
    }
}

ThirdPartyMenuComponent.propTypes = {};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ThirdPartyMenuComponent;
