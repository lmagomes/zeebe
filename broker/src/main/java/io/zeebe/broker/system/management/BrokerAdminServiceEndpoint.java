/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Zeebe Community License 1.0. You may not use this file
 * except in compliance with the Zeebe Community License 1.0.
 */
package io.zeebe.broker.system.management;

import io.zeebe.broker.SpringBrokerBridge;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty("management.endpoint.zeebe-admin.enabled")
public class BrokerAdminServiceEndpoint {

  private static final String BASE_URI = "zeebe-admin";
  private static final String BROKER_PAUSE_PROCESSING_URI = BASE_URI + "/pause-processing";
  private static final String BROKER_UNPAUSE_PROCESSING_URI = BASE_URI + "/resume-processing";
  private static final String BROKER_TAKE_SNAPSHOTS_URI = BASE_URI + "/take-snapshot";
  private static final String BROKER_PREPARE_UPGRADE_URI = BASE_URI + "/prepare-upgrade";
  private static final String BROKER_GET_STATUS_URI = BASE_URI + "/partition-status";

  @Autowired private SpringBrokerBridge springBrokerBridge;

  @PostMapping(value = BROKER_PAUSE_PROCESSING_URI)
  public Map<Integer, PartitionStatus> pauseProcessing() {
    springBrokerBridge.getAdminService().ifPresent(BrokerAdminService::pauseStreamProcessing);
    return currentStatus();
  }

  @PostMapping(value = BROKER_UNPAUSE_PROCESSING_URI)
  public Map<Integer, PartitionStatus> resumeProcessing() {
    springBrokerBridge.getAdminService().ifPresent(BrokerAdminService::resumeStreamProcessing);
    return currentStatus();
  }

  @PostMapping(value = BROKER_TAKE_SNAPSHOTS_URI)
  public Map<Integer, PartitionStatus> takeSnapshot() {
    springBrokerBridge.getAdminService().ifPresent(BrokerAdminService::takeSnapshot);
    return currentStatus();
  }

  @PostMapping(value = BROKER_PREPARE_UPGRADE_URI)
  public Map<Integer, PartitionStatus> prepareForUpgrade() {
    springBrokerBridge.getAdminService().ifPresent(BrokerAdminService::prepareForUpgrade);
    return currentStatus();
  }

  @GetMapping(value = BROKER_GET_STATUS_URI)
  public Map<Integer, PartitionStatus> currentStatus() {
    return springBrokerBridge
        .getAdminService()
        .map(BrokerAdminService::getPartitionStatus)
        .orElse(Map.of());
  }
}
