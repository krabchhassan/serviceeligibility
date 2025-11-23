/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { LoadingSpinner, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import ContractData from './ContractData';
import BenefData from './BenefData';
import DateUtils from '../../../../../../common/utils/DateUtils';
import Constants from '../../../../../../common/utils/Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class Contract extends Component {
    render() {
        const { contract, benefToDisplay, conventions, amcName, numerosAMCEchanges, status, isHTP } = this.props;
        if (!contract) {
            return <LoadingSpinner iconType="circle-o-notch" behavior="primary" />;
        }

        const today = DateUtils.formatServerDate(DateUtils.todayMoment(), Constants.YEARS_MONTH_DAY_FORMAT);
        const dates = [contract?.dateResiliation, benefToDisplay?.dateRadiation, today].filter(
            (date) => date !== null && date !== undefined,
        );
        const minEndDate = dates.reduce((minDate, currentDate) => {
            return currentDate < minDate ? currentDate : minDate;
        });
        const parsedMinDate = DateUtils.parseDateInFormat(minEndDate, Constants.DEFAULT_DATE_PICKER_FORMAT);

        return (
            <>
                <ContractData
                    contract={contract}
                    conventions={conventions}
                    amcName={amcName}
                    status={status}
                    numerosAMCEchanges={numerosAMCEchanges}
                    endContractDate={parsedMinDate}
                    isHTP={isHTP}
                />
                <BenefData benefToDisplay={benefToDisplay} endContractDate={parsedMinDate} />
            </>
        );
    }
}

Contract.propTypes = {
    contract: PropTypes.shape(),
    benefToDisplay: PropTypes.shape(),
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    isHTP: PropTypes.bool,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default Contract;
