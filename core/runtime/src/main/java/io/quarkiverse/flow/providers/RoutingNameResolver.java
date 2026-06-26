package io.quarkiverse.flow.providers;

import io.quarkiverse.flow.config.FlowHttpConfig;
import io.serverlessworkflow.impl.WorkflowDefinitionId;

public class RoutingNameResolver {

    private static final String KEY_SEPARATOR = ":";

    private final FlowHttpConfig flowHttpConfig;

    public RoutingNameResolver(FlowHttpConfig flowHttpConfig) {
        this.flowHttpConfig = flowHttpConfig;
    }

    public String resolveName(WorkflowDefinitionId workflowId, String taskName) {
        String workflowKey = workflowId.toString();

        if (taskName != null && !taskName.isBlank()) {
            String taskKey = workflowKey + KEY_SEPARATOR + taskName;
            FlowHttpConfig.ClientOverride taskOverride = flowHttpConfig.client().get(taskKey);
            if (taskOverride != null && taskOverride.name().isPresent()) {
                return taskOverride.name().get();
            }
        }

        FlowHttpConfig.ClientOverride workflowOverride = flowHttpConfig.client().get(workflowKey);
        if (workflowOverride != null && workflowOverride.name().isPresent()) {
            return workflowOverride.name().get();
        }

        if (flowHttpConfig.named().containsKey(workflowKey)) {
            return workflowKey;
        }

        return null;
    }
}
