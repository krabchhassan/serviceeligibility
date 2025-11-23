/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */

import get from 'lodash/get';
import DateUtils from '../../../../common/utils/DateUtils';
import formConstants from './Constants';
import Constants from '../Beneficiary/Constants';

/* ************************************* */
/* ********      PRIVATE        ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */

/* ************************************* */

function setRegroupementForPilotageFromApi(pilotage) {
    const regroupement = { ...(get(pilotage, 'regroupements[0]') || {}) };
    if (regroupement.dateOuverture) {
        regroupement.dateOuverture = DateUtils.parseDateInFormat(regroupement.dateOuverture);
    }
    if (regroupement.dateSynchronisation) {
        regroupement.dateSynchronisation = DateUtils.parseDateInFormat(regroupement.dateSynchronisation);
    }
    if (regroupement.periodeReferenceDebut) {
        regroupement.periodeReferenceDebut = DateUtils.parseDateInFormat(regroupement.periodeReferenceDebut);
    }
    if (regroupement.periodeReferenceFin) {
        regroupement.periodeReferenceFin = DateUtils.parseDateInFormat(regroupement.periodeReferenceFin);
    }
    if (regroupement.filtreDomaine !== undefined) {
        // The values are inversed
        regroupement.filtreDomaine = regroupement.filtreDomaine ? 'false' : 'true';
    }
    if (regroupement.generateFichier !== undefined) {
        regroupement.generateFichier = regroupement.generateFichier ? 'true' : 'false';
    }
    return regroupement;
}

const transformPilotageFromApi = (pilotage) => {
    const newPilotage = { ...pilotage };
    newPilotage.serviceOuvert = pilotage && pilotage.serviceOuvert ? 'true' : 'false';
    newPilotage.isCarteEditable = pilotage && pilotage.isCarteEditable ? 'true' : 'false';
    newPilotage.regroupements = [];
    if ((pilotage || {}).regroupements && (pilotage || {}).regroupements[0]) {
        const regroupement = setRegroupementForPilotageFromApi(pilotage);

        newPilotage.regroupements.push({ ...regroupement });
    }
    return newPilotage;
};

const transformAlmv3FromApi = (pilotage) => {
    const newPilotage = { ...pilotage };
    newPilotage.serviceOuvert = pilotage.serviceOuvert ? 'true' : 'false';

    newPilotage.regroupements = (newPilotage.regroupements || []).map((item) => {
        const newItem = { ...item };
        if (item.dateOuverture) {
            newItem.dateOuverture = DateUtils.parseDateInFormat(item.dateOuverture);
        }
        if (item.dateSynchronisation) {
            newItem.dateSynchronisation = DateUtils.parseDateInFormat(item.dateSynchronisation);
        }
        // The values are inversed
        newItem.numExterneContratIndividuel = item.numExterneContratIndividuel ? 'false' : 'true';
        newItem.numExterneContratCollectif = item.numExterneContratCollectif ? 'false' : 'true';

        return newItem;
    });
    return newPilotage;
};

const transformConventionTPFromApi = (conventionTP) => {
    const newConventionTP = { ...conventionTP };
    if (conventionTP.dateFin) {
        newConventionTP.dateFin = DateUtils.formatDisplayDate(conventionTP.dateFin);
    }
    return newConventionTP;
};

const transformCodeRenvoiTPFromApi = (codeRenvoiTP) => {
    const newCodeRenvoiTP = { ...codeRenvoiTP };
    if (codeRenvoiTP.dateFin) {
        newCodeRenvoiTP.dateFin = DateUtils.formatDisplayDate(codeRenvoiTP.dateFin);
    }
    return newCodeRenvoiTP;
};
const transformRegroupementDomainesTPFromApi = (regroupementDomainesTP) => {
    const newRegroupementDomainesTP = { ...regroupementDomainesTP };
    if (regroupementDomainesTP.dateFin) {
        newRegroupementDomainesTP.dateFin = DateUtils.formatDisplayDate(regroupementDomainesTP.dateFin);
    }
    return newRegroupementDomainesTP;
};
const transformFondCarteTPFromApi = (fondCarteTP) => {
    const newConventionTP = { ...fondCarteTP };
    if (fondCarteTP.dateFin) {
        newConventionTP.dateFin = DateUtils.formatDisplayDate(fondCarteTP.dateFin);
    }
    return newConventionTP;
};
const buildClosedService = ({ id, nom }) => ({
    serviceOuvert: false,
    id,
    nom,
});

