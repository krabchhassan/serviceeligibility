/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import { Field } from 'redux-form';
import PropTypes from 'prop-types';
import { Col, Row, Button, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import Constants from './Constants';
import { CommonCombobox, CommonDatePicker, CommonInput } from '../../../../../../../common/utils/Form/CommonFields';
import ValidationFactories from '../../../../../../../common/utils/Form/ValidationFactories';
import LabelValuePresenter from '../../../../../../../common/components/LabelValuePresenter/LabelValuePresenter';
import DateUtils from '../../../../../../../common/utils/DateUtils';
import ParametrageAMCUtils from '../ParametrageAMCUtils';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */
const required = ValidationFactories.required();
/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class ConventionTPElementComponent extends Component {
    getNewConventionTPElement() {
        const { t, member, domainesTP, reseauxSoinForCombo, conventionsForCombo } = this.props;

        return (
            <Row>
                <Col xs={2}>
                    <Field
                        id="reseau-soin"
                        key={`${member}.${Constants.FIELDS.reseauSoin}`}
                        name={`${member}.${Constants.FIELDS.reseauSoin}`}
                        component={CommonCombobox}
                        options={reseauxSoinForCombo}
                        placeholder={t(`${Constants.TRAD_MODAL}.reseauSoinPlaceholder`)}
                        searchable
                        clearable
                        formGroupClassName="mb-2"
                        validate={[required]}
                    />
                </Col>
                <Col xs={2}>
                    <Field
                        id="domaine-tp"
                        key={`${member}.${Constants.FIELDS.domaineTP}`}
                        name={`${member}.${Constants.FIELDS.domaineTP}`}
                        component={CommonCombobox}
                        options={domainesTP}
                        placeholder={t(`${Constants.TRAD_MODAL}.domaineTPPlaceholder`)}
                        formGroupClassName="mb-2"
                        searchable
                        clearable
                    />
                </Col>
                <Col xs={2}>
                    <Field
                        id="convention-cible"
                        key={`${member}.${Constants.FIELDS.conventionCible}`}
                        name={`${member}.${Constants.FIELDS.conventionCible}`}
                        component={CommonCombobox}
                        options={conventionsForCombo}
                        placeholder={t(`${Constants.TRAD_MODAL}.conventionCiblePlaceholder`)}
                        formGroupClassName="mb-2"
                        searchable
                        clearable
                        validate={[required]}
                    />
                </Col>
                <Col xs={1}>
                    <Field
                        id={`${member}.${Constants.FIELDS.concatenation}`}
                        key={`${member}.${Constants.FIELDS.concatenation}`}
                        name={`${member}.${Constants.FIELDS.concatenation}`}
                        component={CommonInput}
                        formGroupClassName="mb-2 ml-0 pl-0"
                        value="true"
                        label=" "
                        type="checkbox"
                    />
                </Col>
                <Col xs={2}>
                    <Field
                        id="date-debut"
                        key={`${member}.${Constants.FIELDS.dateDebut}`}
                        name={`${member}.${Constants.FIELDS.dateDebut}`}
                        component={CommonDatePicker}
                        placeholderText={t(`${Constants.TRAD_MODAL}.datePlaceholder`)}
                        formGroupClassName="mb-2"
                        validate={[required]}
                    />
                </Col>
                <Col xs={2}>
                    <Field
                        id="date-fin"
                        key={`${member}.${Constants.FIELDS.dateFin}`}
                        name={`${member}.${Constants.FIELDS.dateFin}`}
                        component={CommonDatePicker}
                        placeholderText={t(`${Constants.TRAD_MODAL}.datePlaceholder`)}
                        formGroupClassName="mb-2"
                    />
                </Col>
                <Col xs={1}>
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

    editExistingConventionTPElement() {
        const { t, member, convention, domainesTP, reseauxSoinForCombo, conventionsForCombo } = this.props;

        return (
            <Row>
                <Col xs={2}>
                    <LabelValuePresenter
                        id="reseau-soin"
                        key={`${member}.${Constants.FIELDS.reseauSoin}`}
                        name={`${member}.${Constants.FIELDS.reseauSoin}`}
                        value={ParametrageAMCUtils.getElementLabelForEdit(
                            (convention || {}).reseauSoin,
                            reseauxSoinForCombo,
                        )}
                        defaultValue="-"
                        inline
                    />
                </Col>
                <Col xs={2}>
                    <LabelValuePresenter
                        id="domaine-tp"
                        key={`${member}.${Constants.FIELDS.domaineTP}`}
                        name={`${member}.${Constants.FIELDS.domaineTP}`}
                        value={ParametrageAMCUtils.getElementLabelForEdit((convention || {}).domaineTP, domainesTP)}
                        defaultValue="-"
                        inline
                    />
                </Col>
                <Col xs={2}>
                    <LabelValuePresenter
                        id="convention-cible"
                        key={`${member}.${Constants.FIELDS.conventionCible}`}
                        name={`${member}.${Constants.FIELDS.conventionCible}`}
                        value={ParametrageAMCUtils.getElementLabelForEdit(
                            (convention || {}).conventionCible,
                            conventionsForCombo,
                        )}
                        defaultValue="-"
                        inline
                    />
                </Col>
                <Col xs={1} className="pl-4">
                    <CgIcon name={(convention || {}).concatenation ? 'validate' : 'cancel'} />
                </Col>
                <Col xs={2}>
                    <LabelValuePresenter
                        id="date-debut"
                        key={`${member}.${Constants.FIELDS.dateDebut}`}
                        name={`${member}.${Constants.FIELDS.dateDebut}`}
                        value={DateUtils.formatDisplayDate((convention || {}).dateDebut)}
                        defaultValue="-"
                        placeholderText={t(`${Constants.TRAD_MODAL}.datePlaceholder`)}
                        inline
                    />
                </Col>
                <Col xs={2}>
                    <Field
                        id="date-fin"
                        key={`${member}.${Constants.FIELDS.dateFin}`}
                        name={`${member}.${Constants.FIELDS.dateFin}`}
                        component={CommonDatePicker}
                        value={DateUtils.formatDisplayDateTime((convention || {}).dateFin)}
                        placeholderText={t(`${Constants.TRAD_MODAL}.datePlaceholder`)}
                        formGroupClassName="mb-2"
                    />
                </Col>
            </Row>
        );
    }

    deleteFields(event) {
        const { deleteConventionTP } = this.props;

        event.preventDefault();
        deleteConventionTP();
    }

    render() {
        const { convention } = this.props;

        if ((convention || {}).isNewConvention) {
            return this.getNewConventionTPElement();
        }
        return this.editExistingConventionTPElement();
    }
}

ConventionTPElementComponent.propTypes = {
    t: PropTypes.func,
    member: PropTypes.string,
    domainesTP: PropTypes.arrayOf(PropTypes.shape()),
    reseauxSoinForCombo: PropTypes.arrayOf(PropTypes.shape()),
    conventionsForCombo: PropTypes.arrayOf(PropTypes.shape()),
    convention: PropTypes.shape(),
    deleteConventionTP: PropTypes.func,
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default ConventionTPElementComponent;
