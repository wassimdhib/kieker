/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.modeltests;

import org.eclipse.emf.ecore.EObject;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureFactory;
import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentFactory;

/**
 * @author S�ren Henning
 *
 * @since 1.13
 */
public class WithMapTester {

	public WithMapTester() {}

	public static void main(final String[] args) {

		final ArchitectureFactory factory = ArchitectureFactory.eINSTANCE;

		// create the model
		final ArchitectureRoot architectureRoot = factory.createArchitectureRoot();
		final ComponentType componentType = factory.createComponentType();
		componentType.setSignature("org.software.component");
		architectureRoot.getComponentTypes().put("org.software.component", componentType);
		final OperationType operationType1 = factory.createOperationType();
		operationType1.setSignature("public void doSomething()");
		componentType.getProvidedOperations().put("public void doSomething()", operationType1);
		final OperationType operationType2 = factory.createOperationType();
		operationType2.setSignature("public String createSomeString()");
		componentType.getProvidedOperations().put("public String createSomeString()", operationType2);

		final DeploymentFactory deploymentFactory = DeploymentFactory.eINSTANCE;
		final DeploymentContext deploymentContext = deploymentFactory.createDeploymentContext();
		deploymentContext.setName("srv01");

		final DeployedComponent deployedComponent = deploymentFactory.createDeployedComponent();
		deployedComponent.setComponentType(componentType);
		deploymentContext.getComponents().put("org.software.component", deployedComponent);

		if (deploymentContext.getComponents().containsKey("org.software.component")) {
			final DeployedComponent component = deploymentContext.getComponents().get("org.software.component");
			final EObject container = component.eContainer();
			final EObject containerContainer = component.eContainer().eContainer();
			final DeploymentContext context = component.getDeploymentContext();
			containerContainer.eClass();
		}

	}

}
