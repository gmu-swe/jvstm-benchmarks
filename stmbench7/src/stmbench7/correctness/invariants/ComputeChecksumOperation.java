package stmbench7.correctness.invariants;

import stmbench7.OperationId;
import stmbench7.Setup;
import stmbench7.annotations.Immutable;
import stmbench7.annotations.ReadOnly;
import stmbench7.annotations.ThreadLocal;
import stmbench7.core.Assembly;
import stmbench7.core.AtomicPart;
import stmbench7.core.BaseAssembly;
import stmbench7.core.ComplexAssembly;
import stmbench7.core.CompositePart;
import stmbench7.core.Document;
import stmbench7.core.Manual;
import stmbench7.core.Module;
import stmbench7.core.Operation;
import stmbench7.core.OperationFailedException;

/**
 * Performs the check of data structure invariants. The operation is always
 * performed in a single thread, without any concurrency, and so it does not
 * have to be thread-safe. However, it is executed by an OperationExecutor, as
 * any other operation.
 */
@Immutable
@ThreadLocal
public class ComputeChecksumOperation implements Operation {

	private final Setup setup;
	private int checksum = 0;

	public ComputeChecksumOperation(Setup setup) {
		this.setup = setup;
	}

	@ReadOnly
	public int performOperation() throws OperationFailedException {
		this.checksum = checksumModule(setup.getModule());
		return 0;
	}

	public int getChecksum() {
		return checksum;
	}

	public OperationId getOperationId() {
		return null;
	}

	private int checksumModule(Module m) {
		return m.getId()
				^ m.getBuildDate()
				^ m.getType().hashCode()
				^ checksumComplexAssembly(m.getDesignRoot())
				^ checksumManual(m.getManual());
	}

	private int checksumManual(Manual m) {
		return m.getId()
				^ m.getText().hashCode()
				^ m.getTitle().hashCode();
	}

	private int checksumComplexAssembly(ComplexAssembly ca) {
		int ret = ca.getId()
				^ ca.getBuildDate()
				^ ca.getLevel()
				^ ca.getType().hashCode();

		for (Assembly a : ca.getSubAssemblies()) {
			if (a instanceof ComplexAssembly)
				ret ^= checksumComplexAssembly((ComplexAssembly)a);
			else
				ret ^= checksumBaseAssembly((BaseAssembly)a);
		}

		return ret;
	}

	private int checksumBaseAssembly(BaseAssembly ba) {
		int ret = ba.getId()
				^ ba.getBuildDate()
				^ ba.getType().hashCode();

		for (CompositePart cp : ba.getComponents()) {
			ret ^= checksumCompositePart(cp);
		}

		return ret;
	}

	private int checksumCompositePart(CompositePart cp) {
		int ret = cp.getId()
				^ cp.getBuildDate()
				^ cp.getType().hashCode()
				^ checksumDocument(cp.getDocumentation())
				^ checksumAtomicPart(cp.getRootPart());

		for (AtomicPart ap : cp.getParts()) {
			ret ^= checksumAtomicPart(ap);
		}

		return ret;
	}

	private int checksumDocument(Document d) {
		return d.getDocumentId()
				^ d.getTitle().hashCode()
				^ d.getText().hashCode();
	}

	private int checksumAtomicPart(AtomicPart ap) {
		return ap.getId()
				^ ap.getBuildDate()
				^ ap.getX()
				^ ap.getY()
				^ ap.getNumToConnections()
				^ ap.getType().hashCode();
	}
}
