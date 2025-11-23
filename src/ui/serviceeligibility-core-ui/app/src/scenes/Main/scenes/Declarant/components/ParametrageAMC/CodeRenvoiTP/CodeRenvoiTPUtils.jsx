import React, { Fragment } from 'react';
import { Button, Col, Label, Row } from '@beyond-framework/common-uitoolkit-beyond';
import { CgIcon } from '@beyond-framework/common-uitoolkit-icons';
import CodeRenvoiTPElement from './CodeRenvoiTPElement';
import ParametrageAMCUtils from '../ParametrageAMCUtils';

function buildFieldArray(fields, t, domainList, reseauxSoin, returnCodesList, codeRenvoiTP, isCopy) {
    const domainesTP = ParametrageAMCUtils.getMappedForComboboxList(domainList);
    const reseauxSoinForCombo = ParametrageAMCUtils.getMappedForComboboxList(reseauxSoin);
    const hasParams = fields && fields.length > 0;
    if (isCopy) {
        (fields || []).getAll()?.forEach((member, index) => {
            if (!member.isNewCodeRenvoi) {
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
                            {`${t('declarant.parametrageAMC.domaineTP')} *`}
                        </Label>
                    </Col>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {t('declarant.parametrageAMC.reseauSoin')}
                        </Label>
                    </Col>
                    <Col xs={2}>
                        <Label className="text-colored-mix-0 cgd-comment mr-2 form-control-label">
                            {`${t('declarant.parametrageAMC.codeRenvoi')} *`}
                        </Label>
                    </Col>
                    <Col xs={1} />
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
                <div className="ml-2">{t('declarant.parametrageAMC.noCodeRenvoiTP')}</div>
            )}

            {(fields || []).map((member, index) => (
                <CodeRenvoiTPElement
                    id={`${member}.${index}`}
                    key={member}
                    deleteCodeRenvoiTP={() => fields.remove(index)}
                    domainesTP={domainesTP}
                    reseauxSoinForCombo={reseauxSoinForCombo}
                    returnCodesList={returnCodesList}
                    member={member}
                    codeRenvoi={(codeRenvoiTP || [])[index]}
                />
            ))}

            <Row className="m-0">
                <Button
                    id="codeRenvoiTPButton"
                    behavior="secondary"
                    outline
                    className="pl-2 pr-2"
                    onClick={(event) => {
                        event.preventDefault();
                        fields.push({ isNewCodeRenvoi: true });
                    }}
                >
                    <CgIcon behavior="secondary" name="plus-square" size="1x" className="ml-1" />
                    <span className="ml-2">{t('declarant.parametrageAMC.addCodeRenvoiTPButton')}</span>
                </Button>
            </Row>
        </Fragment>
    );
}

/* ************************************* */
/* ********       EXPORTS       ******** */
/* ************************************* */
const CodeRenvoiTPUtils = {
    buildFieldArray,
};
export default CodeRenvoiTPUtils;
