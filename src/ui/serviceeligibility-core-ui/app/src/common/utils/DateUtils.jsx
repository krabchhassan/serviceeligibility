/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { DateWrapper } from '@beyond-framework/common-base-date';
import moment from 'moment';
import { i18n } from '@beyond-framework/common-uitoolkit-beyond';
import Constants from '../utils/Constants';

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */
// workaround: forcing en-GB date format for all en locales to avoid displaying month before day.
const getLanguage = () => (i18n.language === 'en' ? 'en-GB' : i18n.language);

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

// convert a string or an already existing moment to a DateWrapper instance.
// Accepted string format : https://momentjs.com/docs/#/parsing/string/
// falsy date parameter will return an invalid date wrapper
const toDateWrapper = (date) => new DateWrapper(date || null, getLanguage());

// create a moment at the start of the current day
const todayMoment = () => new DateWrapper(undefined, getLanguage()).moment().startOf('day');

const formatDisplayMonth = (date) => moment(date).format('MMM-YY');

// is used to format a date to display
// default format is formatS (see common-base-date documentation for available formats)
// formatS is 04/06/2020
// formatL is HH:mm:ss.SSS
// ignoreZeroTimestamp - when this parameter is true, any date with timestamp 0 will be ignored. This is
// needed because sometimes we can't have null for date, so we put the value 1970/01/01.
const formatDisplayDate = (date, defaultString = null, formatName = 'formatS', ignoreZeroTimestamp = false) => {
    const dateWrapper = toDateWrapper(date);
    const dateMoment = dateWrapper.moment();
    if (dateMoment.isValid()) {
        if (ignoreZeroTimestamp && dateMoment.unix() === 0) {
            return defaultString;
        }

        // Si la date est au format YYYY/MM/DD, on force le bon affichage
        // Sans cette correction, moment() peut mal interpréter les dates anciennes (ex: "0001/11/30" → "11/01/2030")
        if (typeof date === 'string' && /^\d{4}\/\d{2}\/\d{2}$/.test(date)) {
            const dateMomentYYYYMMDD = moment(date, 'YYYY/MM/DD', true);
            return dateMomentYYYYMMDD.format('DD/MM/YYYY');
        }

        return dateWrapper.dateFormatter()[formatName]();
    }
    return defaultString;
};

// is used to format a time to display
// default format is formatM (see common-base-date documentation for available formats)
const formatDisplayTime = (date, defaultString = null, formatName = 'formatM') => {
    const dateWrapper = toDateWrapper(date);
    return dateWrapper.moment().isValid() ? dateWrapper.timeFormatter()[formatName]() : defaultString;
};

// is used to format a date and time to display
// default format is formatM (see common-base-date documentation for available formats)
const formatDisplayDateTime = (date, defaultString = null, formatName = 'formatM') => {
    const dateWrapper = toDateWrapper(date);
    return dateWrapper.moment().isValid() ? dateWrapper.dateTimeFormatter()[formatName]() : defaultString;
};

// convert server datetime to local datetime
const formatDisplayDateTimeWithTimeZone = (date) => {
    const dateMoment = moment(`${date}:00.000Z`, `DD/MM/YYYY HH:mm:ss.SSSZ`);
    const utcFormattedDate = moment.utc(dateMoment).toDate();
    return moment(utcFormattedDate).local().format('DD/MM/YYYY HH:mm');
};

// convert local datetime to server datetime
const formatLocalToUtc = (date, isEndOfDay = false) => {
    const dateMoment = moment(`${date} ${isEndOfDay ? '23:59:59:999' : '00.00:00:000'}`, `DD/MM/YYYY HH:mm:ss.SSS`);
    return moment(dateMoment).utc().format('DD/MM/YYYY HH:mm:ss');
};

// is used before sending the values to the API
const formatServerDate = (date, format = Constants.DEFAULT_PARSING_FORMAT) => {
    const mmt = toDateWrapper(date).moment();
    return mmt.isValid() ? mmt.format(format) : null;
};

// parse a raw date (usually coming from the API)
const parseDate = (rawDate, returnInvalidMoment = false) => {
    const mmt = toDateWrapper(rawDate).moment();
    return returnInvalidMoment || mmt.isValid() ? mmt : null;
};

// parse a raw date (usually coming from the API)
const parseDateInFormat = (rawDate, format = Constants.DEFAULT_PARSING_FORMAT, returnInvalidMoment = false) => {
    const mmt = moment(rawDate, format);
    return returnInvalidMoment || mmt.isValid() ? mmt : null;
};

// prop type to allow DateWrapper or moment instances
const propTypesMoment = (props, propName, componentName) => {
    const value = props[propName];
    return !value || moment.isMoment(value)
        ? undefined
        : new Error(`Invalid prop ${propName} supplied to ${componentName}. Validation failed.`);
};

const formatBeneficiaryBirthDateForRequest = (date) => {
    const formatedDate = date || '';
    const dateElements = formatedDate.split('/');
    return dateElements && dateElements.length === 3 ? dateElements[2] + dateElements[1] + dateElements[0] : null;
};

const dateAfterToday = (date) => {
    const mmt = moment(date, Constants.DEFAULT_DATE_PICKER_FORMAT);
    if (mmt.isAfter()) {
        return true;
    }
    return false;
};

const betweenData = (debut, fin) => {
    const today = todayMoment();
    return today.isBetween(debut, fin, undefined, '[]');
};

const transformDateForDisplay = (date) =>
    date ? `${date.substring(8, 10)}/${date.substring(5, 7)}/${date.substring(0, 4)}` : '--/--/----';

const sortRightListByDateDsc = (list) =>
    list.sort((a, b) =>
        moment(b.periode.debut, Constants.DEFAULT_DATE_PICKER_FORMAT).diff(
            moment(a.periode.debut, Constants.DEFAULT_DATE_PICKER_FORMAT),
        ),
    );

const isFormattedDate = (date) => {
    if (typeof date === 'string') {
        const isMatching = date.match(/^(\d{1,2})-(\d{1,2})-(\d{4})$/);
        return !!isMatching;
    }

    return false;
};

const formatStringToDate = (date) => {
    return moment(date, 'DD/MM/YYYY').format(Constants.DEFAULT_DATE_TIME_FORMAT);
};
/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const DateUtils = {
    toDateWrapper,
    todayMoment,
    formatDisplayDate,
    formatServerDate,
    parseDate,
    propTypesMoment,
    formatDisplayTime,
    formatDisplayDateTime,
    formatDisplayMonth,
    parseDateInFormat,
    formatDisplayDateTimeWithTimeZone,
    formatBeneficiaryBirthDateForRequest,
    dateAfterToday,
    transformDateForDisplay,
    sortRightListByDateDsc,
    betweenData,
    formatLocalToUtc,
    isFormattedDate,
    formatStringToDate,
};

export default DateUtils;