const transformDeclarantFromApi = (declarant) => {
    const newDeclarant = { ...declarant };
    newDeclarant.pilotages = [];
    formConstants.SERVICES.forEach((item) => {
        const service = declarant.pilotages.find((pilotage) => pilotage.id === item.id);

        if (!service) {
            const closedService = buildClosedService(item);
            return newDeclarant.pilotages.push(transformPilotageFromApi(closedService));
        }
        return item.id === 'ALMV3'
            ? newDeclarant.pilotages.push(transformAlmv3FromApi(service))
            : newDeclarant.pilotages.push(transformPilotageFromApi(service));
    });
    handleDateSynchroFromApi(newDeclarant);
    handleCouloirClientFromApi(newDeclarant);
    if ((declarant || {}).conventionTP) {
        newDeclarant.conventionTP = [];
        (declarant || {}).conventionTP.forEach((item) => {
            newDeclarant.conventionTP.push(transformConventionTPFromApi(item));
        });
    }
    if ((declarant || {}).codeRenvoiTP) {
        newDeclarant.codeRenvoiTP = [];
        (declarant || {}).codeRenvoiTP.forEach((item) => {
            newDeclarant.codeRenvoiTP.push(transformCodeRenvoiTPFromApi(item));
        });
    }
    if ((declarant || {}).regroupementDomainesTP) {
        newDeclarant.regroupementDomainesTP = [];
        (declarant || {}).regroupementDomainesTP.forEach((item) => {
            newDeclarant.regroupementDomainesTP.push(transformRegroupementDomainesTPFromApi(item));
        });
    }
    if ((declarant || {}).fondCarteTP) {
        newDeclarant.fondCarteTP = [];
        (declarant || {}).fondCarteTP.forEach((item) => {
            newDeclarant.fondCarteTP.push(transformFondCarteTPFromApi(item));
        });
    }
    if ((declarant || {}).delaiRetention) {
        newDeclarant.delaiRetention = (declarant || {}).delaiRetention;
    }

    return newDeclarant;
};

function setRegroupementForPilotageToApi(pilotage) {
    const regroupement = { ...(get(pilotage, 'regroupements[0]') || {}) };
    if (regroupement.typeConventionnement === undefined) {
        if (pilotage.id === 'DCLBENIS') {
            regroupement.typeConventionnement = 'IS';
        } else if (pilotage.id === 'DCLBENSP') {
            regroupement.typeConventionnement = 'SP';
        }
    }
    if (regroupement.typeConventionnement instanceof Object) {
        regroupement.typeConventionnement = regroupement.typeConventionnement.code;
    }
    if (regroupement.periodeValidite instanceof Object) {
        regroupement.periodeValidite = regroupement.periodeValidite.code;
    }
    if (regroupement.dateOuverture) {
        regroupement.dateOuverture = DateUtils.formatServerDate(regroupement.dateOuverture);
    }
    if (regroupement.dateSynchronisation) {
        regroupement.dateSynchronisation = DateUtils.formatServerDate(regroupement.dateSynchronisation);
    }
    if (regroupement.periodeReferenceDebut) {
        regroupement.periodeReferenceDebut = DateUtils.formatServerDate(regroupement.periodeReferenceDebut);
    }
    if (regroupement.periodeReferenceFin) {
        regroupement.periodeReferenceFin = DateUtils.formatServerDate(regroupement.periodeReferenceFin);
    }
    if (regroupement.filtreDomaine !== undefined) {
        // The values are inversed
        regroupement.filtreDomaine = regroupement.filtreDomaine === 'false';
    }
    if (regroupement.generateFichier !== undefined) {
        regroupement.generateFichier = regroupement.generateFichier === 'true';
    }
    if (regroupement.validitesDomainesDroits) {
        regroupement.validitesDomainesDroits = mapValidites(regroupement.validitesDomainesDroits);
    }
    return regroupement;
}

