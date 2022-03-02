package converter

import kotlin.math.pow

enum class UserCommand(val command: String){
    FROM("/from"),
    TO("/to"),
    EXIT("/exit")
}

fun main() = work()

fun work() {
    while (true) {
        println("Do you want to convert /from decimal or /to decimal? (To quit type /exit)")
        when (readLine()!!) {
            UserCommand.FROM.command -> decimalConversion()
            UserCommand.TO.command -> fromBaseToDecimalConversion()
            UserCommand.EXIT.command -> break
            else -> continue
        }
        println()
    }
}

fun fromBaseToDecimalConversion() {
    print("Enter source number: ")
    val sourceNumber = readLine()!!
    print("Enter source base: ")
    val base = readLine()!!.toIntOrNull() ?: 0
    val result = radixToDecimal(sourceNumber = sourceNumber, radix = base)
    println("Conversion to decimal result: $result")
}

/* Transforms the given number to a base */
fun radixToDecimal(sourceNumber: String, radix: Int): String {
    var result = 0.0
    for ((exponent, i) in (sourceNumber.length - 1 downTo 0).withIndex()) { // check the number in reverse
        val x: Double = hexToDecimal(number = sourceNumber[i])
        result += x * radix.toDouble().pow(exponent)
    }
    return result.toInt().toString()
}

/* Transforms the given string to decimal value if it's a hex letter */
fun hexToDecimal(number: Char): Double {
    val hexLetters = mapOf("A" to "10", "B" to "11", "C" to "12", "D" to "13", "E" to "14", "F" to "15")
    return if (number.isDigit()) {
        number.toString().toDouble()
    } else if(hexLetters.containsKey(number.toString().uppercase())){
        hexLetters[number.uppercase()]?.toDouble() ?: number.toString().toDouble()
    } else {
        number.toString().toDouble()
    }
}

/* Aks user for decimal and the objective base */
fun decimalConversion() {
    print("Enter number in decimal system: ")
    val decimal = readLine()!!.toIntOrNull() ?: 0
    print("Enter target base: ")
    val base = readLine()!!.toIntOrNull() ?: 0
    val result = decimalToRadix(decimal = decimal, radix = base)
    println("Conversion result: $result")
}

/* Transform decimal to number in given base */
fun decimalToRadix(decimal: Int, radix: Int): String {
    var d = decimal
    val s = StringBuilder()

    while (d >= radix) {
        val number: String = (d % radix).toString()
        s.append(decimalToHex(number = number))
        d /= radix
    }
    s.append( decimalToHex(number = d.toString()) ) // Last remainder
    return s.toString().reversed()
}

/* Check for numbers 10, 11, 12, 13, 14, 15 to change them to hexadecimal representation */
fun decimalToHex(number: String): String {
    val hexLetters = mapOf("10" to "A", "11" to "B", "12" to "C", "13" to "D", "14" to "E", "15" to "F")
    return if (hexLetters.containsKey(number)) hexLetters[number] ?: number else number
}
