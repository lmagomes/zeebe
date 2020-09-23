/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Licensed under the Zeebe Community License 1.0. You may not use this file
 * except in compliance with the Zeebe Community License 1.0.
 */
package io.zeebe.broker.system.management;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.atomix.raft.RaftServer.Role;
import io.zeebe.engine.processing.streamprocessor.StreamProcessor.Phase;

public final class PartitionStatus {

  private final Role role;
  private final String snapshotId;

  @JsonInclude(Include.NON_NULL)
  private final Long processedPosition;

  @JsonInclude(Include.NON_NULL)
  private final Long processedPositionInSnapshot;

  @JsonInclude(Include.NON_NULL)
  private final Phase streamProcessorPhase;

  private PartitionStatus(
      final Role role,
      final Long processedPosition,
      final String snapshotId,
      final Long processedPositionInSnapshot,
      final Phase streamProcessorPhase) {
    this.role = role;
    this.processedPosition = processedPosition;
    this.snapshotId = snapshotId;
    this.processedPositionInSnapshot = processedPositionInSnapshot;
    this.streamProcessorPhase = streamProcessorPhase;
  }

  public static PartitionStatus ofLeader(
      final Long processedPosition,
      final String snapshotId,
      final Long processedPositionInSnapshot,
      final Phase streamProcessorPhase) {
    return new PartitionStatus(
        Role.LEADER,
        processedPosition,
        snapshotId,
        processedPositionInSnapshot,
        streamProcessorPhase);
  }

  public static PartitionStatus ofFollower(final String snapshotId) {
    return new PartitionStatus(Role.FOLLOWER, null, snapshotId, null, null);
  }

  public Role getRole() {
    return role;
  }

  public long getProcessedPosition() {
    return processedPosition;
  }

  public String getSnapshotId() {
    return snapshotId;
  }

  public Long getProcessedPositionInSnapshot() {
    return processedPositionInSnapshot;
  }

  public Phase getStreamProcessorPhase() {
    return streamProcessorPhase;
  }
}
