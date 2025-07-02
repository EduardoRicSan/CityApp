package com.tablegroup.core.utils.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

suspend fun <T> safeApiCall(call: suspend () -> T): Flow<NetworkResult<T>> = flow {
    emit(NetworkResult.Loading)
    val response = withContext(Dispatchers.IO) { call() }
    emit(NetworkResult.Success(response))
}.catch { e ->
    emit(NetworkResult.Error(e.message ?: "Unknown Error"))
}.flowOn(Dispatchers.IO)