package com.dood.system.endpoints

import io.micronaut.management.endpoint.env.EnvironmentEndpointFilter
import io.micronaut.management.endpoint.env.EnvironmentFilterSpecification
import jakarta.inject.Singleton
import java.util.function.Predicate
import java.util.regex.Pattern

/**
 * This class was the micronaut java example and converted to kotlin using
 * intellij.  It lost the @NotNull annotation on the specification input
 * https://docs.micronaut.io/latest/guide/#environmentEndpoint
 * Look at the configration example to open up (ie not obfuscate)
 *
 * The way this file is is very dangerous, since the micronaut config opens
 * up the security (ie no auth).  Ideally this would be locked down but I'm learning
 *
 */
@Singleton
class AllPlainExceptSecretOrMatchEnvFilter : EnvironmentEndpointFilter {
    override fun specifyFiltering(specification: EnvironmentFilterSpecification) {
        specification
            .maskNone() // All values will be in plain-text apart from the supplied patterns
            .exclude(SECRET_PREFIX_PATTERN)
            .exclude(EXACT_MATCH)
            .exclude(PREDICATE_MATCH)
    }

    companion object {
        // Mask anything starting with `sekrt`
        private val SECRET_PREFIX_PATTERN: Pattern = Pattern.compile("sekrt.*", Pattern.CASE_INSENSITIVE)

        // Mask anything exactly matching `exact-match`
        private const val EXACT_MATCH = "exact-match"

        // Mask anything that starts with `private.`
        private val PREDICATE_MATCH: Predicate<String> = Predicate<String> { name -> name.startsWith("private.") }
    }
}