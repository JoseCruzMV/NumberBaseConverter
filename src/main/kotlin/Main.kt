package converter

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode

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
            if (input == UserCommand.BACK.command) break // if command break is used, return no choose source and target base

            val userNumber = input.split(".") // try to split de given number in its integer and fractional parts
            val number = userNumber[0]
            val fractional = if (userNumber.size > 1) userNumber[1] else ""

            val result = if (sourceBase == "10") decimalToRadix(decimal = number.toBigInteger(), radix = targetBase.toInt()) else {
                val aux = radixToDecimal(sourceNumber = number, radix = sourceBase.toInt())
                decimalToRadix(decimal = aux.toBigInteger(), radix = targetBase.toInt())
            }
            val fractionalResult = if (fractional.isNotEmpty()) {
                if (sourceBase == "10") fractionalDecimalToRadix(fractional = fractional, radix = targetBase.toInt()) else {
                    fractionalDecimalToRadix(
                        fractional = fractionalRadixToDecimal(fractional = fractional, radix = sourceBase.toInt()),
                        radix = targetBase.toInt()
                    )

                }
            } else "" // there's no fractional part
            println("Conversion result: $result" + if (fractionalResult.isNotEmpty()) ".$fractionalResult" else "")
        }
        println()
    }
}

/* Takes a fraction part of a number in some base and transforms it to decimal base */
fun fractionalRadixToDecimal(fractional: String, radix: Int): String {
    var result = BigDecimal.ZERO
    for (i in fractional.indices) {
        val number = letterToDecimal(number = fractional[i]) // If number is a letter returns its number value
        result += if (number.toBigDecimal() == BigDecimal.ZERO)
            BigDecimal.ZERO
        else
            number.toBigDecimal() * ( BigDecimal.ONE.setScale(20) / radix.toBigDecimal().pow( i + 1 ) ) // the current value * base ^ -position without zero
    }
    return if (result == BigDecimal.ZERO) {
        "00000"
    } else {
        result.toString().substring(2)
    }
}

/* Takes a fraction part of a decimal number and transforms it to a given base */
fun fractionalDecimalToRadix(fractional: String, radix: Int): String {
    var fractionNumber = BigDecimal("0.$fractional")
    val result = StringBuilder()
    while (fractionNumber <= BigDecimal.ONE && result.length < 5) {
        val integerReminder = (fractionNumber * radix.toBigDecimal()).toInt()
        result.append(decimalToLetters(number = integerReminder.toString()))
        fractionNumber = (fractionNumber * radix.toBigDecimal()) - integerReminder.toBigDecimal()
    }
    return result.toString()
}

/*fun fromBaseToDecimalConversion() {
    print("Enter source number: ")
    val sourceNumber = readLine()!!
    print("Enter source base: ")
    val base = readLine()!!.toIntOrNull() ?: 0
    val result = radixToDecimal(sourceNumber = sourceNumber, radix = base)
    println("Conversion to decimal result: $result")
}*/

/* Transforms the given number to a decimal number */
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
