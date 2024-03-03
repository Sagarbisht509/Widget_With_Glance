package com.sagar.widget_with_glance

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

object CounterWidget : GlanceAppWidget() {

    val COUNTER_KEY = intPreferencesKey(name = "count")

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            val currentCount = currentState(key = COUNTER_KEY) ?: 0
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                horizontalAlignment = Alignment.Horizontal.CenterHorizontally,
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                Text(
                    text = currentCount.toString(),
                    style = TextStyle(
                        fontSize = 24.sp,
                        color = ColorProvider(Color.White),
                        fontWeight = FontWeight.Medium
                    )
                )
                Button(
                    text = "Increase",
                    onClick = actionRunCallback(IncrementActionCallback::class.java)
                )
            }
        }
    }
}

class IncrementActionCallback : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(
            context = context,
            glanceId = glanceId
        ) { prefs ->
            val currentCount = prefs[CounterWidget.COUNTER_KEY]
            if (currentCount != null) {
                prefs[CounterWidget.COUNTER_KEY] = currentCount + 1
            } else {
                prefs[CounterWidget.COUNTER_KEY] = 1
            }
        }

        CounterWidget.update(context, glanceId)
    }
}

class CounterWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = CounterWidget
}