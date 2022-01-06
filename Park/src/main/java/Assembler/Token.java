package Assembler;

enum token_type {
    TK_SYMBOL,
    TK_AT,
    TK_LBRACKET,
    TK_RBRACKET,
    TK_PLUS,
    TK_MINUS,
    TK_LPAREN,
    TK_RPAREN,
    TK_COLON,
    TK_INTEGER,
    TK_REGISTER
}
class Token{

    public String data;
    token_type type;
    Token(token_type type, String s) {
        this.type = type;
        data = s;
    }
    @Override
    public String toString() {
        return "[" +type.toString() +
                " : '" + data + '\'' +
                ']';
    }
}
