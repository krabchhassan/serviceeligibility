import React from 'react';
import StringUtils from './StringUtils';
import Constants from './Constants';
import DateUtils from './DateUtils';
import DateRangePresenter from './DateRangePresenter';
import PeriodUtils from './PeriodUtils';

const formatRO = (nir) => {
    const formatedNir = nir || '';
    return formatedNir
        .split(/^(\d)(\d{2})(\d{2})(\w{2})(\d{3})(\d{3})$/g)
        .join(' ')
        .trim();
};

const formatIBAN = (iban) => {
    const formatedIban = iban || '';
    return formatedIban
        .replace(/[^\dA-Z]/g, '')
        .replace(/(.{4})/g, '$1 ')
        .trim();
};

const getQuality = (qualite) => {
    switch (qualite) {
        case 'A':
            return 'qualiteAssure';
        case 'C':
            return 'qualiteConjoint';
        case 'E':
            return 'qualiteEnfant';
        default:
            return 'qualiteAutre';
    }
};

const getI18nLabelTypeContrat = (typeContrat) => `beneficiaryRights.resultSearch.typeContrat${typeContrat || 'Autre'}`;

const getConvention = (list, code) => list.find((item) => item.code === code);

const getLibelleConverter = (code, libelle) => `${code || ''}${code && libelle ? ' - ' : ''}${libelle || ''}`;
const getLibelleConverterForRate = (code, libelle) => `${code || ''}${code && libelle ? ' ' : ''}${libelle || ''}`;

const formatServiceTime = (typeService, effetDebut, t) => {
    const dateEffet = effetDebut.substr(0, 10);
    const heureEffet = effetDebut.substr(11, 5);
    const libelleByTypeService = {
        C: 'consoTitle',
        E: 'extractTitle',
        T: 'traitementTitle',
    };
    return t(`beneficiaryRightsDetails.trace.${libelleByTypeService[typeService]}`, {
        date: dateEffet,
        time: heureEffet,
    });
};
const getContractType = (code) => {
    if (code === 'S') {
        return 'Surcomplémentaire';
    } else if (code === 'C') {
        return 'Base + surcomplémentaire';
    }
    return 'Base';
};
const buildContextHeaderSubPanel = (dateExecution, erreur, formatTimeKey, t) => {
    const dateExecutionLabel = formatServiceTime(formatTimeKey, dateExecution, t);
    const rejetLabel = erreur
        ? `${t('beneficiaryRightsDetails.trace.reject')} ${erreur.code} - ${erreur.libelle}`
        : `${t('beneficiaryRightsDetails.trace.noReject')}`;
    return `${dateExecutionLabel} / ${rejetLabel}`;
};
const transformDate = (date) => `${date.substring(6, 8)}/${date.substring(4, 6)}/${date.substring(0, 4)}`;

const formatDetailDroits = (t, detailsDroit, returnCodesList) => {
    const data = [];
    detailsDroit.forEach((detail) => {
        const codeRenvoiObject = Object.values(returnCodesList || {}).find(
            (returnCode) => returnCode.code === detail.codeRenvoi,
        );
        data.push({
            ...detail,
            codeRenvoi: codeRenvoiObject ? codeRenvoiObject.code : detail.codeRenvoi,
            messageRenvoi: codeRenvoiObject
                ? codeRenvoiObject.libelle
                : StringUtils.renderLongText(detail.messageRenvoi, ' ', 80),
        });
    });
    return data;
};

const getPeriode = (periode) => {
    const data = [];
    if (periode != null) {
        periode.forEach((detail) => {
            data.push(detail.periode);
        });
    }
    return data;
};

const stringifyListLots = (codeLots, allLot) => {
    const separator = ', ';
    const listLot = [];
    (codeLots || []).forEach((codeLot) => {
        (allLot || []).forEach((objLot) => {
            if (objLot.id === codeLot) {
                listLot.push(`${objLot.code}-${objLot.libelle}`);
            }
        });
    });
    return listLot.join(separator);
};

const stringifyListGTs = (garantieTechniques) => {
    const separator = ', ';
    const listGT = [];
    (garantieTechniques || []).forEach((gt) => listGT.push(`${gt.codeAssureur}-${gt.codeGarantie}`));
    return listGT.join(separator);
};

