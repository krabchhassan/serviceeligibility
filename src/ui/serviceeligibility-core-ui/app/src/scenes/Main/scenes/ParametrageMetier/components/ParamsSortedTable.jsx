/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import autobind from 'autobind-decorator';
import ReactTable from 'react-table';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class ParamsSortedTable extends Component {
    @autobind
    render() {
        const { t, rowsData, columns } = this.props;

        const pageSize = 10;
        const showPages = rowsData.length > pageSize;

        return (
            <ReactTable
                data={rowsData}
                defaultSorted={[
                    {
                        id: 'code',
                        desc: false,
                    },
                ]}
                columns={columns}
                minRows={pageSize}
                className="-striped -highlight"
                showPageSizeOptions={false}
                showPagination={showPages}
                defaultPageSize={pageSize}
                previousText={t('table.pagination.previous')}
                nextText={t('table.pagination.next')}
                loadingText={t('table.loading')}
                pageText={t('table.pagination.page')}
                ofText={t('table.pagination.of')}
                rowsText={t('table.pagination.rows')}
                noDataText={t('table.noData')}
            />
        );
    }
}

// Prop types
const propTypes = {
    t: PropTypes.func,
    rowsData: PropTypes.shape(),
    columns: PropTypes.shape(),
};

// Default props
const defaultProps = {};

// Add prop types
ParamsSortedTable.propTypes = propTypes;
// Add default props
ParamsSortedTable.defaultProps = defaultProps;

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParamsSortedTable;
