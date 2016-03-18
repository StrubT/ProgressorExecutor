package ch.bfh.progressor.executor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import ch.bfh.progressor.executor.thrift.FunctionSignature;
import ch.bfh.progressor.executor.thrift.Result;
import ch.bfh.progressor.executor.thrift.TestCase;

/**
 * Interface to be implemented by all code execution engines.
 *
 * @author strut1, touwm1 &amp; weidj1
 */

public abstract class CodeExecutor {

	/**
	 * Character set to use for the custom code.
	 */
	public static final Charset RESOURCE_CHARSET = Charset.forName("UTF-8");

	/**
	 * Character set to use for the console output.
	 */
	public static final Charset CONSOLE_CHARSET;

	/**
	 * Name of the class as defined in the template.
	 */
	protected static final String CODE_CLASS_NAME = "CustomClass";

	/**
	 * Placeholder for the custom code fragment as defined in the template.
	 */
	protected static final String CODE_CUSTOM_FRAGMENT = "$CustomCode$";

	/**
	 * Placeholder for the test cases as defined in the template.
	 */
	protected static final String TEST_CASES_FRAGMENT = "$TestCases$";

	public static final Pattern PARAMETER_SEPARATOR_PATTERN = Pattern.compile(",\\s*");

	public static final Pattern KEY_VALUE_SEPARATOR_PATTERN = Pattern.compile(":\\s*");

	private List<String> blacklist;
	private StringBuilder template;

	static {

		try (OutputStreamWriter osw = new OutputStreamWriter(System.out)) {
			CONSOLE_CHARSET = Charset.forName(osw.getEncoding());
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	/**
	 * Gets the unique name of the language the executor supports.
	 *
	 * @return unique name of the supported language
	 */
	public abstract String getLanguage();

	protected String getCodeTemplatePath() {
		return String.format("%s/template.txt", getLanguage());
	}

	protected String getCodeBlacklistPath() {
		return String.format("%s/blacklist.json", getLanguage());
	}

	/**
	 * Gets the fragment(s) for the function signatures in the language the executor supports.
	 *
	 * @param functions function signatures to get fragment(s) for
	 *
	 * @return fragment(s) for the function signatures in the supported language
	 *
	 * @throws ExecutorException if the fragment could not be generated
	 */
	public abstract String getFragment(List<FunctionSignature> functions) throws ExecutorException;

	/**
	 * Gets the blacklist containing the strings not allowed in the code fragment.
	 *
	 * @return a {@link Collection} containing the strings not allowed in the code fragment
	 *
	 * @throws ExecutorException if the fragment could not be read
	 */
	public Collection<String> getBlacklist() throws ExecutorException {

		if (this.blacklist == null)
			try (InputStreamReader reader = new InputStreamReader(this.getClass().getResourceAsStream(getCodeBlacklistPath()), RESOURCE_CHARSET)) {
				this.blacklist = new ArrayList<>();
				JSONTokener tokener = new JSONTokener(reader);

				if (!tokener.more()) throw new JSONException("No root elements present.");
				JSONArray groups = (JSONArray)tokener.nextValue();
				if (!tokener.more()) throw new JSONException("Multiple root elements present.");

				for (int i = 0; i < groups.length(); i++) {
					JSONObject group = groups.getJSONObject(i);
					JSONArray elements = group.getJSONArray("elements");

					for (int j = 0; j < elements.length(); j++) {
						JSONObject element = elements.getJSONObject(j);
						this.blacklist.add(element.getString("keyword"));
					}
				}

			} catch (IOException | JSONException | ClassCastException ex) {
				throw new ExecutorException(true, "Could not read the blacklist.", ex);
			}

		return Collections.unmodifiableList(this.blacklist);
	}

	protected StringBuilder getTemplate() throws IOException {

		final String newLine = String.format("%n");

		if (this.template == null)
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream(getCodeTemplatePath()), RESOURCE_CHARSET))) {
				this.template = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) //read template to StringBuilder
					this.template.append(line).append(newLine);
			}

		return new StringBuilder(this.template); //return a new string builder every time
	}

	/**
	 * Executes a provided code fragment.
	 *
	 * @param codeFragment code fragment to execute
	 * @param functions    function signatures to execute tests on
	 * @param testCases    test cases to execute
	 *
	 * @return a {@link List} containing the {@link Result} for each test case
	 */
	public abstract List<Result> execute(String codeFragment, List<FunctionSignature> functions, List<TestCase> testCases);

	/**
	 * Executes a provided code fragment.
	 *
	 * @param codeFragment code fragment to execute
	 * @param functions    function signatures to execute tests on
	 * @param testCases    test cases to execute
	 *
	 * @return a {@link List} containing the {@link Result} for each test case
	 */
	public List<Result> execute(String codeFragment, List<FunctionSignature> functions, TestCase... testCases) {

		return this.execute(codeFragment, functions, Arrays.asList(testCases));
	}

	protected boolean deleteRecursive(File file) {

		boolean ret = true;

		File[] children; //recursively delete children
		if (file.isDirectory() && (children = file.listFiles()) != null)
			for (File child : children)
				ret &= this.deleteRecursive(child);

		ret &= file.delete(); //delete file itself
		return ret;
	}

	protected String readConsole(Process process) throws ExecutorException {

		final String newLine = String.format("%n");

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), CONSOLE_CHARSET))) {
			StringBuilder sb = new StringBuilder();

			String line; //read every line
			while ((line = reader.readLine()) != null)
				sb.append(line).append(newLine);

			return sb.toString(); //create concatenated string

		} catch (IOException ex) {
			throw new ExecutorException(false, "Could not read the console output.", ex);
		}
	}
}