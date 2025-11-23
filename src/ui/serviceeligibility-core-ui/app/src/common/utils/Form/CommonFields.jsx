/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React from 'react';
import PropTypes from 'prop-types';
import {
    Col,
    FormFeedback,
    FormGroup,
    InPlaceCombobox,
    InPlaceDatePicker,
    InPlaceInput,
    Input,
    InputGroup,
    InputGroupAddon,
    Label,
    Row,
    Tooltip,
    TranslationsSubscriber,
} from '@beyond-framework/common-uitoolkit-beyond';
import moment from 'moment';
import classnames from 'classnames';
import isNil from 'lodash/isNil';
import isObject from 'lodash/isObject';
import isError from 'lodash/isError';
import DateUtils from '../DateUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

const renderLabelTooltipInlined = (label, tooltip) => {
    let element;

    if (label || tooltip) {
        element = (
            <div key="label-tooltip" className="d-flex justify-content-start align-items-baseline">
                {label}
                {tooltip && <div className="ml-auto">{tooltip}</div>}
            </div>
        );
    }

    return element;
};

function buildFeedbackElement(feedbackElement, contentElement, labelPortion) {
    if (feedbackElement) {
        contentElement.push(
            <Row key="second-line" noGutters>
                <Col xs={12 - labelPortion} className="ml-auto">
                    {feedbackElement}
                </Col>
            </Row>,
        );
    }
}

function getContentElement(
    inlineAlignItems,
    labelElement,
    labelPortion,
    firstColCls,
    labelPositionAfter,
    children,
    tooltip,
    addon,
) {
    return [
        <Row key="first-line" noGutters className={`align-items-${inlineAlignItems}`}>
            {labelElement && (
                <Col xs={labelPortion} className={firstColCls}>
                    {!labelPositionAfter && labelElement}
                </Col>
            )}
            <Col xs={12 - labelPortion}>
                <div className="d-flex align-items-baseline">
                    {children}
                    {labelPositionAfter && labelElement}
                    {tooltip}
                    {addon}
                </div>
            </Col>
        </Row>,
    ];
}

function getLabel(id, cls, labelProps, label, inline, noColon, t) {
    return (
        <Label key="label" for={id} className={`${cls} text-colored-mix-0`} {...labelProps}>
            {label}
            {inline && !noColon && t('colon')}
        </Label>
    );
}

function getFormFeedback(cls, feedbackProps, feedback) {
    return (
        <FormFeedback key="feedback" className={cls} {...feedbackProps}>
            {feedback}
        </FormFeedback>
    );
}

