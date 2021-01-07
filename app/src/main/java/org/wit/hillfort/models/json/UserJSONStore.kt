package org.wit.hillfort.models.json

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.helpers.exists
import org.wit.hillfort.helpers.read
import org.wit.hillfort.helpers.write
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.models.UserStore
import java.util.ArrayList

val USER_JSON = "users.json"
val userGsonBuilder = GsonBuilder().setPrettyPrinting().create()
val userlistType = object : TypeToken<ArrayList<UserModel>>() {}.type


class UserJSONStore : UserStore, AnkoLogger {

    val context: Context
    var users = mutableListOf<UserModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, USER_JSON)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<UserModel> {
        return users
    }

    override fun findByEmail(email: String): UserModel? {
        info("Finding User by email: $email")
        var foundUser: UserModel? = users.find { p-> p.email == email}
        return foundUser
    }

    override fun signup(user: UserModel): UserModel {
        user.id = generateRandomId()
        users.add(user)
        serialize()
        return user
    }

    override fun update(user: UserModel) {
        var foundUser: UserModel? = users.find { p -> p.id == user.id }
        if (foundUser != null) {
            foundUser.email = user.email
            foundUser.password = user.password
            logAll();
        }
        serialize()

    }

    fun logAll() {
        users.forEach{ info("${it}") }
    }

    override fun delete(user: UserModel) {
        users.remove(user)
        serialize()
    }

    private fun serialize() {
        val jsonString = userGsonBuilder.toJson(users, userlistType)
        write(context, USER_JSON, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, USER_JSON)
        users = Gson().fromJson(jsonString, userlistType)
    }
}