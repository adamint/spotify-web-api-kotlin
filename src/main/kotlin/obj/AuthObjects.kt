package obj

data class Token(val access_token: String, val token_type: String, val expires_in: Int)

data class ErrorResponse(val error: ErrorObject)

data class ErrorObject(val status: Int, val message: String)

class BadRequestException(error: ErrorObject) : Exception("Received Status Code ${error.status}. Error cause: ${error.message}")