package bk.hungd.App;

public class AppDebug {

	public static final boolean DEBUG = true;

	public static void log(String message) {
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

		System.out.println(" -> " + className + "." + methodName + "(" + lineNumber + ") " + message);
	}

	public static void error(String message) {
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

		System.err.println(" -> " + className + "." + methodName + "(" + lineNumber + ") " + message);
	}

	public static void error(Exception e, String message) {
		String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
		String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();

		System.err.println(" -> " +e.getClass() +": " + className + "." + methodName + "(" + lineNumber + ") " +e.getMessage()+": " + message+"\n");
		e.printStackTrace();
	}

}