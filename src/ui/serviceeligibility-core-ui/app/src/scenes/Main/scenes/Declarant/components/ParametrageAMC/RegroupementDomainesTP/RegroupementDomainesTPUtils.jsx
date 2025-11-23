import React, { Fragment } from 'react';
import { Button, Col, Label, Row } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import RegroupementDomainesTPElement from './RegroupementDomainesTPElement';
import ParametrageAMCUtils from '../ParametrageAMCUtils';

function buildFieldArray(fields, t, domainList, regroupementDomainesTP, isCopy) {
    const domainesTP = ParametrageAMCUtils.getMappedForComboboxList(domainList);
    const hasParams = fields && fields.length > 0;
    if (isCopy) {
        (fields || []).getAll()?.forEach((member, index) => {
            if (!member.isNewRegroupementDomaines) {
                fields.remove(index);
            }
        });
    }
    return (
        <Fragment>
            {hasParams ? (
                <Row>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment form-control-label">
                            {`${t('declarant.parametrageAMC.domaineRegroupementTP')} *`}
                        </Label>
                    </Col>
                    <Col xs={3}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {`${t('declarant.parametrageAMC.codesDomainesTP')} *`}
                        </Label>
                    </Col>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {`${t('declarant.parametrageAMC.niveauRemboursementIdentiqueEdit')} *`}
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
                <div className="ml-2">{t('declarant.parametrageAMC.noRegroupementDomainesTP')}</div>
            )}

            {(fields || []).map((member, index) => (
                <RegroupementDomainesTPElement
                    id={`${member}.${index}`}
                    key={member}
                    deleteRegroupementDomainesTP={() => fields.remove(index)}
                    domainesTP={domainesTP}
                    member={member}
                    regroupementDomaines={(regroupementDomainesTP || [])[index]}
                />
            ))}

            <Row className="m-0">
                <Button
                    id="regroupementDomainesTPButton"
                    behavior="secondary"
                    outline
                    className="pl-2 pr-2"
                    onClick={(event) => {
                        event.preventDefault();
                        fields.push({ isNewRegroupementDomaines: true });
                    }}
                >
                    <CgIcon behavior="secondary" name="plus-square" size="1x" className="ml-1" />
                    <span className="ml-2">{t('declarant.parametrageAMC.addRegroupementDomainesTPButton')}</span>
                </Button>
            </Row>
        </Fragment>
    );
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const RegroupementDomainesTPUtils = {
    buildFieldArray,
};
export default RegroupementDomainesTPUtils;
