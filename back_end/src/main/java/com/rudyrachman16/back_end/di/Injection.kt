package com.rudyrachman16.back_end.di

import com.rudyrachman16.back_end.data.Repository
import com.rudyrachman16.back_end.data.api.ApiGetData
import com.rudyrachman16.back_end.domain.usecase.UseCase
import com.rudyrachman16.back_end.utils.ApiServices

object Injection {
    fun provideUseCase() =
        UseCase.getInstance(Repository.getInstance(ApiGetData.getInstance(ApiServices.apiReq)))
}