package com.himawan.gymstis

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.himawan.gymstis.data.DefaultAppContainer
import com.himawan.gymstis.model.AuthRequest
import com.himawan.gymstis.model.RegisterForm
import kotlinx.coroutines.*


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.himawan.gymstis.ui.GymStisApp

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GymStisApp()
        }
    }
}
