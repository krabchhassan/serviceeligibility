package com.cegedim.utils;

public enum ServiceEnum {
    PRIORISATION(0), VISIODROIT(1), SEL_TP(2), DCLBEN(3), TPG_IS(4), TPG_SP(5), ALMV3(6), CARTE_TP(
            7), CARTE_DEMATERIALISEE(8);

    private Integer ordre;

    private ServiceEnum(int ordre) {
        this.ordre = ordre;
    }

    public int getOrdre() {
        return ordre;
    }

    public static ServiceEnum getByOrdre(Integer ordre) {
        for (ServiceEnum type : ServiceEnum.values()) {
            if (type.getOrdre() == ordre) {
                return type;
            }
        }
        return null;
    }

}
