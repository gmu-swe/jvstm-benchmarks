package stamp.vacation.jvstm.parnest.annotated;

public abstract class Operation {

    public static boolean nestedParallelismOn;
    public static int numberAvailableThreads;
    public static boolean parallelizeUpdateTables;

    public abstract void doOperation();

}
