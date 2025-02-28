package com.hikit.cai2hikit

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

// Test 1: match 1.0 - perfect match
// Test 2: match 0.0 - horrible match
// Test 3: match ±0.60 - match dati geografici perfetto, match dati calcolati completamente KO
// Test 4: match ±0.40 - match dati geografici KO, perfect match dati calcolati completamente

@ExtendWith(MockitoExtension::class)
class TrailSimilarityAlgorithmTest(
    @Mock val mockedTrailRepository: TrailRepository
) {
    @Test
    fun `should check two identical trails`() {}

    @Test
    fun `should check two very different trails`() {}

    @Test
    fun `should check data match, geometry mismatch`() {}

    @Test
    fun `should check data mismatch, geometry match`() {}
}