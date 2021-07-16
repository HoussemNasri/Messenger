package com.nasri.messenger.domain.inputvalidation

interface Verifier<T> {

    /** Returns the list of flaws in the data otherwise an empty list */
    fun verify(data: T): List<Flaw>

    /** Flaw represent an inconsistency in the data according to this object's logic*/
    data class Flaw(
        val title: String,
        val description: String,
        val solutions: List<String> = listOf()
    )

}