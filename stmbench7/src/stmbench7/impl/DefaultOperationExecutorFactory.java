package stmbench7.impl;

import net.jonbell.crij.runtime.CRIJInstrumented;
import net.jonbell.crij.runtime.CheckpointRollbackAgent;
import stmbench7.Benchmark;
import stmbench7.OperationExecutor;
import stmbench7.OperationExecutorFactory;
import stmbench7.Setup;
import stmbench7.annotations.Immutable;
import stmbench7.core.Operation;

/**
 * Default implementation of an OperationExecutorFactory.
 * It creates an OperationExecutor that does not provide
 * any synchronization between threads.
 */
@Immutable
public class DefaultOperationExecutorFactory extends OperationExecutorFactory {

	public OperationExecutor createOperationExecutor(Operation op) {
		return new DefaultOperationExecutor(op);
	}

	@Override
	public void checkpoint(Setup setup) {
		if (!Benchmark.doCheckpointRollback)
			return;

//		CheckpointRollbackAgent.checkpointHeapRoots();
		((CRIJInstrumented)setup).$$crijCheckpoint(CheckpointRollbackAgent.getNewVersionForCheckpoint());
		setup.getModule(); // Touch something to propagate the checkpoint one level in
	}

	@Override
	public void rollback(Setup setup) {
		if (!Benchmark.doCheckpointRollback)
			return;

//		CheckpointRollbackAgent.rollbackHeapRoots();
		((CRIJInstrumented)setup).$$crijRollback(CheckpointRollbackAgent.getNewVersionForRollback());
		setup.getModule(); // Touch something to propagate the rollback one level in
	}
}
