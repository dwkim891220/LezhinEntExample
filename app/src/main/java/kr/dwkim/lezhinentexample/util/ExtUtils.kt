package kr.dwkim.lezhinentexample.util

import android.app.AlertDialog
import android.content.Context
import android.view.View
import io.reactivex.Completable
import io.reactivex.Single
import kr.dwkim.lezhinentexample.util.rx.SchedulerProvider

fun View.show(show: Boolean = true){
    this.visibility = if(show) View.VISIBLE else View.GONE
}

fun View.isVisibility() : Boolean{
    return this.visibility == View.VISIBLE
}

fun <T> Single<T>.with(schedulerProvider: SchedulerProvider): Single<T> =
    observeOn(schedulerProvider.ui())
        .subscribeOn(schedulerProvider.io())

fun Completable.with(schedulerProvider: SchedulerProvider): Completable =
    observeOn(schedulerProvider.ui())
        .subscribeOn(schedulerProvider.io())

fun Context.showDialog(contents: String?) {
    AlertDialog.Builder(this)
        .setMessage(contents)
        .show()
}