const stringifyListLotsForAlm = (codeLots, allLot) => {
    const separator = ', ';
    const listLot = [];
    (codeLots || []).forEach((codeLot) => {
        (allLot || []).forEach((objLot) => {
            if (objLot.code === codeLot) {
                listLot.push(`${objLot.code}-${objLot.libelle}`);
            }
        });
    });
    return listLot.join(separator);
};

const getCodeRenvoiActions = (pathTot, t) => {
    return [
        {
            code: Constants.GARDER,
            label: t(`${pathTot}.garder`),
        },
        {
            code: Constants.INHIBER,
            label: t(`${pathTot}.inhiber`),
        },
        {
            code: Constants.REMPLACER,
            label: t(`${pathTot}.remplacer`),
        },
        {
            code: Constants.COMPLETER,
            label: t(`${pathTot}.completer`),
        },
    ];
};

const disabledCodeRenvoiAction = (code) => {
    const toDisable = [Constants.GARDER, Constants.INHIBER];
    return !code || toDisable.includes(code);
};

const filterAndLimit50 = (options, input) =>
    options.filter((x) => x.label.toLowerCase().includes(input.toLowerCase())).slice(0, 50);

const getListOpenServices = (amc) => {
    const services = [Constants.CARTE_DEMATERIALISEE, Constants.CARTE_TP];
    return (amc.pilotages || [])
        .filter((pilotage) => pilotage.serviceOuvert && services.includes(pilotage.nom))
        .map((pilotage) => pilotage.nom);
};

const calculateStatus = (dateResiliation, dateRadiation, periodeList, suspension) => {
    const today = DateUtils.todayMoment();
    const dateFormat = 'YYYY-MM-DD';
    let status = {
        behavior: 'success',
        status: 'valid',
    };

    if (periodeList) {
        const periodeDebutList = periodeList
            .map((periode) => periode.periodeDebut)
            .filter((date) => date && date !== '-')
            .map((date) => new Date(date));
        const firstPeriodeDebut = periodeDebutList.length ? new Date(Math.min(...periodeDebutList)) : null;

        const periodeFinList = periodeList
            .map((periode) => periode.periodeFin)
            .filter((date) => date && date !== '-')
            .map((date) => new Date(date));
        const lastPeriodeFin = periodeFinList.length ? new Date(Math.max(...periodeFinList)) : null;

        if (firstPeriodeDebut && today.isBefore(firstPeriodeDebut)) {
            // status fermé
            status = {
                behavior: 'warning',
                status: 'ferme',
            };
        }
        if (lastPeriodeFin && today.isAfter(lastPeriodeFin)) {
            if (dateResiliation) {
                const resilDate = DateUtils.parseDateInFormat(dateResiliation, dateFormat, false);
                if (resilDate && resilDate.isBefore(today)) {
                    // status résilié
                    status = {
                        behavior: 'warning',
                        status: 'resilie',
                    };
                    return status;
                }
            } else if (dateRadiation) {
                const radiationDate = DateUtils.parseDateInFormat(dateRadiation, dateFormat, false);
                if (radiationDate && radiationDate.isBefore(today)) {
                    // status radié
                    status = {
                        behavior: 'warning',
                        status: 'radie',
                    };
                    return status;
                }
            } else {
                // status fermé
                status = {
                    behavior: 'warning',
                    status: 'ferme',
                };
                return status;
            }
        }
    }

    if (suspension) {
        const { periodesSuspension } = suspension;

        periodesSuspension.forEach((periodeSuspension) => {
            const debutSuspension = new Date(periodeSuspension.dateDebutSuspension);
            const finSuspension = periodeSuspension.dateFinSuspension;
            if (
                (today.isAfter(debutSuspension) && !finSuspension) ||
                (today.isAfter(debutSuspension) && finSuspension && today.isBefore(new Date(finSuspension)))
            ) {
                // status suspendu
                status = {
                    behavior: 'warning',
                    status: 'suspendu',
                };
            }
        });
    }
    return status;
};

const getBenefIdentity = (nomUsage, nomFamille, prenom) => {
    let name = `${nomUsage || ''}`;
    if (nomFamille && nomUsage && nomFamille !== nomUsage) {
        name += ` (${nomFamille})`;
    } else if (nomFamille) {
        name = `${nomFamille}`;
    }
    name += ` ${prenom || ''}`;
    return name;
};

