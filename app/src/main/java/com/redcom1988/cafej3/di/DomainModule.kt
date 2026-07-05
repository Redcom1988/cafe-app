package com.redcom1988.cafej3.di

import com.redcom1988.domain.analytics.interactor.GetDailySummary
import com.redcom1988.domain.analytics.interactor.GetSales
import com.redcom1988.domain.analytics.interactor.GetTopItems
import com.redcom1988.domain.auth.interactor.GetCurrentUser
import com.redcom1988.domain.auth.interactor.Login
import com.redcom1988.domain.auth.interactor.Logout
import com.redcom1988.domain.auth.interactor.Register
import com.redcom1988.domain.auth.interactor.UpdateProfile
import com.redcom1988.domain.cart.CartManager
import com.redcom1988.domain.menu.interactor.GetCategories
import com.redcom1988.domain.menu.interactor.GetMenuItems
import com.redcom1988.domain.offer.interactor.ApplyOffer
import com.redcom1988.domain.offer.interactor.GetOffers
import com.redcom1988.domain.offer.interactor.GetUserOffers
import com.redcom1988.domain.offer.interactor.RedeemOffer
import com.redcom1988.domain.order.interactor.CancelOrder
import com.redcom1988.domain.order.interactor.ConfirmPayment
import com.redcom1988.domain.order.interactor.CreateAuthenticatedOrder
import com.redcom1988.domain.order.interactor.CreateOrder
import com.redcom1988.domain.order.interactor.GetMyOrders
import com.redcom1988.domain.order.interactor.GetOrderDetail
import com.redcom1988.domain.points.interactor.GetPointBalance
import com.redcom1988.domain.points.interactor.GetPointHistory
import com.redcom1988.domain.preference.ApplicationPreference
import com.redcom1988.domain.table.interactor.ScanTable
import com.redcom1988.domain.table.interactor.TableSession
import org.koin.dsl.module

val domainModule = module {
    single { ApplicationPreference(get()) }
    single { CartManager(get()) }

    single { Login(get(), get()) }
    single { Register(get()) }
    single { Logout(get()) }
    single { GetCurrentUser(get()) }
    single { UpdateProfile(get()) }

    single { GetCategories(get()) }
    single { GetMenuItems(get()) }

    single { ScanTable(get()) }
    single { TableSession(get()) }

    single { CancelOrder(get()) }
    single { CreateOrder(get()) }
    single { CreateAuthenticatedOrder(get()) }
    single { ConfirmPayment(get()) }
    single { GetMyOrders(get()) }
    single { GetOrderDetail(get()) }

    single { GetPointBalance(get()) }
    single { GetPointHistory(get()) }

    single { GetOffers(get()) }
    single { RedeemOffer(get()) }
    single { GetUserOffers(get()) }
    single { ApplyOffer(get()) }

    single { GetSales(get()) }
    single { GetTopItems(get()) }
    single { GetDailySummary(get()) }
}
