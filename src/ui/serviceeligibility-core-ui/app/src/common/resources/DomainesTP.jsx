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
ref.getDomainesTP = ref.custom(`referentialDataWithoutPaging?code=domaines_tp&referenceDate=${today}`);

export { ref as default };
