package com.example.taskmaster.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.taskmaster.ui.viewmodels.EditTaskViewModel

@Composable
fun EditTaskView(
    navController: NavController,
    viewModel: EditTaskViewModel = hiltViewModel()
) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 90.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Edit Task",
                fontSize = 35.sp,
                fontWeight = FontWeight.Medium
            )

            Button(
                onClick = {
                    viewModel.updateTask {
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.size(45.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0288E5),
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Save Task Icon",
                    modifier = Modifier.size(30.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            modifier = Modifier.padding(start = 5.dp, bottom = 10.dp),
            text = "Task Name",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.taskTitle,
            onValueChange = viewModel::onTitleChange,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Color.Gray,
                focusedBorderColor = Color(0xFF0288E5),
                unfocusedBorderColor = Color.Black,
                focusedLabelColor = Color.Black,
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            modifier = Modifier.padding(start = 5.dp, bottom = 10.dp),
            text = "Description",
            fontSize = 20.sp,
            fontWeight = FontWeight.Medium
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = viewModel.taskDescription,
            onValueChange = viewModel::onDescriptionChange,
            minLines = 5,
            shape = RoundedCornerShape(15.dp),
            colors = OutlinedTextFieldDefaults.colors(
                cursorColor = Color.Gray,
                unfocusedBorderColor = Color.Black,
                focusedBorderColor = Color(0xFF0288E5),
                focusedLabelColor = Color.Black,
            )
        )
    }
}