const FormGroupFieldLayout = TranslationsSubscriber('common')((props) => {
    const {
        t,
        id,
        inline,
        inlineAlignItems,
        edit,
        tooltip,
        children,
        label,
        feedback,
        addon,
        labelProps,
        cgdComment,
        labelTextRight,
        labelPositionAfter,
        noColon,
        noLabelMargin,
        feedbackProps,
        formGroupProps,
        labelPortion,
    } = props;

    let labelElement;
    let feedbackElement;
    let contentElement;

    if (label) {
        // isNil => true if null or undefined
        const withCgdComment = isNil(cgdComment) ? inline : cgdComment;
        const classN = classnames(
            {
                'mb-0': inline,
                'cgd-comment': withCgdComment,
            },
            !noLabelMargin && inline && (labelPositionAfter ? 'ml-2' : 'mr-2'),
        );

        labelElement = getLabel(id, classN, labelProps, label, inline, noColon, t);
    }

    if (feedback) {
        const classN2 = classnames(inline ? 'mt-0' : undefined);
        feedbackElement = getFormFeedback(classN2, feedbackProps, feedback);
    }

    if (inline) {
        const withLabelTextRight = isNil(labelTextRight) ? inline : labelTextRight;
        const firstColCls = classnames({ 'text-right': withLabelTextRight });

        contentElement = getContentElement(
            inlineAlignItems,
            labelElement,
            labelPortion,
            firstColCls,
            labelPositionAfter,
            children,
            tooltip,
            addon,
        );

        buildFeedbackElement(feedbackElement, contentElement, labelPortion);
    } else {
        // children opaque data structure with keys assigned to each child
        contentElement = React.Children.toArray(children);

        if (addon) {
            contentElement = [
                <div key="content" className="d-flex align-items-center">
                    {contentElement}
                    {/* add the addon with a key if not already existing */}
                    {addon.key ? addon : React.cloneElement(addon, { key: 'field-addon' })}
                </div>,
            ];
        }

        labelElement = tooltip ? renderLabelTooltipInlined(labelElement, tooltip) : labelElement;
        // insert label before or after main field (ie. children)
        contentElement[labelPositionAfter ? 'push' : 'unshift'](labelElement);

        contentElement.push(feedbackElement);
    }

    const mbCls = inline && (edit ? 'mb-2' : 'mb-0');
    const cls = classnames(mbCls, formGroupProps.className);

    return (
        <FormGroup {...formGroupProps} className={cls}>
            {contentElement}
        </FormGroup>
    );
});
FormGroupFieldLayout.propTypes = {
    t: PropTypes.func,
    id: PropTypes.string,
    inline: PropTypes.bool,
    inlineAlignItems: PropTypes.string,
    edit: PropTypes.bool,
    label: PropTypes.node,
    cgdComment: PropTypes.bool,
    labelTextRight: PropTypes.bool,
    labelPositionAfter: PropTypes.bool,
    noLabelMargin: PropTypes.bool,
    noColon: PropTypes.bool,
    tooltip: PropTypes.node,
    labelProps: PropTypes.shape({
        /* any props : will be directly passed to the underlying Label element */
    }),
    feedbackProps: PropTypes.shape({
        /* any props : will be directly passed to the underlying Feedback element */
    }),
    formGroupProps: PropTypes.shape({
        /* any props : will be directly passed to the underlying FormGroup element */
    }),
    feedback: PropTypes.node,
    addon: PropTypes.node,
    children: PropTypes.node,
    labelPortion: PropTypes.number,
};
FormGroupFieldLayout.defaultProps = {
    labelProps: {},
    feedbackProps: {},
    formGroupProps: {},
    inline: false,
    inlineAlignItems: 'baseline',
    noColon: false,
    noLabelMargin: false,
    labelPositionAfter: false,
    labelPortion: 4,
};

const BasicInput = (props) => {
    const {
        id,
        input,
        type,
        maxLength,
        placeholder,
        disabled,
        rows,
        behavior,
        formGroupClassName,
        edit,
        valueDisplayed,
        inputRef,
        className,
    } = props;

    const additionalLayoutProps = {};

    const formGroupProps = {
        behavior,
        className: formGroupClassName,
    };
    const labelProps = {};

    // checkbox specific
    if (type === 'checkbox') {
        formGroupProps.check = true;
        formGroupProps.disabled = disabled;
        labelProps.check = true;

        if (edit) {
            additionalLayoutProps.labelPositionAfter = true;
            additionalLayoutProps.labelTextRight = false;
            additionalLayoutProps.noColon = true;
            additionalLayoutProps.noLabelMargin = true;
        }
    }

    return (
        <FormGroupFieldLayout
            formGroupProps={formGroupProps}
            labelProps={labelProps}
            {...props}
            {...additionalLayoutProps}
        >
            <InPlaceInput
                className={className}
                id={id}
                type={type}
                behavior={behavior}
                {...input}
                maxLength={maxLength}
                placeholder={placeholder}
                disabled={disabled}
                rows={rows}
                edit={edit}
                valueDisplayed={valueDisplayed}
                getRef={inputRef}
            />
        </FormGroupFieldLayout>
    );
};
BasicInput.propTypes = {
    id: PropTypes.string,
    input: PropTypes.shape({}), // input object from redux-form
    type: PropTypes.string,
    maxLength: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    placeholder: PropTypes.string,
    behavior: PropTypes.string,
    formGroupClassName: PropTypes.string,
    className: PropTypes.string,
    edit: PropTypes.bool,
    valueDisplayed: PropTypes.node,
    disabled: PropTypes.bool,
    rows: PropTypes.number,
    inputRef: PropTypes.func,
};

