package com.cegedim.next.serviceeligibility.core.utils;

public final class PermissionConstants {

  public static final String READ_RIGHT_PERMISSION = "hasAuthority('SE_P_READ_HEALTHCARE')";
  public static final String READ_CONTRACT_PERMISSION = "hasAuthority('SE_P_READ_HEALTHCARE')";
  public static final String READ_PERMISSION = "hasAuthority('SE_P_READ_HEALTHCARE')";

  public static final String CREATE_CONTRACT_PERMISSION = "hasAuthority('SE_P_WRITE_HEALTHCARE')";
  public static final String UPDATE_DATA_INSURED_PERMISSION =
      "hasAuthority('SE_P_WRITE_HEALTHCARE')";
  public static final String DELETE_CONTRACT_PERMISSION = "hasAuthority('SE_P_WRITE_HEALTHCARE')";

  public static final String SUPPORT_BDDS = "hasAuthority('SE_P_SUPPORT_BDDS')";

  /** /** Private constructor. */
  private PermissionConstants() {}
}
