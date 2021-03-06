@file:JvmName("Program") import java.math.*; import java.util.*; import java.util.function.*; import java.util.stream.*; import java.util.regex.*; $CustomCode$

fun main(vararg args: String) {

	java.io.OutputStreamWriter(System.out, java.nio.charset.Charset.forName("UTF-8").newEncoder()).use { `out` ->
$TestCases$
	}
}

fun Float?.hasMinimalDifference(other: Float?): Boolean {

	if (this === null || !this.isFinite() || other === null || !other.isFinite()) return this == other
	else if (this == other) return true
	else return Math.abs(this - other) <= Math.ulp(this)
}

fun Double?.hasMinimalDifference(other: Double?): Boolean {

	if (this === null || !this.isFinite() || other === null || !other.isFinite()) return this == other
	else if (this == other) return true
	else return Math.abs(this - other) <= Math.ulp(this)
}
