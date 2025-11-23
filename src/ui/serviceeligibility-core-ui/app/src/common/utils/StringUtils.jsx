/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import camelCase from 'lodash/camelCase';
import React from 'react';

/* ************************************* */
/* ********      PRIVATE        ******** */
/* ************************************* */
// regex matching apostrophe characters.
const RE_APOS = /['\u2019]/g;

/* ************************************* */
/* ********   PUBLIC FUNCTIONS  ******** */
/* ************************************* */

/**
 * Normalize given string to lowercase, without special char, trimmed, and converted to camelcase.
 * @param label
 * @returns String
 */
const normalize = (label) => {
    if (!label) {
        return '';
    }
    return camelCase(label.replace(RE_APOS, ' ').toLowerCase());
};

const formatNumber = (val) => {
    if (val === undefined || val === null) {
        return '';
    }
    return val.toFixed(2).replace(',', '.');
};

const splitIntoDivs = (title) => title.split('_').map((item) => <div>{item}</div>);
const splitIntoDivsArrayJoin = (title) => title.split(',').map((item) => <div>{item}</div>);

const renderLongText = (text, separator, length) => {
    const items = (text || '').split(separator);
    const lines = [];
    let line = '';
    const itemsNb = items.length;
    let counter = 0;
    items.forEach((item) => {
        counter += 1;
        if (line.length + item.length < length) {
            line = line.length === 0 ? line.concat('', item) : line.concat(`${separator} `, item);
            if (counter === itemsNb) {
                lines.push(line);
            }
        } else {
            lines.push(line);
            line = item;
            if (counter === itemsNb) {
                lines.push(line);
            }
        }
    });
    return (
        <div>
            {(lines || []).map((item) => (
                <div className="cgd-text-overflow" key={lines.indexOf(item)}>
                    {item}
                </div>
            ))}
        </div>
    );
};

function getUrlParams(search) {
    const hashes = search.slice(search.indexOf('?') + 1).split('&');
    return hashes.reduce((params, hash) => {
        const [key, val] = hash.split('=');
        return Object.assign(params, { [key]: decodeURIComponent(val) });
    }, {});
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const StringUtils = {
    normalize,
    formatNumber,
    splitIntoDivs,
    renderLongText,
    getUrlParams,
    splitIntoDivsArrayJoin,
};

export default StringUtils;
