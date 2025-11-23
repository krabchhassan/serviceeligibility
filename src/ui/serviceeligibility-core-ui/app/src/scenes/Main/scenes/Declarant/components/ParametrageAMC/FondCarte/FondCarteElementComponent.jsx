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
class FondCarteElementComponent extends Component {
    getNewFondCarteElement() {
        const { t, member, reseauxSoinForCombo } = this.props;

        return (
            <Row>
                <Col xs={3}>
                    <Field
                        id="reseau-soin"
                        key={`${member}.${Constants.FIELDS.reseauSoin}`}
                        name={`${member}.${Constants.FIELDS.reseauSoin}`}
                        component={CommonCombobox}
                        options={reseauxSoinForCombo}
                        placeholder={t(`${Constants.TRAD_MODAL}.reseauSoinPlaceholder`)}
                        formGroupClassName="mb-2"
                        searchable
                        clearable
                        validate={[required]}
                    />
                </Col>
                <Col xs={4}>
                    <Field
                        id="fond-carte"
                        key={`${member}.${Constants.FIELDS.fondCarte}`}
                        name={`${member}.${Constants.FIELDS.fondCarte}`}
                        component={CommonInput}
                        placeholder={t(`${Constants.TRAD_MODAL}.fondCartePlaceholder`)}
                        searchable
                        clearable
                        formGroupClassName="mb-2"
                        validate={[required]}
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

    editExistingFondCarteElement() {
        const { t, member, fondCarte, reseauxSoinForCombo } = this.props;

        return (
            <Row>
                <Col xs={3}>
                    <LabelValuePresenter
                        id="reseau-soin"
                        key={`${member}.${Constants.FIELDS.reseauSoin}`}
                        name={`${member}.${Constants.FIELDS.reseauSoin}`}
                        value={ParametrageAMCUtils.getElementLabelForEdit(
                            (fondCarte || {}).reseauSoin,
                            reseauxSoinForCombo,
                        )}
                        defaultValue="-"
                        inline
                    />
                </Col>
                <Col xs={4}>
                    <LabelValuePresenter
                        id="fond-carte"
                        key={`${member}.${Constants.FIELDS.fondCarte}`}
                        name={`${member}.${Constants.FIELDS.fondCarte}`}
                        value={(fondCarte || {}).fondCarte}
                        defaultValue="-"
                        inline
                    />
                </Col>
                <Col xs={2}>
                    <LabelValuePresenter
                        id="date-debut"
                        key={`${member}.${Constants.FIELDS.dateDebut}`}
                        name={`${member}.${Constants.FIELDS.dateDebut}`}
                        value={DateUtils.formatDisplayDate((fondCarte || {}).dateDebut)}
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
                        value={DateUtils.formatDisplayDate((fondCarte || {}).dateFin)}
                        placeholderText={t(`${Constants.TRAD_MODAL}.datePlaceholder`)}
                        formGroupClassName="mb-2"
                    />
                </Col>
            </Row>
        );
    }

    deleteFields(event) {
        const { deleteFondCarte } = this.props;

        event.preventDefault();
        deleteFondCarte();
    }

    render() {
        const { fondCarte } = this.props;

        if ((fondCarte || {}).isNewFondCarte) {
            return this.getNewFondCarteElement();
        }
        return this.editExistingFondCarteElement();
    }
}

FondCarteElementComponent.propTypes = {
    t: PropTypes.func,
    deleteFondCarte: PropTypes.func,
    member: PropTypes.string,
    reseauxSoinForCombo: PropTypes.arrayOf(PropTypes.shape()),
    fondCarte: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default FondCarteElementComponent;
