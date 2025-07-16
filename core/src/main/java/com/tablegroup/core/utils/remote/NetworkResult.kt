package com.tablegroup.core.utils.remote

/**
 * Represents the state of a network request.
 *
 * @param T The type of data expected on success.
 */
sealed class NetworkResult<out T> {
    /**
     * Indicates a successful network response containing data.
     */
    data class Success<out T>(val data: T) : NetworkResult<T>()

    /**
     * Indicates a failed network response with an error message.
     */
    data class Error(val message: String) : NetworkResult<Nothing>()

    /**
     * Represents a loading state during a network request.
     */
    data object Loading : NetworkResult<Nothing>()
}
