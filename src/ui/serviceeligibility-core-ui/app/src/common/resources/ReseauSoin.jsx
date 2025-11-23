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
ref.getReseauxSoin = ref.custom(`referentialDataWithoutPaging?code=reseaux&referenceDate=${today}`);

export { ref as default };
