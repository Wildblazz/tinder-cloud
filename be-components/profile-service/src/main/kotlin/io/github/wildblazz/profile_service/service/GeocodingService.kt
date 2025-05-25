package io.github.wildblazz.profile_service.service

import io.github.wildblazz.profile_service.common.Constants
import io.github.wildblazz.profile_service.model.Coordinates
import io.github.wildblazz.profile_service.model.dto.NominatimResponse
import io.github.wildblazz.shared.exception.types.NotFoundException
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

    fun resolveCityCoordinates(city: String): Coordinates {
        val url = UriComponentsBuilder.fromUriString(openstreetmapSearchUrl)
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

        val result = response.body?.firstOrNull()
            ?: throw NotFoundException(Constants.EXCEPTION_COORDINATES_NOT_FOUND, arrayOf(city))

        val lat = result.lat.toDouble()
        val lon = result.lon.toDouble()

        return Coordinates(latitude = lat, longitude = lon)
    }
}
