/***************************************************************************
 * Copyright 2018 Lead Wire (https://leadwire.io)
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

package kieker.monitoring.probe.aspectj.leadwire;



import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.jdbc.JdbcOperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.probe.aspectj.operationExecution.AbstractOperationExecutionAspect;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Wassim DHIB
 * 
 * @since 1.13
 */
@Aspect
public class JdbcPreparedStatementAspect extends AbstractAspectJProbe {

	private static final Log LOG = LogFactory.getLog(AbstractOperationExecutionAspect.class);

	private static final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final String VMNAME = CTRLINST.getHostname();
	private static final long SQL_THRESHOLD = CTRLINST.getSqlThreshold();
	private static final Map<Object, String> statementSqlMap = CTRLINST.getStatementSqlMap();
	
	
	/* Configuration */
	private static final String NOT_WITHIN = " notWithinKieker()";


	/* http://docs.oracle.com/javase/7/docs/api/java/sql/Statement.html */
	private static final String RELATED_CALLS =
			/** PreparedStatement **/
			/* execute */
			  "(  execution(boolean java.sql.PreparedStatement.execute()) "
			/* executeQuery */
			+ "|| execution(java.sql.ResultSet java.sql.PreparedStatement.executeQuery()) "
			/* executeUpdate */
			+ "|| execution(int java.sql.PreparedStatement.executeUpdate()) "
			+ ") && this(thisObject) && " + NOT_WITHIN;

	

	@Around(RELATED_CALLS)
	public final Object operation(final Object thisObject, final ProceedingJoinPoint thisJoinPoint)
			throws Throwable {
		
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		if (!CTRLINST.isProbeActivated(this.signatureToLongString(thisJoinPoint.getSignature()))) {
			return thisJoinPoint.proceed();
		}
		
		// measure before
		final long tin = TIME.getTime();
				
		
				
		// collect data
		final boolean entrypoint;
		final String hostname = VMNAME;
		final String sessionId = SESSIONREGISTRY.recallThreadLocalSessionId();
		final int eoi; // this is executionOrderIndex-th execution in this trace
		final int ess; // this is the height in the dynamic call tree of this execution
		long traceId = CFREGISTRY.recallThreadLocalTraceId(); // traceId, -1 if entry point
		if (traceId == -1) {
			entrypoint = true;
			traceId = CFREGISTRY.getAndStoreUniqueThreadLocalTraceId();
			CFREGISTRY.storeThreadLocalEOI(0);
			CFREGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
		} else {
			entrypoint = false;
			eoi = CFREGISTRY.incrementAndRecallThreadLocalEOI(); // ess > 1
			ess = CFREGISTRY.recallAndIncrementThreadLocalESS(); // ess >= 0
			if ((eoi == -1) || (ess == -1)) {
				LOG.error("eoi and/or ess have invalid values:" + " eoi == " + eoi + " ess == " + ess);
				CTRLINST.terminateMonitoring();
			}
		}
		
	Object retVal;
	    
	 final String sqlStatement = statementSqlMap.get(thisJoinPoint.getThis());
    	
	try {	
		
		retVal = thisJoinPoint.proceed();
		
			} finally {
				
				final long tout = TIME.getTime();
				if (sqlStatement != null && tout-tin > SQL_THRESHOLD && sqlStatement.length() < 65534) {
				CTRLINST.newMonitoringRecord(new JdbcOperationExecutionRecord(sqlStatement, sessionId, traceId, tin, tout, hostname, eoi, ess));
				}
				SESSIONREGISTRY.unsetThreadLocalSessionId();
				
				// cleanup
				if (entrypoint) {
					CFREGISTRY.unsetThreadLocalTraceId();
					CFREGISTRY.unsetThreadLocalEOI();
					CFREGISTRY.unsetThreadLocalESS();
				} else {
					CFREGISTRY.storeThreadLocalESS(ess); // next operation is ess
				}
				
			}
			return retVal;
		
			
	}


}