function setTranscodageDomainesTP(newDeclarant, declarant) {
    newDeclarant.transcodageDomainesTP = [];
    if (declarant.transcodageDomainesTP instanceof Object) {
        Object.values(declarant.transcodageDomainesTP || []).forEach((transco) => {
            const domainesCible = [];
            Object.values(transco.domainesCible || []).forEach((transcoCible) => {
                if (transcoCible.label) {
                    domainesCible.push(transcoCible.value);
                } else {
                    domainesCible.push(transcoCible);
                }
            });
            newDeclarant.transcodageDomainesTP.push({
                domaineSource: (transco.domaineSource || {}).value,
                domainesCible,
            });
        });
    }
}

function setConventionTPs(newDeclarant, declarant) {
    newDeclarant.conventionTP = [];
    if (declarant.conventionTP instanceof Object) {
        Object.values(declarant.conventionTP || []).forEach((convention) => {
            if (convention.dateDebut) {
                if (convention.isNewConvention) {
                    newDeclarant.conventionTP.push({
                        reseauSoin: (convention.reseauSoin || {}).value,
                        domaineTP: (convention.domaineTP || {}).value,
                        conventionCible: (convention.conventionCible || {}).value,
                        concatenation: convention.concatenation,
                        dateDebut: DateUtils.formatStringToDate(convention.dateDebut),
                        dateFin: convention.dateFin && DateUtils.formatStringToDate(convention.dateFin),
                    });
                } else if (convention.dateFin) {
                    newDeclarant.conventionTP.push({
                        reseauSoin: convention.reseauSoin,
                        domaineTP: convention.domaineTP,
                        conventionCible: convention.conventionCible,
                        concatenation: convention.concatenation,
                        dateDebut: convention.dateDebut,
                        dateFin: DateUtils.isFormattedDate(convention.dateFin)
                            ? convention.dateFin
                            : DateUtils.formatStringToDate(convention.dateFin),
                    });
                } else {
                    newDeclarant.conventionTP.push({
                        reseauSoin: convention.reseauSoin,
                        domaineTP: convention.domaineTP,
                        conventionCible: convention.conventionCible,
                        concatenation: convention.concatenation,
                        dateDebut: convention.dateDebut,
                    });
                }
            }
        });
    }
}

function setCodeRenvoiTPs(newDeclarant, declarant) {
    newDeclarant.codeRenvoiTP = [];
    if (declarant.codeRenvoiTP instanceof Object) {
        Object.values(declarant.codeRenvoiTP || []).forEach((codeRenvoi) => {
            if (codeRenvoi.dateDebut) {
                if (codeRenvoi.isNewCodeRenvoi) {
                    newDeclarant.codeRenvoiTP.push({
                        domaineTP: (codeRenvoi.domaineTP || {}).value,
                        reseauSoin: (codeRenvoi.reseauSoin || {}).value,
                        codeRenvoi: (codeRenvoi.codeRenvoi || {}).value,
                        dateDebut: DateUtils.formatStringToDate(codeRenvoi.dateDebut),
                        dateFin: codeRenvoi.dateFin && DateUtils.formatStringToDate(codeRenvoi.dateFin),
                    });
                } else if (codeRenvoi.dateFin) {
                    newDeclarant.codeRenvoiTP.push({
                        domaineTP: codeRenvoi.domaineTP,
                        reseauSoin: codeRenvoi.reseauSoin,
                        codeRenvoi: codeRenvoi.codeRenvoi,
                        dateDebut: codeRenvoi.dateDebut,
                        dateFin: DateUtils.isFormattedDate(codeRenvoi.dateFin)
                            ? codeRenvoi.dateFin
                            : DateUtils.formatStringToDate(codeRenvoi.dateFin),
                    });
                } else {
                    newDeclarant.codeRenvoiTP.push({
                        domaineTP: codeRenvoi.domaineTP,
                        reseauSoin: codeRenvoi.reseauSoin,
                        codeRenvoi: codeRenvoi.codeRenvoi,
                        dateDebut: codeRenvoi.dateDebut,
                    });
                }
            }
        });
    }
}

