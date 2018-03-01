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
import java.sql.SQLException
import java.util.*
import javax.sql.DataSource

@Controller
class UserController {

    @RequestMapping("/userList")
    internal fun userList(model: MutableMap<String, Any>): String {
        val connection = dataSource.getConnection()
        try {
            val stmt = connection.createStatement()
//            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users (tick timestamp)")
//            stmt.executeUpdate("INSERT INTO ticks VALUES (now())")
            val rs = stmt.executeQuery("SELECT * FROM users")

            val output = ArrayList<String>()
            while (rs.next()) {
                val id = rs.getInt("id")
                val username = rs.getString("username")
                val password = rs.getString("password").hashCode()
                val roleId = rs.getInt("role")

                var role = if (roleId != 232)
                    "Simple Deadly"
                else
                    "ADMIN"

                output.add("ID: $id, username: $username, password: $password, role: $role")
            }

            model.put("users", output)
            return "userList"
        } catch (e: Exception) {
            connection.close()
            model.put("message", e.message ?: "Unknown error")
            return "error"
        }

    }

    @RequestMapping("/userAdmin")
    internal fun userAdmin(model: MutableMap<String, Any>): String {
        val connection = dataSource.getConnection()
        try {
            val stmt = connection.createStatement()
            stmt.executeUpdate("INSERT INTO users VALUES (\"admin\", \"admin\", 232)")

//            val rs = stmt.executeQuery("SELECT users FROM ticks")
//
//            val output = ArrayList<String>()
//            while (rs.next()) {
//                output.add("Read from DB: " + rs.getTimestamp("tick"))
//            }

//            model.put("records", output)
            return "userList"
        } catch (e: Exception) {
            connection.close()
            model.put("message", e.message ?: "Unknown error")
            return "error"
        }

    }
}