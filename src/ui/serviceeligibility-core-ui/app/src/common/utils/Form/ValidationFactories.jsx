/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import moment from 'moment';
import isEmpty from 'lodash/isEmpty';
import pull from 'lodash/pull';
import noop from 'lodash/noop';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const requiredLabel = 'fields.required';
/**
 * Validator factory to be used when a value is required for a field.
 * A specific i18n error key, otherwise the default one will be used.
 * @param  {String} [i18nKey='fields.required'] An optional i18n key (default catalog is errors)
 * @return {Function} A "required" validator function
 */
const required =
    (i18nKey = requiredLabel) =>
    (value) =>
        value ? undefined : i18nKey;

const notEmptyArray =
    (i18nKey = requiredLabel) =>
    (value) =>
        value && value.length > 0 ? undefined : i18nKey;

/**
 * Validator factory to be used when at least a value is required for a multi-valued field.
 * A specific i18n error key, otherwise the default one will be used.
 * @param  {String} [i18nKey='fields.required'] An optional i18n key (default catalog is errors)
 * @return {Function} A "required" validator function
 */
const requiredMultiple =
    (i18nKey = requiredLabel) =>
    (value) =>
        isEmpty(value) ? i18nKey : undefined;

/**
 * Validator factory to be used when one field is required among a set of fields
 * @param  {Function} getRequiredValues Function that return the set of values to test.
 * The function receives one parameter which is an object containing all form values.
 * @param  {String} [i18nKey='fields.oneRequired'] An optional i18n key (default catalog is errors)
 * @param  {String} [mark='*'] Optional mark parameter for the i18n key.
 * @return {Function} A "one required" validator function
 */
const oneRequired =
    (getRequiredValues, mark = '*', i18nKey = 'fields.oneRequired') =>
    (value, allValues) =>
        getRequiredValues(allValues).every((v) => !v) ? [i18nKey, { mark }] : undefined;

/**
 * Strict length field validator factory.
 * @param  {Number} length The length to check
 * @param  {String} [i18nKey='fields.strictLength'] An optional i18n key (default catalog is errors)
 * @return {Function} A "strict length" validator function
 */
const strictLength =
    (length, i18nKey = 'fields.strictLength') =>
    (value) =>
        value && value.length !== length ? [i18nKey, { length }] : undefined;

const strictLengthWithoutSpace =
    (length, i18nKey = 'fields.strictLength') =>
    (value) =>
        value && value.replace(/\s/g, '').length !== length ? [i18nKey, { length }] : undefined;

/**
 * In between length field validator factory.
 * @param  {Number} min The min length to check
 * @param  {Number} max The max length to check
 * @param  {String} [i18nKey='fields.inBetweenLength'] An optional i18n key (default catalog is errors)
 * @return {Function} A "in between length" validator function
 */
const inBetweenLength =
    (min, max, i18nKey = 'fields.inBetweenLength') =>
    (value) =>
        value && (value.replace(/\s/g, '').length < min || value.replace(/\s/g, '').length > max)
            ? [i18nKey, { min, max }]
            : undefined;

/**
 * Max length field validator factory.
 * @param  {Number} max The max length to check
 * @param  {String} [i18nKey='fields.maxLength'] An optional i18n key (default catalog is errors)
 * @return {Function} A "max length" validator function
 */
const maxLength =
    (max, i18nKey = 'fields.maxLength') =>
    (value) =>
        value && value.length > max ? [i18nKey, { max }] : undefined;

const maxLengthWithoutSpace =
    (max, i18nKey = 'fields.maxLength') =>
    (value) =>
        value && value.replace(/\s/g, '').length > max ? [i18nKey, { max }] : undefined;

/**
 * Max value field validator factory.
 * @param  {Number} The max value to check
 * @param  {String} [i18nKey='fields.maxValue'] An optional i18n key (default catalog is errors)
 * @return {Function} A "Smaller than" validator function
 */
const maxValue =
    (max, i18nKey = 'fields.maxValue') =>
    (value) =>
        value && value > max ? [i18nKey, { max }] : undefined;

/**
 * Min value field validator factory.
 * @param  {Number} The min value to check inclusive
 * @param  {String} [i18nKey='fields.maxValue'] An optional i18n key (default catalog is errors)
 * @return {Function} A "Bigger or equal than" validator function
 */
const minValue =
    (min, i18nKey = 'fields.minValue') =>
    (value) =>
        value && value <= min ? [i18nKey, { min }] : undefined;

