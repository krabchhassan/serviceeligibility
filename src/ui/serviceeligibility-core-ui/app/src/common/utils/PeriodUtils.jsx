/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import Constants from './Constants';
import DateUtils from './DateUtils';

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */
const isActivePeriod = (periode) => {
    const today = DateUtils.todayMoment();
    if ((periode || {}).debut) {
        const dateDebut = DateUtils.parseDateInFormat(periode.debut, Constants.DEFAULT_DATE_PICKER_FORMAT);
        const dateFin = periode.fin && DateUtils.parseDateInFormat(periode.fin, Constants.DEFAULT_DATE_PICKER_FORMAT);
        if (dateDebut && dateDebut.isSameOrBefore(today) && (!dateFin || today.isSameOrBefore(dateFin))) {
            return true;
        }
    }
    return false;
};

const isActivePeriodAtDate = (periode, referenceDate) => {
    if ((periode || {}).debut) {
        const dateDebut = DateUtils.parseDateInFormat(periode.debut, Constants.YEARS_MONTH_DAY_FORMAT);
        const dateFin = periode.fin && DateUtils.parseDateInFormat(periode.fin, Constants.YEARS_MONTH_DAY_FORMAT);
        if (
            dateDebut &&
            dateDebut.isSameOrBefore(referenceDate) &&
            (!dateFin || referenceDate.isSameOrBefore(dateFin))
        ) {
            return true;
        }
    }
    return false;
};

const getActivePeriodeAtDate = (list, referenceDate) => {
    if (list && list.length > 0) {
        return list.find((item) => isActivePeriodAtDate(item, referenceDate));
    }
    return null;
};

const getActivePeriodeWithCodeAtDate = (object, referenceDate) => {
    if (object && object.length > 0) {
        return object.find((item) => isActivePeriodAtDate(item.periode, referenceDate));
    }
    return null;
};

const lastActivePeriod = (list) => {
    // Tri de la liste des periodes + on retourne la dernière période
    function comparePeriode(periodeA, periodeB) {
        if (periodeA.debut < periodeB.debut || (periodeA.debut === periodeB.debut && periodeA.fin > periodeB.fin))
            return -1;
        if (periodeA.debut > periodeB.debut || (periodeA.debut === periodeB.debut && periodeA.fin < periodeB.fin))
            return 1;
        return 0;
    }
    list.sort(comparePeriode);
    return list[list.length - 1];
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const PeriodUtils = {
    lastActivePeriod,
    isActivePeriod,
    getActivePeriodeAtDate,
    getActivePeriodeWithCodeAtDate,
};

export default PeriodUtils;
