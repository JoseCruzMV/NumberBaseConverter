package converter

fun main() = work()

fun work() {
    decimalConversion()
}

fun decimalConversion() {
    print("Enter number in decimal system: ")
    val decimal = readLine()!!.toIntOrNull() ?: 0
    print("Enter target base: ")
    val base = readLine()!!.toIntOrNull() ?: 0
    val result = decimalToRadix(decimal = decimal, radix = base)
    println("Conversion result: $result")
}

fun decimalToRadix(decimal: Int, radix: Int): String {
    var d = decimal
    val s = StringBuilder()

    while (d >= radix) {
        var number: String = (d % radix).toString()
        s.append(decimalToHex(number = number))
        d /= radix
    }
    s.append( decimalToHex(number = d.toString()) ) // Last remainder
    return s.toString().reversed()
}

fun decimalToHex(number: String): String {
    val hexLetters = mapOf("10" to "A", "11" to "B", "12" to "C", "13" to "D", "14" to "E", "15" to "F")
    return if (hexLetters.containsKey(number)) hexLetters[number] ?: number else number
}