/**
 * Min included value field validator factory.
 * @param  {Number} The min value to check inclusive
 * @param  {String} [i18nKey='fields.maxValue'] An optional i18n key (default catalog is errors)
 * @return {Function} A "Bigger or equal than" validator function
 */
const minIncludedValue =
    (min, i18nKey = 'fields.minValue') =>
    (value) =>
        value && value < min ? [i18nKey, { min }] : undefined;

/**
 * In between value field validator factory.
 * @param  {Number} The min value to check inclusive
 * @param  {Number} The max value to check
 * @param  {String} [i18nKey='fields.maxValue'] An optional i18n key (default catalog is errors)
 * @return {Function} A "Bigger or equal than" validator function
 */
const inBetweenValue =
    (min, max, i18nKey = 'fields.inBetweenValue') =>
    (value) =>
        value && (value < min || value > max) ? [i18nKey, { min, max }] : undefined;

/**
 * Number validator factory.
 * @param  {String} [i18nKey='fields.number'] An optional i18n key (default catalog is errors)
 * @return {Function} A "number" validator function
 */
const isNumber =
    (i18nKey = 'fields.number') =>
    (value) =>
        value && Number.isNaN(Number(value)) ? i18nKey : undefined;

/**
 * Integer validator factory.
 * @param  {String} [i18nKey='fields.integer'] An optional i18n key (default catalog is errors)
 * @return {Function} A "integer" validator function
 */
const isInteger =
    (i18nKey = 'fields.integer') =>
    (value) =>
        value && Number(value) % 1 !== 0 ? i18nKey : undefined;

/**
 * Alphanumeric validator factory.
 * @param  {String} [i18nKey='fields.alpha'] An optional i18n key (default catalog is errors)
 * @return {Function} A "alphanumeric" validator function
 */
const ALPHA_RE = /^[a-zA-Z0-9]+$/;
const isAlpha =
    (i18nKey = 'fields.alpha') =>
    (value) =>
        value && !ALPHA_RE.test(value) ? i18nKey : undefined;

const ALPHA_SP_RE = /^[a-zA-Z0-9_:-]+$/;
const isAlphaSP =
    (i18nKey = 'fields.alphaSP') =>
    (value) =>
        value && !ALPHA_SP_RE.test(value) ? i18nKey : undefined;

/**
 * Url validator factory.
 * @param  {String} [i18nKey='fields.alpha'] An optional i18n key (default catalog is errors)
 * @return {Function} A "url" validator function
 */
// FIXME should use a common solution instead of this simple regexp (not fully valid)
const URL_RE = /^((https?:\/\/)[\w-]+(\.[\w-]+)+\.?(:\d+)?(\/\S*)?)/i;
const isUrl =
    (i18nKey = 'fields.url') =>
    (value) =>
        value && !URL_RE.test(value) ? i18nKey : undefined;

/**
 * Email validator factory.
 * @param  {String} [i18nKey='fields.email'] An optional i18n key (default catalog is errors)
 * @return {Function} A "email" validator function
 */
const EMAIL_RE = /\S+@\S+\.\S+/;
const isEmail =
    (i18nKey = 'fields.email') =>
    (value) =>
        value && !EMAIL_RE.test(String(value).toLowerCase()) ? i18nKey : undefined;

/**
 * Date validator factory.
 * @param  {String} [i18nKey='fields.date'] An optional i18n key (default catalog is errors)
 * @return {Function} A "date" validator function
 */
const isDate =
    (i18nKey = 'fields.date') =>
    (value) => {
        const isValidDate = value && moment.isMoment(value) && value.isValid();
        return value && !isValidDate ? i18nKey : undefined;
    };

const isDateStringDDMMYYYY =
    (i18nKey = 'fields.dateFormat') =>
    (value) => {
        const matcher = /^(\d{2})\/(\d{2})\/(\d{4})$/;
        return value && !matcher.test(value) ? i18nKey : undefined;
    };

/**
 * Validator factory to be used to compare a date
 * @param  {String} compareMethodName The function name to compare the dates (isBefore, isAfter, isSameOfBefore)
 * @param  {Function} getThreshold Function that must return the threshold date to be compared against.
 * The function receives one parameter which is an object containing all form values.
 * @param  {String} [i18nKey='fields.date'] An optional i18n key (default catalog is errors)
 * @param  {String} [errorOnInvalidThreshold='*'] Optional mark parameter for the default i18n key.
 * @return {Function} A "dateComparator" validator function
 */
