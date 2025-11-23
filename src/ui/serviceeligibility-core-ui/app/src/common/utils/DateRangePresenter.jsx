import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import PropTypes from 'prop-types';
import React, { Fragment } from 'react';

const DateRangePresenter = ({ start, end }) => (
    <Fragment>
        <div className="d-flex justify-item-center align-items-center pr-2">
            <CgIcon name="calendar" className="text-success" />
            <CgIcon name="caret-right" className="mx-1" />
            <div>{start || '-'}</div>&nbsp;
            <CgIcon name="calendar" className="text-success" />
            <CgIcon name="caret-left" className="mx-1" />
            <div>{end || '-'}</div>
        </div>
    </Fragment>
);
DateRangePresenter.propTypes = {
    start: PropTypes.string,
    end: PropTypes.string,
};

export default DateRangePresenter;
