import React from 'react';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import { Tooltip } from '@beyond-framework/common-uitoolkit-beyond';
import StringUtils from '../../../../../../common/utils/StringUtils';

function getMappedForComboboxList(list) {
    const objectsList = Object.values(list || [])
        .map((domain) => domain.value)
        .map((item) => ({
            label: `${item.code} - ${item.label}`,
            value: item.code,
        }));

    return objectsList;
}

function getMappedElementsForComboboxList(list) {
    const objectsList = Object.values(list || []).map((item) => ({
        label: item.label,
        value: item.code,
    }));

    return objectsList;
}

function getMappedForComboboxListWithLibelle(list) {
    const objectsList = Object.values(list || []).map((item) => ({
        label: `${item.code} - ${item.libelle}`,
        value: item.code,
    }));

    return objectsList;
}

function getElementLabel(itemToTranscode, list) {
    if (itemToTranscode === 'Sans réseau de soin') {
        return itemToTranscode;
    }
    const objectsList = Object.values(list || []).map((element) => element.value);
    const item = Object.values(objectsList || []).find((object) => itemToTranscode === (object || {}).code);
    return item ? `${(item || {}).code} (${(item || {}).label})` : '-';
}

function getElementLabelForEdit(itemToTranscode, list) {
    const item = Object.values(list || []).find((object) => itemToTranscode === (object || {}).value);
    return item ? (item || {}).label : '-';
}

function getConventionLabel(itemToTranscode, list) {
    const item = Object.values(list || []).find((object) => itemToTranscode === (object || {}).code);
    return (item || {}).label;
}

function getCodeRenvoiLabel(itemToTranscode, list) {
    const item = Object.values(list || []).find((object) => itemToTranscode === (object || {}).code);
    return item ? `${(item || {}).code} (${(item || {}).libelle})` : '-';
}

function getCodeRenvoiLabelEdit(itemToTranscode, list) {
    const item = Object.values(list || []).find((object) => itemToTranscode === (object || {}).code);
    return item ? `${(item || {}).code} - ${(item || {}).libelle}` : '-';
}

function getTooltip(key, props) {
    return (
        <Tooltip placement="right" target={`${key}-${props.row.index}`}>
            {props.value}
        </Tooltip>
    );
}

function generateCommonColumn(key, width, t) {
    return {
        Header: StringUtils.splitIntoDivs(t(`declarant.parametrageAMC.${key}`), false),
        accessor: key,
        id: key,
        width,
        disableSortBy: true,
        key,
        Cell: (props) => (
            <span>
                <span id={`${key}-${props.row.index}`}>{props.value}</span>
                {key === 'codeRenvoi' ? getTooltip(key, props) : null}
            </span>
        ),
    };
}

function generateBooleanCommonColumn(key, width, t) {
    return {
        Header: StringUtils.splitIntoDivs(t(`declarant.parametrageAMC.${key}`), false),
        accessor: key,
        id: key,
        width,
        disableSortBy: true,
        key,
        Cell: (props) => {
            return (
                <span>
                    <span id={`${key}-${props.row.index}`}>
                        <CgIcon name={props.value ? 'validate' : 'cancel'} />
                    </span>
                </span>
            );
        },
    };
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ParametrageAMCUtils = {
    generateCommonColumn,
    generateBooleanCommonColumn,
    getMappedForComboboxList,
    getMappedElementsForComboboxList,
    getMappedForComboboxListWithLibelle,
    getElementLabel,
    getConventionLabel,
    getCodeRenvoiLabel,
    getElementLabelForEdit,
    getCodeRenvoiLabelEdit,
};
export default ParametrageAMCUtils;