function setRegroupementDomainesTPs(newDeclarant, declarant) {
    newDeclarant.regroupementDomainesTP = [];
    if (declarant.regroupementDomainesTP instanceof Object) {
        Object.values(declarant.regroupementDomainesTP || []).forEach((regroupementDomaine) => {
            if (regroupementDomaine.dateDebut) {
                if (regroupementDomaine.isNewRegroupementDomaines) {
                    const codesDomainesTP = [];
                    Object.values(regroupementDomaine.codesDomainesTP || []).forEach((codeDomaine) => {
                        codesDomainesTP.push((codeDomaine || {}).value);
                    });
                    newDeclarant.regroupementDomainesTP.push({
                        domaineRegroupementTP: (regroupementDomaine.domaineRegroupementTP || {}).value,
                        codesDomainesTP,
                        niveauRemboursementIdentique: regroupementDomaine.niveauRemboursementIdentique,
                        dateDebut: DateUtils.formatStringToDate(regroupementDomaine.dateDebut),
                        dateFin:
                            regroupementDomaine.dateFin && DateUtils.formatStringToDate(regroupementDomaine.dateFin),
                    });
                } else if (regroupementDomaine.dateFin) {
                    newDeclarant.regroupementDomainesTP.push({
                        domaineRegroupementTP: regroupementDomaine.domaineRegroupementTP,
                        codesDomainesTP: regroupementDomaine.codesDomainesTP,
                        niveauRemboursementIdentique: regroupementDomaine.niveauRemboursementIdentique,
                        dateDebut: regroupementDomaine.dateDebut,
                        dateFin: DateUtils.isFormattedDate(regroupementDomaine.dateFin)
                            ? regroupementDomaine.dateFin
                            : DateUtils.formatStringToDate(regroupementDomaine.dateFin),
                    });
                } else {
                    newDeclarant.regroupementDomainesTP.push({
                        domaineRegroupementTP: regroupementDomaine.domaineRegroupementTP,
                        codesDomainesTP: regroupementDomaine.codesDomainesTP,
                        niveauRemboursementIdentique: regroupementDomaine.niveauRemboursementIdentique,
                        dateDebut: regroupementDomaine.dateDebut,
                    });
                }
            }
        });
    }
}

function setFondCarteTPs(newDeclarant, declarant) {
    newDeclarant.fondCarteTP = [];
    if (declarant.fondCarteTP instanceof Object) {
        Object.values(declarant.fondCarteTP || []).forEach((fondCarte) => {
            if (fondCarte.dateDebut) {
                if (fondCarte.isNewFondCarte) {
                    newDeclarant.fondCarteTP.push({
                        reseauSoin: (fondCarte.reseauSoin || {}).value,
                        fondCarte: fondCarte.fondCarte || '',
                        dateDebut: DateUtils.formatStringToDate(fondCarte.dateDebut),
                        dateFin: fondCarte.dateFin && DateUtils.formatStringToDate(fondCarte.dateFin),
                    });
                } else if (fondCarte.dateFin) {
                    newDeclarant.fondCarteTP.push({
                        reseauSoin: fondCarte.reseauSoin,
                        fondCarte: fondCarte.fondCarte,
                        dateDebut: fondCarte.dateDebut,
                        dateFin: DateUtils.isFormattedDate(fondCarte.dateFin)
                            ? fondCarte.dateFin
                            : DateUtils.formatStringToDate(fondCarte.dateFin),
                    });
                } else {
                    newDeclarant.fondCarteTP.push({
                        reseauSoin: fondCarte.reseauSoin,
                        fondCarte: fondCarte.fondCarte,
                        dateDebut: fondCarte.dateDebut,
                    });
                }
            }
        });
    }
}

