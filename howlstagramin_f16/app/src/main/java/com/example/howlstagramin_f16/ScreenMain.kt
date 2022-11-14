//package com.example.howlstagramin_f16
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.navigation.NavHost
//import com.example.howlstagramin_f16.ui.theme.Howlstagramin_f16Theme
//fun ScreenMain(){
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = Routes.Login.route) {
//
//        composable(Routes.Login.route) {
//            LoginPage(navController = navController)
//        }
//    }
//}
//}
//
//@Composable
//fun Greeting(name: String) {
//    Button(onClick = { /*TODO*/ },) {
//        Text(
//            text = "로그인z",
//            textAlign =  TextAlign.Center
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    Howlstagramin_f16Theme {
//        Greeting("Android")
//    }
//}