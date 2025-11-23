/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component } from 'react';
import autobind from 'autobind-decorator';
import PropTypes from 'prop-types';
import { Button, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import Regroupement from '../Regroupement';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */

@TranslationsSubscriber(['eligibility', 'common'])
class RegroupementsComponent extends Component {
    @autobind
    addItem() {
        const { existingRegroupements, change } = this.props;
        if (existingRegroupements && existingRegroupements.length) {
            const newArray = [{}, ...existingRegroupements];
            change('pilotages[6].regroupements', newArray);
        } else {
            change('pilotages[6].regroupements', [{}]);
        }
    }

    @autobind
    removeItem(e) {
        const { fields } = this.props;
        const index = e.prefix.split('.')[1].match(/\[(-?\d+)\]/)[1];
        fields.remove(index);
    }

    render() {
        const { t, fields, isEdit, isCreate, isServiceOpen, conventions, calculatedErrors, change } = this.props;
        return (
            <div>
                <div className="d-flex justify-content-center">
                    {(isCreate || isEdit) && isServiceOpen && (
                        <Button type="button" onClick={this.addItem}>
                            {t('declarant.addRegroupement')}
                        </Button>
                    )}
                </div>
                {fields.length === 0 && <div className="p-2 cgd-comment">{t('declarant.noRegroupement')}</div>}

                {fields.map((prefix, index) => (
                    <Regroupement
                        data-index={index}
                        key={prefix}
                        index={index}
                        removeItem={this.removeItem}
                        prefix={prefix}
                        isServiceOpen={isServiceOpen}
                        isCreate={isCreate}
                        isEdit={isEdit}
                        regroupement={fields.get(index)}
                        conventions={conventions}
                        calculatedErrors={calculatedErrors}
                        change={change}
                    />
                ))}
            </div>
        );
    }
}

RegroupementsComponent.propTypes = {
    t: PropTypes.func,
    change: PropTypes.func,
    fields: PropTypes.oneOf(PropTypes.arrayOf(PropTypes.shape()), PropTypes.shape()),
    isCreate: PropTypes.bool,
    isEdit: PropTypes.bool,
    isServiceOpen: PropTypes.bool,
    conventions: PropTypes.arrayOf(PropTypes.shape()),
    calculatedErrors: PropTypes.arrayOf(PropTypes.bool),
    existingRegroupements: PropTypes.arrayOf(PropTypes.shape()),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default RegroupementsComponent;