const transformPilotageToApi = (pilotage) => {
    const newPilotage = { ...pilotage };
    newPilotage.serviceOuvert = pilotage.serviceOuvert === 'true';
    newPilotage.regroupements = [];
    if ((pilotage || {}).regroupements && pilotage.regroupements[0]) {
        const regroupement = setRegroupementForPilotageToApi(pilotage);

        newPilotage.regroupements.push({ ...regroupement });
    }

    return newPilotage;
};

const transformAlmv3ToApi = (pilotage) => {
    const newPilotage = { ...pilotage };
    newPilotage.serviceOuvert = pilotage.serviceOuvert === 'true';

    newPilotage.regroupements = (newPilotage.regroupements || []).map((item) => {
        const newItem = { ...item };
        if (item.dateOuverture) {
            newItem.dateOuverture = DateUtils.formatServerDate(item.dateOuverture);
        }

        if (item.dateSynchronisation) {
            newItem.dateSynchronisation = DateUtils.formatServerDate(item.dateSynchronisation);
        }
        if (item.typeConventionnement instanceof Object) {
            newItem.typeConventionnement = item.typeConventionnement.code;
        }
        // The values are inversed
        newItem.numExterneContratIndividuel = item.numExterneContratIndividuel === 'false';
        newItem.numExterneContratCollectif = item.numExterneContratCollectif === 'false';

        return newItem;
    });

    return newPilotage;
};

function handleDateSynchroToApi(newDeclarant) {
    const pilotageCarteTP = newDeclarant.pilotages.find((pilotage) => pilotage.id === 'CARTETP');
    const pilotageCarteDemat = newDeclarant.pilotages.find((pilotage) => pilotage.id === 'CARTEDEMATERIALISEE');

    if (pilotageCarteDemat.regroupements[0] && pilotageCarteTP.serviceOuvert) {
        if (!pilotageCarteTP.regroupements[0]) {
            pilotageCarteTP.regroupements[0] = {};
        }
        const dateSynchroCarteDemat = pilotageCarteDemat.regroupements[0].dateSynchronisation;
        pilotageCarteTP.regroupements[0].dateSynchronisation = dateSynchroCarteDemat;
        if (dateSynchroCarteDemat === null) {
            pilotageCarteTP.isCarteEditable = 'false';
        }
    }
    if (!pilotageCarteTP.serviceOuvert) {
        pilotageCarteTP.isCarteEditable = 'false';
        if (pilotageCarteTP.regroupements[0]) {
            pilotageCarteTP.regroupements[0].dateSynchronisation = null;
        }
    }
    if (!pilotageCarteDemat.serviceOuvert && pilotageCarteDemat.regroupements[0]) {
        pilotageCarteDemat.regroupements[0].dateSynchronisation = null;
    }
}

function handleDateSynchroFromApi(newDeclarant) {
    const pilotageCarteTP = newDeclarant.pilotages.find((pilotage) => pilotage.id === 'CARTETP');
    const pilotageCarteDemat = newDeclarant.pilotages.find((pilotage) => pilotage.id === 'CARTEDEMATERIALISEE');
    if (
        pilotageCarteTP.regroupements[0]?.dateSynchronisation &&
        !pilotageCarteDemat.regroupements[0]?.dateSynchronisation
    ) {
        if (!pilotageCarteDemat.regroupements[0]) {
            pilotageCarteDemat.regroupements[0] = {};
        }
        pilotageCarteDemat.regroupements[0].dateSynchronisation = pilotageCarteTP.regroupements[0].dateSynchronisation;
    }
}

