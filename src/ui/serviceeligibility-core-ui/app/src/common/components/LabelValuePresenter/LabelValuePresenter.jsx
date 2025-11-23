/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React from 'react';
import PropTypes from 'prop-types';
import { Row, Col, Tooltip, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import classnames from 'classnames';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const LabelValuePresenter = (props) => {
    const {
        t,
        id,
        inline,
        className,
        labelClassName,
        valueClassName,
        tooltip,
        label,
        value,
        defaultValue,
        cgdComment,
        labelTextRight,
        labelMargin,
        noWrap,
        noColon,
        overflowBelow,
        labelPortion,
    } = props;

    const rowCls = classnames('align-items-baseline', className);
    const labelCls = classnames({ 'cgd-comment': cgdComment }, labelMargin, labelClassName);
    const valueCls = classnames(valueClassName);
    let firstColCls = classnames({ 'text-right': labelTextRight });
    const inlineCls = classnames(
        'd-inline-flex',
        { 'text-nowrap': noWrap },
        { 'align-items-baseline': !overflowBelow },
        { 'flex-wrap': overflowBelow },
        className,
    );

    if (id === 'codeCircuit') {
        firstColCls = classnames({ 'text-right': labelTextRight }, 'mb-4');
    }

    if (!value && !defaultValue) {
        return null;
    }

    let tooltipElement;
    if (tooltip) {
        if (!id) {
            throw new Error('id is mandatory when using tooltip');
        }

        const tooltipId = `${id}_tooltip`;
        tooltipElement = [
            <i key="icon" className="fa fa-question-circle ml-2" id={tooltipId} aria-hidden="true" />,
            <Tooltip key="tooltip" placement="bottom" target={tooltipId}>
                {tooltip}
            </Tooltip>,
        ];
    }
    return inline ? (
        <div id={id} className={inlineCls}>
            {label && (
                <div className={labelCls}>
                    {label}
                    {!noColon && t('colon')}
                </div>
            )}
            <div className={valueCls}>{value || defaultValue}</div>
            {tooltipElement}
        </div>
    ) : (
        <Row id={id} noGutters className={rowCls}>
            <Col xs={labelPortion} className={firstColCls}>
                {label && (
                    <div className={labelCls}>
                        {label}
                        {!noColon && t('colon')}
                    </div>
                )}
            </Col>
            <Col xs={12 - labelPortion}>
                <div className={valueCls}>{value || defaultValue}</div>
                {tooltipElement}
            </Col>
        </Row>
    );
};
LabelValuePresenter.propTypes = {
    t: PropTypes.func,
    id: PropTypes.string,
    className: PropTypes.string,
    labelClassName: PropTypes.string,
    valueClassName: PropTypes.string,
    label: PropTypes.node,
    value: PropTypes.node,
    inline: PropTypes.bool,
    defaultValue: PropTypes.node,
    cgdComment: PropTypes.bool,
    labelTextRight: PropTypes.bool,
    labelMargin: PropTypes.string,
    noWrap: PropTypes.bool,
    tooltip: PropTypes.node,
    noColon: PropTypes.bool,
    overflowBelow: PropTypes.bool,
    labelPortion: PropTypes.number,
};
LabelValuePresenter.defaultProps = {
    inline: false,
    cgdComment: true,
    labelTextRight: true,
    labelMargin: 'mr-2',
    noWrap: false,
    noColon: false,
    overflowBelow: false,
    labelPortion: 6,
    defaultValue: '-',
};

const commonTranslator = TranslationsSubscriber('common');

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */

export default commonTranslator(LabelValuePresenter);
