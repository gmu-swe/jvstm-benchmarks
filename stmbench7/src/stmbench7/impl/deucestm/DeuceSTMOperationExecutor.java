package stmbench7.impl.deucestm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.deuce.transaction.Context;

import stmbench7.Benchmark;
import stmbench7.OperationExecutor;
import stmbench7.core.Operation;
import stmbench7.core.OperationFailedException;

public class DeuceSTMOperationExecutor implements OperationExecutor {

	private final Operation op;
	private final Context context;

	private static Method m;

	public DeuceSTMOperationExecutor(Operation op, Context context) {
		this.op = op;
		this.context = context;
	}

	public int execute() throws OperationFailedException {
		if (!Benchmark.useTransactions)
			return op.performOperation();
        if (context == null || (!Benchmark.doCheckpointRollback && Benchmark.useTransactions))
        	return doExecute();
		else {
        	if (m == null) {
        		try {
        			if (!Benchmark.useTransactions)
        				m = this.getClass().getDeclaredMethod("doExecute");
        			else
        				m = this.getClass().getDeclaredMethod("doExecute", Context.class);
				} catch (NoSuchMethodException | SecurityException e) {
					throw new Error(e);
				}
        	}

        	try {
				return (int) m.invoke(this, this.context);
			} catch (IllegalAccessException | IllegalArgumentException e) {
				throw new Error(e);
			} catch (InvocationTargetException e) {
				if (e.getTargetException() instanceof OperationFailedException)
					throw (OperationFailedException) e.getTargetException();
				else
					throw new Error(e);
			}
        }
	}

        @org.deuce.Atomic
	private int doExecute() throws OperationFailedException {
		return op.performOperation();
	}

	public int getLastOperationTimestamp() {
		return 0;
	}

	@Override
	public boolean isOperationReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public int getLastLocalOperationTimestamp() {
		// TODO Auto-generated method stub
		return 0;
	}

}
