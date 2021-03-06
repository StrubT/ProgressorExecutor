package ch.bfh.progressor.executor.tests;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ch.bfh.progressor.executor.api.CodeExecutor;
import ch.bfh.progressor.executor.api.ExecutorException;
import ch.bfh.progressor.executor.api.Result;
import ch.bfh.progressor.executor.api.VersionInformation;
import ch.bfh.progressor.executor.impl.FunctionSignatureImpl;
import ch.bfh.progressor.executor.impl.TestCaseImpl;
import ch.bfh.progressor.executor.thrift.FunctionSignature;
import ch.bfh.progressor.executor.thrift.TestCase;
import ch.bfh.progressor.executor.thrift.executorConstants;

public abstract class CodeExecutorTestBase {

	protected static final List<FunctionSignature> FUNCTIONS = Arrays.asList(new FunctionSignature("helloWorld", Collections.emptyList(), Collections.emptyList(), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeString)),
																																					 new FunctionSignature("concatStrings", Arrays.asList("a", "b"), Arrays.asList(executorConstants.TypeString, executorConstants.TypeString), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeString)),
																																					 new FunctionSignature("minChar", Arrays.asList("a", "b"), Arrays.asList(executorConstants.TypeCharacter, executorConstants.TypeCharacter), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeCharacter)),
																																					 new FunctionSignature("exor", Arrays.asList("a", "b"), Arrays.asList(executorConstants.TypeBoolean, executorConstants.TypeBoolean), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeBoolean)),
																																					 new FunctionSignature("sumInt8", Arrays.asList("a", "b"), Arrays.asList(executorConstants.TypeInt8, executorConstants.TypeInt8), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeInt8)),
																																					 new FunctionSignature("sumInt16", Arrays.asList("a", "b"), Arrays.asList(executorConstants.TypeInt16, executorConstants.TypeInt16), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeInt16)),
																																					 new FunctionSignature("sumInt32", Arrays.asList("a", "b"), Arrays.asList(executorConstants.TypeInt32, executorConstants.TypeInt32), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeInt32)),
																																					 new FunctionSignature("sumInt64", Arrays.asList("a", "b"), Arrays.asList(executorConstants.TypeInt64, executorConstants.TypeInt64), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeInt64)),
																																					 new FunctionSignature("sumFloat32", Arrays.asList("a", "b"), Arrays.asList(executorConstants.TypeFloat32, executorConstants.TypeFloat32), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeFloat32)),
																																					 new FunctionSignature("sumFloat64", Arrays.asList("a", "b"), Arrays.asList(executorConstants.TypeFloat64, executorConstants.TypeFloat64), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeFloat64)),
																																					 new FunctionSignature("sumDecimal", Arrays.asList("a", "b"), Arrays.asList(executorConstants.TypeDecimal, executorConstants.TypeDecimal), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeDecimal)),
																																					 new FunctionSignature("sumInt32Array", Arrays.asList("a", "l"), Arrays.asList(String.format("%s<%s>", executorConstants.TypeContainerArray, executorConstants.TypeInt32), executorConstants.TypeInt32), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeInt32)),
																																					 new FunctionSignature("sumInt32List", Collections.singletonList("l"), Collections.singletonList(String.format("%s<%s>", executorConstants.TypeContainerList, executorConstants.TypeInt32)), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeInt32)),
																																					 new FunctionSignature("sumInt32Set", Collections.singletonList("s"), Collections.singletonList(String.format("%s<%s>", executorConstants.TypeContainerSet, executorConstants.TypeInt32)), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeInt32)),
																																					 new FunctionSignature("getMapEntry", Arrays.asList("m", "k"), Arrays.asList(String.format("%s<%s, %s>", executorConstants.TypeContainerMap, executorConstants.TypeInt32, executorConstants.TypeString), executorConstants.TypeInt32), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeString)),
																																					 new FunctionSignature("getMapListEntry", Arrays.asList("m", "k", "i"), Arrays.asList(String.format("%s<%s, %s<%s>>", executorConstants.TypeContainerMap, executorConstants.TypeInt32, executorConstants.TypeContainerList, executorConstants.TypeString), executorConstants.TypeInt32, executorConstants.TypeInt32), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeString)),
																																					 new FunctionSignature("intersectArray", Arrays.asList("a1", "l1", "a2", "l2"), Arrays.asList(String.format("%s<%s>", executorConstants.TypeContainerArray, executorConstants.TypeInt32), executorConstants.TypeInt32, String.format("%s<%s>", executorConstants.TypeContainerArray, executorConstants.TypeInt32), executorConstants.TypeInt32), Collections.singletonList("return"), Collections.singletonList(String.format("%s<%s>", executorConstants.TypeContainerArray, executorConstants.TypeInt32))),
																																					 new FunctionSignature("intersectList", Arrays.asList("l1", "l2"), Arrays.asList(String.format("%s<%s>", executorConstants.TypeContainerList, executorConstants.TypeInt32), String.format("%s<%s>", executorConstants.TypeContainerList, executorConstants.TypeInt32)), Collections.singletonList("return"), Collections.singletonList(String.format("%s<%s>", executorConstants.TypeContainerList, executorConstants.TypeInt32))),
																																					 new FunctionSignature("intersectSet", Arrays.asList("s1", "s2"), Arrays.asList(String.format("%s<%s>", executorConstants.TypeContainerSet, executorConstants.TypeInt32), String.format("%s<%s>", executorConstants.TypeContainerSet, executorConstants.TypeInt32)), Collections.singletonList("return"), Collections.singletonList(String.format("%s<%s>", executorConstants.TypeContainerSet, executorConstants.TypeInt32))),
																																					 new FunctionSignature("intersectMap", Arrays.asList("m1", "m2"), Arrays.asList(String.format("%s<%s, %s>", executorConstants.TypeContainerMap, executorConstants.TypeInt32, executorConstants.TypeString), String.format("%s<%s, %s>", executorConstants.TypeContainerMap, executorConstants.TypeInt32, executorConstants.TypeString)), Collections.singletonList("return"), Collections.singletonList(String.format("%s<%s, %s>", executorConstants.TypeContainerMap, executorConstants.TypeInt32, executorConstants.TypeString))),
																																					 new FunctionSignature("infiniteLoop", Collections.emptyList(), Collections.emptyList(), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeInt32)),
																																					 new FunctionSignature("recursion", Collections.emptyList(), Collections.emptyList(), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeInt32)),
																																					 new FunctionSignature("error", Collections.emptyList(), Collections.emptyList(), Collections.singletonList("return"), Collections.singletonList(executorConstants.TypeInt32)));

	protected static final List<TestCase> TEST_CASES_SUCCESS = Arrays.asList(new TestCase("helloWorld", Collections.emptyList(), Collections.singletonList("Hello, World!")),
																																					 new TestCase("concatStrings", Arrays.asList("Hello, ", "World!"), Collections.singletonList("Hello, World!")),
																																					 new TestCase("concatStrings", Arrays.asList("Héllô, ", "Wörld£"), Collections.singletonList("Héllô, Wörld£")),
																																					 new TestCase("concatStrings", Arrays.asList("Ңӗllō, ", "Жөrld£"), Collections.singletonList("Ңӗllō, Жөrld£")),
																																					 new TestCase("minChar", Arrays.asList("a", "b"), Collections.singletonList("a")),
																																					 new TestCase("minChar", Arrays.asList("b", "a"), Collections.singletonList("a")),
																																					 new TestCase("exor", Arrays.asList("false", "false"), Collections.singletonList("false")),
																																					 new TestCase("exor", Arrays.asList("true", "false"), Collections.singletonList("true")),
																																					 new TestCase("exor", Arrays.asList("false", "true"), Collections.singletonList("true")),
																																					 new TestCase("exor", Arrays.asList("true", "true"), Collections.singletonList("false")),
																																					 new TestCase("sumInt8", Arrays.asList("0", "0"), Collections.singletonList("0")),
																																					 new TestCase("sumInt8", Arrays.asList("-1", "1"), Collections.singletonList("0")),
																																					 new TestCase("sumInt8", Arrays.asList("0", "1"), Collections.singletonList("1")),
																																					 new TestCase("sumInt8", Arrays.asList("2", "3"), Collections.singletonList("5")),
																																					 new TestCase("sumInt16", Arrays.asList("0", "0"), Collections.singletonList("0")),
																																					 new TestCase("sumInt16", Arrays.asList("-1", "1"), Collections.singletonList("0")),
																																					 new TestCase("sumInt16", Arrays.asList("0", "1"), Collections.singletonList("1")),
																																					 new TestCase("sumInt16", Arrays.asList("2", "3"), Collections.singletonList("5")),
																																					 new TestCase("sumInt32", Arrays.asList("0", "0"), Collections.singletonList("0")),
																																					 new TestCase("sumInt32", Arrays.asList("-1", "1"), Collections.singletonList("0")),
																																					 new TestCase("sumInt32", Arrays.asList("0", "1"), Collections.singletonList("1")),
																																					 new TestCase("sumInt32", Arrays.asList("2", "3"), Collections.singletonList("5")),
																																					 new TestCase("sumInt64", Arrays.asList("0", "0"), Collections.singletonList("0")),
																																					 new TestCase("sumInt64", Arrays.asList("-1", "1"), Collections.singletonList("0")),
																																					 new TestCase("sumInt64", Arrays.asList("0", "1"), Collections.singletonList("1")),
																																					 new TestCase("sumInt64", Arrays.asList("2", "3"), Collections.singletonList("5")),
																																					 new TestCase("sumFloat32", Arrays.asList("0.0", "0.0"), Collections.singletonList("+0.0")),
																																					 new TestCase("sumFloat32", Arrays.asList("0.0", "0.0"), Collections.singletonList("-0.0")),
																																					 new TestCase("sumFloat32", Arrays.asList("-1.1", "+1.1"), Collections.singletonList("0.0")),
																																					 new TestCase("sumFloat32", Arrays.asList("0.0", "3.1415926535897932385"), Collections.singletonList("3.1415926535897932385")),
																																					 new TestCase("sumFloat32", Arrays.asList("3.1415926535897932385", "2.135135483544684"), Collections.singletonList("5.2767281371344772385")),
																																					 new TestCase("sumFloat64", Arrays.asList("0.0", "0.0"), Collections.singletonList("+0.0")),
																																					 new TestCase("sumFloat64", Arrays.asList("0.0", "0.0"), Collections.singletonList("-0.0")),
																																					 new TestCase("sumFloat64", Arrays.asList("-1.1", "+1.1"), Collections.singletonList("0.0")),
																																					 new TestCase("sumFloat64", Arrays.asList("0.0", "3.1415926535897932385"), Collections.singletonList("3.1415926535897932385")),
																																					 new TestCase("sumFloat64", Arrays.asList("3.1415926535897932385", "2.135135483544684"), Collections.singletonList("5.2767281371344772385")),
																																					 new TestCase("sumDecimal", Arrays.asList("0.0", "0.0"), Collections.singletonList("+0.0")),
																																					 new TestCase("sumDecimal", Arrays.asList("0.0", "0.0"), Collections.singletonList("-0.0")),
																																					 new TestCase("sumDecimal", Arrays.asList("-1.1", "+1.1"), Collections.singletonList("0.0")),
																																					 new TestCase("sumDecimal", Arrays.asList("0.0", "3.1415926535897932385"), Collections.singletonList("3.1415926535897932385")),
																																					 new TestCase("sumDecimal", Arrays.asList("3.1415926535897932385", "2.135135483544684"), Collections.singletonList("5.2767281371344772385")),
																																					 new TestCase("sumInt32Array", Arrays.asList("{}", "0"), Collections.singletonList("0")),
																																					 new TestCase("sumInt32Array", Arrays.asList("{0}", "1"), Collections.singletonList("0")),
																																					 new TestCase("sumInt32Array", Arrays.asList("{ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97 }", "25"), Collections.singletonList("1060")),
																																					 new TestCase("sumInt32Array", Arrays.asList("{1,2,3,5,8,13,21,34,55,89,144,233,377,610,987,1597,2584,4181,6765,10946,17711,28657,46368,75025,121393,196418,317811,514229}", "28"), Collections.singletonList("1346267")),
																																					 new TestCase("sumInt32List", Collections.singletonList("{}"), Collections.singletonList("0")),
																																					 new TestCase("sumInt32List", Collections.singletonList("{0}"), Collections.singletonList("0")),
																																					 new TestCase("sumInt32List", Collections.singletonList("{ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97 }"), Collections.singletonList("1060")),
																																					 new TestCase("sumInt32List", Collections.singletonList("{1,2,3,5,8,13,21,34,55,89,144,233,377,610,987,1597,2584,4181,6765,10946,17711,28657,46368,75025,121393,196418,317811,514229}"), Collections.singletonList("1346267")),
																																					 new TestCase("sumInt32Set", Collections.singletonList("{}"), Collections.singletonList("0")),
																																					 new TestCase("sumInt32Set", Collections.singletonList("{0}"), Collections.singletonList("0")),
																																					 new TestCase("sumInt32Set", Collections.singletonList("{ 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97 }"), Collections.singletonList("1060")),
																																					 new TestCase("sumInt32Set", Collections.singletonList("{1,2,3,5,8,13,21,34,55,89,144,233,377,610,987,1597,2584,4181,6765,10946,17711,28657,46368,75025,121393,196418,317811,514229}"), Collections.singletonList("1346267")),
																																					 new TestCase("getMapEntry", Arrays.asList("{ 1: strut1 }", "1"), Collections.singletonList("strut1")),
																																					 new TestCase("getMapEntry", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "2"), Collections.singletonList("touwm1")),
																																					 new TestCase("getMapEntry", Arrays.asList("{2:touwm1,3:weidj1,1:strut1}", "3"), Collections.singletonList("weidj1")),
																																					 new TestCase("getMapListEntry", Arrays.asList("{ 1: { strut1, Thomas Strub } }", "1", "0"), Collections.singletonList("strut1")),
																																					 new TestCase("getMapListEntry", Arrays.asList("{1:{strut1,Thomas Strub},2:{touwm1,Marc Touw},3:{weidj1,Janick Weidmann}}", "2", "1"), Collections.singletonList("Marc Touw")),
																																					 new TestCase("getMapListEntry", Arrays.asList("{2:{touwm1,Marc Touw},3:{weidj1,Janick Weidmann},1:{strut1,Thomas Strub}}", "3", "0"), Collections.singletonList("weidj1")),
																																					 new TestCase("intersectArray", Arrays.asList("{}", "0", "{}", "0"), Collections.singletonList("{}")),
																																					 new TestCase("intersectArray", Arrays.asList("{}", "0", "{0,1,2,3}", "4"), Collections.singletonList("{}")),
																																					 new TestCase("intersectArray", Arrays.asList("{0,1,2,3}", "4", "{}", "0"), Collections.singletonList("{}")),
																																					 new TestCase("intersectArray", Arrays.asList("{-3,-2,-1}", "3", "{1,2,3}", "3"), Collections.singletonList("{}")),
																																					 new TestCase("intersectArray", Arrays.asList("{-3,-2,-1,0}", "4", "{0,1,2,3}", "4"), Collections.singletonList("{0}")),
																																					 new TestCase("intersectArray", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "9", "{-5,-4,-3,-2,-1,0,1,2,3}", "9"), Collections.singletonList("{-3,-2,-1,0,1,2,3}")),
																																					 new TestCase("intersectList", Arrays.asList("{}", "{}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectList", Arrays.asList("{}", "{0,1,2,3}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectList", Arrays.asList("{0,1,2,3}", "{}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectList", Arrays.asList("{-3,-2,-1}", "{1,2,3}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectList", Arrays.asList("{-3,-2,-1,0}", "{0,1,2,3}"), Collections.singletonList("{0}")),
																																					 new TestCase("intersectList", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "{-5,-4,-3,-2,-1,0,1,2,3}"), Collections.singletonList("{-3,-2,-1,0,1,2,3}")),
																																					 new TestCase("intersectSet", Arrays.asList("{}", "{}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectSet", Arrays.asList("{}", "{0,1,2,3}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectSet", Arrays.asList("{0,1,2,3}", "{}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectSet", Arrays.asList("{-3,-2,-1}", "{1,2,3}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectSet", Arrays.asList("{-3,-2,-1,0}", "{0,1,2,3}"), Collections.singletonList("{0}")),
																																					 new TestCase("intersectSet", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "{-5,-4,-3,-2,-1,0,1,2,3}"), Collections.singletonList("{-3,-2,-1,0,1,2,3}")),
																																					 new TestCase("intersectSet", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "{-5,-4,-3,-2,-1,0,1,2,3}"), Collections.singletonList("{0,1,-1,2,-2,3,-3}")),
																																					 new TestCase("intersectMap", Arrays.asList("{}", "{}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectMap", Arrays.asList("{}", "{1:strut1,2:touwm1,3:weidj1}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectMap", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "{}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectMap", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "{1:Thomas Strub,2:Marc Touw,3:Janick Weidmann}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectMap", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "{1:strut1,2:cenga1}"), Collections.singletonList("{1:strut1}")),
																																					 new TestCase("intersectMap", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "{1:strut1,4:cenga1}"), Collections.singletonList("{1:strut1}")),
																																					 new TestCase("intersectMap", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "{1:strut1,2:touwm1,3:weidj1}"), Collections.singletonList("{1:strut1,2:touwm1,3:weidj1}")),
																																					 new TestCase("intersectMap", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "{1:strut1,2:touwm1,3:weidj1}"), Collections.singletonList("{2:touwm1,1:strut1,3:weidj1}")),
																																					 new TestCase("intersectMap", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "{2:touwm1,1:strut1,3:weidj1}"), Collections.singletonList("{1:strut1,2:touwm1,3:weidj1}")));

	protected static final List<TestCase> TEST_CASES_FAILURE = Arrays.asList(new TestCase("helloWorld", Collections.emptyList(), Collections.singletonList("Hello World")),
																																					 new TestCase("minChar", Arrays.asList("b", "a"), Collections.singletonList("b")),
																																					 new TestCase("exor", Arrays.asList("true", "true"), Collections.singletonList("true")),
																																					 new TestCase("sumInt8", Arrays.asList("0", "0"), Collections.singletonList("1")),
																																					 new TestCase("sumInt16", Arrays.asList("0", "0"), Collections.singletonList("1")),
																																					 new TestCase("sumInt32", Arrays.asList("0", "0"), Collections.singletonList("1")),
																																					 new TestCase("sumInt64", Arrays.asList("0", "0"), Collections.singletonList("1")),
																																					 new TestCase("sumFloat32", Arrays.asList("0.0", "0.0"), Collections.singletonList("1.0")),
																																					 new TestCase("sumFloat64", Arrays.asList("0.0", "0.0"), Collections.singletonList("1.0")),
																																					 new TestCase("sumDecimal", Arrays.asList("0.0", "0.0"), Collections.singletonList("1.0")),
																																					 new TestCase("sumInt32Array", Arrays.asList("{0}", "1"), Collections.singletonList("1")),
																																					 new TestCase("sumInt32List", Collections.singletonList("{0}"), Collections.singletonList("1")),
																																					 new TestCase("sumInt32Set", Collections.singletonList("{0}"), Collections.singletonList("1")),
																																					 new TestCase("getMapEntry", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "2"), Collections.singletonList("strut1")),
																																					 new TestCase("getMapListEntry", Arrays.asList("{1:{strut1,Thomas Strub},2:{touwm1,Marc Touw},3:{weidj1,Janick Weidmann}}", "2", "1"), Collections.singletonList("strut1")),
																																					 new TestCase("intersectArray", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "11", "{-5,-4,-3,-2,-1,0,1,2,3}", "11"), Collections.singletonList("{}")),
																																					 new TestCase("intersectArray", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "11", "{-5,-4,-3,-2,-1,0,1,2,3}", "11"), Collections.singletonList("{0}")),
																																					 new TestCase("intersectArray", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "11", "{-5,-4,-3,-2,-1,0,1,2,3}", "11"), Collections.singletonList("{-2,-3,-1,0,1,2,3}")),
																																					 new TestCase("intersectList", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "{-5,-4,-3,-2,-1,0,1,2,3}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectList", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "{-5,-4,-3,-2,-1,0,1,2,3}"), Collections.singletonList("{0}")),
																																					 new TestCase("intersectList", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "{-5,-4,-3,-2,-1,0,1,2,3}"), Collections.singletonList("{-2,-3,-1,0,1,2,3}")),
																																					 new TestCase("intersectSet", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "{-5,-4,-3,-2,-1,0,1,2,3}"), Collections.singletonList("{}")),
																																					 new TestCase("intersectSet", Arrays.asList("{-3,-2,-1,0,1,2,3,4,5}", "{-5,-4,-3,-2,-1,0,1,2,3}"), Collections.singletonList("{0}")),
																																					 new TestCase("intersectMap", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "{1:strut1,4:cenga1}"), Collections.singletonList("{2:strut1}")),
																																					 new TestCase("intersectMap", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "{1:strut1,4:cenga1}"), Collections.singletonList("{1:touwm}")),
																																					 new TestCase("intersectMap", Arrays.asList("{1:strut1,2:touwm1,3:weidj1}", "{1:strut1,4:cenga1}"), Collections.singletonList("{2:touwm}")));

	protected static final List<TestCase> TEST_CASES_ERROR = Arrays.asList(new TestCase("error", Collections.emptyList(), Collections.singletonList("0")));

	protected static final List<TestCase> TEST_CASES_FATAL = Arrays.asList(new TestCase("infiniteLoop", Collections.emptyList(), Collections.singletonList("0")),
																																				 new TestCase("recursion", Collections.emptyList(), Collections.singletonList("0")));

	private CodeExecutor codeExecutor;
	private Logger logger;

	protected abstract CodeExecutor getCodeExecutor();

	protected abstract String getExpectedLanguage();

	protected abstract String getFragment();

	@BeforeClass
	public void setUp() {

		this.codeExecutor = this.getCodeExecutor();
		this.logger = Logger.getLogger(String.format("%sTest", this.codeExecutor.getClass().getName()));
	}

	@Test
	public void testGetLanguage() throws ExecutorException {

		Assert.assertEquals(this.codeExecutor.getLanguage(), this.getExpectedLanguage(), "language name incorrect");
	}

	@Test
	public void testGetBlacklist() throws ExecutorException {

		Set<String> blacklist = this.codeExecutor.getBlacklist();
		Assert.assertNotNull(blacklist, "blacklist is missing");
		Assert.assertNotEquals(blacklist.size(), 0, "blacklist is empty");

		this.logger.info(String.join(" ; ", blacklist));
	}

	@Test
	public void testGetFragment() throws ExecutorException {

		String fragment = this.codeExecutor.getFragment(FunctionSignatureImpl.convertFromThrift(CodeExecutorTestBase.FUNCTIONS));
		Assert.assertNotNull(fragment, "fragment is missing");
		Assert.assertNotEquals(fragment.length(), 0, "fragment is empty");

		this.logger.info(fragment);
	}

	@Test
	public void testGetVersionInformation() throws ExecutorException {

		VersionInformation versionInformation = this.codeExecutor.getVersionInformation();
		Assert.assertNotNull(versionInformation.getLanguageVersion(), "language version is missing");
		Assert.assertNotEquals(versionInformation.getLanguageVersion().length(), 0, "language version is empty");
		Assert.assertNotNull(versionInformation.getCompilerName(), "compiler name is missing");
		Assert.assertNotEquals(versionInformation.getCompilerName().length(), 0, "compiler name is empty");
		Assert.assertNotNull(versionInformation.getCompilerVersion(), "compiler version is missing");
		Assert.assertNotEquals(versionInformation.getCompilerVersion().length(), 0, "compiler version is empty");

		this.logger.info(String.format("v%s (compiler: %s v%s)", versionInformation.getLanguageVersion(), versionInformation.getCompilerName(), versionInformation.getCompilerVersion()));
	}

	@Test
	public void testExecuteSuccess() throws ExecutorException {

		this.testExecute(CodeExecutorTestBase.TEST_CASES_SUCCESS, true);
	}

	@Test
	public void testExecuteFailure() throws ExecutorException {

		this.testExecute(CodeExecutorTestBase.TEST_CASES_FAILURE, false);
	}

	@Test
	public void testExecuteError() throws ExecutorException {

		this.testExecute(CodeExecutorTestBase.TEST_CASES_ERROR, false);
	}

	@Test
	public void testExecuteFatal() throws ExecutorException {

		for (TestCase testCase : CodeExecutorTestBase.TEST_CASES_FATAL)
			this.testExecute(Collections.singletonList(testCase), false);
	}

	protected boolean hasTotalExecutionTime() {
		return true;
	}

	protected boolean hasTotalCompilationTime() {
		return true;
	}

	protected boolean hasTestCaseExecutionTime() {
		return true;
	}

	private void testExecute(List<TestCase> testCases, boolean success) throws ExecutorException {

		List<Result> results = this.codeExecutor.execute(this.getFragment(), TestCaseImpl.convertFromThrift(CodeExecutorTestBase.FUNCTIONS, testCases));
		Assert.assertEquals(results.size(), testCases.size(), "number of results not equal to number of test cases");

		for (int i = 0; i < results.size(); i++) {
			Assert.assertNotNull(results.get(i), String.format("result %d is missing", i));
			Assert.assertEquals(results.get(i).isSuccess(), success, String.format("test case #%d failed: %s", i, results.get(i).getResult()));
			Assert.assertNotNull(results.get(i).getResult(), String.format("actual result %d is missing", i));
			Assert.assertNotEquals(results.get(i).getResult().length(), 0, String.format("actual result %d is empty", i));

			if (success) {
				Assert.assertNotNull(results.get(i).getPerformance(), String.format("performance %d is missing", i));
				if (this.hasTotalExecutionTime())
					Assert.assertTrue(Double.isFinite(results.get(i).getPerformance().getTotalExecutionTimeMilliseconds()), String.format("total execution time %d is missing", i));
				if (this.hasTotalCompilationTime())
					Assert.assertTrue(Double.isFinite(results.get(i).getPerformance().getTotalCompilationTimeMilliseconds()), String.format("total compilation time %d is missing", i));
				if (this.hasTestCaseExecutionTime())
					Assert.assertTrue(Double.isFinite(results.get(i).getPerformance().getTestCaseExecutionTimeMilliseconds()), String.format("test case execution time %d is missing", i));
			}
		}
	}
}
