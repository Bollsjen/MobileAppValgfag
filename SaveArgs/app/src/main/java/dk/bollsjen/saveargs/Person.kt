package dk.bollsjen.saveargs

import java.io.Serializable

data class Person(val name: String, val age: Int): Serializable {
    override fun toString(): String {
        return "Name: " + name + ", Age: " + age
    }
}