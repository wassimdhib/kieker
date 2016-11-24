/**
 */
package kieker.analysisteetime.model.analysismodel.execution.impl;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;

import kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionPackage;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionRoot;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Aggregated Invocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.execution.impl.AggregatedInvocationImpl#getSource <em>Source</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.execution.impl.AggregatedInvocationImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.execution.impl.AggregatedInvocationImpl#getExecutionRoot <em>Execution Root</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AggregatedInvocationImpl extends MinimalEObjectImpl.Container implements AggregatedInvocation {
	/**
	 * The cached value of the '{@link #getSource() <em>Source</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSource()
	 * @generated
	 * @ordered
	 */
	protected DeployedOperation source;

	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected DeployedOperation target;

	/**
	 * The cached value of the '{@link #getExecutionRoot() <em>Execution Root</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExecutionRoot()
	 * @generated
	 * @ordered
	 */
	protected ExecutionRoot executionRoot;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AggregatedInvocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExecutionPackage.Literals.AGGREGATED_INVOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedOperation getSource() {
		if (source != null && source.eIsProxy()) {
			InternalEObject oldSource = (InternalEObject)source;
			source = (DeployedOperation)eResolveProxy(oldSource);
			if (source != oldSource) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.AGGREGATED_INVOCATION__SOURCE, oldSource, source));
			}
		}
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedOperation basicGetSource() {
		return source;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSource(DeployedOperation newSource) {
		DeployedOperation oldSource = source;
		source = newSource;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.AGGREGATED_INVOCATION__SOURCE, oldSource, source));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedOperation getTarget() {
		if (target != null && target.eIsProxy()) {
			InternalEObject oldTarget = (InternalEObject)target;
			target = (DeployedOperation)eResolveProxy(oldTarget);
			if (target != oldTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.AGGREGATED_INVOCATION__TARGET, oldTarget, target));
			}
		}
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedOperation basicGetTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTarget(DeployedOperation newTarget) {
		DeployedOperation oldTarget = target;
		target = newTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.AGGREGATED_INVOCATION__TARGET, oldTarget, target));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExecutionRoot getExecutionRoot() {
		if (executionRoot != null && executionRoot.eIsProxy()) {
			InternalEObject oldExecutionRoot = (InternalEObject)executionRoot;
			executionRoot = (ExecutionRoot)eResolveProxy(oldExecutionRoot);
			if (executionRoot != oldExecutionRoot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.AGGREGATED_INVOCATION__EXECUTION_ROOT, oldExecutionRoot, executionRoot));
			}
		}
		return executionRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExecutionRoot basicGetExecutionRoot() {
		return executionRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetExecutionRoot(ExecutionRoot newExecutionRoot, NotificationChain msgs) {
		ExecutionRoot oldExecutionRoot = executionRoot;
		executionRoot = newExecutionRoot;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExecutionPackage.AGGREGATED_INVOCATION__EXECUTION_ROOT, oldExecutionRoot, newExecutionRoot);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setExecutionRoot(ExecutionRoot newExecutionRoot) {
		if (newExecutionRoot != executionRoot) {
			NotificationChain msgs = null;
			if (executionRoot != null)
				msgs = ((InternalEObject)executionRoot).eInverseRemove(this, ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS, ExecutionRoot.class, msgs);
			if (newExecutionRoot != null)
				msgs = ((InternalEObject)newExecutionRoot).eInverseAdd(this, ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS, ExecutionRoot.class, msgs);
			msgs = basicSetExecutionRoot(newExecutionRoot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.AGGREGATED_INVOCATION__EXECUTION_ROOT, newExecutionRoot, newExecutionRoot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExecutionPackage.AGGREGATED_INVOCATION__EXECUTION_ROOT:
				if (executionRoot != null)
					msgs = ((InternalEObject)executionRoot).eInverseRemove(this, ExecutionPackage.EXECUTION_ROOT__AGGREGATED_INVOCATIONS, ExecutionRoot.class, msgs);
				return basicSetExecutionRoot((ExecutionRoot)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExecutionPackage.AGGREGATED_INVOCATION__EXECUTION_ROOT:
				return basicSetExecutionRoot(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExecutionPackage.AGGREGATED_INVOCATION__SOURCE:
				if (resolve) return getSource();
				return basicGetSource();
			case ExecutionPackage.AGGREGATED_INVOCATION__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case ExecutionPackage.AGGREGATED_INVOCATION__EXECUTION_ROOT:
				if (resolve) return getExecutionRoot();
				return basicGetExecutionRoot();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ExecutionPackage.AGGREGATED_INVOCATION__SOURCE:
				setSource((DeployedOperation)newValue);
				return;
			case ExecutionPackage.AGGREGATED_INVOCATION__TARGET:
				setTarget((DeployedOperation)newValue);
				return;
			case ExecutionPackage.AGGREGATED_INVOCATION__EXECUTION_ROOT:
				setExecutionRoot((ExecutionRoot)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case ExecutionPackage.AGGREGATED_INVOCATION__SOURCE:
				setSource((DeployedOperation)null);
				return;
			case ExecutionPackage.AGGREGATED_INVOCATION__TARGET:
				setTarget((DeployedOperation)null);
				return;
			case ExecutionPackage.AGGREGATED_INVOCATION__EXECUTION_ROOT:
				setExecutionRoot((ExecutionRoot)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case ExecutionPackage.AGGREGATED_INVOCATION__SOURCE:
				return source != null;
			case ExecutionPackage.AGGREGATED_INVOCATION__TARGET:
				return target != null;
			case ExecutionPackage.AGGREGATED_INVOCATION__EXECUTION_ROOT:
				return executionRoot != null;
		}
		return super.eIsSet(featureID);
	}

} //AggregatedInvocationImpl