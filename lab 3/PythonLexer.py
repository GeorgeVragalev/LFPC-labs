TokenTypes = {
    ".": "DOT", ",": "COMMA",
    ";": "SEMICOLON", ":": "COLON", "{": "LBRACE", "}": "RBRACE", "(": "LPAREN", ")": "RPAREN", "[": "LBRACKET",
    "]": "RBRACKET", "+": "PLUS", "-": "MINUS", "/": "SLASH", "%": "MOD", "*": "MULT", "<": "SMALLER", ">": "BIGGER",
    "=": "EQUAL", "!": "NEGATION", "!=": "NEGATION_EQUAL", "==": 'EQUAL_EQUAL', ">=": "BIGGER_EQUAL",
    "<=": "SMALLER_EQUAL",
    "int": "INT", "double": "DOUBLE", "bool": "BOOLEAN", "array": "ARRAY", "char": 'CHAR', "string": "STRING",
    "for": "FOR",
    "while": "WHILE", "do": "DO", "if": "IF", "else": "ELSE", "elif": "ELIF", "function": "FUNCTION", "main": "MAIN",
    "return": "RETURN",
    "and": "AND", "or": "OR", "true": "TRUE", "false": "FALSE", "print": "PRINT", "": "IDENTIFIER"
}

punctuationTokens = {
    ".": "DOT", ",": "COMMA", ";": "SEMICOLON", ":": "COLON", "{": "LBRACE", "}": "RBRACE", "(": "LPAREN",
    ")": "RPAREN",
    "[": "LBRACKET", "]": "RBRACKET", "+": "PLUS", "-": "MINUS", "/": "SLASH", "%": "MOD", "*": "MULT",
}

operationTokens = {
    "<": "SMALLER", ">": "BIGGER", "=": "EQUAL", "!": "NEGATION", "!=": "NEGATION_EQUAL",
    "==": 'EQUAL_EQUAL', ">=": "BIGGER_EQUAL", "<=": "SMALLER_EQUAL",
}

keywordTokens = {
    "int": "INT", "double": "DOUBLE", "bool": "BOOLEAN", "array": "ARRAY", "char": 'CHAR', "string": "STRING",
    "for": "FOR", "while": "WHILE", "do": "DO", "if": "IF", "else": "ELSE", "elif": "ELIF", "function": "FUNCTION",
    "main": "MAIN", "return": "RETURN", "and": "AND", "or": "OR", "true": "TRUE", "false": "FALSE", "print": "PRINT"
}


class Token:
    def __init__(self, type, value):
        self.type = type
        self.value = value


class Lexer:

    def __init__(self, inp):
        self.text = inp
        self.tokens = []
        self.current = 0
        self.line = -1
        self.length = 0
        self.error = False

    def initializer(self, input_line):
        self.text = input_line
        self.current = 0
        self.line += 1
        self.length = len(input_line)
        self.error = False

    def increaseCurrent(self):
        self.current += 1

    def tokenizer(self, input_line):
        self.initializer(input_line)

        while self.current < self.length:
            current_char = self.text[self.current]
            if current_char =='\n':
                break
            if current_char == ' ':
                self.increaseCurrent()
            elif current_char in punctuationTokens:
                self.setPunctuationTokens(current_char)
            elif current_char in operationTokens:
                self.setOperationTokens()
            elif current_char.isalpha():
                self.setKeywordTokens()
            elif current_char.isdigit():
                self.setDigitTokens()

    def setPunctuationTokens(self, punctuation):
        token = Token(punctuation, punctuationTokens.get(punctuation))
        self.tokens.append(token)
        self.increaseCurrent()

    def setOperationTokens(self):
        position = self.current
        operation = ""
        while position != self.length:
            if position - self.current > 2:
                self.error = True
                print("Too many operators error!")
            if self.text[position] in operationTokens:
                position += 1
            else:
                break
        operation = self.text[self.current:position]

        if not self.error:
            if operation in operationTokens:
                token = Token(operation, operationTokens.get(operation))
                self.tokens.append(token)
            else:
                print("Operation Error! Check line here: ", self.line)
        else:
            print("Operation Error! Check line: ", self.line)

        self.current = position

    def setKeywordTokens(self):
        position = self.current
        while position != self.length:
            if self.text[position].isalnum():
                position += 1
            else:
                break
        s = self.text[self.current:position]
        if s in keywordTokens:
            token = Token(s, keywordTokens.get(s))
            self.tokens.append(token)
        else:
            token = Token(s, "IDENTIFIER")
            self.tokens.append(token)
        self.current = position

    def setDigitTokens(self):
        position = self.current
        dot_counter = 0
        while True:
            if position >= self.length:
                break
            if not self.text[position].isdigit() or self.text[position].isdigit() != '.':
                if self.text[position].isalpha():
                    self.error = True
                    break
            if self.text[position] == '.':
                dot_counter += 1
            position += 1

        number = self.text[self.current:position]

        if not self.error:
            if dot_counter == 0:
                token = Token(number, "INT")
                self.tokens.append(token)
            elif dot_counter == 1:
                token = Token(number, "DOUBLE")
                self.tokens.append(token)
            else:
                print("Syntax Error! Wrong number format! Check line: ", self.line)
        else:
            print("Syntax Error! Wrong number format! Check line: ", self.line)

        self.current = position

    def print_tokens(self):
        for token in self.tokens:
            print(token.value, " ", token.type)

lex = Lexer("")
with open("C:\\Users\\vraga\\OneDrive\\Desktop\\UNI\\LFPC\\LFPC labs\\lab 3\\src\\AnotherLexer\\program.txt") as openfileobject:
    for line in openfileobject:
        print(line)
        lex.tokenizer(line)

lex.print_tokens()
