package ch.bfh.progressor.executor.languages;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import ch.bfh.progressor.executor.CodeExecutor;
import ch.bfh.progressor.executor.ExecutorException;
import ch.bfh.progressor.executor.Helpers;
import ch.bfh.progressor.executor.Result;
import ch.bfh.progressor.executor.TestCase;
import ch.bfh.progressor.executor.executorConstants;

/**
 * Code execution engine for java code.
 * Uses a new process to execute the custom java code.
 *
 * @author strut1, touwm1 &amp; weidj1
 */
public class JavaProcessExecutor implements CodeExecutor {

	/** Character set to use for the custom code. */
	public static final Charset CODE_CHARSET = Charset.forName("UTF-8");

	protected static final Path CODE_TEMPLATE = Paths.get("resources", "java", "template-process.java");

	protected static final String CODE_CLASS_NAME = "CustomClass";

	protected static final String CODE_CUSTOM_FRAGMENT = "$CustomCode$";

	protected static final String TEST_CASES_FRAGMENT = "$TestCases$";

	/** Maximum time to use for for the compilation of the user code (in seconds). */
	public static final int COMPILE_TIMEOUT_SECONDS = 3;

	/** Maximum time for the compilation to finish after 'soft' destruction (in seconds). */
	public static final int COMPILE_DESTROY_TIMEOUT_SECONDS = 2;

	/** Maximum time to use for the execution of the user code (in seconds). */
	public static final int EXECUTION_TIMEOUT_SECONDS = 5;

	/** Maximum time for the execution to finish after 'soft' destruction (in seconds). */
	public static final int EXECUTION_DESTROY_TIMEOUT_SECONDS = 3;

