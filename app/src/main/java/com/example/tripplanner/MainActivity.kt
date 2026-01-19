package com.example.tripplanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.tripplanner.ui.theme.TripPlannerApp
import com.example.tripplanner.ui.theme.TripPlannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TripPlannerTheme {
                TripPlannerApp()
            }
        }
    }
}
