/* ************************************* */
/* ********       IMPORTS       ******** */
/* ************************************* */
import React, { Component, Fragment } from 'react';
import { reduxForm } from 'redux-form';
import get from 'lodash.get';
import PropTypes from 'prop-types';
import { Row, Col, Button, TranslationsSubscriber } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import ParamDomaineTPComponent from './ParamDomaineTP';
import Constants from './Constants';

/* ************************************* */
/* ********      VARIABLES      ******** */
/* ************************************* */

/* ************************************* */
/* ********      COMPONENT      ******** */

/* ************************************* */
@TranslationsSubscriber(['eligibility', 'common'])
class CreateValiditesFieldArrayComponent extends Component {
    getMappedDomainsList() {
        const { domainList, fields, formValues } = this.props;

        const objectsList = Object.values(domainList || [])
            .map((domain) => domain.value)
            .map((item) => ({
                label: `${item.code} - ${item.label}`,
                value: item.code,
            }));

        (fields || []).forEach((member) => {
            const domaine = get(formValues, `${member}.${Constants.COMBOBOX.idDomain}`);
            const codeDomaine = get(domaine, 'value', domaine);
            const indexDomaine = objectsList.findIndex((item) => item.value === codeDomaine);

            if (indexDomaine >= 0) {
                objectsList.splice(indexDomaine, 1);
            }
        });

        return objectsList;
    }

    render() {
        const { t, change, fields, domainList, isCreate } = this.props;

        const domainesTP = this.getMappedDomainsList();
        const haveParams = fields && fields.length > 0;
        console.log(fields);
        (fields || []).map((member) => console.log(member));
        (fields || []).map((member, index) => console.log(index));

        return (
            <Fragment>
                {haveParams && (
                    <Row>
                        <Col xs={5} className="text-colored-mix-0">
                            {`${t('declarant.paramModal.selectionDomaine')} : *`}
                        </Col>
                        <Col xs={4} className="text-colored-mix-0">
                            {`${t('declarant.paramModal.dateFinValidite')} : *`}
                        </Col>
                    </Row>
                )}

                {(fields || []).map((member, index) => (
                    <ParamDomaineTPComponent
                        key={member}
                        change={change}
                        deleteParam={() => fields.remove(index)}
                        fullDomainesTP={domainList}
                        domainesTP={domainesTP}
                        member={member}
                        index={index}
                        isCreate={isCreate}
                    />
                ))}

                {!haveParams && <div className="ml-2">{t(`${Constants.TRAD_MODAL}.aucunDomaine`)}</div>}

                <br />
                <Row className="m-0">
                    <Button
                        behavior="secondary"
                        outline
                        className="pl-2 pr-2"
                        onClick={(event) => {
                            event.preventDefault();
                            fields.push({});
                        }}
                    >
                        <CgIcon behavior="secondary" name="plus-square" size="1x" className="ml-1" />
                        <span className="ml-2">{t('declarant.paramModal.ajouterDomaine')}</span>
                    </Button>
                </Row>
                {haveParams && <div className="cgd-comment small mt-3">{t('mandatoryHint')}</div>}
            </Fragment>
        );
    }
}

CreateValiditesFieldArrayComponent.propTypes = {
    t: PropTypes.func,
    domainList: PropTypes.arrayOf(PropTypes.shape()),
    change: PropTypes.func,
    fields: PropTypes.shape({
        push: PropTypes.func,
        name: PropTypes.string,
        remove: PropTypes.func,
        length: PropTypes.number,
    }).isRequired,
    formValues: PropTypes.shape(),
};

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
export default reduxForm({
    form: Constants.CREATE_FORM_NAME,
})(CreateValiditesFieldArrayComponent);
