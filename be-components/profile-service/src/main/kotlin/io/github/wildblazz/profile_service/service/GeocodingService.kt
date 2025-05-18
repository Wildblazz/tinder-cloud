package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.model.Coordinates
import io.github.wildblazz.profile_service.model.dto.NominatimResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

@Service
class GeocodingService(
    private val restTemplate: RestTemplate = RestTemplate(),
    @Value("\${openstreetmap.search-url}")
    private val openstreetmapSearchUrl: String,
    @Value("\${openstreetmap.app-name}")
    private val appName: String
) {

    fun resolveCityCoordinates(city: String): Coordinates? {
        val url = UriComponentsBuilder.fromHttpUrl(openstreetmapSearchUrl)
            .queryParam("q", city)
            .queryParam("format", "json")
            .queryParam("limit", 1)
            .build()
            .toUri()

        val headers = HttpHeaders().apply {
            set("User-Agent", appName)
        }

        val entity = HttpEntity<String>(headers)

        val response = restTemplate.exchange(
            url, HttpMethod.GET, entity, object : ParameterizedTypeReference<List<NominatimResponse>>() {}
        )

        val result = response.body?.firstOrNull() ?: return null

        val lat = result.lat.toDoubleOrNull() ?: return null
        val lon = result.lon.toDoubleOrNull() ?: return null

        return Coordinates(latitude = lat, longitude = lon)
    }
}
