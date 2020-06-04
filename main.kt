package calculator
import java.lang.Double.parseDouble
import java.lang.Exception
import java.math.BigInteger
import java.util.*
import kotlin.system.exitProcess


fun revisacomandos (lineacompleta: String) {
    val linea = lineacompleta.split(" ") // le hace split usando espacios
    val limpia = linea.filterNot { it.isEmpty() } // le hace filter Not te devuelve todos lo que no esta vacio

    val a = limpia[0].toCharArray()

    if (a[0] == '/') {
        when {
            limpia[0] == "/exit" -> { // si el elemento 0 es exit chau
                print("Bye!") // imprime
                exitProcess(1) // no es lo mismo que break porque no es loop
            }
            limpia[0] == "/help" -> println("The program calculates the sum of numbers") // si es help imprime

            else -> {
                println("Unknown command")

            }
        }
    }

}
fun ultimanumero (lineacompleta: String): Boolean {

    val a = lineacompleta
    var numeric = true
    try {
        val num = parseDouble(a)
    } catch (e: NumberFormatException) {
        numeric = false
    }
    return numeric
}

fun primeraletras (lineacompleta: String): Boolean {
    var letra = true

    for (n in lineacompleta) {
        if (n == '1') {
            letra = false
        }
    }
    return letra
}

fun precedence(c: Char): Int {
    when (c) {
        '+', '-' -> return 1
        '*', '/' -> return 2
        '^' -> return 3
    }
    return -1
}

fun infixToPostFix(expression: String): String {
    var result = ""
    val stack = Stack<Char>()
    for (i in 0 until expression.length) {
        val c = expression[i]

        //check if char is operator
        if (precedence(c) > 0) {
            while (stack.isEmpty() == false && precedence(stack.peek()) >= precedence(c)) {
                result += stack.pop()
            }
            stack.push(c)

        } else if (c == ')') {
            var x = stack.pop()
            while (x != '(') {
                result += x
                x = stack.pop()
            }
        } else if (c == '(') {
            stack.push(c)
        } else {
            //character is neither operator nor (
            result += c
        }
    }
    for (i in 0..stack.size-1) {
        result += stack.peek()
        stack.pop()
    }

    return result
}

fun evaluatePostfix(exp: String): BigInteger {
    //create a stack
    val stack = Stack<BigInteger>()

    // Scan all characters one by one
    var i = 0
    while (i < exp.length) {
        var c = exp[i]
        if (c == ' ') {
            i++
            continue
        } else if (Character.isDigit(c)) {
            var n = BigInteger("0")

            //extract the characters and store it in num
            while (Character.isDigit(c)) {
                n = n * 10.toBigInteger() + (c - '0').toBigInteger()
                i++
                c = exp[i]
            }
            i--

            //push the number in stack
            stack.push(n)
        } else {
            val val1 = stack.pop()
            val val2 = stack.pop()
            when (c) {
                '+' -> stack.push(val2 + val1)
                '-' -> stack.push(val2 - val1)
                '/' -> stack.push(val2 / val1)
                '*' -> stack.push(val2 * val1)
            }
        }
        i++
    }
    return stack.pop()
}

fun stringtoarray (lineacompleta: String): List<String> {
    val new = lineacompleta.replace("=", " = ").replace("+++", " + ")
            .replace("--", " + ").replace("---", " - ")
            .replace("*", " * ").replace("+", " + ")

    val linea = new.split(" ")
    var limpia = linea.filterNot { it.isEmpty() }

    return limpia
}

fun convertirvariables (limpia: List<String>): String {
    var newlineacompleta = ""
    for (n in 0..limpia.lastIndex) {
        when {
            variables.containsKey(limpia[n]) -> {
                newlineacompleta = newlineacompleta + " " + variables.getValue(limpia[n])
            }
            !variables.containsKey(limpia[n]) -> {
                newlineacompleta = newlineacompleta + " " + limpia[n]
            }
        }
    }
    return newlineacompleta
}

val variables = mutableMapOf<String, BigInteger>()

