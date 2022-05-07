package kr.dove.api.exceptions

class InvalidInputException : Throwable {
    constructor(message: String): super(message)
    constructor(message: String, cause: Throwable): super (message, cause)
}