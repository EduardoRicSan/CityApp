package com.tablegroup.ualaapptest

import com.tablegroup.domain.model.City
import com.tablegroup.domain.model.toCity
import com.tablegroup.domain.model.toMap
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class CityListMapScreenTest {
    private val exampleCity = City(
        id = 42,
        name = "TestCity",
        country = "TC",
        lat = 12.34,
        lon = 56.78,
        isFavorite = true
    )

    @Test
    fun `cityToMap serializes City correctly`() {
        val map = exampleCity.toMap()

        assertEquals(42, map["id"])
        assertEquals("TestCity", map["name"])
        assertEquals("TC", map["country"])
        assertEquals(12.34, map["lat"])
        assertEquals(56.78, map["lon"])
        assertEquals(true, map["isFavorite"])
    }


    @Test
    fun `mapToCity deserializes map to City correctly`() {
        val map = mapOf(
            "id" to 1,
            "name" to "CityOne",
            "country" to "CO",
            "lat" to 1.1,
            "lon" to 2.2,
            "isFavorite" to false
        )

        val city = map.toCity()

        assertEquals(1, city?.id)
        assertEquals("CityOne", city?.name)
        assertEquals("CO", city?.country)
        assertEquals(1.1, city?.lat ?: 0.0, 0.0001)
        assertEquals(2.2, city?.lon ?: 0.0, 0.0001)
        assertEquals(false, city?.isFavorite)
    }

    @Test
    fun `mapToCity returns null on empty map`() {
        val city = emptyMap<String, Any>().toCity()
        assertNull(city)
    }

    @Test(expected = ClassCastException::class)
    fun `mapToCity throws ClassCastException on invalid types`() {
        val badMap = mapOf(
            "id" to "wrongType",
            "name" to 123,
            "country" to true,
            "lat" to "NaN",
            "lon" to "NaN",
            "isFavorite" to "yes"
        )

        badMap.toCity()
    }
}