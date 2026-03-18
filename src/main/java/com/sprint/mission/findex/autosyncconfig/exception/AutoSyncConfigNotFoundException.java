package com.sprint.mission.findex.autosyncconfig.exception;

import com.sprint.mission.findex.exception.BusinessLogicException;
import com.sprint.mission.findex.exception.ErrorCode;

public class AutoSyncConfigNotFoundException extends BusinessLogicException {

  public AutoSyncConfigNotFoundException() {
    super(ErrorCode.AUTO_SYNC_CONFIG_NOT_FOUND);
  }

}