fun main() {
    val scanner = Scanner(System.`in`)
// usa scanner.hasnext para el while
    loop@ while (scanner.hasNext()){
        val lineacompleta = scanner.nextLine() // ingresa la linea
        if (lineacompleta.isEmpty()) continue //si viene vacia sigue y no imprime nada

        if (lineacompleta[0] == '/') {
            revisacomandos(lineacompleta)
            continue
        }

        val limpia = stringtoarray(lineacompleta)

        // empieza el when
        when {
            limpia.contains("=") -> {
                val ultimaletranumero = ultimanumero(limpia.last())
                val primeraletra = primeraletras(limpia.first())
                val contador = limpia.count()
                when {
                    !variables.containsKey(limpia.first()) && ultimaletranumero == true && primeraletra != false && contador < 4 -> {
                        val a = limpia.first()
                        val b = limpia.last().toBigInteger()
                        variables[a] = b

                    }
                    !variables.containsKey(limpia.first()) && ultimaletranumero == false && variables.containsKey(limpia.last()) && contador < 4 -> {
                        val a = limpia.first()
                        val b = variables.getValue(limpia.last())
                        variables[a] = b
                        //println(variables.getValue(a))
                    }
                    variables.containsKey(limpia.first()) && ultimaletranumero == true && contador < 4 -> {
                        val a = limpia.first()
                        val b = limpia.last().toBigInteger()
                        variables[a] = b
                        //println(variables.getValue(a))
                    }
                    !variables.containsKey(limpia.first()) && ultimaletranumero == true && primeraletra == false && contador < 4 -> {
                        println("Invalid identifier")
                    }
                    !variables.containsKey(limpia.first()) && ultimaletranumero == false && !variables.containsKey(limpia.last()) && contador < 4 -> {
                        println("Invalid assignment")
                    }
                    limpia.size >= 4 -> println("Invalid assignment")
                }
            }

            // limpia.size == 2 -> println("Invalid expression")

            variables.containsKey(limpia.first()) && limpia.size == 1 -> println(variables.getValue(limpia.first()))

            lineacompleta.contains("(") && !lineacompleta.contains(")") -> println("Invalid expression")

            !lineacompleta.contains("(") && lineacompleta.contains(")") -> println("Invalid expression")

            else -> {
                //val new = lineacompleta.replace("=", " = ").replace("+++", " + ").replace("--", " + ").replace("---", " - ")
                val new1 = convertirvariables(limpia)
                val postfix = infixToPostFix(new1)
                println(evaluatePostfix(postfix))
            }

        }
    }
}


