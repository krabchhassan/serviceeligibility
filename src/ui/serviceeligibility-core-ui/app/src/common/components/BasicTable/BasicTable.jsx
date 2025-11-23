/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React from 'react';
import PropTypes from 'prop-types';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const getKey = (index) => {
    return index + 0;
};

const BasicTable = (props) => {
    const { headers, rowsData, id } = props;
    return (
        <table id={id} className="table table-striped table-sm table-bordered my-1">
            <thead>
                <tr>
                    {(headers || []).map((item, headersIndex) => (
                        <th key={`bt-header-${item}-${getKey(headersIndex)}`} scope="col">
                            {item}
                        </th>
                    ))}
                </tr>
            </thead>
            <tbody>
                {(rowsData || []).map((row, rowIndex) => (
                    <tr key={`bt-row-${row.key}-${getKey(rowIndex)}`}>
                        {(row.values || []).map((columnValue, index) => (
                            <td key={getKey(index)}>{columnValue}</td>
                        ))}
                    </tr>
                ))}
            </tbody>
        </table>
    );
};

BasicTable.propTypes = {
    id: PropTypes.string,
    /*
     *  list of value for the column headers of the table
     */
    headers: PropTypes.arrayOf(PropTypes.string),
    /* data to fill the table. Contain an array for each line to display having two properties:
     *   - the key of the line
     *   - the list of every columns values (called values)
     */
    rowsData: PropTypes.arrayOf(PropTypes.shape()),
};
BasicTable.defaultProps = {
    headers: [],
    rowsData: [],
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */

export default BasicTable;
