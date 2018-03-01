package com.example

import com.example.DataSourceGen.dataSource
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import java.sql.Connection
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource

@Controller
class Controller {

    @Value("\${spring.datasource.url}")
    private var dbUrl: String? = null



    @RequestMapping("/")
    internal fun index(): String {
        return "index"
    }

    @RequestMapping("/db")
    internal fun db(model: MutableMap<String, Any>): String {
        lateinit var connection: Connection
        if (dbUrl?.isEmpty() != false){
            val config = HikariConfig()
            config.jdbcUrl = dbUrl
            connection = HikariDataSource(config).connection
        } else {
            connection = HikariDataSource().connection
        }


        try {
            val stmt = connection.createStatement()
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticks (tick timestamp)")
            stmt.executeUpdate("INSERT INTO ticks VALUES (now())")
            val rs = stmt.executeQuery("SELECT tick FROM ticks")

            val output = ArrayList<String>()
            while (rs.next()) {
                output.add("Read from DB: " + rs.getTimestamp("tick"))
            }

            model.put("records", output)
            return "db"
        } catch (e: Exception) {
            connection.close()
            model.put("message", e.message ?: "Unknown error")
            return "error"
        }

    }


}