/*

            // caso en que size se mayor a 1
            /*
            limpia.size > 2 && !limpia.contains("=") -> {
                // crea la variable sum que la inicia con el valor cero del array que siempre es un numero

                limpia = convertirvariables(limpia)

                var sum = limpia[0].toString().toInt() // cero lo pasa a string y luego a int

                // arranca un for que itera desde 1 en adelante porque cero ya esta en sum
                for (n in 1..limpia.lastIndex)

                // dentro del for de 1 hasta el final lastindex inicia un when
                // el when no tiene condicion ni nada... es como si fuera un if
                    when {
                        // caso 1 del when es si n contains el caracter '-'
                        limpia[n].contains('-') ->

                            // si lo tiene empieza un if donde mide adentro del elemento y si es par hace algo
                            if (limpia[n].length % 2 == 0) { // mide si es par
                                // agrega a sum
                                sum += limpia[n + 1].toString().toInt() // creo que con el n + 1 agrega el siguiente numero
                                // ahora resta a sum el n + 1
                            } else { sum -= limpia[n + 1].toString().toInt() } // tmb lo pasa a int
                        // me pierdo en esta parte... no usa lo signos solo suma o resta a sum.. que seria el resultado

                        // caso 2 del when por si contiene el caracter '+'
                        limpia[n].contains('+') -> sum += limpia[n + 1].toString().toInt()

                    }
                println(sum) // sale el resultado
            }

             

https://algorithms.tutorialhorizon.com/convert-infix-to-postfix-expression/


fun revisaprimer (lineacompleta: String) {
    val linea = lineacompleta.split(" ") // le hace split usando espacios
    val limpia = linea.filterNot { it.isEmpty() } // le hace filter Not te devuelve todos lo que no esta vacio

    val a = limpia[0]
    var numeric = true
    val b = limpia[0].toCharArray()

    if (limpia.size == 1 && b[0] != '/') {
        try {
            val num = parseDouble(a)
        } catch (e: NumberFormatException) {
            numeric = false
        }
        if (!numeric) {
            println("Invalid expression")
        } else {
            println(limpia[0].toInt())
        }
    }
}

fun limpiarsumar(a:String) {
    println(a)
    var new = ""
    var menos = 0
    var signo = '-'

    for (x in a) {
        var temp = '-'
        if (x == '-' && x == temp) {
            menos++

        } else if (x.isDigit()) {
            new = new + x
            new = new + temp
            temp = x
        }

    }
    println(menos)
    println(new)

    val count = a.count{ a.contains(it) }
    println(count)

    var new1 = new.substring(0, new.count()-1)

    val b = new1.replace("+", "+")
            .replace("-", "+-")
    println(b)

    var c = b.split("+").filter { it != "" }
    println("C $c")
    val d = c.sumBy { it.toInt() }
    if (c.size !=1){println(d)}
    println("D $d")
}

It is highly complex to extract the operands and operators from this input.
This is where the Exp4j library becomes a lifesaver.
Exp4j library can process an expression in String format and return the result.
Therefore, in the onEquals method, we feed the String value of the txtInput.text to the
ExpressionBuilder and evaluate the result.
------------------------------------------------

https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form

https://stackoverflow.com/questions/2276021/evaluating-a-string-as-a-mathematical-expression-in-javascript


---- estas andan para sacar el vacio adelante ---
val newnew = new.filter{ it != "" }
    println("filtrado los vacios: $newnew")

val newnew = new.drop(1)
    println("filtrado los vacios: $newnew")
    -----------------------

https://stackoverflow.com/questions/4416425/how-to-split-string-with-some-separator-but-without-removing-that-separator-in-j

https://www.geeksforgeeks.org/java-unary-operator-with-examples/ aca esta bien explicado..
si hay un numero 6++ eso significa 7 lo mismo seria ++6

list.count { it == "apple" }

        if (a.isEmpty()) {
            println("Empty!")
            continue
        }


        if (a == "/help") {
            println("The program calculates the sum of numbers")
            continue
        }

val sum = arrayOf(12, 33).sum()

var sum = 0
    for(number in numbers){
        sum += number
    }


---------------------------------------
package calculator
import java.util.*

fun main() {
    val scanner = Scanner(System.`in`)

    do {
        val a = scanner.nextLine()

        if (a == "/exit") {
            println("Bye!")
            break
        }

        if (a == "") continue

        val b = a.split(" ")

        if (b.size == 1) {
            println(a)
            continue

        } else {
            val first = b[0].toInt()
            val second = b[1].toInt()

            var sum = first + second

            println(sum)
        }


    } while (a.toString() != "/exit")

}

-----------------------

fun main() {
    val scanner = Scanner(System.`in`)


    while (scanner.hasNextLine()) {
        val a = scanner.nextLine()

        if (a == "/exit") {
            println("Bye!")
            break
        }

        if (a == "") {
            println("zzz")
            continue
        }

        val b = a.split(" ")

        if (b.size == 1) {
            println(a)
            continue

        } else sumar(a)


    }

}
ESTE CON EL WHILE NO LE GUSTO Y NO PASABA LOS TEST... CON EL DO WHILE SI.. SACALA
---------------------------------------------------------------------
package calculator
import java.util.*

fun sumar(a: String) {
    val b = a.split(" ")
    val c = b.map { it.toInt() }.sum()
    println(c)
}

fun main() {
    val scanner = Scanner(System.`in`)

    do {
        val a = scanner.nextLine()

        if (a == "/exit") {
            println("Bye!")
            break
        }

        if (a == "") continue

        sumar(a)


    } while (a.toString() != "/exit")

}



 */
