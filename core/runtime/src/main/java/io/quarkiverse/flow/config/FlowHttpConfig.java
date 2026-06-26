package io.quarkiverse.flow.config;

import java.util.Map;
import java.util.Optional;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.serverlessworkflow.impl.TaskContext;
import io.serverlessworkflow.impl.WorkflowInstance;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

/**
 * HTTP/OpenAPI client configuration for Quarkus Flow.
 * <p>
 * Prefix: {@code quarkus.flow.http.client}
 * <p>
 * Shapes:
 * <p>
 * Default client (inherits {@link HttpClientConfig}):
 *
 * <pre>
 * quarkus.flow.http.client.connect-timeout=5000
 * quarkus.flow.http.client.read-timeout=10000
 * quarkus.flow.http.client.logging.scope=request-response
 * </pre>
 * <p>
 * Named clients:
 *
 * <pre>
 * quarkus.flow.http.client.named.secureA.connect-timeout=3000
 * quarkus.flow.http.client.named.secureA.user-agent=MyCompanyBot/1.0
 * </pre>
 * <p>
 * Workflow and task-level routing:
 *
 * <pre>
 * quarkus.flow.http.client.client."org.acme:myFlow:0.0.1".name=secureA
 * quarkus.flow.http.client.client."org.acme:myFlow:0.0.1:fetchCustomers".name=secureB
 * </pre>
 */
@ConfigMapping(prefix = "quarkus.flow.http.client")
@ConfigRoot(phase = ConfigPhase.RUN_TIME)
public interface FlowHttpConfig extends HttpClientConfig {

    /**
     * Named HTTP clients, keyed by client name.
     * <p>
     * Each entry maps to:
     *
     * <pre>
     * quarkus.flow.http.client.named.<name>.<property>
     * </pre>
     *
     * For example:
     *
     * <pre>
     * quarkus.flow.http.client.named.secureA.connect-timeout=3000
     * quarkus.flow.http.client.named.secureA.user-agent=MyCompanyBot/1.0
     * </pre>
     *
     * @return the map of named HTTP client configurations
     */
    Map<String, HttpClientConfig> named();

    /**
     * HTTP client name overrides keyed by workflow or task identifier.
     * <p>
     * Keys can be:
     * <ul>
     * <li>{@code <namespace>:<name>:<version>} — workflow-level override</li>
     * <li>{@code <namespace>:<name>:<version>:<taskName>} — task-level override</li>
     * </ul>
     *
     * @return the map of client overrides
     */
    Map<String, ClientOverride> client();

    /**
     * Override for the named HTTP client to use.
     */
    interface ClientOverride {

        /**
         * The named HTTP client to use, configured under
         * {@code quarkus.flow.http.client.named.<name>}.
         *
         * @return the client name
         */
        Optional<String> name();
    }

    /**
     * Whether the HTTP Client should propagate through HTTP headers the correlation metadata.
     * <p>
     * The correlation metadata are:
     * <ul>
     * <li><code>X-Flow-Instance-Id</code> the instance ID see {@link WorkflowInstance#id()}</li>
     * <li><code>X-Flow-Task-Id</code> the task's position that where the request was made, see
     * {@link TaskContext#position()}</li>
     * </ul>
     */
    @WithDefault("true")
    Optional<Boolean> enableMetadataPropagation();

}