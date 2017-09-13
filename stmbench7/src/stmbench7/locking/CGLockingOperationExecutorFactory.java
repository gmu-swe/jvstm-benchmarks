package stmbench7.locking;

import stmbench7.OperationExecutor;
import stmbench7.OperationExecutorFactory;
import stmbench7.Setup;
import stmbench7.core.Operation;

/**
 * An implementation of the OperationExecutorFactory
 * for the coarse-grained locking synchronization.
 */
public class CGLockingOperationExecutorFactory extends OperationExecutorFactory {

	@Override
	public OperationExecutor createOperationExecutor(Operation op) {
		return new CGLockingOperationExecutor(op);
	}

	@Override
	public void checkpoint(Setup setup) {
		throw new Error("Not implemented");
	}

	@Override
	public void rollback(Setup setup) {
		throw new Error("Not implemented");
	}
}
