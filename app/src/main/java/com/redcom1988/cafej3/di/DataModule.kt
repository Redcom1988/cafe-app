package com.redcom1988.cafej3.di

import com.redcom1988.data.remote.Cafej3Api
import com.redcom1988.data.repository.AnalyticsRepositoryImpl
import com.redcom1988.data.repository.AuthRepositoryImpl
import com.redcom1988.data.repository.MenuRepositoryImpl
import com.redcom1988.data.repository.OfferRepositoryImpl
import com.redcom1988.data.repository.OrderRepositoryImpl
import com.redcom1988.data.repository.PointRepositoryImpl
import com.redcom1988.data.repository.TableRepositoryImpl
import com.redcom1988.domain.analytics.repository.AnalyticsRepository
import com.redcom1988.domain.auth.repository.AuthRepository
import com.redcom1988.domain.menu.repository.MenuRepository
import com.redcom1988.domain.offer.repository.OfferRepository
import com.redcom1988.domain.order.repository.OrderRepository
import com.redcom1988.domain.points.repository.PointRepository
import com.redcom1988.domain.table.repository.TableRepository
import org.koin.dsl.module

val dataModule = module {
    single { Cafej3Api(get(), get()) }

    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<MenuRepository> { MenuRepositoryImpl(get()) }
    single<TableRepository> { TableRepositoryImpl(get()) }
    single<OrderRepository> { OrderRepositoryImpl(get()) }
    single<PointRepository> { PointRepositoryImpl(get()) }
    single<OfferRepository> { OfferRepositoryImpl(get()) }
    single<AnalyticsRepository> { AnalyticsRepositoryImpl(get()) }
}