package com.tablegroup.core.utils.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/**
 * Executes a suspend API call safely within a Flow, emitting loading, success, and error states.
 *
 * @param call The suspend function representing the API call.
 * @return A Flow emitting NetworkResult states wrapping the call outcome.
 */
suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Flow<NetworkResult<T>> = flow {
    try {
        Log.d("safeApiCall", "Starting API call...")
        emit(NetworkResult.Loading) // Emitimos loading antes de la llamada

        val result = apiCall()
        Log.d("safeApiCall", "API call successful, result size or value: ${
            when(result) {
                is Collection<*> -> result.size
                else -> result.toString()
            }
        }")

        emit(NetworkResult.Success(result)) // Emitimos éxito con resultado

    } catch (e: Exception) {
        Log.e("safeApiCall", "API call failed: ${e.localizedMessage}", e)
        emit(NetworkResult.Error(e.localizedMessage ?: "Unknown error"))
    }
}.flowOn(Dispatchers.IO)
