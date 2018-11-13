package com.justin.apps.wheretowatch

import com.justin.apps.wheretowatch.model.Model
import com.justin.apps.wheretowatch.network.MediaResponse
import com.justin.apps.wheretowatch.network.TestMediaApi
import io.reactivex.Single
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MediaApiTest {
    var list = emptyList<Model.Media>()

    @Before
    fun setUpList() {
        val api = TestMediaApi()
        val response: Single<MediaResponse> = api.search("us", "marvel")

        var testList: List<Model.Media>

        response.subscribe { s ->
            list = s.results
        }
    }

    @Test
    fun test_response_fromJson() {
        val api = TestMediaApi()
        val response: Single<MediaResponse> = api.search("us", "marvel")

        var testList: List<Model.Media>

        response.subscribe { s ->
            testList = s.results
            assertNotNull(testList)
        }
    }

    @Test
    fun test_response_correct() {
        val media = list[0]
        assertEquals("Marvel's Daredevil", media.name)
    }
}
