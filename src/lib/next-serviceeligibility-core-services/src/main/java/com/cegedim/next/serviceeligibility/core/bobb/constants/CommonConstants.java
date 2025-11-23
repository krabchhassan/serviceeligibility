package com.cegedim.next.serviceeligibility.core.bobb.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommonConstants {
  public static final String SERVICEELIGIBILITY_CORE_UI_GT_DETAIL_URL =
      "/serviceeligibility/core/ui/#/gt/detail/";
  public static final String SERVICEELIGIBILITY_CORE_UI_GT_CREATE_URL =
      "/serviceeligibility/core/ui/#/gt/create/";
  public static final String URL_FORMAT = "%s/%s/%s/permissions";
  public static final String CODE = "code";
  public static final String WRITE_GT_PERMISSION = "SE_P_WRITE_GT";
  public static final String READ_GT_PERMISSION = "SE_P_READ_GT";
  public static final String NEXT_AUTHORIZATION_CORE_API =
      "http://next-authorization-core-api:8080";
  public static final String USERS_ENDPOINT = "v1/users";
  public static final String ERROR_MESSAGE =
      "L’utilisateur n'a pas les habilitations nécessaires pour consulter les GT";
}
