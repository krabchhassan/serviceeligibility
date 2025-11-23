package com.cegedim.next.serviceeligibility.core.utility;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import lombok.Getter;

public class I18NService {

  private static final String MISSING_TRANSLATION = "Missing translation for ";

  static final String MESSAGE_BUNDLE = "MessagesBundle";

  @Getter static ResourceBundle messages;

  public I18NService() {
    messages = ResourceBundle.getBundle(MESSAGE_BUNDLE);
  }

  public String getMessage(String key) {
    if (messages != null) {
      if (messages.containsKey(key)) {
        return messages.getString(key);
      } else {
        return MISSING_TRANSLATION + key;
      }
    } else return null;
  }

  public String getMessage(String key, Object[] params) {
    if (messages != null) {
      if (messages.containsKey(key)) {
        String msgTemp = messages.getString(key);
        if (params != null && params.length > 0) {
          msgTemp = MessageFormat.format(msgTemp, params);
          return msgTemp;
        } else {
          return msgTemp;
        }
      } else {
        return MISSING_TRANSLATION + key;
      }
    } else return null;
  }

  public String getMessage(int iKey) {
    return getMessage(iKey, null);
  }

  public String getMessage(int iKey, Object[] params) {
    StringBuilder key = new StringBuilder(iKey + "");
    for (int i = key.length(); i < 4; i++) {
      key.insert(0, "0");
    }
    if (messages != null) {
      if (messages.containsKey(key.toString())) {
        if (params != null) {
          return MessageFormat.format(messages.getString(key.toString()), params);
        } else {
          return messages.getString(key.toString());
        }
      } else {
        return MISSING_TRANSLATION + key.toString();
      }
    } else {
      return null;
    }
  }
}