	@Override
	public List<Result> execute(String codeFragment, List<TestCase> testCases) {

		List<Result> ret = new ArrayList<>(testCases.size());
		File dir = Paths.get("temp", UUID.randomUUID().toString()).toFile();

		try {
			if (!dir.exists() && !dir.mkdirs())
				throw new ExecutorException("Could not create a temporary directory for the user code.");

			List<String> code = Files.readAllLines(JavaProcessExecutor.CODE_TEMPLATE, JavaProcessExecutor.CODE_CHARSET);
			for (int i = 0; i < code.size(); i++)
				if (code.get(i).endsWith(JavaProcessExecutor.CODE_CUSTOM_FRAGMENT)) {
					code.remove(i);
					code.add(i, codeFragment);

				} else if (code.get(i).endsWith(JavaProcessExecutor.TEST_CASES_FRAGMENT)) {
					code.remove(i);
					code.add(i, this.getTestCaseSignatures(testCases));
				}

			Files.write(Paths.get(dir.getPath(), String.format("%s.java", JavaProcessExecutor.CODE_CLASS_NAME)), code);

			Runtime rnt = Runtime.getRuntime();
			Process cmp = rnt.exec("javac *.java", null, dir);
			if (cmp.waitFor(JavaProcessExecutor.COMPILE_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
				if (cmp.exitValue() != 0)
					throw new ExecutorException("Could not compile the user code.");

			} else {
				cmp.destroy();

				if (!cmp.waitFor(JavaProcessExecutor.COMPILE_DESTROY_TIMEOUT_SECONDS, TimeUnit.SECONDS))
					cmp.destroyForcibly();

				throw new ExecutorException("Could not compile the user code in time.");
			}

			for (int i = 0; i < testCases.size(); i++)
				try {
					Process exec = rnt.exec(String.format("java %s %d", JavaProcessExecutor.CODE_CLASS_NAME, i), null, dir);
					if (exec.waitFor(JavaProcessExecutor.EXECUTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
						if (exec.exitValue() != 0)
							throw new ExecutorException("Could not execute the user code.");

					} else {
						exec.destroy();

						if (!exec.waitFor(JavaProcessExecutor.EXECUTION_DESTROY_TIMEOUT_SECONDS, TimeUnit.SECONDS))
							exec.destroyForcibly();

						throw new ExecutorException("Could not execute the user code in time.");
					}

					try (Scanner outStm = new Scanner(exec.getInputStream(), JavaProcessExecutor.CODE_CHARSET.name())) {
						String res = outStm.useDelimiter("\\A").next();
						ret.add(new Result().setSuccess("OK".equals(res)).setResult(res));
					}

				} catch (Exception ex) {
					ret.add(new Result().setSuccess(false).setResult(Helpers.getExceptionMessage("Could not invoke the user code.", ex)));
				}

		} catch (Throwable ex) {
			Result res = new Result().setSuccess(false).setResult(Helpers.getExceptionMessage("Could not load the user code.", ex));
			for (int i = ret.size(); i < testCases.size(); i++)
				ret.add(res);

		} finally {
			if (dir.exists())
				this.deleteRecursive(dir);
		}

		return ret;
	}

	private boolean deleteRecursive(File file) {

		boolean ret = true;

		File[] children;
		if (file.isDirectory() && (children = file.listFiles()) != null)
			for (File child : children)
				ret &= this.deleteRecursive(child);
		ret &= file.delete();

		return ret;
	}

	private String getTestCaseSignatures(List<TestCase> testCases) throws ExecutorException {

		String nl = String.format("%n");

		StringBuilder sb = new StringBuilder("switch (index) {").append(nl);
		for (int i = 0; i < testCases.size(); i++) {
			TestCase testCase = testCases.get(i);

			sb.append("case ").append(i).append(": ");
			sb.append(JavaProcessExecutor.CODE_CLASS_NAME).append(".assertResult(inst.").append(testCase.getFunctionName()).append("(");

			if (testCase.getInputValuesSize() != testCase.getInputTypesSize())
				throw new ExecutorException("The same number of input values & types have to be defined..");

			for (int j = 0; j < testCase.getInputValuesSize(); j++) {
				if (j > 0) sb.append(", ");
				sb.append(this.getValueLiteral(testCase.getInputValues().get(j), testCase.getInputTypes().get(j)));
			}

			if (testCase.getExpectedOutputValuesSize() != 1 || testCase.getOutputTypesSize() != 1)
				throw new ExecutorException("Exactly one output value has to be defined for a java sample.");

			sb.append("), ").append(this.getValueLiteral(testCase.getExpectedOutputValues().get(0), testCase.getOutputTypes().get(0))).append(");");
			sb.append(" break;").append(nl);
		}
		sb.append("}");

		return sb.toString();
	}

	private String getValueLiteral(String value, String type) throws ExecutorException {

		if ("null".equals(value))
			return "null";

		boolean isArr = type.startsWith(String.format("%s<", executorConstants.TypeContainerArray));
		boolean isLst = type.startsWith(String.format("%s<", executorConstants.TypeContainerList));
		boolean isSet = type.startsWith(String.format("%s<", executorConstants.TypeContainerSet));

		if (isArr || isLst || isSet) {
			int cntTypLen = (isArr ? executorConstants.TypeContainerArray : isLst ? executorConstants.TypeContainerList : executorConstants.TypeContainerSet).length();
			String elmTyp = type.substring(cntTypLen + 1, type.length() - cntTypLen - 2);

			if (elmTyp.split(",\\s*").length != 1)
				throw new ExecutorException("Array, List & Set types need 1 type parameter.");

			StringBuilder sb = new StringBuilder();
			if (isArr)
				sb.append("new ").append(this.getJavaClass(elmTyp)).append("[] { ");
			else if (isLst)
				sb.append(String.format("Arrays.<%s>asList(", this.getJavaClass(elmTyp)));
			else
				sb.append(String.format("new HashSet<%1$s>(Arrays.<%1$s>asList(", this.getJavaClass(elmTyp)));

			boolean first = true;
			for (String elm : value.split(",\\s*")) {
				if (first) first = false;
				else sb.append(", ");
				sb.append(this.getValueLiteral(elm, elmTyp));
			}

			return sb.append(isArr ? " }" : isLst ? ')' : "))").toString();

		} else if (type.startsWith(String.format("%s<", executorConstants.TypeContainerMap))) {
			String elmTyp = type.substring(executorConstants.TypeContainerMap.length() + 1, type.length() - executorConstants.TypeContainerMap.length() - 2);
			String[] kvTyps = elmTyp.split(",\\s*");

			if (kvTyps.length != 2)
				throw new ExecutorException("Map type needs 2 type parameters.");

			StringBuilder sb = new StringBuilder();
			sb.append(String.format("new HashMap<%s, %s>() {{ ", this.getJavaClass(kvTyps[0]), this.getJavaClass(kvTyps[1])));

			for (String ety : value.split(",\\s*")) {
				String[] kv = ety.split(":\\s*");

				if (kv.length != 2)
					throw new ExecutorException("Map entries always need a key and a value.");

				sb.append("put(").append(this.getValueLiteral(kv[0], kvTyps[0])).append(", ").append(this.getValueLiteral(kv[0], kvTyps[0])).append("); ");
			}
			return sb.append("}};").toString();
		}

		switch (type) {
			case executorConstants.TypeString:
				return String.format("\"%s\"", value);

			case executorConstants.TypeCharacter:
				return String.format("'%s'", value);

			case executorConstants.TypeBoolean:
				return Boolean.toString("true".equalsIgnoreCase(value));

			case executorConstants.TypeByte:
			case executorConstants.TypeShort:
			case executorConstants.TypeInteger:
				return String.format("%d", Integer.parseInt(value, 10));

			case executorConstants.TypeLong:
				return String.format("%dL", Long.parseLong(value, 10));

			case executorConstants.TypeSingle:
				return String.format("%ff", Float.parseFloat(value));

			case executorConstants.TypeDouble:
				return String.format("%f", Double.parseDouble(value));

			case executorConstants.TypeDecimal:
				return String.format("new BigDecimal(\"%s\")", value);

			default:
				throw new ExecutorException(String.format("Value type %s is not supported.", type));
		}
	}

	private String getJavaClass(String type) throws ExecutorException {

		if (type.startsWith(String.format("%s<", executorConstants.TypeContainerArray))) {
			String typeParam = type.substring(executorConstants.TypeContainerArray.length() + 1, type.length() - executorConstants.TypeContainerArray.length() - 2);
			return String.format("%s[]", this.getJavaClass(typeParam));

		} else if (type.startsWith(String.format("%s<", executorConstants.TypeContainerList))) {
			String typeParam = type.substring(executorConstants.TypeContainerList.length() + 1, type.length() - executorConstants.TypeContainerList.length() - 2);
			return String.format("List<%s>", this.getJavaClass(typeParam));

		} else if (type.startsWith(String.format("%s<", executorConstants.TypeContainerSet))) {
			String typeParam = type.substring(executorConstants.TypeContainerSet.length() + 1, type.length() - executorConstants.TypeContainerSet.length() - 2);
			return String.format("Set<%s>", this.getJavaClass(typeParam));

		} else if (type.startsWith(String.format("%s<", executorConstants.TypeContainerMap))) {
			String typeParams = type.substring(executorConstants.TypeContainerMap.length() + 1, type.length() - executorConstants.TypeContainerMap.length() - 2);
			String[] typeParamsArray = typeParams.split(",\\s*");
			return String.format("Map<%s, %s>", this.getJavaClass(typeParamsArray[0]), this.getJavaClass(typeParamsArray[1]));
		}

		switch (type) {
			case executorConstants.TypeString:
				return "String";

			case executorConstants.TypeCharacter:
				return "Character";

			case executorConstants.TypeBoolean:
				return "Boolean";

			case executorConstants.TypeByte:
				return "Byte";

			case executorConstants.TypeShort:
				return "Short";

			case executorConstants.TypeInteger:
				return "Integer";

			case executorConstants.TypeLong:
				return "Long";

			case executorConstants.TypeSingle:
				return "Float";

			case executorConstants.TypeDouble:
				return "Double";

			case executorConstants.TypeDecimal:
				return "BigDecimal";

			default:
				throw new ExecutorException(String.format("Value type %s is not supported.", type));
		}
	}
}
