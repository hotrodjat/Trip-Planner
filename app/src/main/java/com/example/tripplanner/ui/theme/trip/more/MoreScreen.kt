package com.example.tripplanner.ui.theme.trip.more

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.example.tripplanner.data.entity.LogisticsEntity
import com.example.tripplanner.ui.theme.trip.LocalTripViewModel

@Composable
fun MoreScreen(navController: NavHostController) {
    val tripViewModel = LocalTripViewModel.current

    val viewModel: MoreViewModel = viewModel(
        factory = viewModelFactory {
            initializer { MoreViewModel(tripViewModel) }
        }
    )

    val categories by viewModel.categories.collectAsState()
    val logistics by tripViewModel.logistics.collectAsState()
    val peopleCount by tripViewModel.people.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "More",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    navController.navigate("more/people") {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "People",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        "${peopleCount.size}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        categories.forEach { category ->
            item {
                CategoryHeader(category)
            }

            val categoryItems = logistics.filter { it.type == category.type }
            items(categoryItems) { item ->
                LogisticsItemCard(item)
            }
        }
    }
}

@Composable
private fun CategoryHeader(category: MoreCategoryData) {
    Text(
        "${category.displayName} (${category.itemCount})",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun LogisticsItemCard(item: LogisticsEntity) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                item.title,
                style = MaterialTheme.typography.titleMedium
            )
            if (item.provider != null) {
                Text(
                    "Provider: ${item.provider}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (item.referenceNumber != null) {
                Text(
                    "Reference: ${item.referenceNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (item.notes != null) {
                Text(
                    item.notes,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
