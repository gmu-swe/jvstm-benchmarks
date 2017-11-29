package stmbench7.impl.deucestm;

import org.deuce.transaction.Context;
import org.deuce.transaction.ContextDelegator;

import stmbench7.Benchmark;
import stmbench7.OperationExecutor;
import stmbench7.OperationExecutorFactory;
import stmbench7.Setup;
import stmbench7.core.Operation;
import stmbench7.impl.DefaultOperationExecutor;

public class DeuceSTMOperationExecutorFactory extends OperationExecutorFactory {

	private Context context = ContextDelegator.getInstance();

	@Override
	public OperationExecutor createOperationExecutor(Operation op) {
		if(op.getOperationId() != null)
			return new DeuceSTMOperationExecutor(op, this.context);
		return new DefaultOperationExecutor(op);
	}

	@Override
	public void checkpoint(Setup setup) {
		if (!Benchmark.doCheckpointRollback) {
			return;
		}

		context.init(0, "Checkpoint");
	}

	@Override
	public void rollback(Setup setup) {
		if (!Benchmark.doCheckpointRollback)
			return;

		context.rollback();
	}

}
