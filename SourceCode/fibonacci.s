    addi b5, b0, #8
    addi b1, b1, #2

fibonacci: ; calculates the 8th fibonacci number
    add b2, b2, b1 ; b2 is old number
    addi b1, b1, b2 ; b1 is current number
    addi b4, b4, #1
    blt, b4, b5, @fibonacci