const getPeriodeList = (benefToDisplay) => {
    const periodList = [];
    if (benefToDisplay && benefToDisplay.maillesDomaineDroits) {
        Object.values(benefToDisplay.maillesDomaineDroits.mailleDomaineTP).forEach((domaine) => {
            periodList.push(...domaine.periodesDroit);
        });
    }
    return periodList;
};

const isActivePeriod = (periodes, referenceDate, t) => {
    let lastPeriode = null;

    if (periodes && periodes.length > 0) {
        lastPeriode = PeriodUtils.getActivePeriodeAtDate(periodes, referenceDate);
    }

    return lastPeriode ? t('yes') : t('no');
};

const returnCodeOfActivePeriod = (object, referenceDate, t) => {
    let lastObjectWithActivePeriod = null;

    if (object && object.length > 0) {
        lastObjectWithActivePeriod = PeriodUtils.getActivePeriodeWithCodeAtDate(object, referenceDate);
    }

    return lastObjectWithActivePeriod
        ? lastObjectWithActivePeriod?.code || formatRO(lastObjectWithActivePeriod?.nir?.code) || t('yes')
        : t('no');
};

const getActivePeriod = (periods, debutDroit, finDroit, t) => {
    const debutD = DateUtils.parseDateInFormat(debutDroit, Constants.DEFAULT_PARSING_FORMAT);
    const finD = DateUtils.parseDateInFormat(finDroit, Constants.DEFAULT_PARSING_FORMAT);

    const activePeriode = periods?.find(({ debut, fin }) => {
        const debutP = DateUtils.parseDateInFormat(debut, Constants.DEFAULT_DATE_PICKER_FORMAT);
        const finP = fin ? DateUtils.parseDateInFormat(fin, Constants.DEFAULT_DATE_PICKER_FORMAT) : null;

        return (debutP.isSameOrBefore(finD) || !finD) && (finP === null || finP.isSameOrAfter(debutD));
    });

    return activePeriode ? (
        <DateRangePresenter
            start={DateUtils.formatDisplayDate(activePeriode.debut, Constants.DEFAULT_EMPTY_DATE)}
            end={DateUtils.formatDisplayDate(activePeriode.fin, Constants.DEFAULT_EMPTY_DATE)}
        />
    ) : (
        t('no')
    );
};

const getActivePeriodFromData = (data, debutDroit, finDroit, t) => {
    const debutD = DateUtils.parseDateInFormat(debutDroit, Constants.DEFAULT_PARSING_FORMAT);
    const finD = DateUtils.parseDateInFormat(finDroit, Constants.DEFAULT_PARSING_FORMAT);

    const activePeriode = data.find(({ periode }) => {
        const debut = DateUtils.parseDateInFormat(periode.debut, Constants.DEFAULT_DATE_PICKER_FORMAT);
        const fin = periode.fin ? DateUtils.parseDateInFormat(periode.fin, Constants.DEFAULT_DATE_PICKER_FORMAT) : null;

        return (debut.isSameOrBefore(finD) || !finD) && (fin === null || fin.isSameOrAfter(debutD));
    });

    return activePeriode ? (
        <DateRangePresenter
            start={DateUtils.formatDisplayDate(activePeriode.periode.debut, Constants.DEFAULT_EMPTY_DATE)}
            end={DateUtils.formatDisplayDate(activePeriode.periode.fin, Constants.DEFAULT_EMPTY_DATE)}
        />
    ) : (
        t('no')
    );
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const businessUtils = {
    formatRO,
    formatIBAN,
    getQuality,
    getI18nLabelTypeContrat,
    getConvention,
    getLibelleConverter,
    getLibelleConverterForRate,
    formatServiceTime,
    buildContextHeaderSubPanel,
    getContractType,
    transformDate,
    formatDetailDroits,
    getPeriode,
    stringifyListLots,
    stringifyListLotsForAlm,
    stringifyListGTs,
    getCodeRenvoiActions,
    disabledCodeRenvoiAction,
    filterAndLimit50,
    getListOpenServices,
    calculateStatus,
    getBenefIdentity,
    getPeriodeList,
    isActivePeriod,
    returnCodeOfActivePeriod,
    getActivePeriod,
    getActivePeriodFromData,
};

export default businessUtils;
