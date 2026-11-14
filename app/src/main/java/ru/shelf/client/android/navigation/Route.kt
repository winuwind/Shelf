package ru.shelf.client.android.navigation

sealed class Routes(val route: String) {
    object Catalog : Routes("catalog")
    object Search : Routes("search")
    object Messages : Routes("messages")
    object Profile : Routes("profile")

    object SignIn : Routes("sign_in")
    object SignUp : Routes("sign_up")

    object Registration : Routes("registration")
    object EmailSentSuccess : Routes("email_sent_success")
    object EmailSentError : Routes("email_sent_error")
    object EmailVerifySuccess : Routes("email_verify_success")
    object EmailVerifyError : Routes("email_verify_error")

    object RestoreAccess : Routes("restore_access")
    object ChangePassword : Routes("change_password")
    object Faq : Routes("faq")
}
