/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.monitoring.probe.aspectj.jersey;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;

import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
@Aspect
@DeclarePrecedence("kieker.monitoring.probe.aspectj.operationExecution.*,kieker.monitoring.probe.aspectj.jersey.*")
public class OperationExecutionJerseyClientInterceptor extends AbstractAspectJProbe {
	private static final Log LOG = LogFactory.getLog(OperationExecutionJerseyClientInterceptor.class);

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final String VMNAME = CTRLINST.getHostname();
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;

	public static final String SESSION_ID_ASYNC_TRACE = "NOSESSION-ASYNCIN";

	@Around("execution(public com.sun.jersey.api.client.ClientResponse com.sun.jersey.client.apache4.ApacheHttpClient4Handler.handle(com.sun.jersey.api.client.ClientRequest))")
	public Object operation(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String signature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(signature)) {
			return thisJoinPoint.proceed();
		}

		boolean entrypoint = true;
		final String hostname = VMNAME;
		final String sessionId = SESSIONREGISTRY.recallThreadLocalSessionId();
		final int eoi; // this is executionOrderIndex-th execution in this trace
		final int ess; // this is the height in the dynamic call tree of this execution
		final int nextESS;
		long traceId = CF_REGISTRY.recallThreadLocalTraceId(); // traceId, -1 if entry point
		if (traceId == -1) {
			entrypoint = true;
			traceId = CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
			CF_REGISTRY.storeThreadLocalEOI(0);
			CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
			nextESS = 1;
		} else {
			entrypoint = false;
			eoi = CF_REGISTRY.incrementAndRecallThreadLocalEOI();
			ess = CF_REGISTRY.recallAndIncrementThreadLocalESS();
			nextESS = ess + 1;
			if ((eoi == -1) || (ess == -1)) {
				LOG.error("eoi and/or ess have invalid values:" + " eoi == " + eoi + " ess == " + ess);
				CTRLINST.terminateMonitoring();
			}
		}

		// Get request header
		final Object[] args = thisJoinPoint.getArgs();
		final ClientRequest request = (ClientRequest) args[0];
		final URI uri = request.getURI();
		LOG.info("URI = " + uri.toString());

		// This is a hack to put all values in the header
		MultivaluedMap<String, Object> requestHeader = request.getHeaders();
		if (requestHeader == null) {
			requestHeader = new MultivaluedHashMap<String, Object>();
		}

		final List<Object> requestHeaderList = new ArrayList<Object>(4);
		requestHeaderList.add(Long.toString(traceId) + "," + sessionId + "," + Integer.toString(eoi) + "," + Integer.toString(nextESS));
		LOG.info("requestHeaderList = " + requestHeaderList);
		requestHeader.put(JerseyHeaderConstants.OPERATION_EXECUTION_JERSEY_HEADER, requestHeaderList);

		// measure before
		final long tin = TIME.getTime();
		// execution of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed(args);

			// Process response
			if (retval instanceof ClientResponse) {
				final ClientResponse response = (ClientResponse) retval;
				final MultivaluedMap<String, String> responseHeader = response.getHeaders();
				if (responseHeader != null) {
					final List<String> responseHeaderList = responseHeader.get(JerseyHeaderConstants.OPERATION_EXECUTION_JERSEY_HEADER);
					if (responseHeaderList != null) {
						LOG.info("responseHeaderList = " + responseHeaderList);
						final String[] responseHeaderArray = responseHeaderList.get(0).split(",");

						// Extract trace id
						final String retTraceIdStr = responseHeaderArray[0];
						Long retTraceId = -1L;
						if (retTraceIdStr != "null") {
							try {
								retTraceId = Long.parseLong(retTraceIdStr);
							} catch (final NumberFormatException exc) {
								LOG.warn("Invalid tradeId");
							}
						}
						if (traceId != retTraceId) {
							LOG.error("TraceId in response header is different from that in request header");
						}

						// Extract session id
						String retSessionId = responseHeaderArray[1];
						if (retSessionId == "null") {
							retSessionId = OperationExecutionRecord.NO_SESSION_ID;
						}

						// Extract eoi
						int retEOI = -1;
						final String retEOIStr = responseHeaderArray[2];
						if (!retEOIStr.equals("null")) {
							try {
								retEOI = Integer.parseInt(retEOIStr);
								CF_REGISTRY.storeThreadLocalEOI(retEOI);
							} catch (final NumberFormatException exc) {
								LOG.warn("Invalid eoi", exc);
							}
						}

					} else {
						LOG.info("No monitoring data found in the response header");
						LOG.info("Is the next tier instrumented?");
						LOG.info("URI = " + uri.toString());
					}
				} else {
					LOG.info("Response header is null");
					LOG.info("Is the next tier instrumented?");
					LOG.info("URI = " + uri.toString());
				}
			}
		} finally {
			// measure after
			final long tout = TIME.getTime();
			CTRLINST.newMonitoringRecord(new OperationExecutionRecord(signature, sessionId, traceId, tin, tout, hostname, eoi, ess));
			// cleanup
			if (entrypoint) {
				CF_REGISTRY.unsetThreadLocalTraceId();
				CF_REGISTRY.unsetThreadLocalEOI();
				CF_REGISTRY.unsetThreadLocalESS();
			} else {
				CF_REGISTRY.storeThreadLocalESS(ess); // next operation is ess
			}
		}
		return retval;
	}
}
