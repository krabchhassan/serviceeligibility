/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import 'react-table/react-table.css';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import BasicTable from '../../../../../../../common/components/BasicTable/BasicTable';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
const checkBox = (val) => (
    <div className="middle-alignment">
        {' '}
        {val === 1 ? (
            <CgIcon name="checked" className="background-variant-2" />
        ) : (
            <CgIcon name="unchecked" className="background-variant-2" />
        )}
    </div>
);

@TranslationsSubscriber(['eligibility', 'breadcrumb', 'common'], { wait: true })
class ParamContextSubComponent extends Component {
    render() {
        const { data: parentData, t } = this.props;
        const { codes } = parentData || {};
        const rowsData = ((parentData || {}).data || []).map((item) => ({
            values: [codes[item.name].col || item.name, codes[item.name].label || '-', checkBox(item.active)],
        }));

        const columns = [
            t('parameters.reference'),
            t('parameters.label'),
            <div className="middle-alignment">{t('parameters.control')}</div>,
        ];
        return <BasicTable headers={columns} rowsData={rowsData} />;
    }
}

ParamContextSubComponent.propTypes = {
    t: PropTypes.func,
    data: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParamContextSubComponent;
