/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import { Field } from 'redux-form';
import get from 'lodash.get';
import PropTypes from 'prop-types';
import { Row, Col, Button, Tooltip, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import { CommonCombobox, CommonInput } from '../../../../../../../../common/utils/Form/CommonFields';
import Constants from './Constants';
import ValidationFactories from '../../../../../../../../common/utils/Form/ValidationFactories';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();

/* ************************************* */
/* ********      COMPONENT      ******** */
/* ************************************* */
const mapDomainsForCombobox = (list) =>
    (list || []).map((item) => ({
        label: `${item.code} - ${item.label}`,
        value: item.code,
    }));

const getUnitsList = (t) => [
    {
        label: t(`${Constants.TRAD_MODAL}.combobox.jours`),
        value: Constants.COMBOBOX.days,
    },
    {
        label: t(`${Constants.TRAD_MODAL}.combobox.mois`),
        value: Constants.COMBOBOX.months,
    },
];

@TranslationsSubscriber(['eligibility', 'common'])
class ParamDomaineTPComponent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            setComboBox: true,
        };
    }
    componentWillReceiveProps(nextProps) {
        const { t, change, data, member } = this.props;
        const { setComboBox } = this.state;

        const gotDomainesTP = nextProps.fullDomainesTP.length !== 0 && nextProps.fullDomainesTP !== [];

        if (gotDomainesTP && setComboBox) {
            this.setState({ setComboBox: false });
            const fullList = Object.values(nextProps.fullDomainesTP || []).map((item) => item.value);
            const domainObject = mapDomainsForCombobox(fullList).find((item) => item.value === data.codeDomaine);

            const unit = get(data, 'unite.value', data.unite || null);
            const unitObject = getUnitsList(t).find((item) => item.value === unit);

            change(`${member}.${Constants.COMBOBOX.idDomain}`, domainObject);
            change(`${member}.${Constants.COMBOBOX.idUnit}`, unitObject);
        }
    }

    changeNumber(number, unit) {
        const { change, member, data } = this.props;

        let transformedNumber = number;
        const recievedUnit = get(unit, 'value', get(data, 'unite.value', Constants.COMBOBOX.days));

        if (recievedUnit === Constants.COMBOBOX.months && transformedNumber > 12) {
            transformedNumber = 12;
        } else if (transformedNumber > 365) {
            transformedNumber = 365;
        } else if (transformedNumber < 1) {
            transformedNumber = 1;
        }

        change(`${member}.${Constants.COMBOBOX.idNumber}`, transformedNumber);
    }

    deleteFields(event) {
        const { deleteParam } = this.props;

        event.preventDefault();
        deleteParam();
    }

    handleNumberChange(event) {
        event.preventDefault();
        this.changeNumber(event.target.value);
    }

    handleUnitChange(unit) {
        const { member, change, data } = this.props;

        if (unit && unit.value !== Constants.COMBOBOX.months) {
            change(`${member}.${Constants.CHECKBOX.idCheckbox}`, false);
        }

        const number = get(data, 'duree', 0);
        this.changeNumber(number, unit);
    }

    render() {
        const { t, domainesTP, member, index, data } = this.props;

        const unitsList = getUnitsList(t);
        const isMonthSelected = get(data, 'unite.value', null) === Constants.COMBOBOX.months;
        const lastColClassname = isMonthSelected ? 'd-flex justify-content-between pl-2' : 'd-flex justify-content-end';
        const tooltipId = Constants.TOOLTIP + index;

        return (
            <Row className="mt-1" style={{ alignSelf: 'center' }}>
                <Col xs={5}>
                    <Field
                        name={`${member}.${Constants.COMBOBOX.idDomain}`}
                        component={CommonCombobox}
                        options={domainesTP}
                        placeholder={t(`${Constants.TRAD_MODAL}.combobox.domainPlaceholder`)}
                        promptTextCreator={(label) => label}
                        searchable
                        clearable
                        formGroupClassName="mb-0"
                        validate={required}
                    />
                </Col>
                <Col xs={4}>
                    <Row>
                        <Col xs={4} className="pr-0">
                            <Field
                                name={`${member}.${Constants.COMBOBOX.idNumber}`}
                                component={CommonInput}
                                placeholder={t(`${Constants.TRAD_MODAL}.combobox.numberPlaceholder`)}
                                promptTextCreator={(label) => label}
                                type="number"
                                onChange={(event) => this.handleNumberChange(event)}
                                formGroupClassName="mb-0"
                                validate={required}
                            />
                        </Col>
                        <Col xs={6}>
                            <Field
                                name={`${member}.${Constants.COMBOBOX.idUnit}`}
                                component={CommonCombobox}
                                options={unitsList}
                                placeholder={t(`${Constants.TRAD_MODAL}.combobox.unitPlaceholder`)}
                                promptTextCreator={(label) => label}
                                onChange={(unit) => this.handleUnitChange(unit)}
                                formGroupClassName="mb-0"
                                validate={required}
                            />
                        </Col>
                        <Col xs={2} className="pl-0">
                            <CgIcon name="information-tooltip" id={tooltipId} className="text-colored-mix-0 pt-2" />
                            <Tooltip target={tooltipId} placement="top">
                                {t(`${Constants.TRAD_MODAL}.dateFinTooltip`)}
                            </Tooltip>
                        </Col>
                    </Row>
                </Col>
                <Col xs={3} className={lastColClassname}>
                    {isMonthSelected && (
                        <Field
                            name={`${member}.${Constants.CHECKBOX.idCheckbox}`}
                            component={CommonInput}
                            label={t(`${Constants.TRAD_MODAL}.posFinMois`)}
                            type="checkbox"
                            formGroupClassName="mb-0 pt-2"
                        />
                    )}
                    <div>
                        <Button
                            behavior="default"
                            outlineNoBorder
                            onClick={(event) => this.deleteFields(event)}
                            className="pt-2"
                        >
                            <CgIcon behavior="secondary" name="trash-o" />
                        </Button>
                    </div>
                </Col>
            </Row>
        );
    }
}

ParamDomaineTPComponent.propTypes = {
    t: PropTypes.func,
    fullDomainesTP: PropTypes.arrayOf(PropTypes.shape()),
    domainesTP: PropTypes.arrayOf(PropTypes.shape()),
    data: PropTypes.shape(),
    change: PropTypes.func,
    member: PropTypes.string,
    deleteParam: PropTypes.func,
    index: PropTypes.number,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ParamDomaineTPComponent;
