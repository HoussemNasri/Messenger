package com.nasri.messenger.domain.inputvalidation

import android.util.Patterns

/** EmailVerifier is used to make sure inputted email are valid, because it's used in more than one place I put it here*/
class EmailVerifier : Verifier<String> {

    override fun verify(data: String): List<Verifier.Flaw> {
        val flaws = ArrayList<Verifier.Flaw>()
        val trimmedData = data.trim()

        if (trimmedData.isBlank()) {
            flaws.add(blankEmailFlaw())
        } else if (!Patterns.EMAIL_ADDRESS.matcher(trimmedData).matches()) {
            flaws.add(invalidEmailPatternFlaw())
        }
        return flaws
    }

    private fun blankEmailFlaw(): Verifier.Flaw {
        return Verifier.Flaw("Blank Email", "Email cannot be blank")
    }

    private fun invalidEmailPatternFlaw(): Verifier.Flaw {
        return Verifier.Flaw("Unmatched Pattern", "Email is invalid")
    }
}