function handleCouloirClientToApi(newDeclarant) {
    const pilotageCarteTP = newDeclarant.pilotages.find((pilotage) => pilotage.id === 'CARTETP');
    const pilotageCarteDemat = newDeclarant.pilotages.find((pilotage) => pilotage.id === 'CARTEDEMATERIALISEE');

    if (pilotageCarteDemat.regroupements[0] && pilotageCarteTP.serviceOuvert) {
        if (!pilotageCarteTP.regroupements[0]) {
            pilotageCarteTP.regroupements[0] = {};
        }
        const couloirClientCarteDemat = pilotageCarteDemat.regroupements[0].couloirClient;
        pilotageCarteTP.regroupements[0].couloirClient = couloirClientCarteDemat;
    }

    if (!pilotageCarteDemat.serviceOuvert && pilotageCarteDemat.regroupements[0]) {
        pilotageCarteDemat.regroupements[0].couloirClient = null;
    }
}

function handleCouloirClientFromApi(newDeclarant) {
    const pilotageCarteTP = newDeclarant.pilotages.find((pilotage) => pilotage.id === 'CARTETP');
    const pilotageCarteDemat = newDeclarant.pilotages.find((pilotage) => pilotage.id === 'CARTEDEMATERIALISEE');
    if (pilotageCarteTP.regroupements[0]?.couloirClient && !pilotageCarteDemat.regroupements[0]?.couloirClient) {
        if (!pilotageCarteDemat.regroupements[0]) {
            pilotageCarteDemat.regroupements[0] = {};
        }
        pilotageCarteDemat.regroupements[0].couloirClient = pilotageCarteTP.regroupements[0].couloirClient;
    }
}

const transformDeclarantToApi = (declarant, clientType) => {
    const newDeclarant = { ...declarant };
    newDeclarant.pilotages = [];
    if (declarant.codeCircuit instanceof Object) {
        newDeclarant.codeCircuit = declarant.codeCircuit.codeCircuit;
    }

    if (declarant.operateurPrincipal instanceof Object) {
        newDeclarant.operateurPrincipal = declarant.operateurPrincipal.code;
    }

    if (clientType !== Constants.TOPOLOGY_INSURER) {
        newDeclarant.delaiRetention = null;
    }
    formConstants.SERVICES.forEach((item) => {
        const service = declarant.pilotages.find((pilotage) => pilotage.id === item.id);
        return service.id === 'ALMV3'
            ? newDeclarant.pilotages.push(transformAlmv3ToApi(service))
            : newDeclarant.pilotages.push(transformPilotageToApi(service));
    });

    handleDateSynchroToApi(newDeclarant);
    handleCouloirClientToApi(newDeclarant);
    setTranscodageDomainesTP(newDeclarant, declarant);
    setConventionTPs(newDeclarant, declarant);
    setCodeRenvoiTPs(newDeclarant, declarant);
    setRegroupementDomainesTPs(newDeclarant, declarant);
    setFondCarteTPs(newDeclarant, declarant);

    if (
        JSON.stringify(newDeclarant.numerosAMCEchanges) === '[""]' ||
        JSON.stringify(newDeclarant.numerosAMCEchanges) === '[]'
    ) {
        delete newDeclarant.numerosAMCEchanges;
    }

    if (
        JSON.stringify(newDeclarant.transcodageDomainesTP) === '[""]' ||
        JSON.stringify(newDeclarant.transcodageDomainesTP) === '[]'
    ) {
        delete newDeclarant.transcodageDomainesTP;
    }

    return newDeclarant;
};

const mapValidites = (validitesDomainesDroits) => {
    const validites = [];

    validitesDomainesDroits.forEach((validite) => {
        validites.push({
            codeDomaine: get(validite, 'codeDomaine.value', null),
            duree: get(validite, 'duree', -1),
            unite: get(validite, 'unite.value', null),
            positionnerFinDeMois: get(validite, 'positionnerFinDeMois', false),
        });
    });

    return validites;
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const DeclarantUtils = {
    transformDeclarantFromApi,
    transformDeclarantToApi,
};

export default DeclarantUtils;
