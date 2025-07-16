package com.tablegroup.core.utils.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

/**
 * Executes a suspend API call safely within a Flow, emitting loading, success, and error states.
 *
 * @param call The suspend function representing the API call.
 * @return A Flow emitting NetworkResult states wrapping the call outcome.
 */
suspend fun <T> safeApiCall(call: suspend () -> T): Flow<NetworkResult<T>> = flow {
    emit(NetworkResult.Loading) // Emit loading state
    val response = withContext(Dispatchers.IO) { call() } // Perform API call on IO dispatcher
    emit(NetworkResult.Success(response)) // Emit success with data
}.catch { e ->
    emit(NetworkResult.Error(e.message ?: "Unknown Error")) // Emit error with message
}.flowOn(Dispatchers.IO) // Flow runs on IO dispatcher
