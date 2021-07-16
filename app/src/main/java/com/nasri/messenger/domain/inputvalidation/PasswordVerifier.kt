package com.nasri.messenger.domain.inputvalidation

class PasswordVerifier : Verifier<String> {

    override fun verify(data: String): List<Verifier.Flaw> {
        val flaws = ArrayList<Verifier.Flaw>()
        val trimmedData = data.trim()

        if (trimmedData.isBlank()) {
            flaws.add(blankPasswordFlaw())
        }

        return flaws
    }

    private fun blankPasswordFlaw(): Verifier.Flaw {
        return Verifier.Flaw("Blank Password", "Password cannot be blank")
    }

}