package kr.dwkim.lezhinentexample.util.rx

/**
 * Abstract ViewModel State
 */
open class ViewModelState

/**
 * Generic Loading ViewModel State
 */
object LoadingState : ViewModelState()

/**
 * Generic Error ViewModel State
 * @param error - caught error
 */
open class ErrorState(val error: Throwable) : ViewModelState()