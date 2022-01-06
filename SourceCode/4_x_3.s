    addi b1, b1, #3

main:
    addi b2, b2, #4
    addi b3, b3, #1
    blt b3, b1, @main
