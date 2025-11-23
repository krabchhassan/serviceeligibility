/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import referentialApi from './ReferentialApi';
import DateUtils from '../utils/DateUtils';

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ref = referentialApi.all('v1');
const today = DateUtils.formatServerDate(DateUtils.todayMoment(), 'YYYY-MM-DD');
ref.getCodesItelis = ref.custom(`referentialDataWithoutPaging?code=code_optique_itelis&referenceDate=${today}`);

export { ref as default };