const dateComparator =
    (compareMethodName, getThreshold, i18nKey = 'fields.date', errorOnInvalidThreshold = false) =>
    (date, allValues) => {
        const threshold = getThreshold(allValues);
        const isValidThreshold = threshold && moment.isMoment(threshold) && threshold.isValid();

        if (!isValidThreshold && errorOnInvalidThreshold) {
            return i18nKey;
        }

        if (isValidThreshold && date && moment.isMoment(date) && !date[compareMethodName](threshold)) {
            return i18nKey;
        }

        return undefined;
    };

/**
 * Validator factory generating a helper for async validation
 * @param  {Object} asyncFieldValidators The async validation functions for each field requiring async validation.
 * Keys are the field names. Each function must return a Promise that will resolve if the validation is passed,
 * or will reject with an error key.
 * @return {Object} A helper containing the following properties:
 * - asyncValidate: The global validation function
 * - fields: Array of field names that are concerned by validation function
 */
const asyncValidator = (asyncFieldValidators) => {
    // Creating a local pool that manages the async validation currenlty running
    const localAsyncValidationPool = (() => {
        const pool = [];
        // unregister from pool function
        const unregister = (p) => () => pull(pool, p);

        return {
            registerAsyncValidation(p) {
                // add to pool and remove once resolved
                pool.push(p);
                p.then(unregister(p), unregister(p)).catch((error) => {
                    console.error(error);
                });
            },
            // important: promise must return a falsy value, otherwise redux-form validation still fails.
            // Hence the use of 'noop'
            waitForCompletion: () =>
                Promise.all(pool)
                    .then(noop)
                    .catch((error) => {
                        console.error(error);
                    }),
        };
    })();

    // previous errors closure
    let previousErrors = {};

    // Core async validate function.
    // - call the validation function of the blurred field
    // - ensure that current async validation are finished before allowing any submit attempt
    const asyncValidate = (values, dispatch, props, blurredFieldName) => {
        // Case of a form submission: blurredFieldName is undefined
        if (!blurredFieldName) {
            // ensure all async validation are completed
            return localAsyncValidationPool.waitForCompletion();
        }

        // update previous errors
        previousErrors = { ...(props.asyncErrors || {}) };

        const blurredFieldValue = values[blurredFieldName];
        if (!blurredFieldValue || blurredFieldValue.length === 0) {
            delete previousErrors[blurredFieldName];
            return Object.keys(previousErrors).length > 0 ? Promise.reject(previousErrors) : Promise.resolve();
        }

        const asyncValidationPromise = asyncFieldValidators[blurredFieldName](
            props,
            blurredFieldName,
            blurredFieldValue,
        )
            .then(
                // success cb
                () => {
                    delete previousErrors[blurredFieldName];
                    return Object.keys(previousErrors).length > 0 ? Promise.reject(previousErrors) : undefined;
                },
                // error cb
                (error) => {
                    previousErrors = {
                        ...previousErrors,
                        [blurredFieldName]: error,
                    };
                    return Promise.reject(previousErrors);
                },
            )
            .catch((error) => {
                console.error(error);
            });

        // promise should be managed by the validation pool
        localAsyncValidationPool.registerAsyncValidation(asyncValidationPromise);

        return asyncValidationPromise;
    };

    return {
        asyncValidate,
        fields: Object.keys(asyncFieldValidators),
    };
};

/**
 * Min included value field validator factory.
 * @param  {Array} array that should not include the value
 * @param  {String} [i18nKey='fields.dontInclude'] An optional i18n key (default catalog is errors)
 * @return {Function} A "Don't include" validator function
 */
const dontIncludeValue =
    (array, i18nKey = 'fields.dontInclude') =>
    (value) => {
        return value && (array || []).includes(value) ? i18nKey : undefined;
    };

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ValidationFactories = {
    required,
    requiredMultiple,
    oneRequired,
    strictLength,
    strictLengthWithoutSpace,
    inBetweenLength,
    maxLength,
    notEmptyArray,
    maxValue,
    maxLengthWithoutSpace,
    minValue,
    minIncludedValue,
    inBetweenValue,
    isNumber,
    isAlpha,
    isAlphaSP,
    isUrl,
    isEmail,
    isDate,
    isDateStringDDMMYYYY,
    dateComparator,
    asyncValidator,
    dontIncludeValue,
    isInteger,
};

export default ValidationFactories;
