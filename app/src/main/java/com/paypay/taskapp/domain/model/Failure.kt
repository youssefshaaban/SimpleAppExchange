package com.paypay.taskapp.domain.model

sealed class Failure {
    object NetworkConnection : Failure()
    object  UnAuthorize : Failure()
    data class ServerError(val message:String?) : Failure()
    data class UnknownError(val message:String?) : Failure()
}