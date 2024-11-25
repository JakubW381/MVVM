package com.example.viewmodelsms

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viewmodelsms.ui.theme.ViewModelSMSTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ViewModelSMSTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //val viewModel : ViewModelSMSManager = viewModel()

                    SMSManager(Modifier.padding(innerPadding))
                }
            }
        }
    }


    @Composable
    fun SMSManager(
        modifier: Modifier,
        viewModel : ViewModelSMSManager = viewModel()
    ){
        val configuration = LocalConfiguration.current
        val isPortrait = configuration.orientation == android.content.res.Configuration.ORIENTATION_PORTRAIT
        val currentPage by viewModel.currentPage.collectAsState()
        val screenCount = viewModel.screenCount
        var totalDrag by remember { mutableStateOf(0f) }
        if(isPortrait){
            Box(
                modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                totalDrag += dragAmount
                                Log.d("PRZESUNIECIE: ","amount: "+dragAmount+" total: "+totalDrag)
                                             },
                            onDragEnd = {
                                if (totalDrag < -50 && currentPage < 2 ) {
                                    viewModel.UpdateCurrentPage(currentPage + 1)
                                } else if (totalDrag > 50 && currentPage > 0 ) {
                                    viewModel.UpdateCurrentPage(currentPage - 1)
                                }
                                totalDrag = 0f
                            }
                        )
                                        },
                contentAlignment = Alignment.Center
            ){
                when(currentPage){
                    0 -> PlaceScreen1(viewModel,modifier)
                    1 -> PlaceScreen2(viewModel,modifier)
                    2 -> PlaceScreen3(viewModel,modifier)
                }
            }
            }else  {
            Row{
                PlaceScreen1(viewModel, modifier.weight(1f))
                PlaceScreen2(viewModel, modifier.weight(1f))
                PlaceScreen3(viewModel, modifier.weight(1f))
            }
        }

    }

    @Composable
    fun PlaceScreen1(viewModel: ViewModelSMSManager , modifier: Modifier){
        val contactNumber = viewModel.contactNumber.collectAsState().value
        val messageContent = viewModel.messageContent.collectAsState().value
        val currentPage = viewModel.currentPage.collectAsState().value

        Column (modifier) {
            TextField(value = contactNumber, onValueChange = {viewModel.UpdateContactNumber(it)}, label = { Text("Contact Number") })
            TextField(value = messageContent, onValueChange = {viewModel.UpdateMessageContent(it)}, label = { Text("Message") })
            Button(onClick = {viewModel.UpdateCurrentPage(1)
                Log.d("SMSManager","$currentPage")}) { Text("->") }

        }
    }

    @Composable
    fun PlaceScreen2(viewModel: ViewModelSMSManager , modifier: Modifier){
        val contactNumber = viewModel.contactNumber.collectAsState().value
        val messageContent = viewModel.messageContent.collectAsState().value

        Column (modifier) {
            Text("Contact Number:")
            Text(contactNumber)
            HorizontalDivider(Modifier.fillMaxWidth())
            Text("Message Content:")
            Text(messageContent)
            Row {
                Button(onClick = {viewModel.UpdateCurrentPage(0)}) { Text("<-")}
                Button(onClick = {viewModel.UpdateCurrentPage(2)}) { Text("->") }
            }
        }
    }

    @Composable
    fun PlaceScreen3(viewModel: ViewModelSMSManager , modifier: Modifier){
        val contactNumber = viewModel.contactNumber.collectAsState().value
        val messageContent = viewModel.messageContent.collectAsState().value
        val intent = Intent(Intent.ACTION_SEND).apply{
            data = Uri.parse("site:$contactNumber")
            putExtra("sms_body",messageContent)
        }
        Column (modifier) {
            Button(onClick = {startActivity(intent)}) {
                Text("Wyślij")
            }
            Button(onClick = {viewModel.UpdateCurrentPage(1)}) { Text("<-")}
        }
    }

}