const BasicInputTel = (props) => {
    const {
        id,
        input,
        type,
        maxLength,
        placeholder,
        disabled,
        rows,
        behavior,
        formGroupClassName,
        edit,
        valueDisplayed,
        inputRef,
        className,
    } = props;

    const additionalLayoutProps = {};

    const formGroupProps = {
        behavior,
        className: formGroupClassName,
    };
    const labelProps = {};

    // checkbox specific
    if (type === 'checkbox') {
        formGroupProps.check = true;
        formGroupProps.disabled = disabled;
        labelProps.check = true;

        if (edit) {
            additionalLayoutProps.labelPositionAfter = true;
            additionalLayoutProps.labelTextRight = false;
            additionalLayoutProps.noColon = true;
            additionalLayoutProps.noLabelMargin = true;
        }
    }

    return (
        <FormGroupFieldLayout
            formGroupProps={formGroupProps}
            labelProps={labelProps}
            {...props}
            {...additionalLayoutProps}
        >
            <InputGroup>
                <InputGroupAddon prepend>+33</InputGroupAddon>
                <Input
                    id={id}
                    type="text"
                    placeholder={placeholder}
                    className={className}
                    behavior={behavior}
                    {...input}
                    maxLength={maxLength}
                    disabled={disabled}
                    rows={rows}
                    edit={edit}
                    valueDisplayed={valueDisplayed}
                    getRef={inputRef}
                />
            </InputGroup>
        </FormGroupFieldLayout>
    );
};
BasicInputTel.propTypes = {
    id: PropTypes.string,
    input: PropTypes.shape({}), // input object from redux-form
    type: PropTypes.string,
    maxLength: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    placeholder: PropTypes.string,
    behavior: PropTypes.string,
    formGroupClassName: PropTypes.string,
    className: PropTypes.string,
    edit: PropTypes.bool,
    valueDisplayed: PropTypes.node,
    disabled: PropTypes.bool,
    rows: PropTypes.number,
    inputRef: PropTypes.func,
};

const BasicRadio = (props) => {
    const { id, input, disabled, formGroupClassName, edit, valueDisplayed, inputRef } = props;
    const finalId = `${id}-${input.value}`;

    const formGroupProps = {
        disabled,
        check: true,
        className: formGroupClassName,
    };

    const labelProps = { check: true };

    return (
        <FormGroupFieldLayout
            formGroupProps={formGroupProps}
            labelProps={labelProps}
            labelPositionAfter
            labelTextRight={false}
            noColon
            {...props}
            id={finalId}
        >
            <InPlaceInput
                type="radio"
                id={finalId}
                {...input}
                disabled={disabled}
                edit={edit}
                valueDisplayed={valueDisplayed}
                getRef={inputRef}
            />
        </FormGroupFieldLayout>
    );
};
BasicRadio.propTypes = {
    id: PropTypes.string,
    input: PropTypes.arrayOf(PropTypes.shape({})), // input object from redux-form
    formGroupClassName: PropTypes.string,
    edit: PropTypes.bool,
    valueDisplayed: PropTypes.node,
    disabled: PropTypes.bool,
    inputRef: PropTypes.func,
};

const BasicCombobox = (props) => {
    const { id, input, behavior, formGroupClassName, edit, valueDisplayed, labelKey, ...passedProps } = props;

    const formGroupProps = {
        behavior,
        className: formGroupClassName,
    };

    // workaround so that the combobox fill all the remaining width
    const wrapperStyle = { flexGrow: 1 };

    let additionalProps = {};
    if (!valueDisplayed && !edit && isObject(input.value)) {
        // improvement: could warn in this case, this is only a workaround
        additionalProps = {
            valueDisplayed: input.value[labelKey || 'label'],
        };
    }

    return (
        <FormGroupFieldLayout formGroupProps={formGroupProps} {...props}>
            <div style={wrapperStyle}>
                <InPlaceCombobox
                    id={id}
                    {...input}
                    onBlur={() => input.onBlur(input.value)}
                    searchable={false}
                    clearable={false}
                    placeholder=""
                    edit={edit}
                    valueDisplayed={valueDisplayed}
                    labelKey={labelKey}
                    behavior={behavior}
                    {...passedProps}
                    {...additionalProps}
                />
            </div>
        </FormGroupFieldLayout>
    );
};
BasicCombobox.propTypes = {
    id: PropTypes.string,
    input: PropTypes.arrayOf(PropTypes.shape({})), // input object from redux-form
    behavior: PropTypes.string,
    formGroupClassName: PropTypes.string,
    edit: PropTypes.bool,
    valueDisplayed: PropTypes.node,
    labelKey: PropTypes.string,
    inlineAlignItems: PropTypes.string,
};
BasicCombobox.defaultProps = {
    inlineAlignItems: 'center',
};

