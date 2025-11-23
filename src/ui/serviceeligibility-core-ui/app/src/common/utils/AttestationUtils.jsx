import businessUtils from './businessUtils';
import StringUtils from './StringUtils';

const getLibelleConvention = (conventions, conventionCode) => {
    const conventionValue = businessUtils.getConvention(conventions, conventionCode);
    return conventionValue ? `${conventionValue.libelle} ` : '';
};

function prepareAttestationRowsData(attestation, conventions) {
    const codeRenvoiEntete = `${attestation.codeRenvoi || ''}${
        attestation.codeRenvoi && attestation.libelleRenvoi ? ' - ' : ''
    }${attestation.libelleRenvoi || ''}`;

    const { details } = attestation;

    const REGEX_NON_COUVERT = /^NC(\/NC)*$/;
    const rowsData = (details || [])
        .filter((detail) => !REGEX_NON_COUVERT.test(detail.taux))
        .map((detail) => {
            const conventionList = (detail.conventions || [])
                .map(
                    (convention) =>
                        convention.code && `${convention.code} - ${getLibelleConvention(conventions, convention.code)}`,
                )
                .join('_');
            const taux = detail.taux ? detail.taux : '-';
            let codeRenvoi = '-';
            if (detail.codeRenvois) {
                codeRenvoi = `${detail.codeRenvois} - ${detail.libelleRenvois}`;
            } else if (detail.libelleRenvois) {
                codeRenvoi = detail.libelleRenvois;
            }
            if (detail.codeRenvoisAdditionnel) {
                codeRenvoi = `${codeRenvoi}_${detail.codeRenvoisAdditionnel} - ${detail.libelleRenvoisAdditionnel}`;
            }
            return {
                ordreEdition: detail.numOrdreEdition,
                domaineDroits: detail.codeDomaine,
                conventions: StringUtils.splitIntoDivs(conventionList),
                taux,
                renvois: codeRenvoi && StringUtils.splitIntoDivs(codeRenvoi),
            };
        });
    return { codeRenvoiEntete, rowsData };
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const attestationUtils = {
    prepareAttestationRowsData,
};

export default attestationUtils;
