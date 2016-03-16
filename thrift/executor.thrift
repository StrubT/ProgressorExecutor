
# https://thrift.apache.org/docs/idl

# .\thrift\thrift-0.9.3.exe -gen java .\thrift\executor.thrift
# .\thrift\thrift-0.9.3.exe -gen js:node .\thrift\executor.thrift

namespace java ch.bfh.progressor.executor.thrift
namespace cpp progressor.executor.thrift
namespace csharp Progressor.Executor.Thrift

const string TypeContainerArray = "array"; #e.g. array<string>   --> asdf,temp, qwertz
const string TypeContainerList = "list"; #e.g. list<string>      --> asdf, temp,qwertz
const string TypeContainerSet = "set"; #e.g. set<string>         --> asdf,temp,  qwertz
const string TypeContainerMap = "map"; #e.g. map<string, string> --> asdf:temp,qwer:tz

const string TypeString = "string";
const string TypeCharacter = "char";
const string TypeBoolean = "bool";
const string TypeInt8 = "int8";
const string TypeInt16 = "int16";
const string TypeInt32 = "int32";
const string TypeInt64 = "int64";
const string TypeFloat32 = "float32";
const string TypeFloat64 = "float64";
const string TypeDecimal = "decimal";

struct FunctionSignature {
	1: string name,
	2: list<string> inputNames,
	3: list<string> inputTypes,
	4: list<string> outputNames,
	5: list<string> outputTypes
}

struct TestCase {
	1: string functionName,
	2: list<string> inputValues,
	3: list<string> expectedOutputValues
}

struct Result {
	1: bool success,
	2: bool fatal,
	3: string result,
	4: PerformanceIndicators performance
}

struct PerformanceIndicators {
	1: double runtimeMilliseconds
}

service ExecutorService {

	list<string> getBlacklist(
		1: string language
	)

	string getFragment(
		1: string language,
		3: list<FunctionSignature> functions
	)

	list<Result> execute(
		1: string language,
		2: string fragment,
		3: list<FunctionSignature> functions,
		4: list<TestCase> testCases
	)
}
