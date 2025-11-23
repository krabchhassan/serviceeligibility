/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import { withHabilitation, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import asyncComponentLoading from './asyncComponentLoading';
import Constants from './utils/Constants';
/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default makeAsyncWithHabilitation;

/* ************************************* */
/* ********  PRIVATE FUNCTIONS  ******** */
/* ************************************* */

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */
/**
 * Make component async loaded.
 * @param WrappedComponent
 * @return {React.ComponentClass<any>}
 */
function makeAsyncWithHabilitation(WrappedComponent, permissions, errorMsg = Constants.ACCES_DENIED_TRANSLATION_CODE) {
    return TranslationsSubscriber('errors')(
        withHabilitation(asyncComponentLoading(WrappedComponent), permissions, errorMsg),
    );
}
