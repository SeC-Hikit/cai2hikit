package com.hikit.cai2hikit

import org.junit.jupiter.api.Assertions.*

// Test prerequisiti: creare un piccolissimo database di sentieri

// ==== Tests dove abbiamo solo un sentiero matchato ==== //

// Test 1: match 0 - input produce output di valore 0
// Test 2: match 1 - input produce output di valore 1
// Test 3: match ±0.5 - input produce output di valore 0.5
// Test 4: match ±0.60 - match dati geografici perfetto, match dati calcolati completamente KO
// Test 5: match ±0.40 - match dati geografici KO, perfect match dati calcolati completamente

// TODO - specifica casi di test ulteriori per multisentiero sulla base della vicinanza

class DTWAlgorithmTest

// TODO: guarda Integration Test su hikit
//@RunWith(SpringRunner.class)
//@SpringBootTest()
//@TestPropertySource(locations = "classpath:application-test.properties")