package stmbench7.impl.jvstm;

import jvstm.Transaction;
import stmbench7.OperationExecutor;
import stmbench7.OperationExecutorFactory;
import stmbench7.annotations.Immutable;
import stmbench7.core.Operation;

@Immutable
public class JVSTMOperationExecutorFactory extends OperationExecutorFactory {
	private Transaction tx;

	public OperationExecutor createOperationExecutor(Operation op) {
		return new JVSTMOperationExecutor(op);
	}

	@Override
	public void checkpoint() {
		tx = Transaction.begin();
	}

	@Override
	public void rollback() {
		tx.abortTx();
		tx = null;
	}

}
