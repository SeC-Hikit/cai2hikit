package com.hikit.cai2hikit.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Record not found")
class NotFoundException(message: String?) : RuntimeException(message)