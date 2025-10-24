//package com.mu54omd.mini_ecommerce.frontend_gradle.ui.screens
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.material3.Button
//import androidx.compose.material3.Card
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.launch
//
//@Composable
//fun OrdersScreen(orderRepo: OrderRepository, onBack: () -> Unit) {
//    val scope = rememberCoroutineScope()
//    var orders by remember { mutableStateOf<List<Order>>(emptyList()) }
//
//    LaunchedEffect(Unit) {
//        scope.launch {
//            orders = orderRepo.getMyOrders()
//        }
//    }
//
//    Column(Modifier.fillMaxSize().padding(16.dp)) {
//        Text("My Orders", style = MaterialTheme.typography.titleLarge)
//        Spacer(Modifier.height(8.dp))
//        LazyColumn {
//            items(orders) { order ->
//                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
//                    Column(Modifier.padding(12.dp)) {
//                        Text("Order #${order.id}")
//                        Text("Status: ${order.status}")
//                        order.items.forEach {
//                            Text("- ${it.product.name} x${it.quantity}")
//                        }
//                    }
//                }
//            }
//        }
//        Spacer(Modifier.height(12.dp))
//        Button(onClick = onBack) { Text("Back") }
//    }
//}