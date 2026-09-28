package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.KaolackPlaces
import com.example.data.model.MotoType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Sma Taxi", appName)
  }

  @Test
  fun `verify kaolack places and moto types`() {
    assertTrue(KaolackPlaces.ALL.isNotEmpty())
    val marcheCentral = KaolackPlaces.ALL.find { it.id == "marche_central" }
    assertTrue(marcheCentral != null)
    assertEquals("Marché Central de Kaolack", marcheCentral?.name)

    assertEquals(3, MotoType.ALL.size)
  }
}