const BasicDatePicker = (props) => {
    const {
        id,
        input,
        behavior,
        minDate: min,
        maxDate: max,
        edit,
        valueDisplayed,
        formGroupClassName,
        ...otherProps
    } = props;

    const passedProps = { ...otherProps };
    delete passedProps.inline;

    const formGroupProps = {
        behavior,
        className: formGroupClassName,
    };

    const value = input.value || null;
    let additionalProps;

    if (edit) {
        additionalProps = {
            minDate: moment.isMoment(min) ? min : undefined,
            maxDate: moment.isMoment(max) ? max : undefined,
        };
    }
    let finalValueDisplayed = valueDisplayed;
    if (!finalValueDisplayed) {
        if (typeof value === 'string') {
            finalValueDisplayed = value;
        } else {
            finalValueDisplayed = DateUtils.formatDisplayDate(value);
        }
    }

    additionalProps = { ...additionalProps, valueDisplayed: finalValueDisplayed };

    return (
        <FormGroupFieldLayout formGroupProps={formGroupProps} {...props}>
            <div>
                <InPlaceDatePicker
                    id={id}
                    behavior={behavior}
                    {...input}
                    value={value}
                    onBlur={() => input.onBlur(value)}
                    edit={edit}
                    {...passedProps}
                    {...additionalProps}
                />
            </div>
        </FormGroupFieldLayout>
    );
};
BasicDatePicker.propTypes = {
    id: PropTypes.string,
    input: PropTypes.arrayOf(PropTypes.shape({})), // input object from redux-form
    behavior: PropTypes.string,
    formGroupClassName: PropTypes.string,
    minDate: PropTypes.oneOfType([PropTypes.string, DateUtils.propTypesMoment]),
    maxDate: PropTypes.oneOfType([PropTypes.string, DateUtils.propTypesMoment]),
    edit: PropTypes.bool,
    valueDisplayed: PropTypes.node,
    inlineAlignItems: PropTypes.string,
};

BasicDatePicker.defaultProps = {
    inlineAlignItems: 'center',
};

const MultiCombobox = (props) => {
    const { id, input, behavior, formGroupClassName, edit, valueDisplayed, labelKey, ...passedProps } = props;

    const formGroupProps = {
        behavior,
        className: formGroupClassName,
    };

    // workaround so that the combobox fill all the remaining width
    const wrapperStyle = { flexGrow: 1 };

    let additionalProps = {};
    if (!valueDisplayed && !edit && isObject(input.value)) {
        // improvement: could warn in this case, this is only a workaround
        additionalProps = {
            valueDisplayed: input.value[labelKey || 'label'],
        };
    }

    return (
        <FormGroupFieldLayout formGroupProps={formGroupProps} {...props}>
            <div style={wrapperStyle}>
                <InPlaceCombobox
                    id={id}
                    {...input}
                    onBlur={() => input.onBlur(input.value)}
                    searchable={false}
                    clearable={false}
                    placeholder=""
                    edit={edit}
                    valueDisplayed={valueDisplayed}
                    labelKey={labelKey}
                    behavior={behavior}
                    multi
                    {...passedProps}
                    {...additionalProps}
                />
            </div>
        </FormGroupFieldLayout>
    );
};
MultiCombobox.propTypes = {
    id: PropTypes.string,
    input: PropTypes.arrayOf(PropTypes.shape({})), // input object from redux-form
    behavior: PropTypes.string,
    formGroupClassName: PropTypes.string,
    edit: PropTypes.bool,
    valueDisplayed: PropTypes.node,
    labelKey: PropTypes.string,
    inlineAlignItems: PropTypes.string,
};
MultiCombobox.defaultProps = {
    inlineAlignItems: 'center',
};
// HoC to handle standard fields features
const withFieldFeatures = (BaseComponent) => {
    const WithFieldFeatures = (props, context) => {
        const {
            tooltip,
            tooltipPlacement = 'top',
            edit,
            showRequired,
            input,
            meta: { form },
        } = props;
        const { formIdPrefix } = context;
        const id = `${formIdPrefix || form}-${input.name}`;
        const tooltipId = `${id}_tooltip`;

        let tooltipElement;
        if (tooltip) {
            tooltipElement = (
                <div className="ml-2 text-colored-mix-0">
                    <i className="fa fa-question-circle" id={tooltipId} />
                    <Tooltip placement={tooltipPlacement} target={tooltipId}>
                        {tooltip}
                    </Tooltip>
                </div>
            );
        }

        return <BaseComponent id={id} {...props} tooltip={tooltipElement} showRequired={edit ? showRequired : false} />;
    };

    WithFieldFeatures.displayName = `WithFieldFeatures(${getDisplayName(BaseComponent)})`;

    WithFieldFeatures.contextTypes = {
        formIdPrefix: PropTypes.string,
    };
    WithFieldFeatures.propTypes = {
        tooltip: PropTypes.node,
        tooltipPlacement: PropTypes.string,
        edit: PropTypes.bool,
        showRequired: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
        input: PropTypes.arrayOf(PropTypes.shape({})), // input object from redux-form
        disabled: PropTypes.bool,
        meta: PropTypes.shape({
            form: PropTypes.string.isRequired,
        }),
    };
    WithFieldFeatures.defaultProps = {
        edit: true,
        disabled: false,
    };

    return WithFieldFeatures;
};

