package com.fjrc.todo

import com.fjrc.todo.util.DateHelper
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Test
import java.util.*

class DateHelperTest {

    @Test
    fun `Test a date period which was an hour ago should retrieve an hour ago`() {
        val date = DateTime.now().minusHours(1)

        val result = DateHelper.calculateTimeAgo(date.toDate())

        Assert.assertEquals("1 hour ago", result)
    }

    @Test
    fun `Test a date period which was few time ago should retrieve`() {
        val date = DateTime.now()

        val result = DateHelper.calculateTimeAgo(date.toDate())

        Assert.assertEquals("moments from now", result)
    }

    @Test
    fun `Test a date period which has passed two weeks should retrieve `() {
        val date = DateTime.now().minusWeeks(2)

        val result = DateHelper.calculateTimeAgo(date.toDate())

        Assert.assertEquals("2 weeks ago", result)
    }
}