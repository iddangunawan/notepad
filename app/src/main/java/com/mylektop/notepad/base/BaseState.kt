package com.mylektop.notepad.base

/**
 * class base for mapping response success or error
 */
sealed class BaseState<out T : Any> {
    class SuccessResponse<out T : Any>(val data: T) : BaseState<T>()
    class FailedResponse(val message: String) : BaseState<Nothing>()
}