/**
 * Format an i18n error.
 * @param  {Function} t The translate function
 * @param  {String/String[]/Error} errorArgs An i18n key (default catalog is errors),
 * or an array where first argument is the error code and the rest is optional error message parameters.
 * First error argument can also be a real error object with error message being the i18n error key.
 * @return {String} The formatted error.
 */
const formatError = (t, error) => {
    const errorArray = !Array.isArray(error) ? [error] : error;
    const errorKey = errorArray[0];
    errorArray[0] = isError(errorKey) ? errorKey.message : errorKey;
    return t(...errorArray);
};

// HoC to enhance a form field with valuable validation information.
const withValidation = (BaseComponent) => {
    const WithValidation = (props) => {
        const {
            t,
            label,
            showRequired,
            withFeedback,
            showFirstValidation,
            forceError,
            meta: { touched, error, warning },
        } = props;

        let behavior;
        let feedback;
        if ((withFeedback && touched) || showFirstValidation || forceError) {
            if (error || forceError) {
                behavior = 'danger';
                feedback = formatError(t, error);
            } else if (warning) {
                behavior = 'warning';
                feedback = formatError(t, warning);
            }
        }

        let formattedLabel;
        if (label && showRequired) {
            formattedLabel = [
                ...React.Children.toArray(label),
                '\u00a0', // Unicode non-breaking space character
                showRequired === true ? '*' : showRequired,
            ];
        }

        return <BaseComponent behavior={behavior} feedback={feedback} {...props} label={formattedLabel || label} />;
    };

    WithValidation.displayName = `WithValidation(${getDisplayName(BaseComponent)})`;

    WithValidation.propTypes = {
        t: PropTypes.func,
        label: PropTypes.node,
        showRequired: PropTypes.oneOfType([PropTypes.bool, PropTypes.string]),
        withFeedback: PropTypes.bool,
        showFirstValidation: PropTypes.bool,
        forceError: PropTypes.bool,
        meta: PropTypes.shape({
            touched: PropTypes.bool.isRequired,
            error: PropTypes.oneOfType([
                PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.string, PropTypes.object])),
                PropTypes.string,
                PropTypes.object,
            ]),
            warning: PropTypes.string,
        }),
    };
    WithValidation.defaultProps = {
        withFeedback: true,
    };

    return WithValidation;
};

function getDisplayName(BaseComponent) {
    return BaseComponent.displayName || BaseComponent.name || 'Component';
}

// Enhance basic field components with translation and validation HoC
const errorTranslator = TranslationsSubscriber('errors');
const commonFieldDecorator = (x) => withFieldFeatures(errorTranslator(withValidation(x)));
const CommonInput = commonFieldDecorator(BasicInput);
const CommonInputTel = commonFieldDecorator(BasicInputTel);
const CommonRadio = commonFieldDecorator(BasicRadio);
const CommonCombobox = commonFieldDecorator(BasicCombobox);
const CommonDatePicker = commonFieldDecorator(BasicDatePicker);
const CommonMultiCombobox = commonFieldDecorator(MultiCombobox);

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */

export { CommonInput, CommonInputTel, CommonRadio, CommonCombobox, CommonDatePicker, CommonMultiCombobox };

export default commonFieldDecorator;
