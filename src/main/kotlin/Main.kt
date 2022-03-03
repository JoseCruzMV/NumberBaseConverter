package converter

import java.math.BigInteger

enum class UserCommand(val command: String){
    /*FROM("/from"),
    TO("/to"),*/
    EXIT("/exit"),
    BACK("/back")
}

fun main() = work()

fun work() {
    while (true) {
        print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
        var input = readLine()!!
        if (input == UserCommand.EXIT.command) break
        val (sourceBase, targetBase) = input.split(" ", limit = 2)

        while (true) {
            print("Enter number in base $sourceBase to convert to base $targetBase (To go back type /back) ")
            input = readLine()!!
            if (input == UserCommand.BACK.command) break
            val number = input
            val result = if (sourceBase == "10") decimalToRadix(decimal = number.toBigInteger(), radix = targetBase.toInt()) else {
                val aux = radixToDecimal(sourceNumber = number, radix = sourceBase.toInt())
                decimalToRadix(decimal = aux.toBigInteger(), radix = targetBase.toInt())
            }
            println("Conversion result: $result")
        }
        println()
    }
}

/*fun fromBaseToDecimalConversion() {
    print("Enter source number: ")
    val sourceNumber = readLine()!!
    print("Enter source base: ")
    val base = readLine()!!.toIntOrNull() ?: 0
    val result = radixToDecimal(sourceNumber = sourceNumber, radix = base)
    println("Conversion to decimal result: $result")
}*/

/* Transforms the given number to a base */
fun radixToDecimal(sourceNumber: String, radix: Int): String {
    var result = BigInteger.ZERO
    for ((exponent, i) in (sourceNumber.length - 1 downTo 0).withIndex()) { // check the number in reverse
        val x = letterToDecimal(number = sourceNumber[i])
        result += x * radix.toBigInteger().pow(exponent)
    }
    return result.toString()
}

/* Transforms the given string to decimal value if it's a hex letter */
fun letterToDecimal(number: Char): BigInteger {
    val firstNumberNeeded = 10 // First numbers that needs letter representation
    val startOfLetters = 65 // Ascii value for letter "A"
    return if (number.isDigit()) number.toString().toBigInteger() // If char is a digit return the same
    else (number.uppercaseChar().code - startOfLetters + firstNumberNeeded).toBigInteger() //

}

/* Aks user for decimal and the objective base */
/*fun decimalConversion() {
    print("Enter number in decimal system: ")
    val decimal = readLine()!!.toIntOrNull() ?: 0
    print("Enter target base: ")
    val base = readLine()!!.toIntOrNull() ?: 0
    val result = decimalToRadix(decimal = decimal, radix = base)
    println("Conversion result: $result")
}*/

/* Transform decimal to number in given base */
fun decimalToRadix(decimal: BigInteger, radix: Int): String {
    var d: BigInteger = decimal
    val s = StringBuilder()
    val base = radix.toBigInteger()

    while (d >= base) {
        val number: String = (d % base).toString()
        s.append(decimalToLetters(number = number))
        d /= base
    }
    s.append( decimalToLetters(number = d.toString()) ) // Last remainder
    return s.toString().reversed()
}

/* Changes the numbers over 10 to its letter representation */
fun decimalToLetters(number: String): String {
    val firstNumberNeeded = 10 // First numbers that needs letter representation
    val startOfLetters = 65 // Ascii value for letter "A"
    // it takes de difference between the start of letter and the objective numbers the add it to startOfLetters to get the letter needed
    return if (number.toInt() < firstNumberNeeded) number else (number.toInt() - firstNumberNeeded + startOfLetters).toChar().toString()
}
