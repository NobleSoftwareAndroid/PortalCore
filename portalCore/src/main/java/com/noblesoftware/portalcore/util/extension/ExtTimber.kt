package com.noblesoftware.portalcore.util.extension

import android.content.Context
import timber.log.Timber

fun logd(message: String?) = Timber.tag("LOG D").d(message ?: "Message")
fun logd(tag: String?, message: String?) = Timber.tag(tag ?: "LOG D").d(message ?: "Message")
fun logd(context: Context, message: String?) =
    Timber.tag("LOG D ${context.javaClass.simpleName}").d(message ?: "Message")

fun logw(message: String?) = Timber.tag("LOG W").w(message ?: "Message")
fun logw(tag: String?, message: String?) = Timber.tag(tag ?: "LOG W").w(message ?: "Message")
fun logw(context: Context, message: String?) =
    Timber.tag("LOG W ${context.javaClass.simpleName}").w(message ?: "Message")

fun loge(message: String?) = Timber.tag("LOG E").e(message ?: "Message")
fun loge(throwable: Throwable?) = Timber.e(throwable?.message)
fun loge(tag: String?, message: String?) = Timber.tag(tag ?: "LOG E").e(message ?: "Message")
fun loge(context: Context, message: String?) =
    Timber.tag("LOG E ${context.javaClass.simpleName}").e(message ?: "Message")

fun logi(message: String?) = Timber.tag("LOG I").i(message ?: "Message")
fun logi(tag: String?, message: String?) = Timber.tag(tag ?: "LOG I").i(message ?: "Message")
fun logi(context: Context, message: String?) =
    Timber.tag("LOG I ${context.javaClass.simpleName}").i(message ?: "Message")