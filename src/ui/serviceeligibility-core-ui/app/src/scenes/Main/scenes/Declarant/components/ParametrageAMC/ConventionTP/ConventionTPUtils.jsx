import React, { Fragment } from 'react';
import { Button, Col, Label, Row } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import ConventionTPElement from './ConventionTPElement';
import ParametrageAMCUtils from '../ParametrageAMCUtils';

function buildFieldArray(fields, t, domainList, reseauxSoin, conventions, conventionTP, isCopy) {
    const domainesTP = ParametrageAMCUtils.getMappedForComboboxList(domainList);
    const reseauxSoinForCombo = ParametrageAMCUtils.getMappedForComboboxList(reseauxSoin);
    const conventionsForCombo = ParametrageAMCUtils.getMappedElementsForComboboxList(conventions);
    const hasParams = fields && fields.length > 0;
    if (isCopy) {
        (fields || []).getAll()?.forEach((member, index) => {
            if (!member.isNewConvention) {
                fields.remove(index);
            }
        });
    }
    return (
        <Fragment>
            {hasParams ? (
                <Row>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {`${t('declarant.parametrageAMC.reseauSoin')} *`}
                        </Label>
                    </Col>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {t('declarant.parametrageAMC.domaineTP')}
                        </Label>
                    </Col>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {`${t('declarant.parametrageAMC.conventionCible')} *`}
                        </Label>
                    </Col>
                    <Col xs={1}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {`${t('declarant.parametrageAMC.concatenation')} *`}
                        </Label>
                    </Col>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {`${t('declarant.parametrageAMC.dateDebut')} *`}
                        </Label>
                    </Col>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {t('declarant.parametrageAMC.dateFin')}
                        </Label>
                    </Col>
                </Row>
            ) : (
                <div className="ml-2">{t('declarant.parametrageAMC.noConventionTP')}</div>
            )}

            {(fields || []).map((member, index) => (
                <ConventionTPElement
                    id={`${member}.${index}`}
                    key={member}
                    deleteConventionTP={() => fields.remove(index)}
                    domainesTP={domainesTP}
                    reseauxSoinForCombo={reseauxSoinForCombo}
                    conventionsForCombo={conventionsForCombo}
                    member={member}
                    convention={(conventionTP || [])[index]}
                />
            ))}

            <Row className="m-0">
                <Button
                    id="conventionTPButton"
                    behavior="secondary"
                    outline
                    className="pl-2 pr-2"
                    onClick={(event) => {
                        event.preventDefault();
                        fields.push({ isNewConvention: true });
                    }}
                >
                    <CgIcon behavior="secondary" name="plus-square" size="1x" className="ml-1" />
                    <span className="ml-2">{t('declarant.parametrageAMC.addConventionTPButton')}</span>
                </Button>
            </Row>
        </Fragment>
    );
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const ConventionTPUtils = {
    buildFieldArray,
};
export default ConventionTPUtils;
