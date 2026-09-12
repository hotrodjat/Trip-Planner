package com.example.tripplanner.ui.theme.trip.overview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tripplanner.ui.theme.trip.LocalTripViewModel
import com.example.tripplanner.ui.theme.trip.more.MoreCategoryData

@Composable
fun OverviewScreen() {
    val tripViewModel = LocalTripViewModel.current

    val totalExpense by tripViewModel.totalExpense.collectAsState()
    val peopleCount by tripViewModel.people.collectAsState()
    val eventsCount by tripViewModel.events.collectAsState()
    val logistics by tripViewModel.logistics.collectAsState()

//    val logisticsByType = logistics.groupBy { it.type }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                "Trip Overview",
                style = MaterialTheme.typography.headlineMedium
            )
        }

        item {
            MetricCard(
                title = "Total Expenses",
                value = "$$totalExpense",
                subtitle = "Across all trip activities"
            )
        }

        item {
            MetricCard(
                title = "Participants",
                value = "${peopleCount.size}",
                subtitle = "People on this trip"
            )
        }

        item {
            MetricCard(
                title = "Upcoming Events",
                value = "${eventsCount.size}",
                subtitle = "Scheduled activities"
            )
        }

        item {
            Text(
                "Logistics Breakdown",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

//        logisticsByType.forEach { (type, items) =>
//            item {
//                LogisticsTypeCard(
//                    type = type,
//                    count = items.size
//                )
//            }
//        }

        logistics.groupBy { it.type }.forEach { (type, items) ->
            item {
                LogisticsTypeCard(
                    type = type,
                    count = items.size
                )
            }
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun LogisticsTypeCard(
    type: String,
    count: Int
) {
    val displayType = when (type) {
        "flight" -> "Flights"
        "hotel" -> "Accommodations"
        "car" -> "Transportation"
        "train" -> "Transportation"
        "bus" -> "Transportation"
        else -> type.capitalize()
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                displayType,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                "$count items",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
